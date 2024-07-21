package com.ssoaharison.punktsspel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
    textColor: Color,
    isEnabled: Boolean,
    onEndTurn: () -> Unit,
) {
  Button(
      modifier = modifier
          .background(background),
      contentPadding = PaddingValues(
          start = 4.dp,
          top = 4.dp,
          end = 4.dp,
          bottom = 4.dp),
      shape = RoundedCornerShape(0),
      colors = ButtonDefaults.buttonColors(
          containerColor = background
      ),
      enabled = isEnabled,
      onClick = onEndTurn,
  ) {
      Column(modifier = Modifier.padding(0.dp)) {
          Text(
              modifier = Modifier
                  .padding(0.dp),
              text = name,
              style = MaterialTheme.typography.titleSmall,
              color = textColor,
          )
          Text(
              modifier = Modifier
                  .padding(0.dp),
              text = "score: $score",
              style = MaterialTheme.typography.bodyMedium,
              color = textColor
          )
      }

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
            darkGreen,
            false,
            {}
        )
    }
}