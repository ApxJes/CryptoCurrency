package com.example.cryptocurrency.presentation.coin_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
): ViewModel() {

    private var _state = MutableStateFlow(CoinDetailsState())
    val state: StateFlow<CoinDetailsState> = _state.asStateFlow()

    fun getCoinDetails(coinId: String) {
        getCoinUseCase(coinId).onEach { result ->
            val newState = when(result) {
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

            _state.value = newState
        }.launchIn(viewModelScope)
    }
}