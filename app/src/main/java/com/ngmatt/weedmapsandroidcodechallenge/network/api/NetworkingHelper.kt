package com.ngmatt.weedmapsandroidcodechallenge.network.api

import com.ngmatt.weedmapsandroidcodechallenge.network.model.BusinessResponse
import com.ngmatt.weedmapsandroidcodechallenge.network.model.ReviewResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object NetworkingHelper {
    fun getService() : Service {
        return NetworkingClient()
            .buildClient()
            .create(Service::class.java)
    }

    interface Service {
        @GET("businesses/search")
        fun getBusinesses(
            @Query("term") term: String,
            @Query("latitude") latitude: Double?,
            @Query("longitude") longitude: Double?,
            @Query("radius") radius: Int,
            @Query("limit") limit: Int,
            @Query("offset") offset: Int) : Call<BusinessResponse>

        @GET("businesses/{id}/reviews")
        fun getBusinessReviews(@Path("id") id: String) : Call<ReviewResponse>
    }
}