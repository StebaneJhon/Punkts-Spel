package com.ssoaharison.punktsspel

import com.ssoaharison.punktsspel.models.Point
import kotlin.contracts.Returns

class Graph {

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

    fun checkForCycle(startPoint: Point): List<Point>? {
        if (adjacencyList.keys.size < 3) {
            return null
        }
        val markedVertexes = mutableListOf<Point>()
        var previousPoint: Point? = null
        var actualPoint = startPoint
        var end = false
        while (!end) {
            if (adjacencyList[actualPoint]?.size == 1) {
                markedVertexes.clear()
                break
            }
            markedVertexes.add(actualPoint)
            val a = adjacencyList[actualPoint]
            for (point in adjacencyList[actualPoint]!!) {
                if (point != previousPoint) {
                    if (point in markedVertexes) {
                        end = true
                        break
                    } else {
                        previousPoint = actualPoint
                        actualPoint = point
                        break
                    }
                }
            }

        }

        return markedVertexes
    }

    fun toAdjacentList(
        list: List<Point>,
        ) {
        list.forEach {  point ->
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
                point.positionColumn?.minus(1) == vertex.positionColumn && point.positionRow?.minus(1) == vertex.positionRow ||
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