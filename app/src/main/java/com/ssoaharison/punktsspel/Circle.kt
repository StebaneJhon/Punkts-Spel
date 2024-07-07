package com.ssoaharison.punktsspel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme

@Composable
fun Circle(
    modifier: Modifier,
    xPosition: Int,
    yPosition: Int,
    size:Int,
    color: Color
) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawCircle(
                color,
                center = Offset(
                    xPosition.dp.toPx(),
                    yPosition.dp.toPx()
                ),
                radius = size.dp.toPx()
            )
        })
}

@Preview
@Composable
fun CirclePreview() {
    PunktsSpelTheme {
        Column(
            modifier = Modifier.padding(10.dp, 10.dp)
        ){
            Circle(
                modifier = Modifier,
                20,
                100,
                60,
                Color.Magenta
            )
        }
    }
}