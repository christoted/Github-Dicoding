package com.example.githubuser.ui.Fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.model.UserResponse
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_user_list.*

class UserListFragment : Fragment(R.layout.fragment_user_list), UserAdapter.UserItemListener{

    lateinit var users: UserResponse
    private lateinit var userAdapter: UserAdapter

    lateinit var viewModel: UserViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        users = UserResponse()

        initRecyclerView()

        viewModel.someUser.observe(viewLifecycleOwner, Observer {response ->
            when(response) {
                is Resource.Success -> {
                    response.data?.let { userResponses ->
                        userAdapter.users.addAll(userResponses)

                        Log.d("Test", "onCreate: ${userAdapter.users.size}")

                        //      userAdapter.differ.submitList(userResponses)
                        Log.d("Test", "${users.size}" )
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
    }

    private fun initRecyclerView() {
        userAdapter = UserAdapter(requireActivity(), users, this)
        recyclerViewMain.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onUserItemClick(Position: Int) {
        val user = users[Position]
        val bundle = Bundle().apply {
            putParcelable("user", user)
        }

        findNavController().navigate(
            R.id.action_userListFragment_to_userDetailFragment,
            bundle
        )
    }
}