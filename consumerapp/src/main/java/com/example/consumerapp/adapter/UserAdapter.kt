package com.example.consumerapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.consumerapp.R
import com.example.consumerapp.databinding.SingleRowUserBinding
import com.example.consumerapp.model.GithubUserItem
import com.squareup.picasso.Picasso

class UserAdapter (
    val context: Context,
    val listUser : ArrayList<GithubUserItem>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = SingleRowUserBinding.bind(itemView)

        fun bind(githubUserItem: GithubUserItem){
            binding.idTvLoginName.text = githubUserItem.login
            Glide.with(context)
                .load(githubUserItem.avatar_url)
                .into(binding.idProfileImageUserList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }
}