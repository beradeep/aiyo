package com.beradeep.aiyo.ui.screens.chat

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ListAutoScrollToBottom(toIndexFromBottom: Int, lazyListState: LazyListState) {
    LaunchedEffect(lazyListState) {
        val itemCountFlow =
            snapshotFlow {
                Pair(
                    lazyListState.layoutInfo.totalItemsCount,
                    lazyListState.layoutInfo.visibleItemsInfo
                        .lastOrNull()?.index ?: 0
                )
            }
        itemCountFlow
            .distinctUntilChanged()
            .collect { (itemCount, lastVisibleItemIndex) ->
                if (itemCount >= toIndexFromBottom && lastVisibleItemIndex < itemCount - 1) {
                    lazyListState.scrollToItem(itemCount - toIndexFromBottom)
                }
            }
    }
}

@Composable
fun ListAutoScrollToTop(toIndex: Int, lazyListState: LazyListState) {
    LaunchedEffect(lazyListState) {
        val itemCountFlow =
            snapshotFlow {
                Pair(
                    lazyListState.layoutInfo.totalItemsCount,
                    lazyListState.firstVisibleItemIndex
                )
            }
        itemCountFlow
            .distinctUntilChanged()
            .collect { (itemCount, firstVisibleItemIndex) ->
                if (itemCount >= toIndex && firstVisibleItemIndex > toIndex) {
                    lazyListState.scrollToItem(toIndex)
                }
            }
    }
}
