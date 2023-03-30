package com.dicoding.doanda.devfinder

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.devfinder.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {

    companion object {
        val EXTRA_USER = "extra_user"

    }

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<User>(EXTRA_USER, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<User>(EXTRA_USER)
        }

        if (user != null) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(50))
            Glide.with(this@UserDetailActivity)
                .load(user.avatar)
                .apply(requestOptions)
                .skipMemoryCache(true)
                .into(binding.imgUserDetailAvatar)
            binding.detailUsername.text = user.name
        }

    }

}