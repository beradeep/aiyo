package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.components.AlertDialog
import com.beradeep.aiyo.ui.components.Button
import com.beradeep.aiyo.ui.components.ButtonVariant
import com.beradeep.aiyo.ui.components.Text
import com.beradeep.aiyo.ui.components.card.Card
import com.beradeep.aiyo.ui.components.textfield.OutlinedTextField

@Composable
fun ApiKeyDialog(
    initial: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit,
    onClear: () -> Unit = { }
) {
    var key by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        onConfirmClick = { onSave(key) },
        title = "API Key",
        text = "Enter your OpenRouter API key",
        confirmButtonText = "Save",
        dismissButtonText = "Cancel",
        content = {
            Card {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "OpenRouter API key", style = LocalTypography.current.h3)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = key,
                        onValueChange = { key = it.trim() },
                        placeholder = { Text(text = "sk-or-v1-...") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            variant = ButtonVariant.Ghost,
                            text = "Clear",
                            onClick = {
                                key = ""
                                onClear()
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(text = "Save", onClick = { onSave(key) }, enabled = key.isNotBlank())
                    }
                }
            }
        }
    )
}