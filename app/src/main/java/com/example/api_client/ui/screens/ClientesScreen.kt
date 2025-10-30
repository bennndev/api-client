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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    refreshTrigger: Boolean = false,
    onBuscarClienteClick: () -> Unit = {},
    onEditarClienteClick: (ClienteModel) -> Unit = {}
) {
    // Estados para manejar los datos de la API
    var clientes by remember { mutableStateOf<List<ClienteModel>>(emptyList()) }
    var clientesOriginales by remember { mutableStateOf<List<ClienteModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var totalClientes by remember { mutableStateOf(0) }
    
    // Estados para búsqueda
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchType by remember { mutableStateOf("nombre") } // "nombre", "documento", "dni", "ruc"
    var isSearching by remember { mutableStateOf(false) }
    
    // Estados para eliminación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var clienteToDelete by remember { mutableStateOf<ClienteModel?>(null) }
    var isDeleting by remember { mutableStateOf(false) }
    
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
    fun cargarClientes() {
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
                        val clientesData = clienteResponse.data ?: emptyList()
                        clientes = clientesData
                        clientesOriginales = clientesData
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
    
    // Función para buscar clientes localmente
    fun buscarClientesLocal(query: String, tipo: String) {
        if (query.isBlank()) {
            clientes = clientesOriginales
            return
        }
        
        clientes = when (tipo) {
            "nombre" -> clientesOriginales.filter { 
                "${it.nombre} ${it.apellido}".contains(query, ignoreCase = true)
            }
            "documento" -> clientesOriginales.filter { 
                it.numeroDocumento.contains(query, ignoreCase = true)
            }
            "dni" -> clientesOriginales.filter { 
                it.tipoDocumento.contains("DNI", ignoreCase = true) && 
                it.numeroDocumento.contains(query, ignoreCase = true)
            }
            "ruc" -> clientesOriginales.filter { 
                it.tipoDocumento.contains("RUC", ignoreCase = true) && 
                it.numeroDocumento.contains(query, ignoreCase = true)
            }
            else -> clientesOriginales
        }
    }
    
    // Función para buscar clientes por API
    fun buscarClientesPorTipo(tipo: String) {
        scope.launch {
            try {
                isSearching = true
                val response = apiService.buscarPorDocumento(tipo.uppercase())
                
                if (response.isSuccessful) {
                    val clienteResponse = response.body()
                    if (clienteResponse?.success == true) {
                        clientes = clienteResponse.data ?: emptyList()
                        println("🔍 Búsqueda completada: ${clientes.size} resultados")
                    } else {
                        errorMessage = "No se encontraron resultados"
                    }
                } else {
                    errorMessage = "Error en la búsqueda: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            } finally {
                isSearching = false
            }
        }
    }
    
    // Función para eliminar cliente
    fun eliminarCliente(cliente: ClienteModel) {
        scope.launch {
            try {
                isDeleting = true
                val response = apiService.deleteCliente(cliente.id ?: return@launch)
                
                if (response.isSuccessful) {
                    // Recargar la lista después de eliminar
                    cargarClientes()
                    println("🗑️ Cliente eliminado: ${cliente.nombre} ${cliente.apellido}")
                } else {
                    errorMessage = "Error al eliminar cliente: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error de conexión: ${e.message}"
            } finally {
                isDeleting = false
                showDeleteDialog = false
                clienteToDelete = null
            }
        }
    }
    
    // Cargar clientes al iniciar la pantalla y cuando se active el refresh
    LaunchedEffect(Unit, refreshTrigger) {
        cargarClientes()
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
                    onClick = { showSearchDialog = true }
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
                        value = clientes.count { 
                            it.tipoDocumento == "DNI"
                        }.toString()
                    )
                }

                InfoCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(118.dp),
                    title = "RUC",
                    value = clientes.count { 
                        it.tipoDocumento == "RUC"
                    }.toString()
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
                            onClick = { cargarClientes() },
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
                        clientId = "${cliente.tipoDocumento}: ${cliente.numeroDocumento} - ID: ${cliente.id ?: "N/A"}",
                        onActionClick = {
                            clienteToDelete = cliente
                            showDeleteDialog = true
                        },
                        onCardClick = {
                            onEditarClienteClick(cliente)
                        }
                    )
                }
            }
        }
    }
    
    // Diálogo de búsqueda
    if (showSearchDialog) {
        AlertDialog(
            onDismissRequest = { 
                showSearchDialog = false
                searchQuery = ""
            },
            title = { Text("Buscar Cliente") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Buscar por nombre o documento") },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                TextButton(
                                    onClick = { 
                                        searchQuery = ""
                                        clientes = clientesOriginales
                                    }
                                ) {
                                    Text("Limpiar")
                                }
                            }
                        }
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { 
                                buscarClientesLocal(searchQuery, "nombre")
                                showSearchDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF366A9A)
                            )
                        ) {
                            Text("Por Nombre", fontSize = 12.sp)
                        }
                        
                        Button(
                            onClick = { 
                                buscarClientesLocal(searchQuery, "documento")
                                showSearchDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF366A9A)
                            )
                        ) {
                            Text("Por Doc.", fontSize = 12.sp)
                        }
                    }
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { 
                                buscarClientesPorTipo("DNI")
                                showSearchDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            enabled = !isSearching
                        ) {
                            if (isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Solo DNI", fontSize = 12.sp)
                            }
                        }
                        
                        Button(
                            onClick = { 
                                buscarClientesPorTipo("RUC")
                                showSearchDialog = false
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF9800)
                            ),
                            enabled = !isSearching
                        ) {
                            if (isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Solo RUC", fontSize = 12.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showSearchDialog = false
                        searchQuery = ""
                    }
                ) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        clientes = clientesOriginales
                        showSearchDialog = false
                        searchQuery = ""
                    }
                ) {
                    Text("Mostrar Todos")
                }
            }
        )
    }
    
    // Diálogo de confirmación de eliminación
    if (showDeleteDialog && clienteToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                clienteToDelete = null
            },
            title = { Text("Confirmar Eliminación") },
            text = { 
                Text("¿Estás seguro de que deseas eliminar a ${clienteToDelete?.nombre} ${clienteToDelete?.apellido}?")
            },
            confirmButton = {
                Button(
                    onClick = { 
                        clienteToDelete?.let { eliminarCliente(it) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53E3E)
                    ),
                    enabled = !isDeleting
                ) {
                    if (isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Eliminar")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showDeleteDialog = false
                        clienteToDelete = null
                    },
                    enabled = !isDeleting
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClientesScreenPreview() {
    ClientesScreen(
        onEditarClienteClick = {}
    )
}
