package com.example.api_client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.api_client.data.model.ClienteModel
import com.example.api_client.ui.screens.ClientesScreen
import com.example.api_client.ui.screens.EditarClienteScreen
import com.example.api_client.ui.screens.NuevoClienteScreen
import com.example.api_client.ui.theme.ApiclientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApiclientTheme {
                var currentScreen by remember { mutableStateOf("clientes") }
                var refreshClientes by remember { mutableStateOf(false) }
                var clienteSeleccionado by remember { mutableStateOf<ClienteModel?>(null) }
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (currentScreen) {
                        "clientes" -> {
                            ClientesScreen(
                                modifier = Modifier.padding(innerPadding),
                                onRegistrarClienteClick = {
                                    currentScreen = "nuevo_cliente"
                                },
                                refreshTrigger = refreshClientes,
                                onBuscarClienteClick = {
                                    // TODO: Implementar búsqueda de cliente
                                },
                                onEditarClienteClick = { cliente ->
                                    clienteSeleccionado = cliente
                                    currentScreen = "editar_cliente"
                                }
                            )
                        }
                        "nuevo_cliente" -> {
                            NuevoClienteScreen(
                                modifier = Modifier.padding(innerPadding),
                                onCloseClick = {
                                    currentScreen = "clientes"
                                },
                                onClienteRegistrado = {
                                    refreshClientes = !refreshClientes // Toggle para activar refresh
                                    currentScreen = "clientes"
                                }
                            )
                        }
                        "editar_cliente" -> {
                            clienteSeleccionado?.let { cliente ->
                                EditarClienteScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    cliente = cliente,
                                    onCloseClick = {
                                        currentScreen = "clientes"
                                    },
                                    onClienteActualizado = {
                                        refreshClientes = !refreshClientes // Toggle para activar refresh
                                        currentScreen = "clientes"
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