package com.example.githubuser.ui.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.model.GithubRepoItem
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
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
    val listRepo = Vector<GithubRepoItem>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        viewModel = (activity as MainActivity).viewModel

        val user: GithubUserItem? = arguments?.getParcelable<GithubUserItem>("user")
        val user1 = args.user
        id_login_user_detail.text = user1.login
        id_user_type_detail.text = user1.type

        id_repository_user_detail.text = "Dummy"

        Glide.with(view)
            .load(user1.avatar_url)
            .into(id_profile_image_user_detail)

        getTotalFollower(user1.login!!)
        getTotalFollowing(user1.login!!)
        getTotalRepo(user1.login!!)


        listFollower.clear()
        listFollowing.clear()
        listRepo.clear()

        val followingFragment: FollowingFragment = FollowingFragment()
        val followerFragment: FollowerFragment = FollowerFragment()

        setCurrentFragment(followingFragment)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.followingFragment -> setCurrentFragment(followingFragment)
                R.id.followerFragment -> setCurrentFragment(followerFragment)
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) = fragmentManager?.beginTransaction()?.apply {
        replace(R.id.flFragment, fragment)
        commit()
    }

    private fun getTotalFollower(login: String) {
        viewModel.getFollowersTotals(login)
        viewModel.showFollowerTotal.observe(viewLifecycleOwner, Observer { responseTotalFollower ->
            when(responseTotalFollower) {
                is Resource.Success -> {
                    responseTotalFollower.data?.let {totalFollowerResponse ->
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
        viewModel.showFollowingTotal.observe(viewLifecycleOwner, Observer {ResourceResponsesTotalFollowing->
            when(ResourceResponsesTotalFollowing) {
                is Resource.Success -> {
                    ResourceResponsesTotalFollowing.data?.let { ResponsesTotalFollowing->
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
    
    private fun getTotalRepo(login: String){
        viewModel.getTotalRepo(login);
        viewModel.showRepository.observe(viewLifecycleOwner, Observer {ResourceGithubResponses->
             when(ResourceGithubResponses) {
                 is Resource.Success -> {
                     ResourceGithubResponses.data?.let {GithubResponses ->
                         listRepo.addAll(GithubResponses)
                         id_repository_user_detail.text = listRepo.size.toString()
                     }
                     listRepo.clear()
                 }
             }
        })
    }
}