package com.example.api_client.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun customDropdownTextFieldColors() = TextFieldDefaults.colors(
    // Color de contenedor cambiado al gris claro
    focusedContainerColor = Color(0xFFF0F1F1),
    unfocusedContainerColor = Color(0xFFF0F1F1),
    disabledContainerColor = Color(0xFFF0F1F1),

    // Colores de texto restaurados a oscuro
    focusedTextColor = Color(0xFF1A1C1C),
    unfocusedTextColor = Color(0xFF1A1C1C),
    focusedPlaceholderColor = Color(0xFF1A1C1C),
    unfocusedPlaceholderColor = Color(0xFF1A1C1C),

    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,

    // Color de cursor e icono restaurados a oscuro
    cursorColor = Color(0xFF1A1C1C),
    focusedTrailingIconColor = Color(0xFF1A1C1C),
    unfocusedTrailingIconColor = Color(0xFF1A1C1C)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    modifier: Modifier = Modifier,
    placeholder: String,
    items: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .height(61.dp),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1A1C1C) // Color de texto restaurado
            ),
            shape = RoundedCornerShape(28.dp),
            colors = customDropdownTextFieldColors(),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF1A1C1C)
                            )
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        isExpanded = false
                    }
                )
            }
        }
    }
}
