package com.beradeep.aiyo.ui.screens.chat

import android.content.ClipData
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.window.PopupPositionProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beradeep.aiyo.domain.model.Conversation
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.Reason
import com.beradeep.aiyo.ui.LocalColors
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.R
import com.beradeep.aiyo.ui.components.AlertDialog
import com.beradeep.aiyo.ui.components.Button
import com.beradeep.aiyo.ui.components.ButtonVariant
import com.beradeep.aiyo.ui.components.ElevatedChip
import com.beradeep.aiyo.ui.components.Icon
import com.beradeep.aiyo.ui.components.IconButton
import com.beradeep.aiyo.ui.components.IconButtonVariant
import com.beradeep.aiyo.ui.components.ModalBottomSheet
import com.beradeep.aiyo.ui.components.Scaffold
import com.beradeep.aiyo.ui.components.Surface
import com.beradeep.aiyo.ui.components.Text
import com.beradeep.aiyo.ui.components.Tooltip
import com.beradeep.aiyo.ui.components.TooltipBox
import com.beradeep.aiyo.ui.components.card.Card
import com.beradeep.aiyo.ui.components.rememberTooltipPositionProvider
import com.beradeep.aiyo.ui.components.rememberTooltipState
import com.beradeep.aiyo.ui.components.textfield.OutlinedTextField
import com.beradeep.aiyo.ui.components.textfield.TextField
import com.beradeep.aiyo.ui.components.topbar.TopBar
import com.beradeep.aiyo.ui.markdownColor
import com.beradeep.aiyo.ui.markdownTypography
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.model.State
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val chatUiState by viewModel.uiState.collectAsStateWithLifecycle()
    ChatScreen(
        uiState = chatUiState,
        onUiEvent = viewModel::onUiEvent
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChatScreen(
    modifier: Modifier = Modifier,
    uiState: ChatUiState,
    onUiEvent: (ChatUiEvent) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.fillMaxWidth(0.7f),
                drawerContainerColor = LocalColors.current.surface,
                drawerContentColor = LocalColors.current.onSurface
            ) {
                Column(
                    Modifier.systemBarsPadding()
                ) {
                    Button(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = { onUiEvent(ChatUiEvent.OnCreateNewConversation) },
                        variant = ButtonVariant.PrimaryOutlined
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.Chat)
                            Spacer(Modifier.width(12.dp))
                            Text(text = "New Chat")
                        }
                    }

                    ConversationList(
                        conversations = uiState.conversations,
                        modifier = Modifier.weight(1f),
                        selectedConversationId = uiState.selectedConversationId,
                        onConversationSelected = { conversation ->
                            onUiEvent(
                                ChatUiEvent.OnConversationSelected(conversation)
                            )
                        }
                    )
                }
            }
        }
    ) {
        var showApiKeyDialog by remember { mutableStateOf(false) }
        var showModelSheet by remember { mutableStateOf(false) }

        Scaffold(
            modifier = modifier,
            topBar = {
                TopBar {
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                    onUiEvent(ChatUiEvent.OnLoadConversations)
                                }
                            },
                            enabled = drawerState.isClosed || drawerState.isAnimationRunning,
                            variant = IconButtonVariant.PrimaryGhost
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Open Menu"
                            )
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            ModelSelector(
                                model = uiState.selectedModel,
                                onClick = { showModelSheet = true }
                            )
                        }
                        IconButton(
                            onClick = { showApiKeyDialog = true },
                            variant = IconButtonVariant.PrimaryGhost
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "Settings"
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            val focusManager = LocalFocusManager.current

            Box(
                Modifier.fillMaxSize()
            ) {
                Column(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .consumeWindowInsets(WindowInsets.systemBars)
                        .imePadding()
                        .padding(8.dp)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                focusManager.clearFocus()
                            }
                        },
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val lazyListState = rememberLazyListState()
                    ListAutoScrollToBottom(2, lazyListState)

                    LazyListPreloader(
                        listState = lazyListState,
                        selectedConversationId = uiState.selectedConversationId,
                        preloadRequest = { onUiEvent(ChatUiEvent.OnPreloadMarkdownRequest(it)) },
                        cancelPreviousRequests = {
                            onUiEvent(ChatUiEvent.OnCancelPreloadMarkdownJobs)
                        },
                        maxPreload = uiState.maxPreload
                    )

                    var longPressX by remember { mutableIntStateOf(0) }
                    var longPressY by remember { mutableIntStateOf(0) }
                    val positionProvider = tooltipPositionProvider(longPressX, longPressY)
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = lazyListState
                    ) {
                        items(
                            items = uiState.messages,
                            key = { it.id }
                        ) { msg ->
                            Spacer(modifier = Modifier.height(8.dp))
                            val tooltipState = rememberTooltipState(isPersistent = true)
                            TooltipBox(
                                state = tooltipState,
                                positionProvider = positionProvider,
                                tooltip = {
                                    val clipboard = LocalClipboard.current
                                    Tooltip(
                                        caretSize = DpSize(0.dp, 0.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Button(
                                            variant = ButtonVariant.PrimaryGhost,
                                            onClick = {
                                                clipboard.nativeClipboard.setPrimaryClip(
                                                    ClipData.newPlainText(
                                                        "Copied from Aiyo",
                                                        msg.content ?: ""
                                                    )
                                                )
                                                tooltipState.dismiss()
                                            }
                                        ) {
                                            Text(text = "Copy")
                                        }
                                    }
                                }
                            ) {
                                val scope = rememberCoroutineScope()
                                Box(
                                    Modifier
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onLongPress = { offset ->
                                                    scope.launch {
                                                        longPressX = offset.x.fastRoundToInt()
                                                        longPressY = offset.y.roundToInt()
                                                        tooltipState.show()
                                                    }
                                                }
                                            )
                                        }
                                ) {
                                    MessageBubble(
                                        content = msg.content ?: "",
                                        isUser = msg.isUser,
                                        markdownState = msg.markdownState
                                    )
                                }
                            }
                        }
                        if (uiState.isLoadingResponse) {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                DotsTyping()
                            }
                        }
                        uiState.streamingResponse?.let { content ->
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                MessageBubble(content = content, isUser = false, State.Loading())
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ChatInputTextField(
                            value = uiState.inputText,
                            onValueChange = { onUiEvent(ChatUiEvent.OnInputTextEdit(it)) },
                            onSend = { onUiEvent(ChatUiEvent.OnMessageSend) },
                            onWebSearch = { onUiEvent(ChatUiEvent.OnWebSearchTapped) },
                            isWebSearchEnabled = uiState.isWebSearchEnabled,
                            isLoadingOrStreamingResponse =
                            uiState.isLoadingResponse || uiState.isStreamingResponse,
                            onReason = { onUiEvent(ChatUiEvent.OnReason(it)) },
                            reasonEffort = uiState.reasoningEffort
                        )
                    }
                }

                val isKeyBoardVisible = WindowInsets.isImeVisible
                if (uiState.messages.isEmpty() && !isKeyBoardVisible) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "AIYO",
                            style = LocalTypography.current.h1,
                            color = LocalColors.current.textSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Start a conversation",
                            style = LocalTypography.current.body1,
                            color = LocalColors.current.textDisabled,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
            }
        }

        ModelSelectionSheet(
            isVisible = showModelSheet,
            models = uiState.models,
            selectedModel = uiState.selectedModel,
            onModelSelected = { model ->
                onUiEvent(ChatUiEvent.OnModelSelected(model))
                showModelSheet = false
            },
            onDismiss = { showModelSheet = false }
        )

        if (showApiKeyDialog || uiState.apiKey.isNullOrBlank()) {
            ApiKeyDialog(
                initial = uiState.apiKey ?: "",
                onSave = { key ->
                    onUiEvent(ChatUiEvent.OnSetApiKey(key))
                    onUiEvent(ChatUiEvent.OnFetchModels)
                    showApiKeyDialog = false
                },
                onDismiss = { showApiKeyDialog = false }
            )
        }
    }
}

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
                            painter = painterResource(R.drawable.neurology_24px),
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

