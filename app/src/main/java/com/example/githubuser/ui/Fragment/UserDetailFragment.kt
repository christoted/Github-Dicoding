package com.example.githubuser.ui.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.model.GithubRepoItem
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.android.synthetic.main.fragment_user_detail.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {
    lateinit var viewModel: UserViewModel
    var totalCount: Int = 0
    var totalCountFollowing: Int = 0
    private val args: UserDetailFragmentArgs by navArgs()
    val listFollower = ArrayList<GithubFollowerItem>()
    val listFollowing = ArrayList<GithubFollowerItem>()
    val listRepo = ArrayList<GithubRepoItem>()

    lateinit var listRepoTest: ArrayList<GithubRepoItem>
    lateinit var listFollowingTest: ArrayList<GithubFollowerItem>
    lateinit var listFollowerTest: ArrayList<GithubFollowerItem>

    var listUser: ArrayList<GithubUserItem> = ArrayList()

    var loginName: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        listRepoTest = arguments?.getParcelableArrayList<GithubRepoItem>("listRepo") ?: listRepo
        Log.d("listRepoTest", "onViewCreated: ${listRepoTest.size}")
        id_repository_user_detail.text = listRepoTest.size.toString()

        listFollowingTest =
            arguments?.getParcelableArrayList<GithubFollowerItem>("listFollowing") ?: listFollowing
        Log.d("listFollowingTest", "onViewCreated: ${listFollowingTest.size}")
        id_following_user_detail.text = listFollowingTest.size.toString()

        listFollowerTest =
            arguments?.getParcelableArrayList<GithubFollowerItem>("listFollower") ?: listFollower
        Log.d("listFollowerTest", "onViewCreated: ${listFollowerTest.size}")
        id_followers_user_detail.text = listFollowerTest.size.toString()

//      val user: GithubUserItem? = arguments?.getParcelable<GithubUserItem>("user")
        val user1 = args.user
        id_login_user_detail.text = user1.login
        id_user_type_detail.text = user1.type

        loginName = user1.login

        Glide.with(view)
            .load(user1.avatar_url)
            .into(id_profile_image_user_detail)

//        getTotalFollower(user1.login!!)
//        getTotalFollowing(user1.login)
//        getTotalRepo(user1.login)

        setCurrentFragment(FollowingFragment2.newInstance(args.user.login.toString()))
        Log.d("jumlah-data", "onViewCreated jumlah data : $listFollowing")
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.followingFragment -> {
                    setCurrentFragment(FollowingFragment2.newInstance(args.user.login.toString()))
                    Log.d("test-send-data", "onViewCreated: " + args.user.login.toString())
//                    val bundle = Bundle().apply {
//                        putString("login", args.user.login)
//                    }
                }
                R.id.followerFragment -> setCurrentFragment(FollowerFragment.newInstance(args.user.login.toString()))
            }
            true
        }

        listFollower.clear()
        listFollowing.clear()
        listRepo.clear()

        checkSavedUser()
        fab.setOnClickListener {
            viewModel.savedToFavourite(user1)
            Snackbar.make(it, "Saved", Snackbar.LENGTH_SHORT).show()
            fab.hide()
            fab_delete.show()
        }
        fab_delete.setOnClickListener {
            viewModel.deleteFavourite(user1)
            Snackbar.make(it, "Deleted", Snackbar.LENGTH_SHORT).show()
            fab.show()
            fab_delete.hide()


        }

    }

    private fun checkSavedUser() {

        viewModel.getAllFavourite().observe(viewLifecycleOwner, Observer {
            listUser.addAll(it)

            for (i in listUser) {
                if (loginName == i.login) {
                    Log.d(
                        "TEST123",
                        "checkSavedUser BAWAH: SAVED $loginName + ${i.login.toString()}"
                    )
                    fab.hide()
                    fab_delete.show()
                    break
                } else {
                    fab_delete.hide()
                    fab.show()
                }
            }
        })



    }

    private fun hideProgressBar() {

    }

    private fun showProgressBar() {

    }

    private fun setCurrentFragment(fragment: Fragment) =
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            args.user.login
            commit()
        }

    private fun getTotalFollower(login: String) {
        viewModel.getFollowersTotals(login)
        viewModel.showFollowerTotal.observe(viewLifecycleOwner, Observer { responseTotalFollower ->
            when (responseTotalFollower) {
                is Resource.Success -> {
                    hideProgressBar()
                    responseTotalFollower.data?.let { totalFollowerResponse ->
                        listFollower.addAll(totalFollowerResponse)
                        totalCount = listFollower.size
                        Log.d("Count", "getTotalFollower: " + totalCount)
                    }
                    id_followers_user_detail.text = totalCount.toString()
                    listFollower.clear()
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
                            listFollowing.addAll(ResponsesTotalFollowing)
                            totalCountFollowing = listFollowing.size
                            Log.d("Count", "Following : " + totalCountFollowing)
                        }
                        id_following_user_detail.text = totalCountFollowing.toString()
                        listFollowing.clear()
                    }
                }
            })
    }

    private fun getTotalRepo(login: String) {
        viewModel.getTotalRepo(login);
        viewModel.showRepository.observe(viewLifecycleOwner, Observer { ResourceGithubResponses ->
            when (ResourceGithubResponses) {
                is Resource.Success -> {
                    hideProgressBar()
                    ResourceGithubResponses.data?.let { GithubResponses ->
                        listRepo.addAll(GithubResponses)
                        id_repository_user_detail.text = listRepo.size.toString()
                    }
                    listRepo.clear()
                }
            }
        })
    }
}