package com.loyalty.app.ui.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loyalty.app.ui.theme.*

/**
 * Componente visual de la Tarjeta de Fidelidad.
 * Muestra el progreso de sellos del usuario en un formato de tarjeta física digital.
 */
@Composable
fun LoyaltyCard(
    currentStamps: Int // Número de sellos que el usuario tiene actualmente (0 a 10)
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = Color(0xFF18181b), // Fondo oscuro de la tarjeta
        shape = RoundedCornerShape(24.dp), // Bordes redondeados
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF27272a)) // Borde fino sutil
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Dibujamos un patrón de fondo (un círculo tenue en la esquina) para decorar
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    color = Amber400.copy(alpha = 0.05f),
                    radius = 300f,
                    center = center.copy(x = size.width, y = 0f)
                )
            }

            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                // Fila superior: Título de la tarjeta y pequeño logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Tarjeta de Fidelidad",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Gana 50 puntos al completar 10 sellos",
                            color = Zinc500,
                            fontSize = 12.sp
                        )
                    }
                    
                    // Pequeño logo circular con las iniciales "TB" (The Beauty)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Amber400),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("TB", fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // SECCIÓN DE SELLOS: Organizados en 5 filas de 2 columnas (Total 10)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (row in 0 until 5) { // Creamos 5 filas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Calculamos si cada hueco debe estar lleno o vacío según 'currentStamps'
                            StampSlot(isFilled = (row * 2) < currentStamps)
                            StampSlot(isFilled = (row * 2 + 1) < currentStamps)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Texto que indica cuánto falta para el premio
                Text(
                    text = "${10 - currentStamps} sellos restantes para tu premio",
                    color = Zinc500,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

/**
 * Cada uno de los círculos donde va el sello.
 */
@Composable
fun StampSlot(isFilled: Boolean) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            // Si está lleno, fondo naranja; si no, gris oscuro
            .background(if (isFilled) Amber400 else Color(0xFF27272a))
            .border(
                1.dp,
                if (isFilled) Amber400 else Color(0xFF3f3f46),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isFilled) {
            // Si tiene el sello, mostramos una estrella negra
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        } else {
            // Si está vacío, mostramos un pequeño punto gris decorativo
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3f3f46))
            )
        }
    }
}