@Composable
private fun MessageBubble(content: String, isUser: Boolean, markdownState: State) {
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

@Composable
private fun ApiKeyDialog(
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
                    Text(text = "Enter your OpenRouter API key")
                    Spacer(modifier = Modifier.height(12.dp))
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
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(text = "Save", onClick = { onSave(key) }, enabled = key.isNotBlank())
                    }
                }
            }
        }
    )
}

@Composable
private fun ModelSelector(model: Model, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = model.ownedBy ?: model.id.substringBefore('/'),
                style = LocalTypography.current.label1,
                color = LocalColors.current.textSecondary,
                fontStyle = FontStyle.Italic
            )
            ElevatedChip(
                onClick = onClick,
                modifier = Modifier.padding(top = 2.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(vertical = 1.dp, horizontal = 4.dp),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        tint = LocalColors.current.tertiary,
                        contentDescription = "Select Model",
                        modifier = Modifier.size(16.dp)
                    )
                }
            ) {
                Spacer(Modifier.width(6.dp))
                Text(
                    text = model.id.substringAfter('/'),
                    color = LocalColors.current.tertiary,
                    style = LocalTypography.current.body3
                )
            }
        }
    }
}

@Composable
fun ModelSelectionSheet(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    models: List<Model>,
    selectedModel: Model,
    onModelSelected: (Model) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        isVisible = isVisible,
        onDismissRequest = onDismiss,
        sheetGesturesEnabled = false
    ) {
        var searchText by remember { mutableStateOf("") }
        val filteredModels by remember {
            derivedStateOf {
                models.filter {
                    it.id.contains(searchText, ignoreCase = true)
                }
            }
        }
        Column(
            modifier =
            modifier
                .navigationBarsPadding()
                .padding(vertical = 16.dp)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Select Model",
                style = LocalTypography.current.h2
            )
            Spacer(modifier = Modifier.height(12.dp))
            TextField(
                modifier = Modifier.padding(horizontal = 16.dp),
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = { Icon(Icons.Rounded.Search) },
                placeholder = { Text("Search") },
                shape = CircleShape
            )
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn {
                items(
                    items = filteredModels,
                    key = { it.id }
                ) { model ->
                    val isSelected = model.id == selectedModel.id
                    Surface(
                        color = if (isSelected) LocalColors.current.surface else LocalColors.current.background
                    ) {
                        Row(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    onModelSelected(model)
                                }
                        ) {
                            Column(
                                modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(vertical = 12.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = model.ownedBy ?: model.id.substringBefore('/'),
                                    style = LocalTypography.current.h4
                                )
                                Text(
                                    modifier = Modifier.padding(top = 2.dp),
                                    text = model.id.substringAfter('/'),
                                    style = LocalTypography.current.body2
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationList(
    conversations: List<Conversation>,
    modifier: Modifier = Modifier,
    onConversationSelected: (Conversation) -> Unit,
    selectedConversationId: String?
) {
    val lazyListState = rememberLazyListState()
    ListAutoScrollToTop(0, lazyListState)

    LazyColumn(modifier = modifier, state = lazyListState) {
        items(conversations, key = { it.id }) { conversation ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color =
                if (selectedConversationId == conversation.id.toString()) {
                    LocalColors.current.background
                } else {
                    LocalColors.current.surface
                }
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onConversationSelected(conversation)
                        }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = conversation.title,
                        style = LocalTypography.current.body1,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color =
                        if (selectedConversationId == conversation.id.toString()) {
                            LocalColors.current.tertiary
                        } else {
                            LocalColors.current.primary
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DotsTyping() {
    val maxOffset = 6f

    @Composable
    fun Dot(offset: Float) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = LocalColors.current.secondary,
                shape = CircleShape
            )
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec =
        infiniteRepeatable(
            animation =
            keyframes {
                durationMillis = delayUnit * 4
                0f at delay using LinearEasing
                maxOffset at delay + delayUnit using LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp

        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}

val dotSize = 6.dp
const val delayUnit = 300

@Composable
fun tooltipPositionProvider(x: Int, y: Int): PopupPositionProvider {
    return remember(x, y) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x1 = anchorBounds.left + x
                val y1 = anchorBounds.top + y
                return IntOffset(x1, y1)
            }
        }
    }
}
