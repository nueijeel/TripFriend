package com.test.tripfriend.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentHomeListBinding
import com.test.tripfriend.databinding.RowHomeListBinding

class HomeListFragment : Fragment() {
    lateinit var fragmentHomeListBinding: FragmentHomeListBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeListBinding = FragmentHomeListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        fragmentHomeListBinding.run {
            recyclerViewHomeList.run {
                adapter = HomeListAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentHomeListBinding.root
    }

    inner class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {
        inner class HomeListViewHolder(rowHomeListBinding: RowHomeListBinding) : RecyclerView.ViewHolder(rowHomeListBinding.root) {
            val textViewHomeListTitle: TextView
            val textViewHomeListDate: TextView
            val textViewHomeListHeadCount: TextView
            val textViewHomeListCity: TextView
            val textViewHomeListLikedCount: TextView

            init {
                textViewHomeListTitle = rowHomeListBinding.textViewHomeListTitle
                textViewHomeListDate = rowHomeListBinding.textViewHomeListDate
                textViewHomeListHeadCount = rowHomeListBinding.textViewHomeListHeadCount
                textViewHomeListCity = rowHomeListBinding.textViewHomeListHeadCount
                textViewHomeListLikedCount = rowHomeListBinding.textViewHomeListLikedCount

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
            val rowHomeListBinding = RowHomeListBinding.inflate(layoutInflater)

            rowHomeListBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            return HomeListViewHolder(rowHomeListBinding)
        }

        override fun getItemCount(): Int {
            return 4
        }

        override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
            holder.textViewHomeListTitle.text = "부산 여행갈분"
            holder.textViewHomeListDate.text = "2023.09.29 ~ 2023.09.30"
            holder.textViewHomeListHeadCount.text = "4명"
            holder.textViewHomeListCity.text = "부산"
            holder.textViewHomeListLikedCount.text = "10"
        }
    }
}