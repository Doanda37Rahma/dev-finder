package com.dicoding.doanda.devfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.devfinder.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

//        val listUsersDemo = listOf<User>(
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
//        )
//        val adapter = UserAdapter(listUsersDemo)
//        binding.rvUsers.adapter = adapter

        findUsers()
    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun findUsers() {
        showLoading(true)
//        TODO("Implement Search Feature")
        val client = ApiConfig.getApiService().searchUsers("doanda")
        client.enqueue(object: Callback<GithubSearchResponse>{
            override fun onResponse(
                call: Call<GithubSearchResponse>,
                response: Response<GithubSearchResponse>
            ) {
                showLoading(false)
                if(response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserListData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onResponseIsUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubSearchResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    private fun setUserListData(items: List<ItemsItem?>?) {
        val listUsers = ArrayList<User>()
        if (items != null) {
            for (item in items) {
                val avatar = item?.avatarUrl ?: getString(R.string.default_avatar_url)
                val name = item?.login ?: getString(R.string.default_username)
                val user = User(avatar, name)
                listUsers.add(user)
            }
        }

        val adapter = UserAdapter(listUsers)
        binding.rvUsers.adapter = adapter
    }

}