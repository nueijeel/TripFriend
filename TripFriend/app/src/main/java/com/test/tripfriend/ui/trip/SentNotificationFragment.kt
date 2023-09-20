package com.test.tripfriend.ui.trip

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.tripfriend.R
import com.test.tripfriend.databinding.FragmentSentNotificationBinding
import com.test.tripfriend.databinding.RowReceivedNotificationBinding
import com.test.tripfriend.databinding.RowSentNotificiationBinding
import com.test.tripfriend.ui.main.MainActivity


class SentNotificationFragment : Fragment() {

    lateinit var fragmentSentNotificationBinding: FragmentSentNotificationBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragmentSentNotificationBinding = FragmentSentNotificationBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        fragmentSentNotificationBinding.run {
            recyclerViewSentNotification.run{
                adapter = SentNotificationAdapter()
                layoutManager = LinearLayoutManager(mainActivity)
            }
        }

        return fragmentSentNotificationBinding.root
    }

    inner class SentNotificationAdapter : RecyclerView.Adapter<SentNotificationAdapter.SentNofiticationViewHolder>(){
        inner class SentNofiticationViewHolder (rowSentNotificiationBinding: RowSentNotificiationBinding) :
                RecyclerView.ViewHolder(rowSentNotificiationBinding.root)
        {
            val textViewSentNotificationRowTitle : TextView
            val textViewSentNotificationRowAccptState : TextView
            val buttonSentNotificationRowCancel : Button

            init {
                textViewSentNotificationRowTitle = rowSentNotificiationBinding.textViewSentNotificationRowTitle
                textViewSentNotificationRowAccptState = rowSentNotificiationBinding.textViewSentNotificationRowAccptState
                buttonSentNotificationRowCancel = rowSentNotificiationBinding.buttonSentNotificationRowCancel
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SentNofiticationViewHolder {
            val rowSentNotificiationBinding = RowSentNotificiationBinding.inflate(layoutInflater)

            rowSentNotificiationBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return SentNofiticationViewHolder(rowSentNotificiationBinding)
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: SentNofiticationViewHolder, position: Int) {
            holder.textViewSentNotificationRowTitle.text = "$position"
        }
    }
}