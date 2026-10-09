package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import kotlinx.coroutines.delay

@Composable
fun BlackHoleLoadingScreen(
    onLoadingComplete: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var statusText by remember { mutableStateOf("Initializing Event Horizon...") }
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val steps = listOf(
            0.18f to "Calibrating Gravitational Field...",
            0.42f to "Synthesizing 350 Core Features...",
            0.68f to "Charging Accretion Matrix...",
            0.88f to "Synchronizing WhatsApp / Discord / Telegram...",
            1.00f to "Singularity Reached. Welcome, Commander."
        )
        for ((targetProgress, text) in steps) {
            statusText = text
            while (progress < targetProgress) {
                progress += 0.02f
                delay(20)
            }
            delay(150)
        }
        delay(400)
        isVisible = false
        delay(400)
        onLoadingComplete()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "BlackHoleSpin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(150),
        label = "progress"
    )

    AnimatedVisibility(
        visible = isVisible,
        exit = fadeOut(animationSpec = tween(400))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            // Gravitational lensing canvas ring effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val baseRadius = size.width * 0.38f

                // Outer photon gravitational rings
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0x5506B6D4),
                            Color(0x338B5CF6),
                            Color.Transparent
                        ),
                        center = center,
                        radius = baseRadius * 1.6f
                    ),
                    radius = baseRadius * 1.5f,
                    center = center
                )

                // Accretion disk halo
                drawCircle(
                    color = Color(0x66FF7700),
                    radius = baseRadius * 1.15f * pulseScale,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )

                drawCircle(
                    color = Color(0x9900E5FF),
                    radius = baseRadius * 0.98f * pulseScale,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Brand Header
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black)
                            .border(1.5.dp, Color.White, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "BGR PRJCT",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 2.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "BLACK HOLE CORE v1.0",
                        color = Color(0xFF67E8F9),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                // Center Black Hole Visual + Accretion Disk Animation
                Box(
                    modifier = Modifier
                        .size(310.dp)
                        .scale(pulseScale),
                    contentAlignment = Alignment.Center
                ) {
                    // Rotating Accretion disk image visual
                    Image(
                        painter = painterResource(id = R.drawable.black_hole_visual),
                        contentDescription = "Black Hole Event Horizon",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .rotate(rotationAngle),
                        contentScale = ContentScale.Crop
                    )

                    // Deep center singularity overlay
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .border(2.dp, Color(0xFF00E5FF).copy(alpha = 0.8f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Inscribed BGR PRJCT bubble graffiti logo
                        Image(
                            painter = painterResource(id = R.drawable.bgr_prjct_logo),
                            contentDescription = "BGR PRJCT Bubble Logo",
                            modifier = Modifier.size(90.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Bottom Titles & Progress
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "BOT GENERATOR",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "BY ARDZYN",
                        color = NeonViolet,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
                    )

                    // Cosmic Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color(0xFF1E1E2E))
                    ) {
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier.fillMaxSize(),
                            color = NeonCyan,
                            trackColor = Color(0x3306B6D4)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = statusText,
                            color = Color(0xFFE2E8F0),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(animatedProgress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
