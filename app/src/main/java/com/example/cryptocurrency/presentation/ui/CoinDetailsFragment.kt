package com.example.cryptocurrency.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.databinding.FragmentCoinDetailsBinding
import com.example.cryptocurrency.presentation.adapter.TeamMemberAdapter
import com.example.cryptocurrency.presentation.coin_details.CoinDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailsFragment : Fragment() {

    private var _binding: FragmentCoinDetailsBinding? = null
    private val binding get() = _binding!!
    private val coinDetailsViewModel: CoinDetailsViewModel by viewModels()
    private lateinit var teamMemberAdapter: TeamMemberAdapter
    private val args: CoinDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        teamMemberAdapter = TeamMemberAdapter()
        val coinId = args.coinId ?: "An unexpected error occur"

        coinDetailsViewModel.getCoinDetails(coinId)
        getCoinDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun getCoinDetails() {
        lifecycleScope.launchWhenStarted {
            coinDetailsViewModel.state.collect { state ->
                if(state.isLoading) {
                    showProgressBar()
                } else {
                    hideProgressBar()
                }

                state.coin?.let { coin ->
                    binding.txvName.text = coin.name
                    binding.txvCoinDescription.text = coin.description
                    binding.txvSymbol.text = "(${coin.symbol})"
                    binding.txvTag1.text = coin.tag.joinToString(", ") {it.name}

                    teamMemberAdapter.differ.submitList(coin.team)
                    recyclerViewForTeamMember()
                }

                if(state.error.isNotEmpty()) {
                    Snackbar.make(binding.root, state.error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun recyclerViewForTeamMember() {
        binding.rcvTeamMembers.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = teamMemberAdapter
        }
    }

    private fun showProgressBar() {
        binding.prgBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.prgBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}