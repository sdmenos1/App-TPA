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

/**
 * Pantalla principal que ve el usuario al entrar.
 * Muestra el progreso de sus sellos, tarjetas completadas y el botón de escaneo.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel() // Gestor de datos para obtener sellos y nombre del usuario
) {
    // Obtenemos el estado actual del usuario (sellos, nombre, etc.)
    val state by viewModel.userState.collectAsState()
    val name = state?.full_name ?: "Usuario"
    val stamps = state?.stamps ?: 0
    val scrollState = rememberScrollState() // Permite que la pantalla se pueda deslizar hacia abajo
    
    // Estado para controlar la visibilidad del escáner
    var showScanner by remember { mutableStateOf(false) }

    if (showScanner) {
        QRScannerScreen(
            onScan = { code ->
                // Por ahora, cualquier código QR que contenga "LOYALTY_STAMP" añade un sello
                if (code.contains("LOYALTY_STAMP")) {
                    viewModel.addStamp()
                    showScanner = false
                }
            },
            onClose = { showScanner = false }
        )
    } else {
        Scaffold(
        containerColor = Zinc950 // Color de fondo oscuro de la pantalla
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState) // Hace que el contenido sea desplazable
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Cabecera: Saludo al usuario y contador de premios ganados
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

                // Insignia que muestra cuántos premios (tarjetas de 10 sellos) ha completado
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
                            text = "${stamps / 10} premios",
                            color = Amber400,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta Visual de Fidelidad (Muestra los 10 sellos en 2 columnas)
            LoyaltyCard(currentStamps = stamps % 10)

            Spacer(modifier = Modifier.height(32.dp))

            // Botón principal para escanear el código QR del local
            Button(
                onClick = { showScanner = true },
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
                                colors = listOf(Amber400, Amber600) // Degradado naranja/ámbar
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "ESCANEAR CÓDIGO",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Estadísticas rápidas: Total de sellos y tarjetas completas
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
                    value = "${stamps / 10}",
                    label = "Tarjetas completadas",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Cuadro informativo o de felicitación
            Surface(
                color = Zinc900.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Zinc800.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (stamps % 10 == 0 && stamps > 0) 
                        "¡Felicidades! Has completado tu tarjeta. Reclama tu premio."
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
}

/**
 * Componente pequeño para mostrar una estadística (Número + Texto)
 */
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
