package com.example.cryptocurrency.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrency.R
import com.example.cryptocurrency.data.remote.dto.TeamMember

class TeamMemberAdapter: RecyclerView.Adapter<TeamMemberAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    private lateinit var personName: TextView
    private lateinit var personJob: TextView

    private val differCallBack = object : DiffUtil.ItemCallback<TeamMember>() {
        override fun areItemsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TeamMember, newItem: TeamMember): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.team_members, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val person = differ.currentList[position]

        personName = holder.itemView.findViewById(R.id.txvPersonName)
        personJob = holder.itemView.findViewById(R.id.txvPersonJob)

        holder.itemView.apply {
            personName.text = person.name
            personJob.text = person.position
        }
    }
}