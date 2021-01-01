package com.example.githubuser.ui.Fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.model.GithubUserResponse
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.android.synthetic.main.fragment_user_list.*

class UserListFragment : Fragment(R.layout.fragment_user_list), UserAdapter.UserItemListener{
    lateinit var userAdapter: UserAdapter

    lateinit var viewModel: UserViewModel

    val githubUsers: ArrayList<GithubUserItem> = ArrayList()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel


        initRecyclerView()

        viewModel.someUser.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { githubResponses ->
                      //  userAdapter.users.addAll(githubResponses.items)
                        githubUsers.addAll(githubResponses.items)
                        //      userAdapter.differ.submitList(userResponses)
                     
                        userAdapter.notifyDataSetChanged()
                    }
                }

                is Resource.Error -> {
                    response.data?.let {
                        Log.d("12345", "Error")
                    }
                }
            }
        })

        id_search_user_fragment_edt_search.setOnKeyListener(View.OnKeyListener{ view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                //Perform Code
                searchUser()
                Log.d("ukuran", "onViewCreated: " + githubUsers.size)
                return@OnKeyListener true
            }
            false
        })

        githubUsers.clear()
    }

    private fun searchUser() {
       // userAdapter.users.clear()
        githubUsers.clear()
        if (id_search_user_fragment_edt_search.text?.isEmpty()!!) {
            Toast.makeText(activity, "isi oy", Toast.LENGTH_SHORT).show()
        } else {
            val resultSearch: String = id_search_user_fragment_edt_search.text.toString()
            viewModel.getSearchUser(resultSearch)

            viewModel.searchUser.observe(viewLifecycleOwner, Observer {responseSearch->
                when(responseSearch) {
                    is Resource.Success -> {  
                        responseSearch.data?.let {searchResponse ->  
                         //   userAdapter.users.addAll(searchResponse)
                            if ( githubUsers.size == 0) {
                                githubUsers.addAll(searchResponse.items)
                                userAdapter.notifyDataSetChanged()
                            }

                        }

                    }
                    
                    is Resource.Error -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

    }

    private fun initRecyclerView() {
        userAdapter = UserAdapter(requireActivity(), githubUsers, this)
        recyclerViewMain.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onUserItemClick(Position: Int) {
        val user = userAdapter.users[Position]
        Toast.makeText(activity, "" + Position, Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putParcelable("user", user)
        }

        findNavController().navigate(
            R.id.action_userListFragment_to_userDetailFragment,
            bundle
        )
    }
}