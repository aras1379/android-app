package com.example.project_group27_sljg.screens


import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_group27_sljg.models.BoardLayout1
import com.example.project_group27_sljg.ui.theme.Project_group27_sljgTheme
import com.example.project_group27_sljg.ui.theme.backgroundcolor1
import com.example.project_group27_sljg.ui.theme.lightBlue1
import com.example.project_group27_sljg.ui.theme.lightBlue2
import com.example.project_group27_sljg.ui.theme.paint1darker
import com.example.project_group27_sljg.ui.theme.paint1darker2
import com.example.project_group27_sljg.ui.theme.textColor1
import com.example.project_group27_sljg.ui.theme.transparent
import com.example.project_group27_sljg.ui.theme.wave
import com.example.project_group27_sljg.viewmodels.GameViewModel
import io.garrit.android.multiplayer.SupabaseService


/*TODO: SHOW SHIP NAME ,
   SET SHIP PLACEMENT
   */

class Board(
    val size: Int = 10,
    private val player1Name: String,
    private val player2Name: String
) {

    //IS GOING TO BE MOVED NOT USED



}
//CHAT GPT HELP W COORDINATES
//https://chat.openai.com/share/359b424b-25eb-4f88-8869-2245abf18df0
@SuppressLint("UnrememberedMutableState", "StateFlowValueCalledInComposition")
@Composable
fun BoardScreen(gameViewModel: GameViewModel = viewModel(),  navController: NavController = rememberNavController(),
                playerBoard1: String = (SupabaseService.currentGame?.player1?.name ?: ""),
                playerBoard2: String = (SupabaseService.currentGame?.player2?.name ?: "")) {
    BoardLayout1(
        navController = navController,
        playerBoard1 = playerBoard1,
        playerBoard2 = playerBoard2
    )

    val isPlayerReady by gameViewModel.playerLogic.playerReady.collectAsState()
    val arePlayersReady by gameViewModel.playerLogic.arePlayersReady.collectAsState()
    val opponentReady by gameViewModel.playerLogic.isOppPlayerReady.collectAsState()
    val challReady by gameViewModel.playerLogic.isCurrPlayerReady.collectAsState()

    val playerDone = mutableStateOf(false )
    val areBothPlayersReady by gameViewModel.playerLogic.arePlayersReady.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(1f)
    ) {
        Box {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, top = 170.dp, end = 40.dp)
                    .background(lightBlue1, shape = CutCornerShape(15.dp))
                    .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
                    .clip(CutCornerShape(15.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)

                ) {
                    FilledIconButton(onClick = { gameViewModel.rotateShip() }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Turn")
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            gameViewModel.playerReady()
                            // gameViewModel.isPlayClicked.value = true
                            //gameViewModel.playerLogic.checkBothPlayersReady()
                            print("areplayersready OK go into gameScreen from Board")

                            navController.navigate("gameScreen")


                        },
                        enabled = gameViewModel.isAllShipSet(),
                        shape = CutCornerShape(10.dp),
                        border = BorderStroke(2.dp, lightBlue2),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = paint1darker2,
                            contentColor = textColor1
                        ),
                        modifier = Modifier
                            .size(width = 150.dp, height = 45.dp),

                        ) {
                        Icon(
                            Icons.Outlined.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            text = "PLAY!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

            }
            /* if () {
                 Box(
                     modifier = Modifier
                         .padding(top = 170.dp)
                         .zIndex(4f)
                         .size(width = 350.dp, height = 80.dp)
                 ) {
                     waitPopup(gameViewModel = gameViewModel)
                 }
             }*/
        }

        //val selectedShip by gameViewModel.selectedShip.collectAsState()

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val boardState = gameViewModel.boardState.value
            LazyVerticalGrid(
                columns = GridCells.Fixed(gameViewModel.gridSize),
                modifier = Modifier

                    .padding(start = 32.dp, top = 20.dp, end = 32.dp),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.Center,

                ) {
                val coordinates = gameViewModel.coordinates
                items(coordinates) { cell ->
                    val index = coordinates.indexOf(cell)
                    val row = index / gameViewModel.gridSize
                    val col = index % gameViewModel.gridSize

                    val isShipSet = gameViewModel.isShipSetAt(row, col)

                    BoardCell(
                        cell = cell,
                        isShipSet = isShipSet,
                        gameViewModel = gameViewModel,
                        onClick = {
                            gameViewModel.placeShip(row, col)
                            //if(gameViewModel.coordinates == coordinate.isActive){
                            //  gameViewModel.placeShip(row,col)

                            //}
                        }
                    )
                }

            }

        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp)
                .background(lightBlue2)
                .border(5.dp, paint1darker2),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //https://www.geeksforgeeks.org/horizontal-listview-in-android-using-jetpack-compose/
            Column {
                shipListView(LocalContext.current, gameViewModel = gameViewModel)
            }
        }

    }
}
//GRIDS
//https://developer.android.com/jetpack/compose/lists


