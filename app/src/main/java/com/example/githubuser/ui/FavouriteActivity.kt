package com.example.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.database.UserDatabase
import com.example.githubuser.dbbasic.DatabaseContract
import com.example.githubuser.dbbasic.UserHelper
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.ui.ViewModel.UserViewModelFactory
import kotlinx.android.synthetic.main.activity_favourite.*
import kotlinx.coroutines.*

class FavouriteActivity : AppCompatActivity(), UserAdapter.UserItemListener{

    lateinit var viewModel: UserViewModel
    lateinit var userAdapter : UserAdapter

    var listUser: ArrayList<GithubUserItem> = ArrayList()

    private lateinit var userHelper: UserHelper
    
    companion object {
        val TAG = FavouriteActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        userHelper = UserHelper.getInstance(this)
        userHelper.open()

        val db = UserDatabase(this)
        val repository = UserRepository(db)
        val viewModelFactory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.convertCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            Log.d(TAG, "loadUserSync: ${users.size}")

            if ( users.size > 0) {
                listUser.addAll(users)
            } else {
                listUser = ArrayList()
                Toast.makeText(this@FavouriteActivity, "Empty", Toast.LENGTH_SHORT).show()
            }
            userAdapter = UserAdapter(applicationContext, listUser, this@FavouriteActivity)
            recylerViewFavourite.adapter = userAdapter
            initRecyclerView()
            userAdapter.notifyDataSetChanged()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        recylerViewFavourite.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
        }
    }



    private fun loadUserSync(){

        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.convertCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            Log.d(TAG, "loadUserSync: ${users.size}")

            if ( users.size > 0) {
                listUser.addAll(users)
            } else {
                listUser = ArrayList()
                Toast.makeText(this@FavouriteActivity, "Empty", Toast.LENGTH_SHORT).show()
            }
        }

        userAdapter = UserAdapter(applicationContext, listUser, itemListener = this@FavouriteActivity)
        initRecyclerView()
        Log.d(TAG, "onCreate: bwah ${listUser.size}")
        userAdapter.notifyDataSetChanged()

    }

    override fun onUserItemClick(Position: Int) {
        TODO("Not yet implemented")
    }

}
