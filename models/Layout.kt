package com.example.project_group27_sljg.models

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_group27_sljg.R
import com.example.project_group27_sljg.ui.theme.Project_group27_sljgTheme
import com.example.project_group27_sljg.ui.theme.lightBlue1
import com.example.project_group27_sljg.ui.theme.lightBlue2
import com.example.project_group27_sljg.ui.theme.paint1darker2
import com.example.project_group27_sljg.ui.theme.textColor1
import com.example.project_group27_sljg.ui.theme.wave

import kotlin.math.sin

//LAYOUT COPIED FROM MAIN SCREEN
@OptIn(ExperimentalTextApi::class)  //https://chat.openai.com/share/219fc72c-0fec-4593-8885-2782fd0d70d9
@Composable
fun LayoutElements(modifier: Modifier = Modifier,
                   navController: NavController?=null,
                   title: String? = null,
                   title2: String? = null,
                   buttonText: String? = null,
                   buttonText2: String? = null,
                   buttonTextResult: String? = null,
                   navButtonText: String? = null,
                   navButtonText2: String?= null,
                   navButtonTextResult: String? = null,
                   picture: Int? = null) {
    //wave height
    val amplitude = 20f
    //wave frequency
    val frequency = 0.04f

    fun calculateWaveY(x: Float, canvasHeight: Float): Float {
        //wave top placement
        val centerY = canvasHeight / 2 + 50f
        return centerY + amplitude * sin(frequency * x)
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


    val textFamily =
        FontFamily(
            Font(
                R.font.teko,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(290),
                    FontVariation.width(30f),
                    FontVariation.slant(-6f)
                )
            )
        )

    val displayLargeTextButtonStyle = TextStyle(
        fontFamily = textFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )


    Surface(
        color = lightBlue1,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 4.dp, horizontal = 8.dp)

    ) {
        // CHATGPT https://chat.openai.com/share/344cc314-1db5-4f36-886b-63da4bc4ca0e
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
                            x, calculateWaveY(x, size.height)
                        )
                        x += 10
                    }
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()

                }
                drawPath(path, color = wave)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    picture?.let{
                        Image(
                            painter = painterResource(id = picture),
                            contentDescription = "boat picture",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }
                }

                title?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Text(
                            text = title,
                            style = displayLargeTextStyle,
                            color = textColor1
                        )
                    }
                }

                title2?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {


                        Column(
                            modifier = Modifier
                                .size(width = 200.dp, height = 70.dp)
                                .background(textColor1, shape = CutCornerShape(15.dp))
                                .border(4.dp, paint1darker2, shape = CutCornerShape(15.dp))
                                .clip(CutCornerShape(15.dp))
                                .padding(),

                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = title2,
                                style = displayLargeTextStyle,
                                color = paint1darker2,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                buttonText?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 100.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        navButtonText?.let {
                            Button(
                                onClick = { navController?.navigate(navButtonText) },
                                shape = CutCornerShape(10.dp),
                                border = BorderStroke(2.dp, lightBlue2),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = paint1darker2,
                                    contentColor = textColor1
                                )
                            ) {
                                Text(
                                    text = buttonText,
                                    style = displayLargeTextButtonStyle,
                                    color = textColor1
                                )
                            }
                        }
                    }
                }

                buttonText2?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        navButtonText2?.let {
                            Button(
                                onClick = { navController?.navigate(navButtonText2) },
                                shape = CutCornerShape(10.dp),
                                border = BorderStroke(2.dp, lightBlue2),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = paint1darker2,
                                    contentColor = textColor1
                                )
                            ) {
                                Text(
                                    text = buttonText2,
                                    style = displayLargeTextButtonStyle,
                                    color = textColor1
                                )
                            }
                        }
                    }
                }

                buttonTextResult?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 300.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        navButtonTextResult?.let {
                            Button(
                                onClick = { navController?.navigate(navButtonTextResult) },
                                shape = CutCornerShape(10.dp),
                                border = BorderStroke(2.dp, lightBlue2),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = paint1darker2,
                                    contentColor = textColor1
                                )
                            ) {
                                Text(
                                    text = buttonTextResult,
                                 //   style = displayLargeTextButtonStyle,
                                    style = TextStyle(
                                        fontFamily = com.example.project_group27_sljg.ui.theme.displayLargeFontFamily,
                                        fontWeight = FontWeight.Bold),
                                    color = textColor1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun LayoutElementsPreview() {
    val navController = rememberNavController()

    Project_group27_sljgTheme {
        LayoutElements(modifier = Modifier, navController = navController)
    }
}