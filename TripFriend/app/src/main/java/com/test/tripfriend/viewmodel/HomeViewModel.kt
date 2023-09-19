package com.test.tripfriend.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tripfriend.dataclassmodel.TripPost
import com.test.tripfriend.repository.TripPostRepository
import kotlinx.coroutines.runBlocking

class HomeViewModel : ViewModel() {
    val tripPostRepository = TripPostRepository()

    val tripPostList = MutableLiveData<List<TripPost>>()

    fun getTripPostData() {
        val currentTripPostSnapshot =
            runBlocking { tripPostRepository.getDocumentData() }

        if(currentTripPostSnapshot != null) {
            val currentTripPost = currentTripPostSnapshot.toObjects(TripPost::class.java)

            tripPostList.value = currentTripPost
        }
    }
}