package com.ssoaharison.punktsspel

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssoaharison.punktsspel.components.State
import com.ssoaharison.punktsspel.models.Point
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PunktsSpelApp(
    modifier: Modifier,
    punktsSpelViewModel: PunktsSpelViewModel = viewModel()
) {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var scoreP1 by remember {
        mutableIntStateOf(0)
    }

    var scoreP2 by remember {
        mutableIntStateOf(0)
    }

    var verticesList = listOf<Point>().toMutableStateList()


    PunktsSpelTheme {
        Scaffold (
            modifier = modifier
                .fillMaxSize(),
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ){ contentPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(contentPadding)
            ) {

                State(
                    modifier = modifier,
                    player1Sore = scoreP1,
                    player2Sore = scoreP2,
                )

                GameBoard(
                    modifier = modifier,
                    verticalLines = 7,
                    horizontalLines = 5,
                    listPoints = punktsSpelViewModel.points,
                    listPaths = punktsSpelViewModel.paths,
                    onAddPoint = { point ->
                        punktsSpelViewModel.addPoint(point)
                    },
                    onShowSnackBar = { text ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message = text)
                        }

                    },
                    onCatching = { point ->
                        if (punktsSpelViewModel.whosPoint(point) != point.toPlayer && punktsSpelViewModel.isActive(point)) {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Inte din punkt :/")
                            }

                            return@GameBoard
                        }
                        if (punktsSpelViewModel.isCaught(point)) {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Kan inte använda den här:)")
                            }
                            return@GameBoard
                        }
                        if (punktsSpelViewModel.isActive(point)) {
                            verticesList.add(point)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Välja en av dina punkter.")
                            }

                        }
                    },
                    onDoneCatching = { player ->
                        if (!verticesList.isEmpty()) {
                            val vList = verticesList
                            val graph = Graph()
                            graph.toAdjacentList(verticesList)
                            val cycle = graph.checkForCycle(verticesList[0])
                            if (cycle.isNullOrEmpty()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(message = "Den här är inte stängd?")
                                }
                                graph.clearAdjacencyList()
                                verticesList.clear()
                                //vList.clear()
                            } else {
                                punktsSpelViewModel.addPath(cycle)
                                val edgeToEdgeGraph = graph.toEdgeToEdgeList()
                                punktsSpelViewModel.countCaughtPoints(edgeToEdgeGraph, player)
                                scoreP1 = punktsSpelViewModel.getScoreP1()
                                scoreP2 = punktsSpelViewModel.getScoreP2()
                                graph.clearAdjacencyList()
                                verticesList.clear()
                                //vList.clear()
                            }
                        }
                    }
                )
            }
        }

    }

}


@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun PunktsSpelAppPreview() {
    PunktsSpelTheme {
        PunktsSpelApp(Modifier)
    }
}