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
import com.ssoaharison.punktsspel.ui.theme.lightGreen
import com.ssoaharison.punktsspel.ui.theme.lightNavy

@Composable
fun State(
    modifier: Modifier
) {
    Column(
        modifier = Modifier
    ) {
        StateItem(
            modifier = modifier.weight(1f),
            name = "Player 1",
            score = 5,
            background = lightNavy,
            textColor = darkNavy
        )
        StateItem(
            modifier = modifier.weight(1f),
            name = "Player 2",
            score = 4,
            background = lightGreen,
            textColor = darkGreen
        )
    }

}

@Preview
@Composable
fun StatePreview() {
    PunktsSpelTheme {
        State(Modifier)
    }
}