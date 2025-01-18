package com.example.project_group27_sljg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.project_group27_sljg.ui.theme.Project_group27_sljgTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.project_group27_sljg.screens.BoardScreen
import com.example.project_group27_sljg.screens.CreatePlayer
import com.example.project_group27_sljg.screens.GameScreen
import com.example.project_group27_sljg.screens.LobbyScreen
import com.example.project_group27_sljg.screens.MainScreen
import com.example.project_group27_sljg.viewmodels.GameViewModel
import io.garrit.android.multiplayer.SupabaseService.serverState

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val gameViewModel : GameViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val serverState = serverState
            Project_group27_sljgTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .padding()
                            .fillMaxSize()
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.CreatePlayer.route
                        ) {
                            composable(Screen.MainScreen.route) {
                                MainScreen(modifier = Modifier, navController = navController, serverState = serverState)
                            }

                            composable(Screen.CreatePlayer.route) {
                                CreatePlayer(navController = navController)
                            }

                            composable(Screen.LobbyScreen.route){


                                LobbyScreen(gameViewModel = gameViewModel, navController = navController,  serverState = serverState)
                            }

                            composable(Screen.BoardScreen.route){
                                //create new viewmodel

                                BoardScreen(gameViewModel = gameViewModel, navController = navController)
                            }

                            composable(Screen.GameScreen.route){
                                GameScreen(gameViewModel=gameViewModel, navController=navController)
                            }

                            //GÖR EN COMPOSABLE NAVIGATION FÖR DIN LOBBY
                        }

                    }
                }
            }
        }
    }
}