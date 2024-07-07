package com.ssoaharison.punktsspel

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel

class PunktsSpelViewModel: ViewModel() {

    private val _points = createPointList(5, 3).toMutableStateList()
    val points: List<List<Point>>
        get() = _points

    fun addPoint(
        point: Point
    ) {
        val pointInPointList = _points[point.positionColumn!!][point.positionRow!!]
        if (!pointInPointList.isActive) {
            pointInPointList.id = point.id
            pointInPointList.isActive = true
            pointInPointList.positionRow = point.positionRow
            pointInPointList.positionColumn = point.positionColumn
            pointInPointList.x = point.x
            pointInPointList.y = point.y
            pointInPointList.toPlayer = point.toPlayer
            pointInPointList.color = point.color
        }
    }

}

fun createPointList(
    verticalLines: Int,
    horizontalLines: Int
): MutableList<MutableList<Point>> {
    val temporaryPoints = mutableListOf<MutableList<Point>>()
    val vr = verticalLines + 2
    val hr = horizontalLines + 2
    for ( h in 0 until hr) {
        val pointList = mutableListOf<Point>()
        for (v in 0 until vr) {
            pointList.add(
                Point(
                    isActive = false,
                )
            )
        }
        temporaryPoints.add(pointList)
    }
    return temporaryPoints
}

