package com.example.cryptocurrency.domain.model

import com.example.cryptocurrency.data.remote.dto.Tag
import com.example.cryptocurrency.data.remote.dto.TeamMember

data class CoinDetails(
    val coinId: String,
    val name: String,
    val description: String,
    val symbol: String,
    val rank: Int,
    val isActive: Boolean,
    val tag: List<Tag>,
    val team: List<TeamMember>
)
