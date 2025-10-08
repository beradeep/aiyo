package com.beradeep.aiyo.ui.basics.components.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.ui.AiyoTheme
import com.beradeep.aiyo.ui.basics.components.textfield.base.CommonDecorationBox
import com.beradeep.aiyo.ui.basics.components.textfield.base.FocusedOutlineThickness
import com.beradeep.aiyo.ui.basics.components.textfield.base.HorizontalIconPadding
import com.beradeep.aiyo.ui.basics.components.textfield.base.LabelBottomPadding
import com.beradeep.aiyo.ui.basics.components.textfield.base.SupportingTopPadding
import com.beradeep.aiyo.ui.basics.components.textfield.base.TextFieldColors
import com.beradeep.aiyo.ui.basics.components.textfield.base.TextFieldHorizontalPadding
import com.beradeep.aiyo.ui.basics.components.textfield.base.TextFieldMinHeight
import com.beradeep.aiyo.ui.basics.components.textfield.base.TextFieldVerticalPadding
import com.beradeep.aiyo.ui.basics.components.textfield.base.UnfocusedOutlineThickness
import com.beradeep.aiyo.ui.basics.components.textfield.base.containerOutline

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AiyoTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = TextFieldDefaults.Shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value)
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
            modifier
                .defaultMinSize(
                    minHeight = TextFieldDefaults.MinHeight
                ).fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    shape = shape
                )
            }
        )
    }
}

@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = AiyoTheme.typography.input,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    shape: Shape = TextFieldDefaults.Shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    cursorBrush: Brush = SolidColor(colors.cursorColor(isError).value)
) {
    val textColor =
        textStyle.color.takeOrElse {
            colors.textColor(enabled, isError, interactionSource).value
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.selectionColors) {
        BasicTextField(
            modifier =
            modifier
                .defaultMinSize(
                    minHeight = TextFieldDefaults.MinHeight
                ).fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.DecorationBox(
                    value = value.text,
                    innerTextField = innerTextField,
                    visualTransformation = visualTransformation,
                    label = label,
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon,
                    prefix = prefix,
                    suffix = suffix,
                    supportingText = supportingText,
                    enabled = enabled,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = TextFieldDefaults.colors(),
                    shape = shape
                )
            }
        )
    }
}

@Immutable
object TextFieldDefaults {
    val MinHeight = TextFieldMinHeight
    val Shape: Shape = RoundedCornerShape(8.dp)

