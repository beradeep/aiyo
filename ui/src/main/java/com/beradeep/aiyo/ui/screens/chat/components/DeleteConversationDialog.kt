package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.runtime.Composable
import com.beradeep.aiyo.ui.basics.components.AlertDialog

@Composable
fun DeleteConversationDialog(
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        onConfirmClick = onDelete,
        title = "Delete Conversation",
        text = "Are you sure you want to delete this conversation? It will be permanently removed.",
        confirmButtonText = "Delete",
        dismissButtonText = "Cancel"
    )
}