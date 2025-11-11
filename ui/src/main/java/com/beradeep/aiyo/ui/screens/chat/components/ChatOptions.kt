package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.ui.AiyoTheme
import com.beradeep.aiyo.ui.basics.components.Icon
import com.beradeep.aiyo.ui.basics.components.IconButton
import com.beradeep.aiyo.ui.basics.components.IconButtonVariant
import com.beradeep.aiyo.ui.basics.components.Text
import com.beradeep.aiyo.ui.basics.components.Tooltip
import com.beradeep.aiyo.ui.basics.components.TooltipBox
import com.beradeep.aiyo.ui.basics.components.rememberTooltipState
import kotlinx.coroutines.launch

@Composable
fun ChatOptions(
    modifier: Modifier = Modifier,
    conversation: Conversation,
    onClickDelete: (Conversation) -> Unit,
    onClickRename: (Conversation) -> Unit,
    onClickStar: (Conversation) -> Unit
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    TooltipBox(
        state = tooltipState,
        tooltip = {
            Tooltip(
                caretSize = DpSize(0.dp, 0.dp),
                shape = RoundedCornerShape(12.dp),
                maxWidth = 220.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                tooltipState.dismiss()
                                onClickRename(conversation)
                            }
                    ) {
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Rename")
                            Icon(Icons.Outlined.Edit)
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                tooltipState.dismiss()
                                onClickStar(conversation.copy(isStarred = !conversation.isStarred))
                            }
                    ) {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            Arrangement.SpaceBetween
                        ) {
                            if (conversation.isStarred) {
                                Text("Unstar")
                                Icon(Icons.Outlined.StarOutline)
                            } else {
                                Text("Star")
                                Icon(Icons.Filled.Star)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                tooltipState.dismiss()
                                onClickDelete(conversation)
                            }
                    ) {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            Arrangement.SpaceBetween
                        ) {
                            Text("Delete")
                            Icon(
                                Icons.Outlined.Delete,
                                tint = AiyoTheme.colors.error
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    ) {
        val scope = rememberCoroutineScope()
        IconButton(
            onClick = { scope.launch { tooltipState.show() } },
            variant = IconButtonVariant.PrimaryGhost
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Options"
            )
        }
    }
}