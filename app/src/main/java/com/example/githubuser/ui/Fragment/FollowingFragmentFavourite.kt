package com.example.githubuser.ui.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.FollowFollowerAdapter
import com.example.githubuser.databinding.FragmentFollowingFavouriteBinding
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.ui.FavouriteActivity
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.coroutines.*


private const val ARG_PARAM1 = "param1"


class FollowingFragmentFavourite : Fragment() {

    private lateinit var binding: FragmentFollowingFavouriteBinding
    // TODO: Rename and change types of parameters
    private var loginName: String? = null

    lateinit var viewModel: UserViewModel

    lateinit var followingAdapter: FollowFollowerAdapter

    private var listFollowing = ArrayList<GithubFollowerItem>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginName = it.getString(ARG_PARAM1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as FavouriteActivity).viewModel
        listFollowing.clear()

        showProgressBar()
        GlobalScope.launch(Dispatchers.IO) {
            delay(500)
            withContext(Dispatchers.Main) {
                getTotalFollowing(loginName!!)
            }
        }

        followingAdapter = FollowFollowerAdapter(requireActivity(), listFollowing)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        rv_following.apply {
            adapter = followingAdapter
            layoutManager = LinearLayoutManager(activity)
        }

        followingAdapter.notifyDataSetChanged()
    }

    private fun getTotalFollowing(login: String) {
        showProgressBar()
        viewModel.getFollowersTotals(login)
        viewModel.showFollowingTotal.observe(viewLifecycleOwner, Observer { responseTotalFollower ->
            when (responseTotalFollower) {
                is Resource.Success -> {
                    responseTotalFollower.data?.let { totalFollowerResponse ->
                        listFollowing.clear()
                        listFollowing.addAll(totalFollowerResponse)
                        followingAdapter.notifyDataSetChanged()
                    }
                    hideProgressBar()
                }

            }
        })
    }

    private fun hideProgressBar() {
        binding.progressBarFollowingFavourite.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarFollowingFavourite.visibility = View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingFavouriteBinding.inflate(layoutInflater, container, false)
        listFollowing.clear()
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FollowingFragmentFavourite.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(loginName: String) =
            FollowingFragmentFavourite().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, loginName)
                }
            }
    }
}