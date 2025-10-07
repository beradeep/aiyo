package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.components.Text
import com.beradeep.aiyo.ui.components.card.Card
import com.beradeep.aiyo.ui.markdownColor
import com.beradeep.aiyo.ui.markdownTypography
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.model.State

@Composable
fun MessageBubble(content: String, isUser: Boolean, markdownState: State) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        val modifier =
            if (isUser) {
                Modifier.fillMaxWidth(0.9f)
            } else {
                Modifier.fillMaxWidth()
            }
        if (isUser) {
            Card(modifier) {
                Box(Modifier.padding(12.dp)) {
                    Text(text = content, style = LocalTypography.current.body1)
                }
            }
        } else {
            Box(modifier.padding(12.dp)) {
                Markdown(
                    markdownState,
                    markdownColor(),
                    markdownTypography(),
                    components =
                    markdownComponents(
                        codeBlock = highlightedCodeBlock,
                        codeFence = highlightedCodeFence
                    ),
                    loading = { Text(text = content, style = LocalTypography.current.body1) },
                    error = {
                        Text(
                            text = "Parse error: ${(markdownState as? State.Error)?.result}",
                            style = LocalTypography.current.body1
                        )
                    }
                )
            }
        }
    }
}