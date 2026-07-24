package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.basics.components.Text
import com.beradeep.aiyo.ui.basics.components.card.Card
import com.beradeep.aiyo.ui.markdownColor
import com.beradeep.aiyo.ui.markdownTypography
import com.mikepenz.markdown.annotator.annotatorSettings
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.MarkdownComponent
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownTable
import com.mikepenz.markdown.compose.elements.MarkdownTableHeader
import com.mikepenz.markdown.compose.elements.MarkdownTableRow
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.model.State

@Composable
fun MessageBubble(content: String, isUser: Boolean, markdownState: State, fontSize: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        val textStyle = LocalTypography.current.body1.withFontSize(fontSize)
        val modifier =
            if (isUser) {
                Modifier.fillMaxWidth(0.9f)
            } else {
                Modifier.fillMaxWidth()
            }
        if (isUser) {
            Card(modifier, shape = MessageBubbleDefaults.UserBubbleShape) {
                Box(Modifier.padding(12.dp)) {
                    Text(text = content, style = textStyle)
                }
            }
        } else {
            Box(modifier.padding(12.dp)) {
                Markdown(
                    markdownState,
                    markdownColor(),
                    markdownTypography(
                        h1 = LocalTypography.current.h1.withFontSize(fontSize + 8),
                        h2 = LocalTypography.current.h2.withFontSize(fontSize + 6),
                        h3 = LocalTypography.current.h3.withFontSize(fontSize + 4),
                        h4 = LocalTypography.current.h4.withFontSize(fontSize + 2),
                        h5 = textStyle,
                        h6 = textStyle,
                        text = textStyle,
                        code = textStyle.copy(fontFamily = FontFamily.Monospace),
                        inlineCode = textStyle.copy(fontFamily = FontFamily.Monospace),
                        quote = textStyle,
                        paragraph = textStyle,
                        ordered = textStyle,
                        bullet = textStyle,
                        list = textStyle,
                        table = textStyle
                    ),
                    components = customMarkDownComponents(),
                    loading = { Text(text = content, style = textStyle) },
                    error = {
                        Text(
                            text = "Parse error: ${(markdownState as? State.Error)?.result}",
                            style = textStyle
                        )
                    }
                )
            }
        }
    }
}

private fun customMarkDownComponents() =
    markdownComponents(
        codeBlock = highlightedCodeBlock,
        codeFence = highlightedCodeFence,
        table = customMarkdownTable()
    )

private fun customMarkdownTable(): MarkdownComponent = {
    MarkdownTable(
        node = it.node,
        content = it.content,
        style = it.typography.table,
        headerBlock = { content, header, tableWidth, style ->
            MarkdownTableHeader(
                content = content,
                header = header,
                tableWidth = tableWidth,
                style = style,
                annotatorSettings = annotatorSettings(),
                maxLines = 2
            )
        },
        rowBlock = { content, header, tableWidth, style ->
            MarkdownTableRow(
                content = content,
                header = header,
                tableWidth = tableWidth,
                style = style,
                annotatorSettings = annotatorSettings(),
                maxLines = Int.MAX_VALUE
            )
        }
    )
}

private fun TextStyle.withFontSize(fontSize: Int): TextStyle {
    return copy(
        fontSize = fontSize.sp,
        lineHeight = (fontSize + 8).sp
    )
}

object MessageBubbleDefaults {

    val userBubbleTopEnd = 4.dp
    val userBubbleBottomEnd = 12.dp
    val userBubbleTopStart = 12.dp
    val userBubbleBottomStart = 12.dp

    val UserBubbleShape = RoundedCornerShape(
        topStart = userBubbleTopStart,
        topEnd = userBubbleTopEnd,
        bottomStart = userBubbleBottomStart,
        bottomEnd = userBubbleBottomEnd
    )
}
