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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

    var showSnackbar by remember { mutableStateOf(false) }
    var snackBarText by remember { mutableStateOf("") }

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
                    modifier = modifier
                )

                GameBoard(
                    modifier = modifier,
                    verticalLines = 5,
                    horizontalLines = 3,
                    listPoints = punktsSpelViewModel.points,
                    onAddPoint = { point ->
                        punktsSpelViewModel.addPoint(point)
                    },
                    onShowSnackBar = { text ->
                        snackBarText = text
                        showSnackbar = !showSnackbar
                    },
                    onCatching = { point ->
                        //TODO: Add points in verticesList
                        if (punktsSpelViewModel.whosPoint(point) != point.toPlayer) {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Inte din punkt :/")
                            }
                            showSnackbar = !showSnackbar
                            return@GameBoard
                        }
                        if (punktsSpelViewModel.isActive(point)) {
                            verticesList.add(point)
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Kan inte använda den här!")
                            }
                            showSnackbar = !showSnackbar
                        }
                        //TODO: Check for cycle
                    }
                )
            }
            if ( showSnackbar ) {
                scope.launch {
                    snackbarHostState.showSnackbar(message = snackBarText)
                }
                showSnackbar = !showSnackbar
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