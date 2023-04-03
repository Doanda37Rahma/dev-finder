package com.dicoding.doanda.devfinder

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.devfinder.api.ApiConfig
import com.dicoding.doanda.devfinder.api.GithubSearchResponse
import com.dicoding.doanda.devfinder.api.ItemsItem
import com.dicoding.doanda.devfinder.databinding.ActivityMainBinding
import com.dicoding.doanda.devfinder.user.User
import com.dicoding.doanda.devfinder.user.UserAdapter
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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                findUsers(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        return true
    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun findUsers(query: String?) {
        showLoading(true)
        val userQuery = query ?: getString(R.string.default_username)
        val client = ApiConfig.getApiService().searchUsers(userQuery)
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
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

}