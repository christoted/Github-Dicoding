package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.database.UserDatabase
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.ui.ViewModel.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_favourite.*

class FavouriteActivity : AppCompatActivity(), UserAdapter.UserItemListener{

    lateinit var viewModel: UserViewModel
    lateinit var userAdapter : UserAdapter

    var listUser: ArrayList<GithubUserItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        val db = UserDatabase(this)
        val repository = UserRepository(db)
        val viewModelFactory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)



        viewModel.getAllFavourite().observe(this, Observer {
            listUser.addAll(it)
            userAdapter = UserAdapter(this, listUser, this)
            initRecyclerView()
            Log.d("size-user", "onCreate: atas ${listUser.size}")
            userAdapter.notifyDataSetChanged()
        })

        Log.d("size-user", "onCreate: bawah ${listUser.size}")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        recylerViewFavourite.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
        }
    }

    override fun onUserItemClick(Position: Int) {
        Log.d("test", "onUserItemClick: $Position")
    }
}