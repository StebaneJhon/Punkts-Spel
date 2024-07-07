package com.ssoaharison.punktsspel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme
import com.ssoaharison.punktsspel.ui.theme.baseGreen
import com.ssoaharison.punktsspel.ui.theme.baseNavy
import com.ssoaharison.punktsspel.ui.theme.primaryContainerLight

@Composable
fun GameBoard (
    modifier: Modifier,
    verticalLines: Int,
    horizontalLines: Int,
    listPoints: List<List<Point>>,
    onAddPoint: (Point) -> Unit,
    onShowSnackBar: (String) -> Unit,

) {

    var canvasSize by remember {
        mutableStateOf(Size.Zero)
    }
    var clickPointOffset by remember {
        mutableStateOf(Offset.Zero)
    }
    var actualPlayer by remember {
        mutableIntStateOf(1)
    }

    val barWidthPx = 2.dp


    Box(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        Canvas(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { offset ->
                            clickPointOffset = offset
                            val positionRowPoint =
                                ((clickPointOffset.x.minus(barWidthPx.toPx()) / canvasSize.width * verticalLines.plus(
                                    2
                                )).toInt())
                            val positionColumnPoint =
                                ((clickPointOffset.y.minus(barWidthPx.toPx()) / canvasSize.height * horizontalLines.plus(
                                    2
                                )).toInt())

                            val xPosition =
                                positionRowPoint * (canvasSize.width / (verticalLines + 1))
                            val yPosition =
                                positionColumnPoint * (canvasSize.height / (horizontalLines + 1))

                            val pointColor = if (actualPlayer == 1) baseGreen else baseNavy
                            val pointId = "$positionColumnPoint:$positionRowPoint"

                            val newPoint = Point(
                                pointId,
                                true,
                                positionRowPoint,
                                positionColumnPoint,
                                xPosition,
                                yPosition,
                                actualPlayer,
                                pointColor
                            )

                            if (!isPointActive(listPoints, newPoint)) {
                                onAddPoint(newPoint)
                                actualPlayer = if (actualPlayer == 1) 2 else 1
                            } else {
                                onShowSnackBar("Ser utt som att du inte kan sätta en punkt här.")
                            }

                        }
                    )
                }

        ) {

            val canvasHeight = size.height
            val canvasWidth = size.width
            canvasSize = Size(canvasWidth, canvasHeight)

            drawRect(
                primaryContainerLight,
                style = Stroke(barWidthPx.toPx()))

            val verticalSize = size.width / (verticalLines + 1)
            repeat(verticalLines) { i ->
                val startX = verticalSize * (i + 1)
                drawLine(
                    primaryContainerLight,
                    start = Offset(startX, 0f),
                    end = Offset(startX, size.height),
                    strokeWidth = barWidthPx.toPx()
                )
            }

            val sectionSize = size.height / (horizontalLines + 1)
            repeat(horizontalLines) { i ->
                val startY = sectionSize * (i + 1)
                drawLine(
                    primaryContainerLight,
                    start = Offset(0f, startY),
                    end = Offset(size.width, startY),
                    strokeWidth = barWidthPx.toPx()
                )
            }

            listPoints.forEach { row ->
                row.forEach { point ->
                    if (point.isActive) {
                        drawCircle(
                            point.color!!,
                            center = Offset(
                                point.x!!,
                                point.y!!
                            ),
                            radius = 7.dp.toPx()
                        )
                    }
                }
            }

        }
    }
}

fun isPointActive(
    listPoints: List<List<Point>>,
    point: Point,
    ): Boolean {

    var isActive = false
    listPoints.forEach { row ->
        row.forEach { p ->
            if ( p.id == point.id && p.isActive ) {
                isActive = true
            }
        }
    }

    return isActive
}



@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun PreviewGameBoard() {
    PunktsSpelTheme {
        Column (
        ) {
            GameBoard(modifier = Modifier,
                10,
                5,
                listOf<List<Point>>(),
                {null},
                {null},
                )
        }
    }
}

