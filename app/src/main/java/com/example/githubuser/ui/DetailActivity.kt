package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuser.R
import com.example.githubuser.model.UserItem
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object{
        val USER = "user_passed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val user: UserItem = intent.getParcelableExtra<UserItem>(USER) as UserItem
        id_login_user_detail.text = user.login
        id_user_type_detail.text = user.type

    }
}