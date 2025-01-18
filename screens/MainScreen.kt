package com.example.project_group27_sljg.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import com.example.project_group27_sljg.models.LayoutElements
import com.example.project_group27_sljg.R
import com.example.project_group27_sljg.ui.theme.Project_group27_sljgTheme
import io.garrit.android.multiplayer.ServerState
import io.garrit.android.multiplayer.SupabaseService.serverState
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_group27_sljg.viewmodels.LobbyViewModel


@OptIn(ExperimentalTextApi::class)
@Composable
fun MainScreen(lobbyViewModel: LobbyViewModel = viewModel(),
               serverState: MutableStateFlow<ServerState>,
               modifier: Modifier = Modifier,
               navController: NavController = rememberNavController()) {

    //https://fonts.google.com
    //https://developer.android.com/jetpack/compose/text/fonts

    //CHATGPT: Not cited but inspo
    //https://chat.openai.com/share/90cb9677-7e94-48ff-b8ed-372a2bf9ba54
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingStart = with(LocalDensity.current) { (screenWidth / 2) + (screenWidth / 4) }


    LayoutElements(
        navController = navController,
        title = "Welcome to",
        title2 = "BattleShips",
        buttonText = "Create Player",
        navButtonText = "createPlayer",
        picture = R.drawable.boat2
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
    }
}

@Preview
@Composable
fun MainScreenPreview(modifier : Modifier = Modifier){
    val serverState = serverState
    Project_group27_sljgTheme {
        MainScreen(modifier = Modifier, serverState = serverState)
    }
}