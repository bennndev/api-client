package com.example.api_client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.api_client.network.ClienteApiService
import com.example.api_client.network.ClienteListResponse
import com.example.api_client.data.model.ClienteModel
import com.example.api_client.ui.components.ClientInfoCard
import com.example.api_client.ui.components.InfoCard
import com.example.api_client.ui.components.ScreenHeader
import com.example.api_client.ui.components.StatefulButton
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ClientesScreen(
    modifier: Modifier = Modifier,
    onRegistrarClienteClick: () -> Unit = {},
    onBuscarClienteClick: () -> Unit = {}
) {
    // Estados para manejar los datos de la API
    var clientes by remember { mutableStateOf<List<ClienteModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var totalClientes by remember { mutableStateOf(0) }
    
    val scope = rememberCoroutineScope()
    
    // Configuración de Retrofit (esto debería estar en un módulo de DI en una app real)
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/") // URL para emulador Android
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    val apiService = remember { retrofit.create(ClienteApiService::class.java) }
    
    // Función para cargar clientes
    fun loadClientes() {
        scope.launch {
            try {
                isLoading = true
                errorMessage = null
                println("🔄 Iniciando petición a: http://10.0.2.2:8000/api/clientes/")
                
                val response = apiService.getClientes()
                println("📡 Respuesta recibida - Código: ${response.code()}")
                
                if (response.isSuccessful) {
                    val clienteResponse = response.body()
                    println("✅ Respuesta exitosa: $clienteResponse")
                    
                    if (clienteResponse?.success == true) {
                        clientes = clienteResponse.data ?: emptyList()
                        totalClientes = clienteResponse.count ?: clientes.size
                        println("📋 Clientes cargados: ${clientes.size}")
                    } else {
                        errorMessage = clienteResponse?.message ?: "Error desconocido"
                        println("❌ Error en respuesta: ${clienteResponse?.message}")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    errorMessage = "Error ${response.code()}: ${response.message()}"
                    println("❌ Error HTTP ${response.code()}: ${response.message()}")
                    println("❌ Error body: $errorBody")
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
                println("💥 Excepción: ${e.message}")
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
    
    // Cargar datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        loadClientes()
    }
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            ScreenHeader(
                title = "Clientes",
                modifier = Modifier.padding(top = 35.dp)
            )
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatefulButton(
                    modifier = Modifier.weight(1f),
                    text = "Buscar Cliente",
                    icon = Icons.Default.Search,
                    isActive = true,
                    onClick = onBuscarClienteClick
                )
                StatefulButton(
                    modifier = Modifier.weight(1f),
                    text = "Registrar Cliente",
                    icon = Icons.Default.Add,
                    isActive = false,
                    onClick = onRegistrarClienteClick
                )
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.fillMaxWidth().height(118.dp),
                        title = "Totales",
                        value = totalClientes.toString()
                    )
                    InfoCard(
                        modifier = Modifier.fillMaxWidth().height(118.dp),
                        title = "DNI",
                        value = clientes.count { it.documento == "dni" }.toString()
                    )
                }

                InfoCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(118.dp),
                    title = "RUC",
                    value = clientes.count { it.documento == "ruc" }.toString()
                )
            }
        }

        // Mostrar loading, error o lista de clientes
        when {
            isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF366A9A)
                        )
                    }
                }
            }
            
            errorMessage != null -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error al cargar clientes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE53E3E)
                        )
                        Text(
                            text = errorMessage!!,
                            fontSize = 14.sp,
                            color = Color(0xFF5D5F5F),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        StatefulButton(
                            text = "Reintentar",
                            icon = Icons.Default.Search,
                            isActive = false,
                            onClick = { loadClientes() },
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
            
            clientes.isEmpty() -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay clientes registrados",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5D5F5F)
                        )
                    }
                }
            }
            
            else -> {
                items(clientes) { cliente ->
                    ClientInfoCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(103.dp),
                        clientName = "${cliente.nombre} ${cliente.apellido}",
                        clientId = "${cliente.documento.uppercase()}: ${cliente.id ?: "N/A"}"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientesScreenPreview() {
    ClientesScreen()
}
