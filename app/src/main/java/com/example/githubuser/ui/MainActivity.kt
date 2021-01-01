package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.database.UserDatabase
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.ui.ViewModel.UserViewModelFactory
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user_detail.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = UserDatabase(this)
        val repository = UserRepository(db)
        val viewModelFactory = UserViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_item, menu)

//        val search : MenuItem = menu.findItem(R.id.search)
//        val searchView = search.actionView as SearchView
//
//        searchView.queryHint = "Search"
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                Log.d("12345", "onQueryTextSubmit: $p0")
//
//                return true
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//               return true
//            }
//
//        })


        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.favourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }

        return true
    }

//    private fun setCurrentFragment(fragment: Fragment) =
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.navHostFragment, fragment)
//            commit()
//        }

}