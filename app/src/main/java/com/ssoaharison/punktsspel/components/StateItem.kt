package com.ssoaharison.punktsspel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme
import com.ssoaharison.punktsspel.ui.theme.darkGreen
import com.ssoaharison.punktsspel.ui.theme.lightGreen

@Composable
fun StateItem(
    modifier: Modifier,
    name: String,
    score: Int,
    background: Color,
    textColor: Color
) {
  Column(
      modifier
          .background(background)
          .fillMaxHeight()
          .padding(16.dp)
  ) {
      Text(
          text = name,
          style = MaterialTheme.typography.titleMedium,
          color = textColor
      )
      Text(
          text = "score: $score",
          style = MaterialTheme.typography.bodyMedium,
          color = textColor
      )
  }
}

@Preview
@Composable
fun StateItemPreview() {
    PunktsSpelTheme {
        StateItem(
            modifier = Modifier,
            name = "Player 1",
            score = 5,
            lightGreen,
            darkGreen
        )
    }
}