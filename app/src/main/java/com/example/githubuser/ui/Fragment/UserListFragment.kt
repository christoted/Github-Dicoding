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
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.model.GithubRepoItem
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.model.GithubUserResponse
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.android.synthetic.main.fragment_user_list.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class UserListFragment : Fragment(R.layout.fragment_user_list), UserAdapter.UserItemListener {
    lateinit var userAdapter: UserAdapter

    lateinit var viewModel: UserViewModel

    val githubUsers: ArrayList<GithubUserItem> = ArrayList()
    val listRepo = ArrayList<GithubRepoItem>()
    val listFollower = ArrayList<GithubFollowerItem>()
    val listFollowing = ArrayList<GithubFollowerItem>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel


        initRecyclerView()
        showProgressBar()
        viewModel.someUser.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { githubResponses ->
                        //  userAdapter.users.addAll(githubResponses.items)
                        githubUsers.addAll(githubResponses.items)
                        //      userAdapter.differ.submitList(userResponses)
                        userAdapter.notifyDataSetChanged()
                    }
                    hideProgressBar()
                }

                is Resource.Error -> {
                    response.data?.let {
                        Log.d("12345", "Error")
                    }
                }
            }
        })

        id_search_user_fragment_edt_search.setOnKeyListener(View.OnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                showProgressBar()
                searchUser()
                Log.d("ukuran", "onViewCreated: " + githubUsers.size)
                return@OnKeyListener true
            }
            false
        })

        githubUsers.clear()
    }

    private fun searchUser() {
        showProgressBar()
        if (id_search_user_fragment_edt_search.text?.isEmpty()!!) {
            Toast.makeText(activity, "isi oy", Toast.LENGTH_SHORT).show()
        } else {
            showProgressBar()
            val resultSearch: String = id_search_user_fragment_edt_search.text.toString()
            viewModel.getSearchUser(resultSearch)
            viewModel.searchUser.observe(viewLifecycleOwner, Observer { responseSearch ->
                showProgressBar()
                when (responseSearch) {
                    is Resource.Success -> {
                        responseSearch.data?.let { searchResponse ->
                            githubUsers.clear()
                            githubUsers.addAll(searchResponse.items)
                            userAdapter.notifyDataSetChanged()
                        }
                        hideProgressBar()
                    }

                    is Resource.Error -> {
                        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun initRecyclerView() {
        userAdapter = UserAdapter(requireActivity(), githubUsers, this)
        recyclerViewMain.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getTotalRepo(login: String) {
        viewModel.getTotalRepo(login);
        viewModel.showRepository.observe(viewLifecycleOwner, Observer { ResourceGithubResponses ->
            when (ResourceGithubResponses) {
                is Resource.Success -> {
                    ResourceGithubResponses.data?.let { GithubResponses ->
                        listRepo.clear()
                        listRepo.addAll(GithubResponses)
//                        id_repository_user_detail.text = listRepo.size.toString()
                        Log.d("sizeListRepo", "onUserItemClick: dalam ${listRepo.size}")
                    }

                }
            }
        })
    }

    private fun getTotalFollower(login: String) {
        viewModel.getFollowersTotals(login)
        viewModel.showFollowerTotal.observe(viewLifecycleOwner, Observer { responseTotalFollower ->
            when (responseTotalFollower) {
                is Resource.Success -> {
                    hideProgressBar()
                    responseTotalFollower.data?.let { totalFollowerResponse ->
                        listFollower.clear()
                        listFollower.addAll(totalFollowerResponse)
                    }
                }

            }
        })
    }

    private fun getTotalFollowing(login: String) {
        viewModel.getFollowingTotal(login)
        viewModel.showFollowingTotal.observe(
            viewLifecycleOwner,
            Observer { ResourceResponsesTotalFollowing ->
                when (ResourceResponsesTotalFollowing) {
                    is Resource.Success -> {
                        hideProgressBar()
                        ResourceResponsesTotalFollowing.data?.let { ResponsesTotalFollowing ->
                            listFollowing.clear()
                            listFollowing.addAll(ResponsesTotalFollowing)

                        }

                    }
                }
            })
    }

    override fun onUserItemClick(Position: Int) {
        val user = userAdapter.users[Position]
           Toast.makeText(activity, "Please wait for a second", Toast.LENGTH_SHORT).show()

        getTotalRepo(user.login ?: "christoted")
        getTotalFollower(user.login ?: "christoted")
        getTotalFollowing(user.login ?: "christoted")

        val bundle = Bundle().apply {
            putParcelable("user", user)
            putParcelableArrayList("listRepo", listRepo)
            putParcelableArrayList("listFollowing", listFollowing)
            putParcelableArrayList("listFollower", listFollower)
        }

        Timer("").schedule(1000) {
            Log.d("sizeListRepo", "onUserItemClick: luar ${listRepo.size}")
            Log.d("sizeListRepo", "onUserItemClick: Follower luar ${listFollower.size}")
            Log.d("sizeListRepo", "onUserItemClick: following luar ${listFollowing.size}")
            findNavController().navigate(
                R.id.action_userListFragment_to_userDetailFragment,
                bundle
            )
        }
    }
}