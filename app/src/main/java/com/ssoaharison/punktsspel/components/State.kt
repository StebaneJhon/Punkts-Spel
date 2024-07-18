package com.ssoaharison.punktsspel.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme
import com.ssoaharison.punktsspel.ui.theme.darkGreen
import com.ssoaharison.punktsspel.ui.theme.darkNavy
import com.ssoaharison.punktsspel.ui.theme.darkRed
import com.ssoaharison.punktsspel.ui.theme.lightGreen
import com.ssoaharison.punktsspel.ui.theme.lightNavy
import com.ssoaharison.punktsspel.ui.theme.lightRed

@Composable
fun State(
    modifier: Modifier,
    player1Sore: Int,
    player2Sore: Int,
) {
    Column(
        modifier = modifier
    ) {
        StateItem(
            modifier = modifier
                .padding(top = 8.dp, start = 8.dp),
            name = "Player 1",
            score = player1Sore,
            background = lightNavy,
            textColor = darkNavy
        )
        StateItem(
            modifier = modifier
                .padding(top = 8.dp, start = 8.dp, bottom = 4.dp),
            name = "Player 2",
            score = player2Sore,
            background = lightRed,
            textColor = darkRed
        )
    }

}

@Preview
@Composable
fun StatePreview() {
    PunktsSpelTheme {
        State(Modifier, 1, 2)
    }
}