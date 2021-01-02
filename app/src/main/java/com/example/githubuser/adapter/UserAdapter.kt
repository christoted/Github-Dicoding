package com.example.githubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.model.GithubUserResponse
import kotlinx.android.synthetic.main.single_row_user.view.*

class UserAdapter(
    val context: Context,
    val users: ArrayList<GithubUserItem>,
    val itemListener: UserItemListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View, itemListener: UserItemListener) : RecyclerView.ViewHolder(view){

        init {
//            itemView.buttonTest.setOnClickListener{
//                itemListener.onUserItemClick(adapterPosition)
//            }
            itemView.setOnClickListener{
                itemListener.onUserItemClick(adapterPosition)
            }
        }

        fun bind(user : GithubUserItem) {
            itemView.id_tv_login_name.text = user.login
            Glide.with(context)
                .load(user.avatar_url)
                .into(itemView.id_profile_image_user_list)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_row_user, parent, false)
        return UserViewHolder(view, itemListener)
    }

    override fun getItemCount(): Int {
        return users.size
    //    return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
       val position = users[position]
   //     val position = differ.currentList[position]
        holder.bind(position)
    }

    private val differCallBack = object : DiffUtil.ItemCallback<GithubUserItem>() {
        //Check apakah item nya sama, berhubung kita pake online get API maka cek image url nya sabi
        override fun areItemsTheSame(oldItem: GithubUserItem, newItem: GithubUserItem): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: GithubUserItem, newItem: GithubUserItem): Boolean {
            return oldItem == newItem
        }

    }

  //  val differ =  AsyncListDiffer(this, differCallBack)
  interface UserItemListener{
      fun onUserItemClick(Position: Int)
  }
}

