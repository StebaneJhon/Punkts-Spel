package com.ssoaharison.punktsspel

import com.ssoaharison.punktsspel.models.Point

class Graph<Point> {

    private val adjacencyList = mutableMapOf<Point, MutableList<Point>>()

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

    fun isCycle() {
        
    }

}