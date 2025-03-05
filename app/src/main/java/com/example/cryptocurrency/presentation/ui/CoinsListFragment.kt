package com.example.cryptocurrency.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrency.R
import com.example.cryptocurrency.databinding.FragmentCoinsListBinding
import com.example.cryptocurrency.presentation.adapter.CoinAdapter
import com.example.cryptocurrency.presentation.coins_list.CoinsListViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinsListFragment : Fragment() {

    private var _binding: FragmentCoinsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var coinAdapter: CoinAdapter
    private val coinsListViewModel: CoinsListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinAdapter = CoinAdapter(requireContext())

        collectState()
        recyclerViewCoinsList()
        navigatedToCoinDetailsFragment()

    }

    private fun collectState() {
        lifecycleScope.launchWhenStarted {
            coinsListViewModel.state.collect { state ->
                if(state.isLoading) {
                    showProgressBar()
                } else {
                    hideProgressBar()
                }

                state.coins.let {
                    coinAdapter.differ.submitList(it)
                }

                if(state.error.isNotEmpty()) {
                    Snackbar.make(binding.root, state.error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigatedToCoinDetailsFragment() {
        coinAdapter.onClickListener {coinId ->
            val action = CoinsListFragmentDirections.actionCoinsListFragmentToCoinDetailsFragment(coinId)
            findNavController().navigate(action)
        }
    }

    private fun recyclerViewCoinsList() {
        binding.rcvCoinsList.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = coinAdapter
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