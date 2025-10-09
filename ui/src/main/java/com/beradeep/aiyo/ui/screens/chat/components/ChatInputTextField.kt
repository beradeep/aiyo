package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.domain.model.Reason
import com.beradeep.aiyo.ui.LocalColors
import com.beradeep.aiyo.ui.R
import com.beradeep.aiyo.ui.basics.components.Icon
import com.beradeep.aiyo.ui.basics.components.IconButton
import com.beradeep.aiyo.ui.basics.components.IconButtonVariant
import com.beradeep.aiyo.ui.basics.components.Text
import com.beradeep.aiyo.ui.basics.components.Tooltip
import com.beradeep.aiyo.ui.basics.components.TooltipBox
import com.beradeep.aiyo.ui.basics.components.rememberTooltipPositionProvider
import com.beradeep.aiyo.ui.basics.components.rememberTooltipState
import com.beradeep.aiyo.ui.basics.components.textfield.TextField
import kotlinx.coroutines.launch

@Composable
fun RowScope.ChatInputTextField(
    value: String,
    reasonEffort: Reason,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onWebSearch: () -> Unit,
    onReason: (Reason) -> Unit,
    isWebSearchEnabled: Boolean,
    isLoadingOrStreamingResponse: Boolean
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.weight(1f),
        singleLine = false,
        maxLines = 3,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
        leadingIcon = {
            Row {
                IconButton(
                    shape = CircleShape,
                    variant = IconButtonVariant.PrimaryGhost,
                    onClick = onWebSearch
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Language,
                        tint =
                        if (isWebSearchEnabled) {
                            LocalColors.current.tertiary
                        } else {
                            LocalColors.current.primary
                        }
                    )
                }
                TooltipBox(
                    state = tooltipState,
                    positionProvider = rememberTooltipPositionProvider(12.dp),
                    tooltip = {
                        Tooltip(
                            caretSize = DpSize(0.dp, 0.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column {
                                Reason.entries.forEach { effort ->
                                    Row(
                                        Modifier
                                            .padding(8.dp)
                                            .clickable {
                                                onReason(effort)
                                                tooltipState.dismiss()
                                            }
                                    ) {
                                        Text(effort.name)
                                    }
                                }
                            }
                        }
                    }
                ) {
                    val scope = rememberCoroutineScope()
                    IconButton(
                        shape = CircleShape,
                        variant = IconButtonVariant.PrimaryGhost,
                        onClick = { scope.launch { tooltipState.show() } }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cognition_24px),
                            tint =
                            if (reasonEffort != Reason.None) {
                                LocalColors.current.tertiary
                            } else {
                                LocalColors.current.primary
                            }
                        )
                    }
                }
            }
        },
        trailingIcon = {
            IconButton(
                shape = CircleShape,
                enabled = value.isNotBlank() && !isLoadingOrStreamingResponse,
                variant = IconButtonVariant.PrimaryGhost,
                onClick = onSend
            ) {
                Icon(Icons.AutoMirrored.Rounded.Send)
            }
        },
        placeholder = { Text(text = "Type a message...") },
        shape = CircleShape
    )
}