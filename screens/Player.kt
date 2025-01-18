package com.example.project_group27_sljg.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_group27_sljg.models.LayoutElements
import com.example.project_group27_sljg.ui.theme.Project_group27_sljgTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_group27_sljg.R
import com.example.project_group27_sljg.Screen
import com.example.project_group27_sljg.ui.theme.displayLargeFontFamily
import com.example.project_group27_sljg.ui.theme.lightBlue2
import com.example.project_group27_sljg.ui.theme.paint1darker2
import com.example.project_group27_sljg.ui.theme.textColor1
import com.example.project_group27_sljg.viewmodels.LobbyViewModel


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalTextApi::class)
@Composable
fun CreatePlayer(
    lobbyViewModel: LobbyViewModel = viewModel(),
    navController : NavController = rememberNavController()
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingStart = with(LocalDensity.current) { (screenWidth / 2) + (screenWidth/4)}
    var name by rememberSaveable {
        mutableStateOf("")
    }
    LayoutElements(
          navController = navController,
          title = "Create player:",
          picture = R.drawable.boatpic
      )
          Column (
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(top = paddingStart),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally
          ) {
              Text(
                  text = "Enter name:",
                  style = TextStyle(
                          fontFamily = displayLargeFontFamily,
                          fontWeight = FontWeight.Bold
               )
              )

              BasicTextField(
                  value = name,
                  modifier = Modifier
                      .fillMaxWidth(0.7f)
                      .padding(16.dp)
                      .background(Color.White),
                  onValueChange = {
                      name = it
                  }
              )
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(top = 40.dp),
                  horizontalArrangement = Arrangement.Center,
                  verticalAlignment = Alignment.CenterVertically
              ) {
                  Button(
                      onClick = {
                          if(name.isNotBlank()){
                              lobbyViewModel.createPlayer(name)
                          }

                          navController.navigate(Screen.LobbyScreen.route)
                      },
                      shape = CutCornerShape(10.dp),
                      border = BorderStroke(2.dp, lightBlue2),
                      colors = ButtonDefaults.buttonColors(
                          containerColor = paint1darker2,
                          contentColor = textColor1
                      )
                  ) {
                      Text(
                          text = "Join Lobby",
                          style = TextStyle(
                              fontFamily = displayLargeFontFamily,
                              fontWeight = FontWeight.Bold),
                          color = textColor1
                      )
                  }
              }
          }
      }


    @Preview
    @Composable
    fun CreatePlayerPreview() {
        val navController = rememberNavController()

        Project_group27_sljgTheme {
              CreatePlayer( navController = navController)
        }
    }



