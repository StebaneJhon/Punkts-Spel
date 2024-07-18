package com.ssoaharison.punktsspel

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssoaharison.punktsspel.components.State
import com.ssoaharison.punktsspel.models.Point
import com.ssoaharison.punktsspel.ui.theme.PunktsSpelTheme
import com.ssoaharison.punktsspel.util.Graph
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
                Column(
                    modifier = modifier
                        .fillMaxHeight()
                        .width(110.dp)) {
                    State(
                        modifier = modifier
                            .weight(1F)
                            .fillMaxWidth(),
                        player1Sore = scoreP1,
                        player2Sore = scoreP2,
                    )
                    Button(
                        modifier = modifier
                            .padding(start = 8.dp, bottom = 3.dp, end = 0.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(0),
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  MaterialTheme.colorScheme.inversePrimary
                        ),
                        onClick = {
                            punktsSpelViewModel.restart()
                            scoreP1 = 0
                            scoreP2 = 0
                            verticesList.clear()
                        }
                    ) {
                        Text(text = "Restart")
                    }
                }


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
                        if (punktsSpelViewModel.isActive(point) && point !in verticesList) {
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