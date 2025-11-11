package com.beradeep.aiyo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import com.mikepenz.markdown.model.MarkdownTypography

@Composable
fun fontFamily() = FontFamily.Default

data class Typography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val label3: TextStyle,
    val button: TextStyle,
    val input: TextStyle
)

private val defaultTypography =
    Typography(
        h1 =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        h2 =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        h3 =
        TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        h4 =
        TextStyle(
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        body1 =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        body2 =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.15.sp
        ),
        body3 =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.15.sp
        ),
        label1 =
        TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        label2 =
        TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        label3 =
        TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 10.sp,
            lineHeight = 12.sp,
            letterSpacing = 0.5.sp
        ),
        button =
        TextStyle(
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 1.sp
        ),
        input =
        TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        )
    )

@Composable
fun provideTypography(): Typography {
    val fontFamily = fontFamily()

    return defaultTypography.copy(
        h1 = defaultTypography.h1.copy(fontFamily = fontFamily),
        h2 = defaultTypography.h2.copy(fontFamily = fontFamily),
        h3 = defaultTypography.h3.copy(fontFamily = fontFamily),
        h4 = defaultTypography.h4.copy(fontFamily = fontFamily),
        body1 = defaultTypography.body1.copy(fontFamily = fontFamily),
        body2 = defaultTypography.body2.copy(fontFamily = fontFamily),
        body3 = defaultTypography.body3.copy(fontFamily = fontFamily),
        label1 = defaultTypography.label1.copy(fontFamily = fontFamily),
        label2 = defaultTypography.label2.copy(fontFamily = fontFamily),
        label3 = defaultTypography.label3.copy(fontFamily = fontFamily),
        button = defaultTypography.button.copy(fontFamily = fontFamily),
        input = defaultTypography.input.copy(fontFamily = fontFamily)
    )
}

val LocalTypography = staticCompositionLocalOf { defaultTypography }
val LocalTextStyle = compositionLocalOf(structuralEqualityPolicy()) { TextStyle.Default }

@Composable
fun markdownTypography(
    h1: TextStyle = provideTypography().h1,
    h2: TextStyle = provideTypography().h2,
    h3: TextStyle = provideTypography().h3,
    h4: TextStyle = provideTypography().h4,
    h5: TextStyle = provideTypography().body1,
    h6: TextStyle = provideTypography().label1,
    text: TextStyle = provideTypography().body1,
    code: TextStyle = provideTypography().body2.copy(fontFamily = FontFamily.Monospace),
    inlineCode: TextStyle = text.copy(fontFamily = FontFamily.Monospace),
    quote: TextStyle = provideTypography().body1.plus(SpanStyle(fontStyle = FontStyle.Italic)),
    paragraph: TextStyle = provideTypography().body1,
    ordered: TextStyle = provideTypography().body1,
    bullet: TextStyle = provideTypography().body1,
    list: TextStyle = provideTypography().body1,
    textLink: TextLinkStyles =
        TextLinkStyles(
            style =
            provideTypography()
                .body3
                .copy(
                    color = AiyoTheme.colors.tertiary,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                ).toSpanStyle()
        ),
    table: TextStyle = text
): MarkdownTypography = DefaultMarkdownTypography(
    h1 = h1,
    h2 = h2,
    h3 = h3,
    h4 = h4,
    h5 = h5,
    h6 = h6,
    text = text,
    quote = quote,
    code = code,
    inlineCode = inlineCode,
    paragraph = paragraph,
    ordered = ordered,
    bullet = bullet,
    list = list,
    textLink = textLink,
    table = table
)
