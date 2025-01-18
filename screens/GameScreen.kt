package com.example.project_group27_sljg.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle.Alignment.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_group27_sljg.R
import com.example.project_group27_sljg.models.BoardLayout
import com.example.project_group27_sljg.ui.theme.background3
import com.example.project_group27_sljg.ui.theme.background4
import com.example.project_group27_sljg.ui.theme.lightBlue1
import com.example.project_group27_sljg.ui.theme.lightBlue2
import com.example.project_group27_sljg.ui.theme.paint1darker
import com.example.project_group27_sljg.ui.theme.paint1darker2
import com.example.project_group27_sljg.ui.theme.paint1lighter
import com.example.project_group27_sljg.ui.theme.textColor1
import com.example.project_group27_sljg.ui.theme.transparent
import com.example.project_group27_sljg.ui.theme.wave
import com.example.project_group27_sljg.viewmodels.GameViewModel
import io.garrit.android.multiplayer.ActionResult
import io.garrit.android.multiplayer.GameResult
import io.garrit.android.multiplayer.SupabaseService

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalTextApi::class)
@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel(),
    navController: NavController = rememberNavController(),
    player1: String = (SupabaseService.currentGame?.player1?.name ?: ""),
    player2: String = (SupabaseService.currentGame?.player2?.name ?: "")
) {
    val text = when (gameViewModel.updateStatusTxt.value) {
        ActionResult.HIT -> "HIT"
        ActionResult.SUNK -> "SUNK"
        ActionResult.MISS -> "MISS"
        else -> "Wait for shot"
    }

    val fontForXY =
        FontFamily(
            Font(
                R.font.novasquare,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(790),

                    )
            )
        )
    val displayFontForWY = TextStyle(
        fontFamily = fontForXY,
        fontSize = 45.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start,
        lineHeight = 0.sp,
        letterSpacing = 0.sp,
        textIndent = TextIndent.None,
    )

    BoardLayout(
        navController = navController,
        gameViewModel = gameViewModel,
        player1 = player1,
        player2 = player2
    )
    if (gameViewModel.showPopUp.value) {
        Box(modifier = Modifier
            .fillMaxSize()
            .zIndex(5f)) {
            StatusPopUp(text = text, gameViewModel = gameViewModel)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        /*TODO: EV LÄGG IN I LAYOUT */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = 170.dp, end = 40.dp)
                .background(lightBlue1, shape = CutCornerShape(15.dp))
                .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
                .clip(CutCornerShape(15.dp)),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                val buttonText = if (gameViewModel.playerLogic.isPlayerTurn.value) {
                    if (gameViewModel.isSelectedCellBool.value) {
                        "Send shot"
                    }else {
                        "Select Cell"
                    }
                } else {
                    "Wait"
                }
                Button(
                    /*TODO: IF column checked: Send shot,
                                   Else: Choose cell**/
                    onClick = {
                        val ixRow = gameViewModel.selectedCellCoordinates.value?.component1()
                        val ixCol = gameViewModel.selectedCellCoordinates.value?.component2()
                        val coordinates =
                            gameViewModel.selectedCellCoordinates.value ?: return@Button
                        if (gameViewModel.isSelectedCellBool.value) {
                            println("SHOT is sent: $coordinates")
                            if (ixRow != null && ixCol != null) {
                                gameViewModel.selectCell(ixRow, ixCol)
                            }
                            gameViewModel.buttonShotClick()

                        }
                    },
                    enabled = gameViewModel.isSelectedCellBool.value && gameViewModel.playerLogic.isPlayerTurn.value , // IF SELECTED CELL ,

                    shape = CutCornerShape(10.dp),
                    border = BorderStroke(2.dp, lightBlue2),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = paint1darker2,

                        contentColor = textColor1
                    ),
                    modifier = Modifier
                        .size(width = 190.dp, height = 45.dp),


                    ) {
                    Icon(
                        Icons.Outlined.Send,
                        contentDescription = "Action handler",
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = buttonText,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // Overlap two boxes
            // https://stackoverflow.com/questions/70799640/overlap-two-box-jetpack-compose
            BoxWithConstraints() {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(45.dp)
                        .background(lightBlue1, shape = CutCornerShape(10.dp))
                        .border(4.dp, paint1darker2, shape = CutCornerShape(10.dp))
                        .clip(CutCornerShape(10.dp)),

                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(45.dp)
                            .padding(top = 0.dp),
                        verticalAlignment = Alignment.Top

                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 0.dp)
                                .fillMaxHeight()
                                .background(paint1darker),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        )
                        {

                            Text(
                                text = "X",
                                style = displayFontForWY,
                                color = paint1lighter,
                                modifier = Modifier.padding(top = 0.dp),
                                lineHeight = 0.sp,
                                textAlign = TextAlign.Center,

                                )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .width(50.dp)
                                .fillMaxHeight()
                                .background(paint1lighter),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Y",
                                style = displayFontForWY,
                                color = paint1darker,
                                modifier = Modifier.padding(top = 0.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .width(120.dp)
                            .zIndex(2f)
                            .background(transparent),
                        contentAlignment = Alignment.Center

                    ) {
                        ViewCoordinates(gameViewModel = gameViewModel)


                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val playerTurnView = PlayerTurnView(gameViewModel = gameViewModel)

            //https://developer.android.com/jetpack/compose/animation/composabes-modifiers
            if (!gameViewModel.playerLogic.isPlayerTurn.value) {
                PlayerWaitView(gameViewModel)
            } else {
                GameGrid(
                    gridData = playerTurnView,
                    gameViewModel = gameViewModel,
                    onClick = { /* No-op in preview */ })
            }
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            StatusMessage(gameViewModel = gameViewModel)

        }
        ResultPopup(navController = navController,
            gameResult = gameViewModel.gameResult.value, gameViewModel = gameViewModel,
            onDismiss = { gameViewModel.gameResult.value = null }
        )
    }
}


