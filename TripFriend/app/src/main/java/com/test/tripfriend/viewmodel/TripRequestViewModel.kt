package com.test.tripfriend.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.tripfriend.dataclassmodel.TripRequest
import com.test.tripfriend.dataclassmodel.User
import com.test.tripfriend.repository.TripRequestRepository
import kotlinx.coroutines.runBlocking

class TripRequestViewModel : ViewModel() {

    val tripRequestRepository = TripRequestRepository()

    //요청 객체 리스트
    private val _tripRequest = MutableLiveData<List<TripRequest>>()
    val tripRequest : LiveData<List<TripRequest>>
        get() = _tripRequest

    //요청 문서
    private val _tripRequestDocumentId = MutableLiveData<List<String>>()
    val tripRequestDocumentId : LiveData<List<String>>
        get() = _tripRequestDocumentId

    fun getAllTripRequest(tripRequestWriterEmail : String){

        val tripRequestQuerySnapshot = runBlocking {
            tripRequestRepository.getAllTripRequest(tripRequestWriterEmail)
        }

        if(tripRequestQuerySnapshot != null){
            val documentIdList = mutableListOf<String>()
            val tripRequestList = mutableListOf<TripRequest>()

            tripRequestQuerySnapshot.documents.forEach { documentSnapshot ->
                documentIdList.add(documentSnapshot.id)
            }
            _tripRequestDocumentId.value = documentIdList

            val tripRequests = tripRequestQuerySnapshot.toObjects(TripRequest::class.java)
            tripRequests.forEach { request ->
                tripRequestList.add(request)
            }
            _tripRequest.value = tripRequestList
        }
    }
}