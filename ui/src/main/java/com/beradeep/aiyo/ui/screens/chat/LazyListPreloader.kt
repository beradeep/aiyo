package com.beradeep.aiyo.ui.screens.chat

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.beradeep.aiyo.domain.model.Conversation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LazyListPreloader(
    listState: LazyListState,
    selectedConversation: Conversation?,
    preloadRequest: (Int) -> Unit,
    cancelPreviousRequests: () -> Unit,
    maxPreload: Int
) {
    var lastEnd by remember(selectedConversation?.id) { mutableIntStateOf(0) }
    var lastStart by remember(selectedConversation?.id) { mutableIntStateOf(0) }
    var lastFirstVisible by remember(selectedConversation?.id) { mutableIntStateOf(-1) }
    var wasIncreasing by remember(selectedConversation?.id) { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    DisposableEffect(selectedConversation?.id) {
        val job =
            scope.launch {
                val flow =
                    snapshotFlow {
                        val layoutInfo = listState.layoutInfo
                        Triple(
                            listState.firstVisibleItemIndex,
                            layoutInfo.visibleItemsInfo.size,
                            layoutInfo.totalItemsCount
                        )
                    }.distinctUntilChanged()

                flow.firstOrNull()?.let { (_, _, totalCount) ->
                    if (totalCount > 0) {
                        cancelPreviousRequests()

                        // Reset state
                        lastEnd = 0
                        lastStart = 0
                        lastFirstVisible = -1
                        wasIncreasing = true

                        // Preload initially visible items
                        val visibleRange =
                            0 until minOf(maxPreload, totalCount)
                        for (index in visibleRange) {
                            preloadRequest(index)
                        }
                    }
                }

                withContext(Dispatchers.Default) {
                    flow.collectLatest { (firstVisible, visibleCount, totalCount) ->
                        if (totalCount == 0 || firstVisible < 0) return@collectLatest

                        // Determine scroll direction
                        val isIncreasing = firstVisible >= lastFirstVisible

                        // Cancel previous requests if direction changed significantly
                        if (wasIncreasing != isIncreasing) {
                            wasIncreasing = isIncreasing
                            cancelPreviousRequests()
                        }

                        lastFirstVisible = firstVisible

                        // Calculate preload range based on current visible items and scroll direction
                        val lastVisibleIndex = firstVisible + visibleCount - 1
                        val (preloadStart, preloadEnd) =
                            if (isIncreasing) {
                                // Scrolling down - preload items after visible range
                                val start = maxOf(lastVisibleIndex + 1, lastEnd)
                                val end = minOf(start + maxPreload, totalCount)
                                start to end
                            } else {
                                // Scrolling up - preload items before visible range
                                val end = minOf(firstVisible, lastStart)
                                val start = maxOf(end - maxPreload, 0)
                                start to end
                            }

                        // Also ensure currently visible items are preloaded
                        val visibleStart = firstVisible
                        val visibleEnd = minOf(lastVisibleIndex + 1, totalCount)

                        // Combine visible and preload ranges
                        val allStart = minOf(visibleStart, preloadStart)
                        val allEnd = maxOf(visibleEnd, preloadEnd)

                        // Update tracking state
                        lastStart = allStart
                        lastEnd = allEnd

                        // Request preload for all items in range, but don't exceed actual message count
                        for (index in allStart until minOf(allEnd, totalCount)) {
                            preloadRequest(index)
                        }
                    }
                }
            }

        onDispose {
            job.cancel()
            cancelPreviousRequests()
        }
    }
}
