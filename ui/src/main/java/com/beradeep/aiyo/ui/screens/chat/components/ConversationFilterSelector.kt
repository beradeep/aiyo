package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.ui.LocalColors
import com.beradeep.aiyo.ui.screens.chat.ConversationFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationFilterSelector(
    selectedFilter: ConversationFilter,
    onFilterSelected: (ConversationFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        ConversationFilter.entries.forEach { filter ->
            SegmentedButton(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = filter.ordinal,
                    count = ConversationFilter.entries.size
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = LocalColors.current.primary,
                    activeContentColor = LocalColors.current.onPrimary,
                    inactiveContainerColor = LocalColors.current.surface,
                    inactiveContentColor = LocalColors.current.onSurface
                )
            ) {
                Text(
                    text = when (filter) {
                        ConversationFilter.RECENT -> "Recent"
                        ConversationFilter.STARRED -> "Starred"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
    Spacer(modifier = Modifier.width(16.dp))
}
