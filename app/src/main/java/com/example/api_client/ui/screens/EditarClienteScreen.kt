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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import com.example.api_client.data.model.ClienteModel
import com.example.api_client.network.ClienteApiService
import com.example.api_client.ui.components.AlertDialogType
import com.example.api_client.ui.components.CustomAlertDialog
import com.example.api_client.ui.components.CustomDropdownField
import com.example.api_client.ui.components.CustomTextField
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun EditarClienteScreen(
    modifier: Modifier = Modifier,
    cliente: ClienteModel,
    onCloseClick: () -> Unit = {},
    onClienteActualizado: () -> Unit = {}
) {
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correoElectronico by remember { mutableStateOf("") }
    var tipoDocumento by remember { mutableStateOf("") }
    var numeroDocumento by remember { mutableStateOf("") }
    
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    
    val tiposDocumento = listOf("DNI", "RUC")
    
    // Configurar Retrofit
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService = remember { retrofit.create(ClienteApiService::class.java) }
    
    // Cargar datos del cliente al iniciar
    LaunchedEffect(cliente) {
        nombres = cliente.nombre ?: ""
        apellidos = cliente.apellido ?: ""
        telefono = cliente.telefono ?: ""
        correoElectronico = cliente.email ?: ""
        numeroDocumento = cliente.numeroDocumento ?: ""
        tipoDocumento = cliente.tipoDocumento ?: "DNI"
    }
    
    // Función para actualizar cliente
    fun actualizarCliente() {
        if (nombres.isBlank() || apellidos.isBlank() || telefono.isBlank() || 
            correoElectronico.isBlank() || tipoDocumento.isBlank() || numeroDocumento.isBlank()) {
            errorMessage = "Por favor, completa todos los campos"
            showErrorDialog = true
            return
        }
        
        isLoading = true
        scope.launch {
            try {
                Log.d("EditarCliente", "Iniciando actualización de cliente ID: ${cliente.id}")
                Log.d("EditarCliente", "Datos enviados: nombres=$nombres, apellidos=$apellidos, telefono=$telefono, email=$correoElectronico, tipoDocumento=$tipoDocumento, numeroDocumento=$numeroDocumento")
                
                val clienteActualizado = ClienteModel(
                    id = cliente.id,
                    nombre = nombres,
                    apellido = apellidos,
                    telefono = telefono,
                    email = correoElectronico,
                    tipoDocumento = tipoDocumento,
                    numeroDocumento = numeroDocumento
                )
                
                Log.d("EditarCliente", "Cliente actualizado: $clienteActualizado")
                Log.d("EditarCliente", "Enviando petición al servidor...")
                
                val response = apiService.updateCliente(cliente.id ?: return@launch, clienteActualizado)
                
                Log.d("EditarCliente", "Respuesta recibida - Código: ${response.code()}")
                Log.d("EditarCliente", "Respuesta exitosa: ${response.isSuccessful}")
                Log.d("EditarCliente", "Cuerpo de respuesta: ${response.body()}")
                Log.d("EditarCliente", "Error de respuesta: ${response.errorBody()?.string()}")
                
                if (response.isSuccessful && response.body()?.success == true) {
                    Log.d("EditarCliente", "Cliente actualizado exitosamente")
                    showSuccessDialog = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("EditarCliente", "Error en respuesta: $errorBody")
                    errorMessage = response.body()?.message ?: errorBody ?: "Error al actualizar cliente"
                    showErrorDialog = true
                }
            } catch (e: Exception) {
                Log.e("EditarCliente", "Excepción durante la actualización", e)
                errorMessage = "Error de conexión: ${e.message}"
                showErrorDialog = true
            } finally {
                isLoading = false
            }
        }
    }

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
                    .align(Alignment.CenterEnd)
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
                text = "Editar cliente",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1C1C)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Actualizar información del cliente",
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
                onClick = { actualizarCliente() },
                enabled = !isLoading,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF366A9A),
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Actualizar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
    
    // Diálogo de éxito
    if (showSuccessDialog) {
        CustomAlertDialog(
            type = AlertDialogType.SUCCESS,
            message = "Cliente actualizado exitosamente",
            onDismissRequest = {
                showSuccessDialog = false
                onClienteActualizado()
            },
            onConfirmClick = {
                showSuccessDialog = false
                onClienteActualizado()
            }
        )
    }
    
    // Diálogo de error
    if (showErrorDialog) {
        CustomAlertDialog(
            type = AlertDialogType.ERROR,
            message = errorMessage,
            onDismissRequest = {
                showErrorDialog = false
            },
            onConfirmClick = {
                showErrorDialog = false
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditarClienteScreenPreview() {
    val clienteEjemplo = ClienteModel(
        id = 1,
        nombre = "Juan",
        apellido = "Pérez",
        telefono = "987654321",
        email = "juan@example.com",
        tipoDocumento = "DNI",
        numeroDocumento = "12345678"
    )
    
    EditarClienteScreen(
        cliente = clienteEjemplo,
        onCloseClick = { },
        onClienteActualizado = { }
    )
}