package com.ssoaharison.punktsspel.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

class Point(
    id: String? = null,
    isActive: Boolean = false,
    positionRow: Int? = null,
    positionColumn: Int? = null,
    x: Float? = null,
    y: Float? = null,
    toPlayer: Int? = null,
    color: Color? = null
) {
    var id by mutableStateOf(id)
    var isActive by mutableStateOf(isActive)
    var positionRow by mutableStateOf(positionRow)
    var positionColumn by mutableStateOf(positionColumn)
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
    var toPlayer by mutableStateOf(toPlayer)
    var color by mutableStateOf(color)
}
