package com.example.cryptocurrency.presentation.coins_list

import com.example.cryptocurrency.domain.model.Coin

data class CoinsListState(
    var isLoading: Boolean = false,
    var coins: List<Coin> = emptyList(),
    var error: String = ""
)
