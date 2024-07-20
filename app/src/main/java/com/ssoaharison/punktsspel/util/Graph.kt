package com.ssoaharison.punktsspel.util

import android.util.Log
import com.ssoaharison.punktsspel.models.Point
import com.ssoaharison.punktsspel.models.PointPairEdgeToEdge

class Graph {

    companion object {
        private const val TAG = "Graph"
    }

    private val adjacencyList = mutableMapOf<Point, MutableList<Point>>()

    fun clearAdjacencyList() {
        adjacencyList.clear()
    }

    fun addVertex(vertex: Point) {
        adjacencyList[vertex] = mutableListOf()
    }

    fun addEdge(from: Point, to: Point) {
        adjacencyList[from]?.add(to)
        adjacencyList[to]?.add(from)
    }

    fun removeVertex(vertex: Point) {
        adjacencyList[vertex]?.let { adjVertices ->
            adjVertices.forEach { adjVertex ->
                adjacencyList[adjVertex]?.remove(vertex)
            }
            adjacencyList.remove(vertex)
        }
    }

    fun removeEdge(from: Point, to: Point) {
        adjacencyList[from]?.remove(to)
        adjacencyList[to]?.remove(from)
    }

    fun getAdjacencyList(): Map<Point, List<Point>> {
        return adjacencyList.toMap()
    }

    fun checkForCycle(startPoint: Point): List<List<Point>>? {
        val adjacencyListSize = adjacencyList.keys.size
        if (adjacencyListSize < 3) {
            return null
        }
        val markedVertexes = mutableListOf<Point>()
        val cycle = mutableListOf<Point>()
        val cycleList = mutableListOf<List<Point>>()
        var actualPoint = startPoint

        while (adjacencyListSize >= markedVertexes.size) {
            if (adjacencyList[actualPoint]?.size == 1) {
                markedVertexes.clear()
                cycle.clear()
                break
            }
            if (actualPoint !in markedVertexes) {
                markedVertexes.add(actualPoint)
                cycle.add(actualPoint)
            }

            val point = getUnmarkedNeighbor(actualPoint, markedVertexes)
            if (point == null && markedVertexes.size == adjacencyListSize) {
                cycleList.add(cycle)
                break
            } else if (point == null && markedVertexes.size < adjacencyListSize) {
                cycleList.add(cycle.toList())
                cycle.removeAt(cycle.size.minus(1))
                actualPoint = cycle.last()
            } else {
                actualPoint = point!!
            }
        }

        return cycleList.filter { it.first() in adjacencyList[it.last()]!! && it.size > 3 }
    }

    private fun getUnmarkedNeighbor(vertex: Point, markedVertex: List<Point>): Point? {
        adjacencyList[vertex]?.forEach { point ->
            if (point !in markedVertex) {
                return point
            }
        }
        return null
    }

    private fun hasUnmarkedVertex(vertex: Point, markedVertexes: List<Point>): Boolean {
        adjacencyList[vertex]?.forEach { point ->
            if (point !in markedVertexes) {
                return true
            }
        }
        return false
    }

    fun toEdgeToEdgeList(cycle: List<Point>): List<PointPairEdgeToEdge> {
        val startPoint = adjacencyList.keys.first()
        val result = mutableListOf<PointPairEdgeToEdge>()
        val paired = mutableListOf<Point>()
        if (!cycle.isNullOrEmpty()) {
            //TODO: Change "cycle.first" to loop over all cycles in the cycle list her and in line 89
            cycle.forEach { point1 ->
                if (point1 !in paired) {
                    cycle.forEach { point2 ->
                        if (point2 != point1 && point2.positionColumn == point1.positionColumn) {
                            if (point1.positionRow!! > point2.positionRow!!) {
                                result.add(PointPairEdgeToEdge(point2, point1))
                            } else {
                                result.add(PointPairEdgeToEdge(point1, point2))
                            }
                            paired.add(point2)
                        }
                    }

                }
                paired.add(point1)
            }
        } else {
            Log.e(TAG, "Ingen graph att jobba med!")
        }
        return result
    }

    fun toAdjacentList(
        list: List<Point>,
    ) {
        list.forEach { point ->
            if (point !in adjacencyList.keys) {
                addVertex(point)
            }
        }
        adjacencyList.keys.forEach { point ->
            val neighbors = getNeighbors(point)
            neighbors.forEach { neighbor ->
                if (neighbor !in adjacencyList[point]!!) {
                    addEdge(point, neighbor)
                }
            }
        }
    }

    fun getNeighbors(
        point: Point
    ): List<Point> {
        val neighbors = mutableListOf<Point>()

        adjacencyList.keys.forEach { vertex ->
            if (
                point.positionColumn?.minus(1) == vertex.positionColumn && point.positionRow == vertex.positionRow ||
                point.positionColumn?.minus(1) == vertex.positionColumn && point.positionRow?.plus(1) == vertex.positionRow ||
                point.positionColumn?.minus(1) == vertex.positionColumn && point.positionRow?.minus(
                    1
                ) == vertex.positionRow ||
                point.positionColumn?.plus(1) == vertex.positionColumn && point.positionRow == vertex.positionRow ||
                point.positionColumn?.plus(1) == vertex.positionColumn && point.positionRow?.plus(1) == vertex.positionRow ||
                point.positionColumn?.plus(1) == vertex.positionColumn && point.positionRow?.minus(1) == vertex.positionRow ||
                point.positionRow?.minus(1) == vertex.positionRow && point.positionColumn == vertex.positionColumn ||
                point.positionRow?.plus(1) == vertex.positionRow && point.positionColumn == vertex.positionColumn
            ) {
                neighbors.add(vertex)
            }
        }

        return neighbors
    }

}