    private fun contentPadding(
        start: Dp = TextFieldHorizontalPadding,
        end: Dp = TextFieldHorizontalPadding,
        top: Dp = TextFieldVerticalPadding,
        bottom: Dp = TextFieldVerticalPadding
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun labelPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = LabelBottomPadding
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    private fun supportingTextPadding(
        start: Dp = 0.dp,
        top: Dp = SupportingTopPadding,
        end: Dp = TextFieldHorizontalPadding,
        bottom: Dp = 0.dp
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun leadingIconPadding(
        start: Dp = HorizontalIconPadding,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = 0.dp
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    private fun trailingIconPadding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = HorizontalIconPadding,
        bottom: Dp = 0.dp
    ): PaddingValues = PaddingValues(start, top, end, bottom)

    @Composable
    fun containerBorderThickness(interactionSource: InteractionSource): Dp {
        val focused by interactionSource.collectIsFocusedAsState()

        return if (focused) FocusedOutlineThickness else UnfocusedOutlineThickness
    }

    @Composable
    fun DecorationBox(
        value: String,
        innerTextField: @Composable () -> Unit,
        enabled: Boolean,
        visualTransformation: VisualTransformation,
        interactionSource: InteractionSource,
        isError: Boolean = false,
        label: @Composable (() -> Unit)? = null,
        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        prefix: @Composable (() -> Unit)? = null,
        suffix: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,
        shape: Shape = Shape,
        colors: TextFieldColors = colors(),
        container: @Composable () -> Unit = {
            ContainerBox(enabled, isError, interactionSource, colors, shape)
        }
    ) {
        CommonDecorationBox(
            value = value,
            innerTextField = innerTextField,
            visualTransformation = visualTransformation,
            placeholder = placeholder,
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            enabled = enabled,
            isError = isError,
            interactionSource = interactionSource,
            colors = colors,
            contentPadding = contentPadding(),
            labelPadding = labelPadding(),
            supportingTextPadding = supportingTextPadding(),
            leadingIconPadding = leadingIconPadding(),
            trailingIconPadding = trailingIconPadding(),
            container = container
        )
    }

    @Composable
    fun ContainerBox(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource,
        colors: TextFieldColors,
        shape: Shape = Shape,
        borderThickness: Dp = containerBorderThickness(interactionSource)
    ) {
        Box(
            Modifier
                .background(colors.containerColor(enabled, isError, interactionSource).value, shape)
                .containerOutline(
                    enabled,
                    isError,
                    interactionSource,
                    colors,
                    borderThickness,
                    shape
                )
        )
    }

    @Composable
    fun colors(): TextFieldColors = TextFieldColors(
        focusedTextColor = AiyoTheme.colors.text,
        unfocusedTextColor = AiyoTheme.colors.text,
        disabledTextColor = AiyoTheme.colors.onDisabled,
        errorTextColor = AiyoTheme.colors.text,
        focusedContainerColor = AiyoTheme.colors.surface,
        unfocusedContainerColor = AiyoTheme.colors.surface,
        disabledContainerColor = AiyoTheme.colors.disabled,
        errorContainerColor = AiyoTheme.colors.surface,
        cursorColor = AiyoTheme.colors.primary,
        errorCursorColor = AiyoTheme.colors.error,
        textSelectionColors = LocalTextSelectionColors.current,
        focusedOutlineColor = AiyoTheme.colors.transparent,
        unfocusedOutlineColor = AiyoTheme.colors.transparent,
        disabledOutlineColor = AiyoTheme.colors.transparent,
        errorOutlineColor = AiyoTheme.colors.error,
        focusedLeadingIconColor = AiyoTheme.colors.primary,
        unfocusedLeadingIconColor = AiyoTheme.colors.primary,
        disabledLeadingIconColor = AiyoTheme.colors.onDisabled,
        errorLeadingIconColor = AiyoTheme.colors.primary,
        focusedTrailingIconColor = AiyoTheme.colors.primary,
        unfocusedTrailingIconColor = AiyoTheme.colors.primary,
        disabledTrailingIconColor = AiyoTheme.colors.onDisabled,
        errorTrailingIconColor = AiyoTheme.colors.primary,
        focusedLabelColor = AiyoTheme.colors.primary,
        unfocusedLabelColor = AiyoTheme.colors.primary,
        disabledLabelColor = AiyoTheme.colors.textDisabled,
        errorLabelColor = AiyoTheme.colors.error,
        focusedPlaceholderColor = AiyoTheme.colors.textSecondary,
        unfocusedPlaceholderColor = AiyoTheme.colors.textSecondary,
        disabledPlaceholderColor = AiyoTheme.colors.textDisabled,
        errorPlaceholderColor = AiyoTheme.colors.textSecondary,
        focusedSupportingTextColor = AiyoTheme.colors.primary,
        unfocusedSupportingTextColor = AiyoTheme.colors.primary,
        disabledSupportingTextColor = AiyoTheme.colors.textDisabled,
        errorSupportingTextColor = AiyoTheme.colors.error,
        focusedPrefixColor = AiyoTheme.colors.primary,
        unfocusedPrefixColor = AiyoTheme.colors.primary,
        disabledPrefixColor = AiyoTheme.colors.onDisabled,
        errorPrefixColor = AiyoTheme.colors.primary,
        focusedSuffixColor = AiyoTheme.colors.primary,
        unfocusedSuffixColor = AiyoTheme.colors.primary,
        disabledSuffixColor = AiyoTheme.colors.onDisabled,
        errorSuffixColor = AiyoTheme.colors.primary
    )
}
