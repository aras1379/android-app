package com.example.project_group27_sljg.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.SupabaseService
import kotlinx.coroutines.launch
import io.garrit.android.multiplayer.Game
import androidx.compose.runtime.mutableStateOf



class LobbyViewModel : ViewModel() {


    //I got the buttons to change to "Challenged" when pressing it, but it made all buttons for all players change
    //So ChatGpt helped with how to change just the button pressed for the specific player
    //https://chat.openai.com/share/2ce8797c-b547-46bc-b86d-e74421594c74
    val challengedUser = mutableStateMapOf<String, MutableState<Boolean>>()

    fun createPlayer(name: String){
        viewModelScope.launch {
            val player = Player(name = name)
            SupabaseService.joinLobby(player)
        }
    }

    fun acceptInvite(game: Game) {
        viewModelScope.launch {
            SupabaseService.acceptInvite(game)
        }
    }

    fun declineInvite(game: Game) {
        viewModelScope.launch {
            SupabaseService.declineInvite(game)
        }
    }

    fun invite(opponent: Player) {
        viewModelScope.launch {
            SupabaseService.invite(opponent)
            challengedUser[opponent.id] = mutableStateOf(true)
        }
    }
}