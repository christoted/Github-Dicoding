package com.example.githubuser.ui.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.FollowFollowerAdapter
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_following.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FollowingFragment2 : Fragment() {

    private var login: String? = null
    lateinit var viewModel: UserViewModel

    lateinit var followingAdapter: FollowFollowerAdapter

    private var listFollowing = ArrayList<GithubFollowerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            login = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_following, container, false)
        listFollowing.clear()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("test-login", "onCreateView: $login")

        viewModel = (activity as MainActivity).viewModel
        listFollowing.clear()
        getTotalFollowing(login!!)
        followingAdapter = FollowFollowerAdapter(requireActivity(), listFollowing)
        setupRecyclerView()


        Log.d("jumlah-data", "onViewCreated: " + listFollowing.size)

    }

    private fun getTotalFollowing(login: String) {
        listFollowing.clear()
        viewModel.getFollowersTotals(login)
        viewModel.showFollowingTotal.observe(viewLifecycleOwner, Observer { responseTotalFollower ->
            when (responseTotalFollower) {
                is Resource.Success -> {
                    responseTotalFollower.data?.let { totalFollowerResponse ->
                        listFollowing.addAll(totalFollowerResponse)
                        Log.d("jumlah-data", "getTotalFollowing: ${listFollowing.size}")
                        followingAdapter.notifyDataSetChanged()
                    }

                }

            }
        })

    }

    private fun setupRecyclerView() {
        rv_following.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        followingAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(login: String) =
            FollowingFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, login)
                }
            }
    }
}