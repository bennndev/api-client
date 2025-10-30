package com.example.api_client.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClientInfoCard(
    modifier: Modifier = Modifier,
    clientName: String,
    clientId: String,
    onActionClick: () -> Unit, // <-- 1. Nuevo parámetro para la acción
    onCardClick: () -> Unit = {} // <-- Nuevo parámetro para clic en la tarjeta
) {
    Surface(
        modifier = modifier.clickable { onCardClick() },
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFF0F1F1)
    ) {
        // 2. Cambiado a Row para alinear horizontalmente
        Row(
            modifier = Modifier
                .fillMaxSize()
                // Mantenemos el padding original
                .padding(horizontal = 10.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Separa el texto y el botón
        ) {
            // Columna para los textos (como antes)
            Column(
                modifier = Modifier.weight(1f), // Ocupa el espacio disponible
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = clientName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1C1C)
                )

                Text(
                    text = clientId,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D5F5F)
                )
            }

            // 3. Nuevo botón de acción (Campana)
            Box(
                modifier = Modifier
                    .size(40.dp) // Tamaño del círculo de fondo
                    // Color de fondo rojo pálido (tomado del diálogo de error)
                    .background(Color(0xFFFDE5E5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onActionClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Acción", // O "Eliminar" si prefieres
                        // Color de icono rojo (tomado del diálogo de error)
                        tint = Color(0xFFC93D3D),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientInfoCardPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ClientInfoCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(103.dp),
            clientName = "\"Nombre del Cliente\"",
            clientId = "\"DNI/RUC\"",
            onActionClick = {}, // <-- 4. Añadido al preview
            onCardClick = {} // <-- Añadido al preview
        )
    }
}
