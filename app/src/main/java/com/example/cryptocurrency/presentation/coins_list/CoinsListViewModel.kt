package com.example.cryptocurrency.presentation.coins_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrency.common.Resource
import com.example.cryptocurrency.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinsListViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
): ViewModel() {

    private val _state: MutableStateFlow<CoinsListState> = MutableStateFlow(CoinsListState())
    val state: StateFlow<CoinsListState> = _state.asStateFlow()

    init {
        getCoins()
    }

    private fun getCoins() {
        getCoinsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = CoinsListState(
                        coins = result.data ?: emptyList()
                    )
                }

                is Resource.Error -> {
                    _state.value = CoinsListState(
                        error = result.message ?: "An unexpected error occur"
                    )
                }

                is Resource.Loading -> {
                    _state.value = CoinsListState(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}