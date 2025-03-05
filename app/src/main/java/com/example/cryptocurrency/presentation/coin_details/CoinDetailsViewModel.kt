package com.example.cryptocurrency.presentation.coin_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.Constants.PARAMETER_COIN_ID
import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.use_case.get_coin.GetCoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val getCoinUseCase: GetCoinUseCase,
    saveStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(CoinDetailsState())
    val state: StateFlow<CoinDetailsState> = _state.asStateFlow()

    init {
        saveStateHandle.get<String>(PARAMETER_COIN_ID)?.let { coinId ->
            getCoinDetails(coinId)
        }
    }

    private fun getCoinDetails(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
            when(result) {
                is Resource.Success -> CoinDetailsState(
                    coin = result.data
                )

                is Resource.Error -> CoinDetailsState(
                    error = result.message ?: "An unexpected error occur"
                )

                is Resource.Loading -> CoinDetailsState(
                    isLoading = true
                )
            }
        }.launchIn(viewModelScope)
    }
}