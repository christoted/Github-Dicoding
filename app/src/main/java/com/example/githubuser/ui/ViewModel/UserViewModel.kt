package com.example.githubuser.ui.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.model.UserResponse
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class UserViewModel(
    val repository: UserRepository
) : ViewModel() {
    val someUser: MutableLiveData<Resource<UserResponse>> = MutableLiveData()

    init {
        getSomeUser()
    }


    fun getSomeUser() = viewModelScope.launch {
        val response = repository.getSomeUser()
        someUser.postValue(checkResponse(response))
    }

    private fun checkResponse(response: Response<UserResponse>) : Resource<UserResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }


}