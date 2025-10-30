package com.example.api_client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.api_client.ui.components.CustomDropdownField
import com.example.api_client.ui.components.CustomTextField

@Composable
fun NuevoClienteScreen(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    onRegistrarClick: () -> Unit = {}
) {
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }
    var tipoDocumento by remember { mutableStateOf("") }
    var numeroDocumento by remember { mutableStateOf("") }

    val tiposDocumento = listOf("DNI", "RUC", "Pasaporte", "Carnet de Extranjería")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd) //
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color(0xFF5D5F5F),
                    modifier = Modifier.size(24.dp)
                )
            }
        }


        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Registrar nuevo cliente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1C1C)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Información básica del cliente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF5D5F5F)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Datos personales",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF5D5F5F),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomTextField(
            value = nombres,
            onValueChange = { nombres = it },
            placeholder = "Nombres",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            placeholder = "Apellidos",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Contacto",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF5D5F5F),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomTextField(
            value = telefono,
            onValueChange = { telefono = it },
            placeholder = "Teléfono",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomTextField(
            value = correoElectronico,
            onValueChange = { correoElectronico = it },
            placeholder = "Correo electrónico",
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Identificación",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF5D5F5F),
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomDropdownField(
            placeholder = "Tipo de documento",
            items = tiposDocumento,
            selectedItem = tipoDocumento,
            onItemSelected = { tipoDocumento = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )
        CustomTextField(
            value = numeroDocumento,
            onValueChange = { numeroDocumento = it },
            placeholder = "Número de documento",
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = onRegistrarClick,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF366A9A),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Registrar",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NuevoClienteScreenPreview() {
    NuevoClienteScreen(
        onCloseClick = { },
        onRegistrarClick = { }
    )
}
