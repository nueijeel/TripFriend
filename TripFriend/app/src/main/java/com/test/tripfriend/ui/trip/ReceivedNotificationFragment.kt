package com.test.tripfriend.ui.trip

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentReceivedNotificationBinding
import com.test.tripfriend.databinding.RowReceivedNotificationBinding
import com.test.tripfriend.ui.main.MainActivity


class ReceivedNotificationFragment : Fragment() {

    lateinit var fragmentReceivedNotificationBinding: FragmentReceivedNotificationBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentReceivedNotificationBinding = FragmentReceivedNotificationBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentReceivedNotificationBinding.run {
            recyclerViewReceivedNotification.run {
                adapter = ReceivedNotificationiAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentReceivedNotificationBinding.root
    }

    inner class ReceivedNotificationiAdapter : RecyclerView.Adapter<ReceivedNotificationiAdapter.ReceivedNotificationViewHolder>(){
        inner class ReceivedNotificationViewHolder (rowReceivedNotificationBinding: RowReceivedNotificationBinding) :
                RecyclerView.ViewHolder (rowReceivedNotificationBinding.root)
        {
            val textViewSentNotificationRowTitle : TextView
            val imageViewNotificationRowProfileImage : ImageView
            val textViewNotificationRowNickname : TextView
            val textViewNotificationRowContent : TextView
            val buttonNotificationRowAccept : Button
            val buttonNotificationRowRefuse : Button

            init {
                textViewSentNotificationRowTitle = rowReceivedNotificationBinding.textViewSentNotificationRowTitle
                imageViewNotificationRowProfileImage = rowReceivedNotificationBinding.imageViewNotificationRowProfileImage
                textViewNotificationRowNickname = rowReceivedNotificationBinding.textViewNotificationRowNickname
                textViewNotificationRowContent = rowReceivedNotificationBinding.textViewNotificationRowContent
                buttonNotificationRowAccept = rowReceivedNotificationBinding.buttonNotificationRowAccept
                buttonNotificationRowRefuse = rowReceivedNotificationBinding.buttonNotificationRowRefuse
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedNotificationViewHolder {
            val rowReceivedNotificationBinding = RowReceivedNotificationBinding.inflate(layoutInflater)

            rowReceivedNotificationBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ReceivedNotificationViewHolder(rowReceivedNotificationBinding)
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: ReceivedNotificationViewHolder, position: Int) {
            holder.textViewSentNotificationRowTitle.text = "$position"
        }
    }
}