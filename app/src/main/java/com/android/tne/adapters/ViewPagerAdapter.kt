package com.android.tne.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.tne.R
import com.android.tne.adapters.ViewPagerAdapter.PagerViewHolder
import com.android.tne.ui.activity.MainActivity
import com.android.tne.ui.fragment.BoardFragment
import com.android.tne.ui.fragment.HomeFragment

class ViewPagerAdapter(var datalist : ArrayList<Int>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.image_list_item, parent, false)) {
        val imageView2 = itemView.findViewById<ImageView>(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.imageView2.setImageResource(datalist[position])
    }

    override fun getItemCount(): Int = datalist.size
}