@Composable
fun BoardCell(cell: GameViewModel.GridCellType,
              isShipSet: Boolean,
              gameViewModel: GameViewModel,

              onClick: () -> Unit)
{
    // var currentShip: GameViewModel.Ship? by mutableStateOf(null)
    //val isShipSet = coordinates.isShipId?.let{id ->
    //  gameViewModel.ships.value.find{it.id == id}?.isSet?.value ?: false
    //}?: false

    Box(
        modifier = Modifier
            .size(gameViewModel.cellSize)
            .background(
                if (cell.isShip.value) textColor1
                else if (cell.isEdge.value) backgroundcolor1
                else lightBlue1
            )
            .border(2.dp, wave)
            .clickable {
                onClick()
            }
    ){
        Text(
            text = "",
            modifier = Modifier.align(Alignment.Center),
            color = textColor1
        )
    }
}
// https://developer.android.com/jetpack/compose/animation/quick-guide#animate-text-scale
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun waitPopup(gameViewModel: GameViewModel) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xD3AA6704),
        targetValue = Color(0xE9CA8E35),
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )


    Box(
        modifier = Modifier
            .zIndex(3f)
            .fillMaxWidth()
            .padding(start = 40.dp, top = 170.dp, end = 40.dp)
            .background(lightBlue1, shape = CutCornerShape(15.dp))
            .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
            .clip(CutCornerShape(15.dp)),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 170.dp, end = 40.dp)
                .background(lightBlue1, shape = CutCornerShape(15.dp))
                .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
                .clip(CutCornerShape(15.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Wait for Opponent",
                color = animatedColor,
                fontSize = 40.sp

            )

        }
    }
}


// https://www.geeksforgeeks.org/horizontal-listview-in-android-using-jetpack-compose/


//https://developer.android.com/jetpack/androidx/releases/paging
//https://developer.android.com/jetpack/compose/layouts/pager

// https://chat.openai.com/share/fbe31b2e-117c-4642-a52d-c0967d982de6
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun shipListView(context: Context, gameViewModel: GameViewModel = viewModel()){
    val ships = gameViewModel.ships.value
    val showPopUp by gameViewModel.showPopUp
    val countShips = gameViewModel.ships.value.count { ship ->
        ship.isSet.value
    }
    Box(){
        if(countShips == 0){
            ShipPopUp(gameViewModel = gameViewModel)
        }else{

        }
        LazyRow(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(ships) { ship ->
                val isSelected = ship.id == gameViewModel.selectedShipId.value
                ShipCard(ship = ship, isSelected = isSelected)
                {
                    gameViewModel.setSelectedShip(ship)
                    Toast.makeText(
                        context,
                        ships[ship.id].name + " selected",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
        }
    }





}

@Composable
fun ShipPopUp(gameViewModel: GameViewModel){
    val showPopUp by gameViewModel.showPopUp
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xD2196581),
        targetValue = Color(0xDA55B1DB),
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )

    AnimatedVisibility(visible = showPopUp,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedColor)
                .zIndex(4f)
                .clickable { gameViewModel.showPopUp.value = false },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Select Ship",
                color = textColor1,
                fontSize = 30.sp
            )

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipCard(ship: GameViewModel.Ship, isSelected: Boolean, onClick: () -> Unit){
    Card(

        modifier = Modifier
            .padding(10.dp)
            .background(
                color = (if (ship.isUnmarked) textColor1 else if (ship.isShipPlaced) paint1darker else transparent),
                shape = RoundedCornerShape(10.dp)
            )
            .size(150.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = transparent
        ),
        onClick = onClick,
    ){
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){

            Image(
                painterResource(id = ship.imageResourceId),
                contentDescription = ship.name,
                modifier = Modifier.fillMaxWidth()
            )

        }

    }
}

@Preview
@Composable
fun BoardViewPreview(){
    val navController = rememberNavController()
    val player = "Hej"

    Project_group27_sljgTheme {
        BoardScreen(navController = navController)
    }

}