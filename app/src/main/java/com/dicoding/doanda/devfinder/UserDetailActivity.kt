package com.dicoding.doanda.devfinder

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dicoding.doanda.devfinder.databinding.ActivityUserDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

            findUserDetail(user.name)
            setFollowList(user.name)


//
//            var requestOptions = RequestOptions()
//            requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(50))
//            Glide.with(this@UserDetailActivity)
//                .load(user.avatar)
//                .apply(requestOptions)
//                .skipMemoryCache(true)
//                .into(binding.imgUserDetailAvatar)
//            binding.detailUsername.text = user.name


        }
    }

    private fun setFollowList(name: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = name
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun findUserDetail(name: String) {
        showLoading(true)
        val username = name
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        var requestOptions = RequestOptions()
                        requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(50))
                        Glide.with(this@UserDetailActivity)
                            .load(responseBody.avatarUrl)
                            .apply(requestOptions)
                            .skipMemoryCache(true)
                            .into(binding.imgUserDetailAvatar)
                        binding.detailName.text = responseBody.name
                        binding.detailUsername.text = responseBody.login
                        binding.detailFollowers.text = getString(R.string.followers, responseBody.followers.toString())
                        binding.detailFollowing.text = getString(R.string.following, responseBody.following.toString())
                    }
                } else {
                    Log.e(TAG, "onResponseIsUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailuer: ${t.message}")
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}