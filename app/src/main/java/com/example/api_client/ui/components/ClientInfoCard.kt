package com.example.api_client.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
    clientId: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFFF9F9F9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 20.dp),
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
            clientId = "\"DNI/RUC\""
        )
    }
}
