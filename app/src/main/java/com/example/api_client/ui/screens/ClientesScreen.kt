package com.example.api_client.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.api_client.ui.components.ClientInfoCard
import com.example.api_client.ui.components.InfoCard
import com.example.api_client.ui.components.ScreenHeader
import com.example.api_client.ui.components.StatefulButton

@Composable
fun ClientesScreen() {
    LazyColumn(
        modifier = Modifier
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
                    onClick = {}
                )
                StatefulButton(
                    modifier = Modifier.weight(1f),
                    text = "Registrar Cliente",
                    icon = Icons.Default.Add,
                    isActive = false,
                    onClick = {}
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
                        value = "100"
                    )
                    InfoCard(
                        modifier = Modifier.fillMaxWidth().height(118.dp),
                        title = "Inactivos",
                        value = "20"
                    )
                }

                InfoCard(
                    modifier = Modifier
                        .weight(1f)
                        .height(118.dp),
                    title = "Activos",
                    value = "50"
                )
            }
        }

        items(3) {
            ClientInfoCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(103.dp),
                clientName = "\"Nombre del Cliente\"",
                clientId = "\"DNI/RUC\""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientesScreenPreview() {
    ClientesScreen()
}
