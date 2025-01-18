package com.example.project_group27_sljg

sealed class Screen(val route: String){
    object HomeScreen : Screen("homeScreen")

    object MainScreen : Screen ("mainScreen")

    object CreatePlayer : Screen("createPlayer")

    object LobbyScreen : Screen("lobbyScreen")
    object BoardScreen : Screen("boardScreen")

    /*class BoardScreen : Screen("boardScreen"){
        fun createRoute(gameId: String, challangerId: String, opponentId: String,)=
            "$route/$gameId/$challangerId/$opponentId"
    }
*/
    object GameScreen: Screen("gameScreen")

/*
    class GameScreen : Screen("gameScreen"){
        fun createRoute2(challangerId: String, opponentId: String,gameId: String)=
            "$route/$challangerId/$opponentId/$gameId/"
    }
*/

}
