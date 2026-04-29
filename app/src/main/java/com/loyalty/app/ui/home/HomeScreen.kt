package com.loyalty.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loyalty.app.ui.home.components.LoyaltyCard
import com.loyalty.app.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.userState.collectAsState()
    val name = state?.full_name ?: "Usuario"
    val stamps = state?.stamps ?: 0
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
           // We could add a BottomNavigation here if needed
        },
        containerColor = Zinc950
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mi Tarjeta",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Hola, $name",
                        color = Zinc500,
                        fontSize = 14.sp
                    )
                }

                Surface(
                    color = Amber400.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Amber400.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Amber400,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${stamps / 5} premios",
                            color = Amber400,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Loyalty Card
            LoyaltyCard(currentStamps = stamps % 5)

            Spacer(modifier = Modifier.height(32.dp))

            // Scan Button
            Button(
                onClick = { /* Open Scanner */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(20.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Amber400, Amber600)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.Black)
                        Text(
                            text = "ESCANEAR CÓDIGO (Próximamente)",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatBox(
                    value = "$stamps",
                    label = "Sellos totales",
                    modifier = Modifier.weight(1f)
                )
                StatBox(
                    value = "${stamps / 5}",
                    label = "Tarjetas completadas",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Box
            Surface(
                color = Zinc900.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Zinc800.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (stamps % 5 == 0 && stamps > 0) 
                        "¡Felicidades! Muestra este código en el local para recibir tu regalo."
                        else "Escanea el código QR después de tu servicio para recibir un sello.",
                    color = Zinc500,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(20.dp),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun StatBox(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Zinc900.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, Zinc800.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = value,
                color = Amber400,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = label,
                color = Color(0xFF52525b),
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
