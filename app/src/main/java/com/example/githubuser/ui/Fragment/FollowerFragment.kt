package com.example.githubuser.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.FollowFollowerAdapter
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_following.*

private const val ARG_PARAM1 = "param1"


class FollowerFragment : Fragment(R.layout.fragment_follower) {
    private var login: String? = null

    private lateinit var viewModel: UserViewModel

    private lateinit var followerAdapter: FollowFollowerAdapter
    var listFollower: ArrayList<GithubFollowerItem> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            login = it.getString(ARG_PARAM1)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        getTotalFollower(login!!)
        followerAdapter = FollowFollowerAdapter(requireActivity(), listFollower)
        setupRecyclerView()

        listFollower.clear()
    }

    private fun getTotalFollower(login: String) {
        showProgressBar()
        viewModel.getFollowersTotals(login)
        viewModel.showFollowerTotal.observe(
            viewLifecycleOwner,
            Observer { ResourceGithubFollowers ->
                when (ResourceGithubFollowers) {
                    is com.example.githubuser.util.Resource.Success -> {

                        ResourceGithubFollowers.data?.let { GithubFolowerRes ->
                            listFollower.clear()
                            listFollower.addAll(GithubFolowerRes)
                            followerAdapter.notifyDataSetChanged()
                        }
                        hideProgressBar()
                    }
                }
            })

    }

    private fun hideProgressBar() {
        progressBarFollowwer.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBarFollowwer.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        rv_follower.apply {
            adapter = followerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(login: String) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, login)

                }
            }
    }
}