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
import com.example.githubuser.databinding.FragmentFollowerFavouriteBinding
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.ui.FavouriteActivity
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FollowerFragmentFavourite : Fragment() {
    // TODO: Rename and change types of parameters
    private var loginName: String? = null

    private lateinit var binding: FragmentFollowerFavouriteBinding

    private lateinit var viewModel: UserViewModel

    private lateinit var followerAdapter: FollowFollowerAdapter
    var listFollower: ArrayList<GithubFollowerItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            loginName = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFollowerFavouriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FavouriteActivity).viewModel
        showProgressBar()
        GlobalScope.launch(Dispatchers.IO) {
            delay(500)
            withContext(Dispatchers.Main) {
                getTotalFollower(loginName ?: "christoted")
            }
        }


        followerAdapter = FollowFollowerAdapter(requireActivity(), listFollower)
        setupRecyclerView()

        listFollower.clear()

    }

    private fun setupRecyclerView() {
        rv_follower.apply {
            adapter = followerAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun getTotalFollower(loginName: String) {
        showProgressBar()
        viewModel.getFollowersTotals(loginName)
        viewModel.showFollowerTotal.observe(
            viewLifecycleOwner,
            Observer { ResourceGithubFollowers ->
                when (ResourceGithubFollowers) {
                    is Resource.Success -> {

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
        binding.progressBarFollowwer.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarFollowwer.visibility = View.VISIBLE
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(loginName: String) =
            FollowerFragmentFavourite().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, loginName)
                }
            }
    }
}