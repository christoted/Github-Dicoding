package com.example.githubuser.ui.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentUserDetailFavouriteBinding
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.model.GithubRepoItem
import com.example.githubuser.model.GithubUserItem
import kotlinx.android.synthetic.main.fragment_user_detail.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserDetailFragmentFavourite.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserDetailFragmentFavourite : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var listRepoTest: ArrayList<GithubRepoItem>
    lateinit var listFollowingTest: ArrayList<GithubFollowerItem>
    lateinit var listFollowerTest: ArrayList<GithubFollowerItem>


    private lateinit var binding: FragmentUserDetailFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserDetailFavouriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listRepoTest =
            arguments?.getParcelableArrayList<GithubRepoItem>("listRepo") as ArrayList<GithubRepoItem>
        listFollowerTest =
            arguments?.getParcelableArrayList<GithubFollowerItem>("listFollower") as ArrayList<GithubFollowerItem>
        listFollowingTest =
            arguments?.getParcelableArrayList<GithubRepoItem>("listFollowing") as ArrayList<GithubFollowerItem>


        val user = arguments?.getParcelable<GithubUserItem>("user")
        binding.idLoginUserDetail.text = user?.login ?: "Christoted"

        Glide.with(requireActivity())
            .load(user?.avatar_url)
            .into(binding.idProfileImageUserDetail)

        binding.idUserTypeDetail.text = user?.type ?: "User"

        binding.idRepositoryUserDetail.text = listRepoTest.size.toString()
        binding.idFollowersUserDetail.text = listFollowerTest.size.toString()
        binding.idFollowingUserDetail.text = listFollowingTest.size.toString()

        setCurrentFragment(FollowerFragmentFavourite.newInstance(user?.login ?: "christoted"))

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.followerFragment -> {
                    setCurrentFragment(FollowerFragmentFavourite.newInstance(user?.login ?: "christoted"))
                }

                R.id.followingFragment -> {
                    setCurrentFragment(FollowingFragmentFavourite.newInstance(user?.login ?: "christoted"))
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) = childFragmentManager.beginTransaction().apply {
        replace(R.id.flFragmentFav, fragment)
        commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserDetailFragmentFavourite.
         */
        private val TAG = UserDetailFragment::class.java.simpleName
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserDetailFragmentFavourite().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}