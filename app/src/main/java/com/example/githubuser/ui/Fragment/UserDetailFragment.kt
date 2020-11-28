package com.example.githubuser.ui.Fragment
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.ui.MainActivity
import com.example.githubuser.ui.ViewModel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_detail.*

class UserDetailFragment : Fragment(R.layout.fragment_user_detail) {
    lateinit var viewModel: UserViewModel

    val args : UserDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        val user = args.user

        id_login_user_detail.text = user.login
        id_user_type_detail.text = user.type
    }
}