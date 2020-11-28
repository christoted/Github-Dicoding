package com.example.githubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.model.UserItem
import com.example.githubuser.model.UserResponse
import kotlinx.android.synthetic.main.single_row_user.view.*

class UserAdapter(
    val context: Context,
    val users: UserResponse,
    val itemListener: UserItemListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View, itemListener: UserItemListener) : RecyclerView.ViewHolder(view){

        init {
            itemView.setOnClickListener{
                itemListener.onUserItemClick(adapterPosition)
            }
        }

        fun bind(user : UserItem) {
            itemView.id_tv_login_name.text = user.login
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

    private val differCallBack = object : DiffUtil.ItemCallback<UserItem>() {
        //Check apakah item nya sama, berhubung kita pake online get API maka cek image url nya sabi
        override fun areItemsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: UserItem, newItem: UserItem): Boolean {
            return oldItem == newItem
        }

    }

  //  val differ =  AsyncListDiffer(this, differCallBack)
  interface UserItemListener{
      fun onUserItemClick(Position: Int)
  }
}

