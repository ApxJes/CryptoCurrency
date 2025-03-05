package com.example.cryptocurrency.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency.R
import com.example.cryptocurrency.domain.model.Coin

class CoinAdapter(
    private val context: Context
) : RecyclerView.Adapter<CoinAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val coinName: TextView = itemView.findViewById(R.id.txvCoinName)
        val coinSymbol: TextView = itemView.findViewById(R.id.txvCoinSymbol)
        val isActiveCoin: TextView = itemView.findViewById(R.id.txvIsActive)
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.coin_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val coin = differ.currentList[position]

        holder.apply {
            coinName.text = coin.name
            coinSymbol.text = "(${coin.symbol})"
            isActiveCoin.text = if (coin.isActive) "Active" else "Inactive"

            if (coin.isActive) {
                isActiveCoin.setTextColor(ContextCompat.getColor(context, R.color.green ))
            } else {
                isActiveCoin.setTextColor(ContextCompat.getColor(context, R.color.red))
            }
        }

        holder.itemView.setOnClickListener {
            onClick?.let { it(coin.id) }
        }
    }

    private var onClick: ((String) -> Unit)? = null
    fun onClickListener(listener: (String) -> Unit) {
        onClick = listener
    }
}