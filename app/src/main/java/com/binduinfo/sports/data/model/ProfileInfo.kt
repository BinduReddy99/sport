package com.binduinfo.sports.data.model

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.binduinfo.sports.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView


data class ProfileInfo(
    val profile: Profile,
    val success: Int
)

@BindingAdapter("profilePic")
fun loadImage(imageView: CircleImageView, logo: String?) {
    Glide.with(imageView.context.applicationContext).load(logo)
        .apply(RequestOptions.circleCropTransform())
        .placeholder(ContextCompat.getDrawable(imageView.context, R.drawable.ic_use_white))
        .into(imageView)
}

