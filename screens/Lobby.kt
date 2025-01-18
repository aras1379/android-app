package com.example.project_group27_sljg.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_group27_sljg.models.LayoutElements
import com.example.project_group27_sljg.ui.theme.Project_group27_sljgTheme
import io.garrit.android.multiplayer.ServerState
import kotlinx.coroutines.flow.StateFlow
import io.garrit.android.multiplayer.SupabaseService.serverState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_group27_sljg.Screen
import com.example.project_group27_sljg.ui.theme.paint11
import com.example.project_group27_sljg.ui.theme.paint1darker2
import com.example.project_group27_sljg.ui.theme.textColor1
import com.example.project_group27_sljg.viewmodels.LobbyViewModel
import io.garrit.android.multiplayer.Game
import io.garrit.android.multiplayer.SupabaseService.users
import io.garrit.android.multiplayer.SupabaseService.games
import com.example.project_group27_sljg.ui.theme.displayLargeFontFamily
import com.example.project_group27_sljg.viewmodels.GameViewModel


//ChatGPT:
// https://chat.openai.com/share/43dda95e-97f9-4485-9933-b6df00086e1d
@Composable
fun ServerButtons(serverState: StateFlow<ServerState>,
                  lobbyViewModel: LobbyViewModel,
                  navController: NavController, gameViewModel: GameViewModel ) {
    val currentState by serverState.collectAsState()

    //ChatGPT helped with how to section the lobby screen into three parts
     val sectionHeight = (LocalDensity.current.density * 75).dp

    LayoutElements(
        title2 = "Lobby",
    )
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = sectionHeight, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentState == ServerState.LOBBY) {
                LobbyPlayerList(lobbyViewModel = lobbyViewModel)
            }
            // https://developer.android.com/jetpack/compose/side-effects
            LaunchedEffect(currentState) {
                if (currentState == ServerState.GAME) {
                    gameViewModel.resetGame()
                    gameViewModel.resetAllCells()
                    navController.navigate(Screen.BoardScreen.route)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = sectionHeight, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (currentState == ServerState.LOBBY) {
                ReceivedInvitations(lobbyViewModel = lobbyViewModel, navController = navController)
            }
        }
    }
}

@Composable
fun LobbyPlayerList(lobbyViewModel: LobbyViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (users.isEmpty()) {
            Text(
                text = "No players online.",
                style = TextStyle(
                    fontFamily = displayLargeFontFamily,
                    fontWeight = FontWeight.Bold
                )
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(users) { user ->
                    //I got the buttons to change to "Challenged" when pressing it, but it made all buttons for all players change
                    //So ChatGpt helped with how to change just the button pressed for the specific player
                    //https://chat.openai.com/share/2ce8797c-b547-46bc-b86d-e74421594c74
                    val opponentChallenged = lobbyViewModel.challengedUser[user.id]?.value ?: false
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = user.name,
                            style = TextStyle(
                                fontFamily = displayLargeFontFamily,
                                fontWeight = FontWeight.Bold),
                            color = Color.Black,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(width = 0.dp, height = 55.dp)
                                .background(Color.Yellow, shape = CutCornerShape(15.dp))
                                .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
                                .clip(CutCornerShape(15.dp))
                                .padding(16.dp)
                        )

                        val buttonText = if (opponentChallenged) "Challenged" else "Challenge"
                        Button(
                            onClick = { lobbyViewModel.invite(user) },
                            modifier = Modifier
                                .size(width = 150.dp, height = 40.dp)
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = paint1darker2,
                                contentColor = textColor1
                            ),

                            ) {
                            Text(
                                text = buttonText,
                                style = TextStyle(
                                    fontFamily = displayLargeFontFamily,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ReceivedInvitations(lobbyViewModel: LobbyViewModel,
                        navController : NavController
) {

    if (games.isNotEmpty()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    //I managed to fix popup but could not figure out how to make the popup appear over other content
                    //So ChatGPT helped with that part, but I changed some things around as well
                    //https://chat.openai.com/share/57bdd319-d202-4c4c-9d20-169ed0dd87e4

                    .background(color = paint11, shape = CutCornerShape(15.dp))
                    .graphicsLayer(
                        shape = MaterialTheme.shapes.medium,
                        clip = true,
                        alpha = 0.8f
                    )
                    .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
                    .clip(CutCornerShape(15.dp))
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(games) { invitation ->
                    //https://developer.android.com/jetpack/compose/components/dialog
                    var showDialog by remember { mutableStateOf(true) }
                    if (showDialog) {
                        InvitationDialog(
                            invitation = invitation,
                            onAccept = {
                                lobbyViewModel.acceptInvite(invitation)
                                navController.navigate(Screen.BoardScreen.route)
                                showDialog = false
                            },
                            onDecline = {
                                lobbyViewModel.declineInvite(invitation)
                                navController.navigate(Screen.LobbyScreen.route)
                                showDialog = false
                            },
                        )
                    }
                }
            }
        }
    }
}

//https://developer.android.com/jetpack/compose/components/dialog
@Composable
fun InvitationDialog(
    invitation: Game,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    //https://chat.openai.com/share/a3efaec2-67b9-4892-a445-b627e282b6a4
    //ChatGPT helped with how to be able to display the invitations with the correct sender
    val send = invitation.player1
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF196581),
        targetValue = Color(0xFF5EBCDF),
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )
    Box(
        modifier = Modifier
            //.drawBehind { drawRect(animatedColor) }
            .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
            .background(animatedColor, shape = CutCornerShape(15.dp))
            .padding()
            .fillMaxSize()
            .graphicsLayer(clip = false)

            .clip(CutCornerShape(15.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Invited by ${send.name}",
                    style = TextStyle(
                        fontFamily = displayLargeFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onAccept() },
                    modifier = Modifier
                        .size(width = 130.dp, height = 40.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = paint1darker2,
                        contentColor = textColor1
                    )
                ) {
                    Text(
                        text = "Accept",
                        style = TextStyle(
                            fontFamily = displayLargeFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Button(
                    onClick = { onDecline() },
                    modifier = Modifier
                        .size(width = 130.dp, height = 40.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = paint1darker2,
                        contentColor = textColor1
                    )
                ) {
                    Text(
                        text = "Decline",
                        style = TextStyle(
                            fontFamily = displayLargeFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun LobbyScreen(
    gameViewModel: GameViewModel = viewModel(),
    serverState: StateFlow<ServerState>,
    lobbyViewModel: LobbyViewModel = viewModel(),
    navController: NavController = rememberNavController(),

) {
    ServerButtons(serverState = serverState, lobbyViewModel = lobbyViewModel, navController = navController, gameViewModel=gameViewModel )
}

@Preview
@Composable
fun LobbyPreview(){
    val navController = rememberNavController()
    val serverState = serverState
    val lobbyViewModel = viewModel<LobbyViewModel>()


    Project_group27_sljgTheme {
        LobbyScreen(navController = navController,  serverState = serverState, lobbyViewModel = lobbyViewModel)
    }
}