package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import kotlin.random.Random

private data class Star(
    val xRatio: Float,
    val yRatio: Float,
    val radius: Float,
    val alphaBase: Float,
    val speedFactor: Float
)

@Composable
fun GalaxyBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "StarTwinkle")
    val twinklePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6.28318f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinkle"
    )

    val stars = remember {
        val rand = Random(42)
        List(70) {
            Star(
                xRatio = rand.nextFloat(),
                yRatio = rand.nextFloat(),
                radius = rand.nextFloat() * 1.8f + 0.6f,
                alphaBase = rand.nextFloat() * 0.5f + 0.3f,
                speedFactor = rand.nextFloat() * 2f + 1f
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // High quality galaxy texture background
        Image(
            painter = painterResource(id = R.drawable.galaxy_bg),
            contentDescription = "Galaxy Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Twinkling starlight particle layer
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                val x = star.xRatio * size.width
                val y = star.yRatio * size.height
                val currentAlpha = (star.alphaBase + 0.3f * kotlin.math.sin(twinklePhase * star.speedFactor)).coerceIn(0.1f, 1f)
                drawCircle(
                    color = Color.White.copy(alpha = currentAlpha),
                    radius = star.radius,
                    center = Offset(x, y)
                )
            }
        }

        // Screen content
        content()
    }
}
