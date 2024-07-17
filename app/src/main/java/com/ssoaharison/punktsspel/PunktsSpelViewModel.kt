package com.ssoaharison.punktsspel

import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.ssoaharison.punktsspel.models.Point

class PunktsSpelViewModel: ViewModel() {

    private val _points = createPointList(5, 3).toMutableStateList()
    val points: List<List<Point>>
        get() = _points

    private val _paths = mutableListOf<Path>().toMutableStateList()
    val paths: List<Path>
        get() = _paths

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

    fun whosPoint(
        point: Point,
    ): Int? {
        var user: Int? = null
        _points.forEach { row ->
            row.forEach { p ->
                if ( p.id == point.id && p.isActive ) {
                    user = p.toPlayer
                }
            }
        }
        return user
    }

    fun isActive(
        point: Point,
    ): Boolean {

        var isActive = false
        _points.forEach { row ->
            row.forEach { p ->
                if ( p.id == point.id && p.isActive ) {
                    isActive = true
                }
            }
        }

        return isActive
    }

    fun addPath(vertexes: List<Point>) {
        val path = generatePath(vertexes)
        _paths.add(path)
    }

}

fun generatePath(vertexes: List<Point>): Path {
    val path = Path()
    path.moveTo(vertexes[0].x!!, vertexes[0].y!!)
    vertexes.forEach { point ->
        val x = point.x ?: 0.0.toFloat()
        val y = point.y ?: 0.0.toFloat()
        path.lineTo(x , y)
    }
    path.close()
    return path
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

