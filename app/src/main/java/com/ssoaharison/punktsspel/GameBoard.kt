package com.ssoaharison.punktsspel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssoaharison.punktsspel.models.PathModel
import com.ssoaharison.punktsspel.models.Point
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme
import com.ssoaharison.punktsspel.ui.theme.baseNavy
import com.ssoaharison.punktsspel.ui.theme.baseRed
import com.ssoaharison.punktsspel.ui.theme.primaryContainerLight
import com.ssoaharison.punktsspel.util.PointCatchingState.ARE_CATCHING
import com.ssoaharison.punktsspel.util.PointCatchingState.ARE_NOT_CATCHING
import com.ssoaharison.punktsspel.util.PointCatchingState.CATCHING_DONE

@Composable
fun GameBoard (
    modifier: Modifier,
    verticalLines: Int,
    horizontalLines: Int,
    onAddPoint: (Point) -> Unit,
    onShowSnackBar: (String) -> Unit,
    onCatching: (Point) -> Unit,
    onDoneCatching: (Int) -> Unit,
    viewModel: PunktsSpelViewModel
) {


    var canvasSize by remember {
        mutableStateOf(Size.Zero)
    }
    var clickPointOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    var areCatching by rememberSaveable { mutableStateOf(ARE_NOT_CATCHING) }


    val barWidthPx = 2.dp

    Row (
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1F)
        ) {
            Canvas(
                modifier = modifier
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

                                val pointColor = if (viewModel.player == 2) baseRed else baseNavy
                                val pointId = "$positionColumnPoint:$positionRowPoint"

                                val newPoint = Point(
                                    pointId,
                                    true,
                                    positionRowPoint,
                                    positionColumnPoint,
                                    xPosition,
                                    yPosition,
                                    viewModel.player,
                                    pointColor
                                )

                                when (areCatching) {
                                    ARE_CATCHING -> {
                                        onCatching(newPoint)
                                    }
                                    ARE_NOT_CATCHING -> {
                                        if (!isPointActive(viewModel.points, newPoint) && viewModel.posedPoint == 0) {
                                            onAddPoint(newPoint)
                                        } else {
                                            onShowSnackBar("Ser ut som att du inte kan sätta en punkt här.")
                                        }
                                    }
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

                viewModel.points.forEach { row ->
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

                viewModel.paths.forEach { pathModel ->
                    drawPath(
                        pathModel.path,
                        pathModel.color,
                        0.5f,
                        Stroke(2.dp.toPx())
                    )

                    drawPath(
                        pathModel.path,
                        pathModel.color,
                        0.1f,
                        Fill
                    )
                }

            }
        }

        val buttonColor = if(areCatching == ARE_NOT_CATCHING) {
            MaterialTheme.colorScheme.inversePrimary
        } else {
            if (viewModel.player == 1) {
                baseNavy
            } else {
                baseRed
            }
        }

        Column(
            modifier = modifier
        ) {
            Button(
                onClick = {
                    areCatching = if (areCatching == ARE_NOT_CATCHING) {
                        ARE_CATCHING
                    } else {
                        ARE_NOT_CATCHING
                    }
                    if(areCatching == ARE_NOT_CATCHING) {
                        onDoneCatching(viewModel.player)
                    }
                },
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
                shape = RoundedCornerShape(0),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            ) {
                if (areCatching == ARE_NOT_CATCHING) {
                    Text(text = "Catch")
                } else {
                    Text(text = "Done")
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
                {null},
                {null},
                {null},
                {null},
                viewModel = PunktsSpelViewModel()
                )
        }
    }
}

