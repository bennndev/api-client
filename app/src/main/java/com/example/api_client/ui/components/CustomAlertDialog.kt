package com.example.api_client.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

enum class AlertDialogType {
    SUCCESS, ERROR
}

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    type: AlertDialogType,
    message: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit
) {
    val iconBackgroundColor: Color
    val buttonBackgroundColor: Color
    val buttonTextColor: Color

    when (type) {
        AlertDialogType.SUCCESS -> {
            iconBackgroundColor = Color(0xFFE5F0FD) // Azul claro para éxito
            buttonBackgroundColor = Color(0xFF366A9A) // Azul oscuro para éxito
            buttonTextColor = Color.White
        }
        AlertDialogType.ERROR -> {
            iconBackgroundColor = Color(0xFFFDE5E5) // Rojo claro para error
            buttonBackgroundColor = Color(0xFFC93D3D) // Rojo oscuro para error
            buttonTextColor = Color.White
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(28.dp), // Corner radius de 28
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 25.dp, vertical = 25.dp), // Padding horizontal y vertical de 25
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp) // Gap de 30 entre elementos
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(60.dp) // Tamaño del círculo del icono
                        .background(iconBackgroundColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check, // Usamos el Check para ambos, el color lo diferencia
                        contentDescription = if (type == AlertDialogType.SUCCESS) "Éxito" else "Error",
                        tint = when (type) { // Color del check dentro del círculo
                            AlertDialogType.SUCCESS -> Color(0xFF366A9A) // Azul oscuro
                            AlertDialogType.ERROR -> Color(0xFFC93D3D) // Rojo oscuro
                        },
                        modifier = Modifier.size(36.dp) // Tamaño del icono Check
                    )
                }

                // Mensaje
                Text(
                    text = message,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D5F5F),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp // Para que se vea bien en dos líneas si es necesario
                )

                // Botón "Aceptar"
                Button(
                    onClick = onConfirmClick,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonBackgroundColor,
                        contentColor = buttonTextColor
                    )
                ) {
                    Text(
                        text = "Aceptar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp) // Padding interno del texto del botón
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CustomAlertDialogSuccessPreview() {
    CustomAlertDialog(
        type = AlertDialogType.SUCCESS,
        message = "OT asignada correctamente",
        onDismissRequest = {},
        onConfirmClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CustomAlertDialogErrorPreview() {
    CustomAlertDialog(
        type = AlertDialogType.ERROR,
        message = "OT marcada como error",
        onDismissRequest = {},
        onConfirmClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun CustomAlertDialogLongMessagePreview() {
    CustomAlertDialog(
        type = AlertDialogType.SUCCESS,
        message = "¡Felicidades! Tu orden de trabajo ha sido asignada correctamente y el proceso ha finalizado.",
        onDismissRequest = {},
        onConfirmClick = {}
    )
}
