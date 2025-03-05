package com.example.cryptocurrency.presentation.coin_details

import com.example.cryptocurrency.domain.model.CoinDetails

data class CoinDetailsState(
    var isLoading: Boolean = false,
    var coin: CoinDetails? = null,
    var error: String = ""
)
