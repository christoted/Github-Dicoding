package com.example.consumerapp

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.consumerapp.adapter.UserAdapter
import com.example.consumerapp.databinding.ActivityMainBinding
import com.example.consumerapp.dbbasic.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.consumerapp.helper.MappingHelper
import com.example.consumerapp.model.GithubUserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
     var listUser : ArrayList<GithubUserItem> = ArrayList()

    private lateinit var binding: ActivityMainBinding

    companion object {
        private val EXTRA_STATE = "extra_state"
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadNotesAsync()
                Log.d(TAG, "onChange: ")
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)


        if (savedInstanceState == null) {
            loadNotesAsync()
        } else {
            savedInstanceState.getParcelableArrayList<GithubUserItem>(EXTRA_STATE)?.also { listUser.addAll(it) }
        }

    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.convertCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            Log.d(TAG, "loadUserSync: ${users.size}")
            Log.d(TAG, "loadUserSync: ${users[0].avatar_url}")
            listUser.clear()
            if ( users.size > 0) {
                listUser.addAll(users)
            } else {
                listUser = ArrayList()
                Toast.makeText(this@MainActivity, "Empty", Toast.LENGTH_SHORT).show()
            }

            userAdapter = UserAdapter(this@MainActivity, listUser)
            binding.recyclerViewMain.adapter = userAdapter
            initRecyclerView()
            Log.d(TAG, "onCreate: bwah ${listUser.size}")
            userAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewMain.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, listUser)
    }
}