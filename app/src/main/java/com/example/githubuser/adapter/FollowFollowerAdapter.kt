package com.example.githubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.model.GithubFollowerItem
import kotlinx.android.synthetic.main.item_following.view.*

class FollowFollowerAdapter(
    val context : Context,
    val listFollowFollower: ArrayList<GithubFollowerItem>
) : RecyclerView.Adapter<FollowFollowerAdapter.FollowFollowerViewHolder>() {

    inner class FollowFollowerViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init {

        }

        fun bind(followerItem: GithubFollowerItem) {
            itemView.id_tv_item_following_name.text = followerItem.login
            Glide.with(context)
                .load(followerItem.avatar_url)
                .into(itemView.id_iv_item_following)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowFollowerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following,parent, false)

        return FollowFollowerViewHolder(view)
    }

    override fun getItemCount(): Int {

        return listFollowFollower.size
    }

    override fun onBindViewHolder(holder: FollowFollowerViewHolder, position: Int) {
        val followerItem = listFollowFollower[position]
        holder.bind(followerItem)
    }
}