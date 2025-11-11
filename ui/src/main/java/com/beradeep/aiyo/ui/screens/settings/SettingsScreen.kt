package com.beradeep.aiyo.ui.screens.settings

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ModelTraining
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.domain.model.ThemeType
import com.beradeep.aiyo.ui.AiyoTheme
import com.beradeep.aiyo.ui.DarkColors
import com.beradeep.aiyo.ui.LightColors
import com.beradeep.aiyo.ui.LocalColors
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.basics.components.HorizontalDivider
import com.beradeep.aiyo.ui.basics.components.Icon
import com.beradeep.aiyo.ui.basics.components.IconButton
import com.beradeep.aiyo.ui.basics.components.IconButtonVariant
import com.beradeep.aiyo.ui.basics.components.Scaffold
import com.beradeep.aiyo.ui.basics.components.Text
import com.beradeep.aiyo.ui.basics.components.textfield.OutlinedTextField
import com.beradeep.aiyo.ui.basics.components.topbar.TopBar
import com.beradeep.aiyo.ui.screens.chat.components.ModelSelectorChip
import com.beradeep.aiyo.ui.screens.components.ModelSelectionSheet

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopBar {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        variant = IconButtonVariant.PrimaryGhost
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Spacer(Modifier.width(24.dp))
                    Text("Settings", style = LocalTypography.current.h1)
                }
            }
        }
    ) { innerPadding ->
        val focusManager = LocalFocusManager.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                    }
                }
        ) {
            SettingsSection(
                title = "API Configuration",
                icon = Icons.Filled.Key
            ) {
                ApiKeySetting(
                    apiKey = uiState.apiKey,
                    onApiKeyChanged = { newKey ->
                        viewModel.onUiEvent(SettingsUiEvent.OnSetApiKey(newKey))
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            SettingsSection(
                title = "Model Configuration",
                icon = Icons.Filled.ModelTraining
            ) {
                ModelSelectionSetting(
                    selectedModel = uiState.selectedModel,
                    onShowModelSheet = {
                        viewModel.onUiEvent(SettingsUiEvent.OnShowModelSelectionSheet)
                    }
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            SettingsSection(
                title = "Theme Configuration",
                icon = Icons.Filled.InvertColors
            ) {
                ThemeSelectionSetting(
                    modifier = Modifier.fillMaxWidth(),
                    selectedThemeType = uiState.themeType,
                    onUpdateThemeType = { newThemeType ->
                        viewModel.onUiEvent(SettingsUiEvent.OnUpdateThemeType(newThemeType))
                    }
                )
            }
        }

        ModelSelectionSheet(
            isVisible = uiState.showModelSelectionSheet,
            isFetchingModels = uiState.isFetchingModels,
            fetchModels = {
                viewModel.onUiEvent(SettingsUiEvent.OnFetchModels)
            },
            models = uiState.models,
            selectedModel = uiState.selectedModel,
            onModelSelected = { model ->
                viewModel.onUiEvent(SettingsUiEvent.OnModelSelected(model))
            },
            onDismiss = {
                viewModel.onUiEvent(SettingsUiEvent.OnDismissModelSelectionSheet)
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AiyoTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = LocalTypography.current.h3
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
private fun ApiKeySetting(
    apiKey: String?,
    onApiKeyChanged: (String) -> Unit
) {
    var isApiKeyVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "API Key",
            style = LocalTypography.current.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = apiKey ?: "",
                onValueChange = onApiKeyChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Enter your API key") },
                trailingIcon = {
                    IconButton(
                        variant = IconButtonVariant.PrimaryGhost,
                        onClick = { isApiKeyVisible = !isApiKeyVisible }
                    ) {
                        Icon(
                            imageVector = if (isApiKeyVisible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                            contentDescription = if (isApiKeyVisible) "Hide API key" else "Show API key"
                        )
                    }
                },
                visualTransformation = if (isApiKeyVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                supportingText = {
                    Text(
                        text = "Your API key is only stored locally on this device.",
                        color = AiyoTheme.colors.textSecondary,
                        style = AiyoTheme.typography.body3
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ModelSelectionSetting(
    selectedModel: Model,
    onShowModelSheet: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Default Model",
                    style = LocalTypography.current.body1
                )
                Text(
                    text = "Requires app restart to apply change.",
                    color = AiyoTheme.colors.textSecondary,
                    style = AiyoTheme.typography.body3
                )
            }
            ModelSelectorChip(selectedModel, onShowModelSheet)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun ThemeSelectionSetting(
    modifier: Modifier = Modifier,
    isDarkModeOn: Boolean = isSystemInDarkTheme(),
    selectedThemeType: ThemeType,
    onUpdateThemeType: (ThemeType) -> Unit
) {
    val colors by remember(selectedThemeType) {
        derivedStateOf {
            when (selectedThemeType) {
                ThemeType.Light -> LightColors
                ThemeType.Dark -> DarkColors
                ThemeType.System -> if (isDarkModeOn) DarkColors else LightColors
            }
        }
    }
    LocalColors.current.value = colors
    SingleChoiceSegmentedButtonRow(modifier) {
        ThemeType.entries.forEach { themeType ->
            SegmentedButton(
                selected = themeType == selectedThemeType,
                onClick = { onUpdateThemeType(themeType) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = themeType.ordinal,
                    count = ThemeType.entries.size
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = AiyoTheme.colors.primary,
                    activeContentColor = AiyoTheme.colors.onPrimary,
                    inactiveContainerColor = AiyoTheme.colors.surface,
                    inactiveContentColor = AiyoTheme.colors.onSurface
                )
            ) {
                androidx.compose.material3.Text(
                    text = themeType.name,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
