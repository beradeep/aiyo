package com.beradeep.aiyo.ui.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.ui.AiyoTheme
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.basics.components.Icon
import com.beradeep.aiyo.ui.basics.components.IconButton
import com.beradeep.aiyo.ui.basics.components.IconButtonVariant
import com.beradeep.aiyo.ui.basics.components.ModalBottomSheet
import com.beradeep.aiyo.ui.basics.components.Surface
import com.beradeep.aiyo.ui.basics.components.Text
import com.beradeep.aiyo.ui.basics.components.progressindicators.LinearProgressIndicator
import com.beradeep.aiyo.ui.basics.components.textfield.TextField
import java.util.Locale

@Composable
fun ModelSelectionSheet(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    isFetchingModels: Boolean,
    fetchModels: () -> Unit,
    models: List<Model>,
    selectedModel: Model,
    favoriteModelIds: Set<String>,
    onModelSelected: (Model) -> Unit,
    onToggleFavoriteModel: (Model) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        isVisible = isVisible,
        onDismissRequest = onDismiss,
        sheetGesturesEnabled = false
    ) {
        var searchText by remember { mutableStateOf("") }
        val filteredModels by remember(models, favoriteModelIds) {
            derivedStateOf {
                models
                    .filter {
                        it.id.contains(searchText, ignoreCase = true)
                    }
                    .sortedByDescending { it.id in favoriteModelIds }
            }
        }
        Column(
            modifier = modifier
                .navigationBarsPadding()
                .padding(vertical = 16.dp)
        ) {
            LaunchedEffect(Unit) {
                fetchModels()
            }

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
            AnimatedVisibility(isFetchingModels) {
                LinearProgressIndicator(Modifier.fillMaxWidth(), strokeCap = StrokeCap.Square)
            }
            LazyColumn {
                items(
                    items = filteredModels,
                    key = { it.id }
                ) { model ->
                    val isSelected = model.id == selectedModel.id
                    val isFavorite = model.id in favoriteModelIds
                    Surface(
                        color = if (isSelected) AiyoTheme.colors.surface else AiyoTheme.colors.background
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
                                Row(
                                    modifier = Modifier.padding(top = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f, fill = false),
                                        text = model.id.substringAfter('/'),
                                        style = LocalTypography.current.body2
                                    )
                                    model.pricingLabel()?.let { pricing ->
                                        Text(
                                            modifier = Modifier.padding(start = 8.dp),
                                            text = pricing,
                                            style = LocalTypography.current.label3,
                                            color = AiyoTheme.colors.tertiary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            IconButton(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                variant = IconButtonVariant.PrimaryGhost,
                                onClick = { onToggleFavoriteModel(model) }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) {
                                        Icons.Filled.Star
                                    } else {
                                        Icons.Outlined.StarOutline
                                    },
                                    contentDescription = if (isFavorite) "Unfavorite" else "Favorite",
                                    tint = if (isFavorite) {
                                        AiyoTheme.colors.primary
                                    } else {
                                        AiyoTheme.colors.tertiary
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Model.pricingLabel(): String? {
    val input = inputPricePerMillion ?: return null
    val output = outputPricePerMillion ?: return null
    if (input == 0.0 && output == 0.0) return "Free"
    return "${formatPricePerMillion(input)} in | ${formatPricePerMillion(output)} out"
}

private fun formatPricePerMillion(price: Double): String {
    val formatted = when {
        price == 0.0 -> "0"
        price < 0.01 -> String.format(Locale.US, "%.4f", price)
        price < 100 -> String.format(Locale.US, "%.2f", price)
        else -> String.format(Locale.US, "%.0f", price)
    }
    return "$$formatted/M"
}