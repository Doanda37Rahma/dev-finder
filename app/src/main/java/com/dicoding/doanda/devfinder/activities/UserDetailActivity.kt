package com.dicoding.doanda.devfinder.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.devfinder.R
import com.dicoding.doanda.devfinder.adapters.SectionsPagerAdapter
import com.dicoding.doanda.devfinder.databinding.ActivityUserDetailBinding
import com.dicoding.doanda.devfinder.models.UserDetailViewModel
import com.dicoding.doanda.devfinder.models.UserDetailViewModelFactory
import com.dicoding.doanda.devfinder.models.UserModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class UserDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
        const val TAG = "UserDetailActivity"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following,
        )
    }

    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var userDetailViewModel: UserDetailViewModel
    private lateinit var userDetailViewModelFactory: UserDetailViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<UserModel>(EXTRA_USER, UserModel::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UserModel>(EXTRA_USER)
        }

        if (user != null) {
            val username = user.username ?: getString(R.string.default_username)

            userDetailViewModelFactory = UserDetailViewModelFactory(username)
            userDetailViewModel = ViewModelProvider(this, userDetailViewModelFactory)
                .get(UserDetailViewModel::class.java)
        }

        userDetailViewModel.userDetail.observe(this) { userDetail ->
            setUserDetailData(userDetail)
            setFollowList(userDetail.username)
        }

        userDetailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setUserDetailData(user: UserModel) {
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(50))
        Glide.with(this@UserDetailActivity)
            .load(user.avatar)
            .apply(requestOptions)
            .skipMemoryCache(true)
            .into(binding.imgUserDetailAvatar)
        binding.detailName.text = user.name
        binding.detailUsername.text = user.username
        binding.detailFollowers.text = getString(R.string.followers, user.followerCount)
        binding.detailFollowing.text = getString(R.string.following, user.followingCount)
    }

    private fun setFollowList(username: String?) {
        val userName = username.toString()
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = userName
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }


    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}