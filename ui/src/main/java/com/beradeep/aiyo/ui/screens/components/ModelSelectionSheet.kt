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
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.ui.AiyoTheme
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.basics.components.Icon
import com.beradeep.aiyo.ui.basics.components.ModalBottomSheet
import com.beradeep.aiyo.ui.basics.components.Surface
import com.beradeep.aiyo.ui.basics.components.Text
import com.beradeep.aiyo.ui.basics.components.progressindicators.LinearProgressIndicator
import com.beradeep.aiyo.ui.basics.components.textfield.TextField

@Composable
fun ModelSelectionSheet(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    isFetchingModels: Boolean,
    fetchModels: () -> Unit,
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