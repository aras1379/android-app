package com.example.project_group27_sljg.models

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_group27_sljg.R
import com.example.project_group27_sljg.ui.theme.lightBlue1
import com.example.project_group27_sljg.ui.theme.lightBlue2
import com.example.project_group27_sljg.ui.theme.wave
import kotlin.math.sin
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.project_group27_sljg.ui.theme.paint1darker2
import com.example.project_group27_sljg.ui.theme.textColor1


//Code from LayoutBoard with some changes to make the name in the Board not blink for current player
//Like it does in LayoutBoard

@OptIn(ExperimentalTextApi::class)
@Composable
fun BoardLayout1(
    navController: NavController?= null,
    playerBoard1: String? = null,
    playerBoard2: String? = null
    ) {
    var isSettingsVisible by remember {mutableStateOf(false)}
    val isSettingsVisible2 by remember { mutableStateOf(false) }
    //wave height
    val amplitude2 = 20f
    //wave frequency
    val frequency2 = 0.04f

    fun calculateWaveY2(x: Float, canvasHeight: Float): Float {
        //wave top placement
        val centerY = canvasHeight / 7
        return centerY + amplitude2 * sin(frequency2 * x)
    }

    val displayLargeFontFamily =
        FontFamily(
            Font(
                R.font.cinzel_variable,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(290),
                    FontVariation.width(30f),
                    FontVariation.slant(-6f)
                )
            )
        )

    val displayLargeTextStyle = TextStyle(
        fontFamily = displayLargeFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
    )

    val displayMediumTextStyle = TextStyle(
        fontFamily = displayLargeFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )

    Surface(
        color = lightBlue1,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp, horizontal = 8.dp)

    ) {
        // ChatGPT https://chat.openai.com/share/344cc314-1db5-4f36-886b-63da4bc4ca0e
        // https://developer.android.com/jetpack/compose/graphics/draw/overview
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 20.dp)
                .background(color = lightBlue2),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val path = Path().apply {
                    moveTo(0f, size.height / 2)

                    var x = 0f
                    while (x < size.width) {
                        lineTo(
                            x, calculateWaveY2(x, size.height)
                        )
                        x += 10
                    }
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()

                }
                drawPath(path, color = wave)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.Center,

                ) {
                playerBoard1?.let {
                    Column(
                        modifier = Modifier
                            .zIndex(5f)
                            .size(width = 100.dp, height = 50.dp)
                            //.drawBehind { drawRect(animatedColor) }
                            .background(textColor1, shape = CutCornerShape(15.dp))
                            .border(4.dp,
                                paint1darker2,
                                shape = CutCornerShape(15.dp))
                            .clip(CutCornerShape(15.dp))
                            .padding(),

                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = playerBoard1,
                            style = displayMediumTextStyle,
                            color = paint1darker2,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "VS",
                        style = displayLargeTextStyle,
                        color = textColor1,
                        textAlign = TextAlign.Center
                    )
                }
                playerBoard2?.let {
                    Column(
                        modifier = Modifier
                            .size(width = 100.dp, height = 50.dp)
                            .background(textColor1, shape = CutCornerShape(15.dp))
                            .border(4.dp,
                             paint1darker2,
                                shape = CutCornerShape(15.dp))
                            .clip(CutCornerShape(15.dp))
                            .padding(),

                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = playerBoard2,
                            style = displayMediumTextStyle,
                            color = paint1darker2,
                            textAlign = TextAlign.Center
                        )
                    }

                }


            }
        }

        //Popup window, resources:
        // ChatGPT: https://chat.openai.com/share/bdd1354b-5724-4e4b-b828-d3cbb3926750
        // https://www.geeksforgeeks.org/popup-window-in-android-using-jetpack-compose/

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.End,

            ){
            FilledIconButton(onClick = { isSettingsVisible = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Settings, contentDescription ="Settings")
            }
            //Settings screen popup
            if(isSettingsVisible){
                SettingsPopup { isSettingsVisible = false }
            }

        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if (isSettingsVisible2) {
                Box(
                    modifier = Modifier
                        .zIndex(5f)
                        .fillMaxWidth()
                        .size(width = 250.dp, height = 350.dp)
                        .padding(16.dp)
                        .background(lightBlue1)
                        .border(10.dp, textColor1),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Settings",
                                style = displayMediumTextStyle,
                                color = textColor1,
                                textAlign = TextAlign.Center
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = { navController?.navigateUp() },
                                shape = CutCornerShape(10.dp),
                                border = BorderStroke(2.dp, lightBlue2),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = paint1darker2,
                                    contentColor = textColor1
                                ),
                                modifier = Modifier
                                    .size(width = 150.dp, height = 45.dp),
                            ) {
                                Text(
                                    text = "Go back",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp)
                            }
                        }
                    }


                }

            }




        }
    }
}
