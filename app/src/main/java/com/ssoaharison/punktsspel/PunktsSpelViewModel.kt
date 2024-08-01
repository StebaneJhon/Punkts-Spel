package com.ssoaharison.punktsspel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.ssoaharison.punktsspel.models.PathModel
import com.ssoaharison.punktsspel.models.Point
import com.ssoaharison.punktsspel.models.PointPairEdgeToEdge

class PunktsSpelViewModel: ViewModel() {

    private var _verticalLines by mutableIntStateOf(8)
    val verticalLines: Int
        get() = _verticalLines

    private var _horizontalLines by mutableIntStateOf(5)
    val horizontalLines: Int
        get() = _horizontalLines


    private val _points = createPointList(verticalLines, horizontalLines).toMutableStateList()
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

    private var _player by mutableIntStateOf(1)
    val player: Int
        get() = _player

    private var _posedPoint by mutableIntStateOf(0)
    val posedPoint: Int
        get() = _posedPoint

    private var _verticesList = listOf<Point>().toMutableStateList()
    val verticesList: List<Point>
        get() = _verticesList

    fun clearVerticesList() {
        _verticesList.clear()
    }

    fun addVertexToVerticesList(point: Point) {
        _verticesList.add(point)
    }

    fun isVerticesListEmpty() = _verticesList.isEmpty()

    fun getScoreP1() = p1CaughtPoints.size
    fun getScoreP2() = p2CaughtPoints.size

    fun increasePosedPoint() {
        _posedPoint++
    }

    private fun initPosedPoint() {
        _posedPoint = 0
    }

    fun switchPlayer() {
        _player = if (_player == 1) {
            2
        } else {
            1
        }
        initPosedPoint()
    }

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

    fun addPath(cycles: List<List<Point>>) {
        cycles.forEach { cycle ->
            val path = generatePath(cycle)
            val color = cycle[0].color !!
            val pathModel = PathModel(color, path)
            _paths.add(pathModel)
        }

    }

    fun countCaughtPoints(edgeToEdgeGraph: List<PointPairEdgeToEdge>) {
        edgeToEdgeGraph.forEach { pair ->
            val positionColumn = pair.higher.positionColumn
            _points[positionColumn!!].forEach { point ->
                if (point.isActive) {
                    if (
                        pair.lower.positionRow!! < point.positionRow!!  &&
                        point.positionRow!! < pair.higher.positionRow!! &&
                        point.toPlayer != player
                    ) {
                        catch(point)
                    }
                }
            }
        }
    }

    private fun catch(point: Point) {
        if (point !in _p1CaughtPoints && point !in _p2CaughtPoints) {
            if (player == 1) {
                _p1CaughtPoints.add(point)
            } else {
                _p2CaughtPoints.add(point)
            }
        }
    }

    fun restart() {
        clearBoard()
        _paths.clear()
        _p1CaughtPoints.clear()
        _p2CaughtPoints.clear()
    }

    private fun clearBoard() {
        _points.forEach { row ->
            for (index in row.indices) {
                row[index] = Point(isActive = false,)
            }
        }
    }

    fun arePointInListPoint(point: Point, pointList: List<Point>): Boolean {
        pointList.forEach { p ->
            if ( p.id == point.id ) {
                return true
            }
        }
        return false
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

