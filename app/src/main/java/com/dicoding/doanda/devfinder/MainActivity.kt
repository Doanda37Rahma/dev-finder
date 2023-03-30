package com.dicoding.doanda.devfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.devfinder.databinding.ActivityMainBinding

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

        val listUsersDemo = listOf<User>(
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
            User("https://avatars.githubusercontent.com/u/69817149?v=4", "doanda"),
        )

        val adapter = UserAdapter(listUsersDemo)

        binding.rvUsers.adapter = adapter
    }
}