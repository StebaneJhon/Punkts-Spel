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
import com.ssoaharison.punktsspel.ui.theme.baseNavy
import com.ssoaharison.punktsspel.ui.theme.baseRed
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
    player: Int,
    onEndTurn: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        StateItem(
            modifier = modifier
                .padding(top = 8.dp, start = 8.dp),
            name = "Player 1",
            score = player1Sore,
            background = if (player==1) baseNavy else lightNavy,
            textColor = if (player==1) lightNavy else darkNavy,
            isEnabled = player==1,
            onEndTurn = onEndTurn,
        )
        StateItem(
            modifier = modifier
                .padding(top = 8.dp, start = 8.dp, bottom = 4.dp),
            name = "Player 2",
            score = player2Sore,
            background = if (player == 2) baseRed else lightRed,
            textColor = if (player == 2) lightRed else darkRed,
            isEnabled = player==2,
            onEndTurn = onEndTurn,
        )
    }

}

@Preview
@Composable
fun StatePreview() {
    PunktsSpelTheme {
        State(
            Modifier,
            1,
            2,
            1,
            {}
            )
    }
}