//CHATGPT , not exact solution ive changed after
//https://chat.openai.com/share/95631511-0a5a-4334-8b04-f2ee20cdd40b
/*TODO : Spara skeppen och sunk i egen? Kolla först vad gridCell är */
@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalTextApi::class)
@Composable
fun StatusMessage(gameViewModel: GameViewModel){
    val pixelFontStyle = FontFamily(Font(
        R.font.pixelfont,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(290),
            FontVariation.width(30f),
            FontVariation.slant(-6f)
        )
    ))

    val pixelFont = TextStyle(
        fontFamily = pixelFontStyle,
        fontSize = 50.sp,
    )
    val text = when (gameViewModel.updateStatusTxt.value){
        ActionResult.HIT -> "HIT"
        ActionResult.SUNK -> "SUNK"
        ActionResult.MISS -> "MISS"
        else -> "Wait for shot"
    }
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
   /* val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "scale"
    )*/
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF196581),
        targetValue = Color(0xFF55B1DB),
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )


    Column(
        modifier = Modifier
            .size(width = 280.dp, height = 80.dp)

            .border(5.dp, color = animatedColor, shape = CutCornerShape(8.dp))
            .background(Color(0xFF79C1DC), shape = CutCornerShape(8.dp))
            .clip(shape = CutCornerShape(8.dp))
            ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        Row() {

                if (!gameViewModel.playerLogic.isPlayerTurn.value) {

                    Text(
                        text = text,
                        style = pixelFont,
                        color = animatedColor,
                        fontSize = 16.sp,

                        //.align(Alignment.Center),
                        // Text composable does not take TextMotion as a parameter.
                        // Provide it via style argument but make sure that we are copying from current theme

                    )
                } else {
                    Text(
                        text = text,
                        style = pixelFont,
                        color = animatedColor,
                        fontSize = 16.sp,
                        modifier = Modifier
                    )
            }
        }
        }
}
//https://developer.android.com/jetpack/compose/animation/quick-guide
@OptIn(ExperimentalTextApi::class)
@Composable
fun StatusPopUp(text: String, gameViewModel: GameViewModel){
    val text = text
    val showPopUp by gameViewModel.showPopUp

    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xC83C9ABD),
        targetValue = Color(0xFF06232E),
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )

    val animatedColorText by infiniteTransition.animateColor(
        initialValue = Color(0xFF06232E),
        targetValue = Color(0xC83C9ABD),
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
        label = "color"
    )

    val pixelFontStyle = FontFamily(Font(
        R.font.pixelfont,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(290),
            FontVariation.width(30f),
            FontVariation.slant(-6f)
        )
    ))

    val pixelFont = TextStyle(
        fontFamily = pixelFontStyle,
        fontSize = 50.sp,
    )

    AnimatedVisibility(visible = showPopUp,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(transparent)
                .zIndex(4f),

            contentAlignment = Alignment.Center
        ){
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(animatedColor)
                    .clickable {
                        gameViewModel.showPopUp.value = false
                    },
            )
            Text(
                text= text,
                color = textColor1,
                style = pixelFont
            )
        }

    }

}


