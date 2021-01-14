package com.example.githubuser.ui

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.database.UserDatabase
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion.CONTENT_URI
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
        private val TAG = FavouriteActivity::class.java.simpleName
        private const val EXTRA_STATE = "EXTRA_STATE"

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

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserSync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if ( savedInstanceState == null) {
            loadUserSync()
        } else {
            savedInstanceState.getParcelableArrayList<GithubUserItem>(EXTRA_STATE)?.also {
                listUser.addAll(it)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() {
        recylerViewFavourite.apply {
            layoutManager = LinearLayoutManager(this@FavouriteActivity)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listUser)
    }

    private fun loadUserSync(){
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
              //  val cursor = userHelper.queryAll()
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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
            Log.d(TAG, "onCreate: bwah ${listUser.size}")
            userAdapter.notifyDataSetChanged()
        }
    }

    override fun onUserItemClick(Position: Int) {
        TODO("Not yet implemented")
    }

}
