package com.loyalty.app.ui.home.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.loyalty.app.ui.theme.*

@Composable
fun LoyaltyCard(
    currentStamps: Int,
    salonName: String = "Salón de Belleza"
) {
    val totalStamps = 5
    val progress = animateFloatAsState(
        targetValue = currentStamps.toFloat() / totalStamps,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Amber400.copy(alpha = 0.1f))
            .padding(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Zinc800, Zinc900)
                    )
                )
                .border(1.dp, Amber400.copy(alpha = 0.2f), RoundedCornerShape(22.dp))
                .padding(24.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Amber400, Amber600)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = salonName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "TARJETA DE FIDELIDAD",
                        color = Amber400.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stamps Grid (Simulated with 2 columns)
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StampCircle(isFilled = currentStamps > 0, modifier = Modifier.weight(1f))
                    StampCircle(isFilled = currentStamps > 1, modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StampCircle(isFilled = currentStamps > 2, modifier = Modifier.weight(1f))
                    StampCircle(isFilled = currentStamps > 3, modifier = Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                   StampCircle(isFilled = currentStamps > 4, isReward = true, modifier = Modifier.weight(1f))
                   Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Progress Bar
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(Zinc700.copy(alpha = 0.5f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.value)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Amber400, Amber500)
                                )
                            )
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$currentStamps de $totalStamps sellos",
                        color = Zinc500,
                        fontSize = 10.sp
                    )
                    Text(
                        text = if (currentStamps >= totalStamps) "¡Premio ganado!" else "Faltan ${totalStamps - currentStamps}",
                        color = Amber400,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun StampCircle(
    isFilled: Boolean,
    isReward: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (isFilled) Color.Transparent else Zinc800.copy(alpha = 0.5f))
            .border(
                width = 1.dp,
                color = if (isFilled) Amber400.copy(alpha = 0.5f) else Zinc700.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(if (isFilled) 0.dp else 2.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isFilled) {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Amber400.copy(alpha = 0.2f), Amber600.copy(alpha = 0.2f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isReward) Icons.Default.Star else Icons.Default.Check,
                    contentDescription = null,
                    tint = Amber400,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
             Icon(
                imageVector = if (isReward) Icons.Default.Star else Icons.Default.Check,
                contentDescription = null,
                tint = Zinc700,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
