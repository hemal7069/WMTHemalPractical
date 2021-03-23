package com.wmt.hemal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textview.MaterialTextView
import com.wmt.hemal.R
import com.wmt.hemal.model.User

class UserAdapter(
    private val context: Context,
    private val users: List<User>
) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user: User = users[position]

        // Profile Pic
        Glide
            .with(context)
            .load(user.profilePic)
            .apply(RequestOptions())
            .dontAnimate()
            .skipMemoryCache(true)
            .placeholder(R.drawable.ic_user)
            .error(R.drawable.ic_user)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.NORMAL)
            .into(holder.ivUserProfile)

        // User Name
        holder.tvUserName.text = user.username

        // User email address
        holder.tvEmail.text = user.email
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivUserProfile: AppCompatImageView = itemView.findViewById(R.id.ivUserProfile)
        var tvUserName: MaterialTextView = itemView.findViewById(R.id.tvUserName)
        var tvEmail: MaterialTextView = itemView.findViewById(R.id.tvEmail)
    }
}