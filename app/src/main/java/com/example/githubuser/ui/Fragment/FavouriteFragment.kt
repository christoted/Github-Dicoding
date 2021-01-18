package com.example.githubuser.ui.Fragment

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.dbbasic.DatabaseContract
import com.example.githubuser.dbbasic.UserHelper
import com.example.githubuser.helper.MappingHelper
import com.example.githubuser.model.GithubFollowerItem
import com.example.githubuser.model.GithubRepoItem
import com.example.githubuser.model.GithubUserItem
import com.example.githubuser.ui.FavouriteActivity
import com.example.githubuser.ui.Fragment.UserDetailFragment.Companion.TAG
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import com.example.githubuser.util.Resource
import kotlinx.android.synthetic.main.fragment_favourite.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FavouriteFragment : Fragment(), UserAdapter.UserItemListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var listUser: ArrayList<GithubUserItem> = ArrayList()

    private lateinit var userHelper: UserHelper

    lateinit var userAdapter : UserAdapter

    lateinit var viewModel: UserViewModel

    val githubUsers: ArrayList<GithubUserItem> = ArrayList()
    val listRepo = ArrayList<GithubRepoItem>()
    val listFollower = ArrayList<GithubFollowerItem>()
    val listFollowing = ArrayList<GithubFollowerItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as FavouriteActivity).viewModel

        userHelper = UserHelper.getInstance(requireContext())
        userHelper.open()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)


        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserSync()

            }
        }


        userAdapter = UserAdapter(requireActivity(), listUser, this@FavouriteFragment)
        recylerViewFavourite.adapter = userAdapter
        initRecyclerView()
        Log.d(TAG, "onCreate: bwah ${listUser.size}")
        activity?.contentResolver?.registerContentObserver(DatabaseContract.UserColumns.CONTENT_URI, true, myObserver)

        if ( savedInstanceState == null) {
            loadUserSync()
        } else {
            savedInstanceState.getParcelableArrayList<GithubUserItem>(EXTRA_STATE)?.also {
                listUser.addAll(it)
            }
        }
    }

    private fun initRecyclerView() {
        recylerViewFavourite.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun loadUserSync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                //  val cursor = userHelper.queryAll()
                val cursor = activity?.contentResolver?.query(DatabaseContract.UserColumns.CONTENT_URI, null, null, null, null)
                MappingHelper.convertCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            Log.d(EXTRA_STATE, "loadUserSync: ${users.size}")

            if ( users.size > 0) {
                listUser.addAll(users)
            } else {
                listUser = ArrayList()
            }

            userAdapter.notifyDataSetChanged()
        }
    }


    private fun getTotalRepo(login: String) {
        viewModel.getTotalRepo(login);
        viewModel.showRepository.observe(viewLifecycleOwner, androidx.lifecycle.Observer { responsesTotalRepository ->
            when(responsesTotalRepository) {
                is Resource.Success -> {
                    responsesTotalRepository.data?.let {totalRepository ->
                        listRepo.clear()
                        listRepo.addAll(totalRepository)
                    }
                }
            }
        })
    }

    private fun getTotalFollower(login: String) {
        viewModel.getFollowersTotals(login)
        viewModel.showFollowerTotal.observe(viewLifecycleOwner, androidx.lifecycle.Observer { responseTotalFollower ->
            when (responseTotalFollower) {
                is Resource.Success -> {
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
            androidx.lifecycle.Observer { ResourceResponsesTotalFollowing ->
                when (ResourceResponsesTotalFollowing) {
                    is Resource.Success -> {
                        ResourceResponsesTotalFollowing.data?.let { ResponsesTotalFollowing ->
                            listFollowing.clear()
                            listFollowing.addAll(ResponsesTotalFollowing)

                        }

                    }
                }
            })
        }


    companion object {

        private val TAG = FavouriteActivity::class.java.simpleName
        private const val EXTRA_STATE = "EXTRA_STATE"

    }


    override fun onUserItemClick(Position: Int) {
        val user = userAdapter.users[Position]

        Toast.makeText(activity, "Please wait for a second", Toast.LENGTH_SHORT).show()

        getTotalRepo(user.login ?: "christoted")
        Log.d("TAG", "onUserItemClick: ${listRepo.size}")
        getTotalFollower(user.login ?: "christoted")
        Log.d("TAG", "onUserItemClick: ${listFollower.size}")
        getTotalFollowing(user.login ?: "christoted")
        Log.d("TAG", "onUserItemClick: ${listFollowing.size}")

        val bundle = Bundle().apply {
            putParcelable("user", user)
            putParcelableArrayList("listRepo", listRepo)
            putParcelableArrayList("listFollowing", listFollowing)
            putParcelableArrayList("listFollower", listFollower)
        }

        Timer("").schedule(1000) {
            findNavController().navigate(
                R.id.action_favouriteFragment_to_userDetailFragmentFavourite,
                bundle
            )
        }
    }
}