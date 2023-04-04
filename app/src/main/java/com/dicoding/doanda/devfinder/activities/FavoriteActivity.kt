package com.dicoding.doanda.devfinder.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.devfinder.R
import com.dicoding.doanda.devfinder.adapters.UserModelAdapter
import com.dicoding.doanda.devfinder.database.FavoriteUser
import com.dicoding.doanda.devfinder.databinding.ActivityFavoriteBinding
import com.dicoding.doanda.devfinder.models.FavoriteViewModel
import com.dicoding.doanda.devfinder.helper.FavoriteViewModelFactory
import com.dicoding.doanda.devfinder.models.UserDetail

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorites)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteViewModelFactory = FavoriteViewModelFactory(application)
        favoriteViewModel = ViewModelProvider(this, favoriteViewModelFactory)
            .get(FavoriteViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        favoriteViewModel.getAllUsers().observe(this) { userList ->
            if (userList != null) {
                setUserListData(userList)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUserListData(favoriteUsers: List<FavoriteUser>) {
        val listUsers = ArrayList<UserDetail>()
        for (favoriteUser in favoriteUsers) {
            val avatar = favoriteUser.avatar
            val username = favoriteUser.username
            val user = UserDetail(avatar = avatar, username = username, null, null, null)
            listUsers.add(user)
        }

        val adapter = UserModelAdapter(listUsers)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UserModelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserDetail) {
                val intent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }
}