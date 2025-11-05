package com.gamedns.ui.screens.games

import androidx.lifecycle.ViewModel
import com.gamedns.data.model.GameProfile
import com.gamedns.data.model.PredefinedDnsServers
import com.gamedns.data.model.PredefinedGames
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(GamesUiState())
    val uiState: StateFlow<GamesUiState> = _uiState.asStateFlow()
    
    init {
        loadGames()
    }
    
    private fun loadGames() {
        _uiState.value = _uiState.value.copy(
            games = PredefinedGames.allGames
        )
    }
    
    fun getRecommendedDnsNames(game: GameProfile): List<String> {
        return game.recommendedDns.mapNotNull { dnsId ->
            PredefinedDnsServers.getById(dnsId)?.name
        }
    }
}

data class GamesUiState(
    val games: List<GameProfile> = emptyList()
)
