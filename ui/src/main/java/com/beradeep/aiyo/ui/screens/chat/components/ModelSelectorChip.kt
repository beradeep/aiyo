package com.beradeep.aiyo.ui.screens.chat.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.beradeep.aiyo.domain.model.Model
import com.beradeep.aiyo.ui.LocalColors
import com.beradeep.aiyo.ui.LocalTypography
import com.beradeep.aiyo.ui.basics.components.ElevatedChip
import com.beradeep.aiyo.ui.basics.components.Icon
import com.beradeep.aiyo.ui.basics.components.Text

@Composable
fun ModelSelectorChip(model: Model, onClick: () -> Unit) {
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