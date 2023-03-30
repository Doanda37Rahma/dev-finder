package com.dicoding.doanda.devfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class UserAdapter(private val listUser: List<User>) :RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUserName: TextView = view.findViewById(R.id.tv_user_name)
        val imgUserAvatar: ImageView = view.findViewById(R.id.img_user_avatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (avatar, name) = listUser[position]
//        holder.imgUserAvatar.setImageResource(avatar)
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transform(FitCenter(), RoundedCorners(16))
        Glide.with(holder.itemView.context)
            .load(avatar)
            .apply(requestOptions)
            .skipMemoryCache(true)
            .into(holder.imgUserAvatar)
        holder.tvUserName.text = name

        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
            }
        }
    }
}