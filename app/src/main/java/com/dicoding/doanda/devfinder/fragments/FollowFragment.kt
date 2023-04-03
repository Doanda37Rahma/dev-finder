package com.dicoding.doanda.devfinder.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.doanda.devfinder.R
import com.dicoding.doanda.devfinder.activities.UserDetailActivity
import com.dicoding.doanda.devfinder.databinding.FragmentFollowBinding
import com.dicoding.doanda.devfinder.network.ApiConfig
import com.dicoding.doanda.devfinder.network.ItemsItem
import com.dicoding.doanda.devfinder.models.UserModel
import com.dicoding.doanda.devfinder.adapters.UserModelAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragment : Fragment() {

    companion object {
        const val ARG_USERNAME = "username"
        const val ARG_POSITION = "position"
        const val TAG = "FollowFragment"
    }

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private var position: Int = 0
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME).toString()
        }

        val layoutManager = LinearLayoutManager(context)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        if (position == 1) {
            findFollowers(username)
        } else {
            findFollowing(username)
        }
    }

    private fun findFollowers(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserListData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onResponseIsUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun findFollowing(username: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserListData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onResponseIsUnsuccessful: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })    }

    private fun showLoading(isLoading: Boolean) {

        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setUserListData(items: List<ItemsItem?>?) {
        val listUsers = ArrayList<UserModel>()
        if (items != null) {
            for (item in items) {
                val avatar = item?.avatarUrl ?: getString(R.string.default_avatar_url)
                val username = item?.login ?: getString(R.string.default_username)
                val user = UserModel(avatar = avatar, username = username, null, null, null)
                listUsers.add(user)
            }
        }

        val adapter = UserModelAdapter(listUsers)
        binding.rvUsers.adapter = adapter
        adapter.setOnItemClickCallback(object : UserModelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                val intent = Intent(activity, UserDetailActivity::class.java)
                intent.putExtra(UserDetailActivity.EXTRA_USER, data)
                startActivity(intent)
            }
        })
    }

}