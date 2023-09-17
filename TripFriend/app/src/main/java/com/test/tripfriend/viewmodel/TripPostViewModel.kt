package com.test.tripfriend.viewmodel

import androidx.lifecycle.ViewModel
import com.test.tripfriend.repository.TripPostRepository

class TripPostViewModel: ViewModel() {
    val tripPostRepository = TripPostRepository()

    fun getTripPostData() {
    }
}