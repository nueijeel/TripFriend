package com.test.tripfriend.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentInProgressBinding
import com.test.tripfriend.databinding.FragmentLikedListBinding
import com.test.tripfriend.databinding.RowTripMainBinding
import com.test.tripfriend.ui.main.MainActivity
import com.test.tripfriend.viewmodel.TripPostViewModel

class LikedListFragment : Fragment() {
    lateinit var fragmentLikedListBinding: FragmentLikedListBinding
    lateinit var mainActivity: MainActivity

    lateinit var tripPostViewModel: TripPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentLikedListBinding = FragmentLikedListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentLikedListBinding.root
    }

    inner class LikedListAdapter : RecyclerView.Adapter<LikedListAdapter.LikedListViewHolder>() {
        inner class LikedListViewHolder(rowTripMainBinding: RowTripMainBinding) : RecyclerView.ViewHolder(rowTripMainBinding.root) {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikedListViewHolder {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: LikedListViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

    }
}