@OptIn(ExperimentalTextApi::class)
@Composable
fun ViewCoordinates(gameViewModel: GameViewModel) {
    val textX = gameViewModel.selectedCellCoordinates.value?.first?.toString() ?: ""
    val textY = gameViewModel.selectedCellCoordinates.value?.second?.toString() ?: ""
    val fontForXY =
        FontFamily(
            Font(
                R.font.novasquare,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(790),

                    )
            )
        )
    val displayFontForWY = TextStyle(
        fontFamily = fontForXY,
        fontSize = 30.sp,
        fontWeight = FontWeight.Medium,
        // textAlign = TextAlign.Start,
        lineHeight = 0.sp,
        letterSpacing = 0.sp,
        textIndent = TextIndent.None,
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Column(
            modifier = Modifier
                .padding()
                .zIndex(1f)
                .weight(1f),

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = textX, //"($selectedCoord.first}")
                style = displayFontForWY,
                textAlign = TextAlign.Center,
                color = background3
            )
        }
        Column(
            modifier = Modifier
                .padding()
                .zIndex(1f)
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text= textY,
                //(${selectedCoord.second}")
                style = displayFontForWY,
                textAlign = TextAlign.Center,
                color = background4
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GameGrid(gridData: List<List<GameViewModel.GridCellType>>,
             gameViewModel: GameViewModel,
             onClick: (GameViewModel.GridCellType) -> Unit = {}) {
    LazyVerticalGrid(columns = GridCells.Fixed(gameViewModel.gridSize),
        modifier = Modifier
            .padding(start = 32.dp, top = 20.dp, end = 32.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.Center,
    ) {

        itemsIndexed(gridData.flatten()) { index, cell ->
           // var selectedIndex by remember { mutableStateOf(-1) }
            val x = index % gameViewModel.gridSize
            val y = index / gameViewModel.gridSize


            if(gameViewModel.playerLogic.isPlayerTurn.value){
                PlayerTurnGridCell(cell = cell, gameViewModel = gameViewModel,
                    onClick = {
                    gameViewModel.selectCell(x,y)})
            }else{
                OpponentGridCell(cell = cell, gameViewModel = gameViewModel) {
                }
            }
            //GridCell(cell, gameViewModel, onClick = { gameViewModel.selectCell(x,y)})
        }
    }
}

//marking of cells :
//https://stackoverflow.com/questions/74563970/change-background-color-of-surface-item-when-click-in-jetpack-compose
@Composable
fun PlayerTurnGridCell(cell: GameViewModel.GridCellType, gameViewModel: GameViewModel, onClick: () -> Unit) {
    val cellColor = when{
        cell.isHit.value && cell.markedCell.value -> Color.Red
        cell.shotCell.value -> Color.Gray
        cell.markedCell.value -> Color.White
        else -> lightBlue1
    }
    Box(
        modifier = Modifier
            .size(gameViewModel.cellSize)
            .background(
                cellColor
            )
            .border(2.dp, wave)
            .clickable {
                onClick()
            }

    ){
    }
}

@Composable
fun OpponentGridCell(cell: GameViewModel.GridCellType, gameViewModel: GameViewModel, onClick: () -> Unit) {
    val cellColor = when {
        cell.isHit.value && cell.isShip.value -> Color.Red
        cell.isShip.value -> textColor1
        else -> lightBlue1
    }
    Box(
        modifier = Modifier
            .size(gameViewModel.cellSize)
            .background(
                cellColor
            )
            .border(2.dp, wave)
            .clickable {
                onClick()
            }

    ) {

    }
}

@Composable
fun PlayerWaitView(gameViewModel: GameViewModel){
    GameGrid(gridData = gameViewModel.boardState.value.cells, gameViewModel)
}

fun PlayerTurnView(gameViewModel: GameViewModel): List<List<GameViewModel.GridCellType>> {
    return gameViewModel.boardState.value.cells.map{ row ->
        row.map{ cell ->
            GameViewModel.GridCellType(
                isShip = mutableStateOf(false),
                isHit = cell.isHit,
                markedCell = cell.markedCell,
                shotCell = cell.shotCell
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultPopup(navController: NavController, gameResult: GameResult?, gameViewModel: GameViewModel, onDismiss: () -> Unit){
    if( gameViewModel.isGameFinished.value ) {

        AlertDialog(onDismissRequest = { /*TODO*/ },
            title = { Text("GAME OVER") },
            text = { Text(if (gameResult == GameResult.LOSE) "You have lost!" else "You have WON!") },

            confirmButton = {
                //https://stackoverflow.com/questions/69415996/jetpack-compose-navcontroller-popbackstack-multiple-pages
                Button(onClick = {

                    navController.popBackStack(route = "createPlayer", inclusive = false)

                   //
                }) {
                    Text("Leave Game")
                }
        })
    }
}

@Preview(showBackground = true)
@Composable
fun GamePreview() {
    val challangerId = "Sara"
    val opponentId = "Jenny"

    GameScreen(player1 = challangerId, player2 = opponentId )
    GameScreen()

}