package com.ssoaharison.punktsspel

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.ssoaharison.punktsspel.models.PathModel
import com.ssoaharison.punktsspel.models.Point
import com.ssoaharison.punktsspel.models.PointPairEdgeToEdge

class PunktsSpelViewModel: ViewModel() {

    private val _points = createPointList(7, 5).toMutableStateList()
    val points: List<List<Point>>
        get() = _points

    private val _p1CaughtPoints = mutableListOf<Point>().toMutableStateList()
    val p1CaughtPoints: List<Point>
        get() = _p1CaughtPoints

    private val _p2CaughtPoints = mutableListOf<Point>().toMutableStateList()
    val p2CaughtPoints: List<Point>
        get() = _p2CaughtPoints

    private val _paths = mutableListOf<PathModel>().toMutableStateList()
    val paths: List<PathModel>
        get() = _paths

    fun getScoreP1() = p1CaughtPoints.size
    fun getScoreP2() = p2CaughtPoints.size


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

    fun isCaught(point: Point): Boolean {
        p1CaughtPoints.forEach { p ->
            if (p.id == point.id) {
                return true
            }
        }
        p2CaughtPoints.forEach { p ->
            if (p.id == point.id) {
                return true
            }
        }
        return false
    }

    fun addPath(vertexes: List<Point>) {
        val path = generatePath(vertexes)
        val color = vertexes[0].color !!
        val pathModel = PathModel(color, path)
        _paths.add(pathModel)
    }

    fun countCaughtPoints(edgeToEdgeGraph: List<PointPairEdgeToEdge>, player: Int) {

        edgeToEdgeGraph.forEach { pair ->
            val positionColumn = pair.higher.positionColumn
            _points[positionColumn!!].forEach { point ->
                if (point.isActive) {
                    if (pair.lower.positionRow!! < point.positionRow!!  &&
                        point.positionRow!! < pair.higher.positionRow!!
                    ) {
                        if (player == 2) {
                            _p1CaughtPoints.add(point)
                        } else {
                            _p2CaughtPoints.add(point)
                        }
                    }
                }
            }
        }

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

