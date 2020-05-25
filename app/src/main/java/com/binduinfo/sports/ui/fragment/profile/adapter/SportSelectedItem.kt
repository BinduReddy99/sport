package com.binduinfo.sports.ui.fragment.profile.adapter

import com.binduinfo.sports.R
import com.binduinfo.sports.data.model.Sport
import com.binduinfo.sports.databinding.ProfileSportSelectedBinding
import com.xwray.groupie.databinding.BindableItem

class SportSelectedItem(val sport: Sport): BindableItem<ProfileSportSelectedBinding> (){
    override fun getLayout(): Int  = R.layout.profile_sport_selected

    override fun bind(viewBinding: ProfileSportSelectedBinding, position: Int) {
        viewBinding.sportSetView = sport
    }
}