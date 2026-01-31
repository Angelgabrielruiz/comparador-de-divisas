package com.angelruiz.convertidor_divisas.features.currency.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun LineChart(
    data: List<Double>,
    modifier: Modifier = Modifier,
    graphColor: Color = MaterialTheme.colorScheme.primary
) {
    if (data.isEmpty()) return

    // Preparamos los colores y estilos de texto
    val axisColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val textPaint = remember {
        Paint().apply {
            color = axisColor
            textSize = 30f // Tamaño del texto de los valores
            textAlign = Paint.Align.CENTER
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp) // Aumentamos un poco la altura para que quepan los textos
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            // Dejamos margen arriba y abajo para que el texto no se corte
            val paddingVertical = 60f

            val maxVal = data.maxOrNull() ?: 1.0
            val minVal = data.minOrNull() ?: 0.0
            val range = if (maxVal - minVal == 0.0) 1.0 else maxVal - minVal

            val path = Path()
            val xStep = width / (data.size - 1)

            data.forEachIndexed { index, value ->
                // Calculamos X e Y
                val x = index * xStep
                // Normalizamos Y respetando el padding
                val normalizedY = height - paddingVertical - ((value - minVal) / range * (height - (paddingVertical * 2))).toFloat()

                if (index == 0) {
                    path.moveTo(x, normalizedY)
                } else {
                    path.lineTo(x, normalizedY)
                }

                // 1. DIBUJAR PUNTOS (Círculos)
                drawCircle(
                    color = graphColor,
                    radius = 8f,
                    center = Offset(x, normalizedY)
                )

                // 2. DIBUJAR TEXTO (Valor de la moneda)
                drawContext.canvas.nativeCanvas.drawText(
                    String.format(Locale.US, "%.2f", value), // Solo 2 decimales
                    x,
                    normalizedY - 20f,
                    textPaint
                )

                // 3. DIBUJAR ETIQUETA DEL DÍA (Abajo)
                val dayLabel = if (index == data.size - 1) "Hoy" else "-${data.size - 1 - index}d"
                drawContext.canvas.nativeCanvas.drawText(
                    dayLabel,
                    x,
                    height - 10f, // Casi al final del canvas
                    textPaint
                )
            }

            // Dibujamos la línea conectora
            drawPath(
                path = path,
                color = graphColor,
                style = Stroke(width = 5f)
            )

            val fillPath = Path()
            fillPath.addPath(path)
            fillPath.lineTo(width, height - paddingVertical)
            fillPath.lineTo(0f, height - paddingVertical)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        graphColor.copy(alpha = 0.3f),
                        Color.Transparent
                    ),
                    endY = height - paddingVertical
                )
            )
        }
    }
}