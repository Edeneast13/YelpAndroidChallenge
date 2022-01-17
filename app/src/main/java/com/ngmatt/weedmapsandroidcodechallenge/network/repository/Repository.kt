package com.ngmatt.weedmapsandroidcodechallenge.network.repository

import com.ngmatt.weedmapsandroidcodechallenge.network.api.NetworkingHelper
import com.ngmatt.weedmapsandroidcodechallenge.network.model.BusinessResponse
import com.ngmatt.weedmapsandroidcodechallenge.network.model.ReviewResponse
import retrofit2.Call

class Repository(private val service: NetworkingHelper.Service) {
    private val searchLimit = 15
    private val radius = 10000

    fun getBusinesses(term: String, lat: Double?, lng: Double?, offset: Int): Call<BusinessResponse> {
        return service.getBusinesses(term, lat, lng, radius, searchLimit, offset)
    }

    fun getBusinessReviews(id: String) : Call<ReviewResponse> {
        return service.getBusinessReviews(id)
    }

    interface OnTaskCompleteListener {
        fun onSuccess(response: Any?)
        fun onFailure(t: Throwable)
        fun onError(response: Any?, code: Int?)
    }
}