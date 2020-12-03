package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.ui.ViewModel.UserViewModelFactory
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = UserRepository()
        val viewModelFactory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)



    }


}