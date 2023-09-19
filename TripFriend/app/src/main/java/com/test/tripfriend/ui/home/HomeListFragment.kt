package com.test.tripfriend.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.databinding.FragmentHomeListBinding
import com.test.tripfriend.databinding.RowHomeListBinding
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.ui.trip.TripMainFragment
import com.test.tripfriend.viewmodel.HomeViewModel
import com.test.tripfriend.viewmodel.TripPostViewModel

class HomeListFragment : Fragment() {
    lateinit var fragmentHomeListBinding: FragmentHomeListBinding
    lateinit var mainActivity: MainActivity

    lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentHomeListBinding = FragmentHomeListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initHomeViewModel()

        fragmentHomeListBinding.run {
            recyclerViewHomeList.run {
                adapter = HomeListAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentHomeListBinding.root
    }

    inner class HomeListAdapter : RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {
        var homePostItemList :List<TripPost> = emptyList()

        fun updateItemList(newList: List<TripPost>) {
            this.homePostItemList = newList
            notifyDataSetChanged()

            Log.d("qwer", "${homePostItemList.joinToString(separator = ", ")}")
            Log.d("qwer", "${homePostItemList.get(0)}")
        }

        inner class HomeListViewHolder(rowHomeListBinding: RowHomeListBinding) : RecyclerView.ViewHolder(rowHomeListBinding.root) {
            val textViewHomeMainListRowTitle: TextView
            val textViewHomeMainListDate: TextView
            val textViewHomeMainListRowNOP: TextView
            val textViewHomeMainListRowLocation: TextView
            val textViewHomeMainListRowHashTag: TextView

            init {
                textViewHomeMainListRowTitle = rowHomeListBinding.textViewHomeMainListRowTitle
                textViewHomeMainListDate = rowHomeListBinding.textViewHomeMainListDate
                textViewHomeMainListRowNOP = rowHomeListBinding.textViewHomeMainListRowNOP
                textViewHomeMainListRowLocation = rowHomeListBinding.textViewHomeMainListRowLocation
                textViewHomeMainListRowHashTag = rowHomeListBinding.textViewHomeMainListRowHashTag

                rowHomeListBinding.root.setOnClickListener {
                    mainActivity.replaceFragment(MainActivity.READ_POST_FRAGMENT, true, true, null)

                }
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
            return homePostItemList.size
        }

        override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
            holder.textViewHomeMainListRowTitle.text = homePostItemList.get(position).tripPostTitle
            holder.textViewHomeMainListDate.text = "${homePostItemList.get(position).tripPostDate?.get(0)} ~ ${homePostItemList.get(position).tripPostDate?.get(1)}"
            holder.textViewHomeMainListRowNOP.text = homePostItemList.get(position).tripPostMemberCount.toString()
            holder.textViewHomeMainListRowLocation.text = homePostItemList.get(position).tripPostLocationName
            holder.textViewHomeMainListRowHashTag.text = homePostItemList.get(position).tripPostHashTag
        }
    }

    fun initHomeViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.getTripPostData()

        homeViewModel.tripPostList.observe(viewLifecycleOwner){
            if(it != null) {
                (fragmentHomeListBinding.recyclerViewHomeList.adapter as? HomeListAdapter)?.updateItemList(it)
            }
        }
    }
}