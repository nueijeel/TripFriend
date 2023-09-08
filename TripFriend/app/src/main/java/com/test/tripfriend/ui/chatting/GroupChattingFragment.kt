package com.test.tripfriend.ui.chatting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.tripfriend.MainActivity
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentGroupChattingBinding
import com.test.tripfriend.databinding.RowChattingGroupBinding

class GroupChattingFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var fragmentGroupChattingBinding: FragmentGroupChattingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainActivity = activity as MainActivity
        fragmentGroupChattingBinding = FragmentGroupChattingBinding.inflate(layoutInflater)

        fragmentGroupChattingBinding.run {
            recyclerViewGroupChatting.run {
                adapter = GroupChattingAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentGroupChattingBinding.root
    }

    // GroupChatting 어댑터
    inner class GroupChattingAdapter :
        RecyclerView.Adapter<GroupChattingAdapter.GroupChattingViewHolder>() {

        inner class GroupChattingViewHolder(rowChattingGroupBinding: RowChattingGroupBinding) :
            RecyclerView.ViewHolder(rowChattingGroupBinding.root) {
            val textViewRowGroupChattingTitle: TextView
            val textViewRowGroupChattingNumber: TextView
            val textViewRowGroupChattingMessage: TextView
            val textViewRowGroupChattingDate: TextView

            init {
                textViewRowGroupChattingTitle = rowChattingGroupBinding.textViewRowGroupChattingTitle
                textViewRowGroupChattingNumber = rowChattingGroupBinding.textViewRowGroupChattingNumber
                textViewRowGroupChattingMessage = rowChattingGroupBinding.textViewRowGroupChattingMessage
                textViewRowGroupChattingDate = rowChattingGroupBinding.textViewRowGroupChattingDate
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChattingViewHolder {
            val rowChattingGroupBinding = RowChattingGroupBinding.inflate(layoutInflater)

            rowChattingGroupBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return GroupChattingViewHolder(rowChattingGroupBinding)
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: GroupChattingViewHolder, position: Int) {
            holder.textViewRowGroupChattingTitle.text = "제목 $position"
        }
    }

}