package com.example.githubuser.ui.Fragment

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.dbbasic.DatabaseContract
import com.example.githubuser.dbbasic.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.example.githubuser.dbbasic.UserHelper
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.model.GithubRepoItem
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class UserDetailFragment : Fragment(R.layout.fragment_user_detail){
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

    private lateinit var userHelper: UserHelper

    private lateinit var uriWithId: Uri

    companion object {
        val TAG = UserDetailFragment::class.java.simpleName
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userHelper = UserHelper.getInstance(activity!!)
        userHelper.open()

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

        val user1 = args.user
        id_login_user_detail.text = user1.login
        id_user_type_detail.text = user1.type

        loginName = user1.login

        Glide.with(view)
            .load(user1.avatar_url)
            .into(id_profile_image_user_detail)

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user1.id)


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

        val values = ContentValues()
        values.put(DatabaseContract.UserColumns._ID, user1.id)
        values.put(DatabaseContract.UserColumns.LOGIN, user1.login)
        values.put(DatabaseContract.UserColumns.AVATAR_URL, user1.avatar_url)
        values.put(DatabaseContract.UserColumns.TYPE, user1.type)
        
        fab_delete.setOnClickListener{
            activity?.contentResolver?.delete(uriWithId, null, null )
      //      userHelper.deleteByLoginName(loginName.toString())
            Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()
            fab_add.show()
            fab_delete.hide()
        }

        fab_add.setOnClickListener {
            activity?.contentResolver?.insert(CONTENT_URI, values)
       //     userHelper.insert(values)
       //     Log.d(TAG, "onViewCreated: ${userHelper.insert(values)}")
            Snackbar.make(it, "Success Added", Snackbar.LENGTH_SHORT).show()
            fab_add.hide()
            fab_delete.show()
        }



    }

    private fun checkSavedUser() {

        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = withContext(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(uriWithId, null,null,null,null)
                MappingHelper.convertCursorToArrayList(cursor)
            }

            val users = deferredUser

            if ( users.size > 0) {
                listUser.addAll(users)
            } else {
                Toast.makeText(activity, "Empty", Toast.LENGTH_SHORT).show()
            }

            for ( i in listUser) {
                if (loginName == i.login) {
                    Log.d(
                        TAG,
                        "checkSavedUser BAWAH: SAVED $loginName + ${i.login.toString()}"
                    )
                    fab_add.hide()
                    fab_delete.show()
                    break
                } else {
                    fab_delete.hide()
                    fab_add.show()
                }
            }
        }

//        viewModel.getAllFavourite().observe(viewLifecycleOwner, Observer {
//            listUser.addAll(it)
//
//            for (i in listUser) {
//                if (loginName == i.login) {
//                    Log.d(
//                        "TEST123",
//                        "checkSavedUser BAWAH: SAVED $loginName + ${i.login.toString()}"
//                    )
//                    fab.hide()
//                    fab_delete.show()
//                    break
//                } else {
//                    fab_delete.hide()
//                    fab.show()
//                }
//            }
//        })



    }

    private fun setCurrentFragment(fragment: Fragment) =
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            args.user.login
            commit()
        }

}