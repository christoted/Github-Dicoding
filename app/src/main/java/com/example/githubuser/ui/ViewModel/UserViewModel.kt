package com.example.githubuser.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.GithubFollowerResponse
import com.example.githubuser.model.GithubUserResponse
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(
    val repository: UserRepository
) : ViewModel() {
    val someUser: MutableLiveData<Resource<GithubUserResponse>> = MutableLiveData()

    val searchUser: MutableLiveData<Resource<GithubUserResponse>> = MutableLiveData()

    val showFollowerTotal: MutableLiveData<Resource<GithubFollowerResponse>> = MutableLiveData()

    val showFollowingTotal: MutableLiveData<Resource<GithubFollowerResponse>> = MutableLiveData()

    init {
        getSomeUser()
    }

    fun getSearchUser(q: String) = viewModelScope.launch {
        val responseSearch = repository.searchUser(q)
        searchUser.postValue(checkResponse(responseSearch))
    }


    fun getSomeUser() = viewModelScope.launch {
        val response = repository.getSomeUser()
        someUser.postValue(checkResponse(response))

    }

    fun getFollowersTotals(login: String) = viewModelScope.launch {
           val responsesFollower = repository.getFollower(login) as Response<GithubFollowerResponse>
           showFollowerTotal.postValue(checkResponseFollower(responsesFollower))
    }

    fun getFollowingTotal(login: String) = viewModelScope.launch {
        val responsesFollower = repository.getFollowing(login) as Response<GithubFollowerResponse>
        showFollowingTotal.postValue(checkResponseFollower(responsesFollower))
    }


    private fun checkResponseFollower(response: Response<GithubFollowerResponse>) : Resource<GithubFollowerResponse> {
        if (response.isSuccessful) {
            response.body()?.let{
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun checkResponse(response: Response<GithubUserResponse>) : Resource<GithubUserResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }


}