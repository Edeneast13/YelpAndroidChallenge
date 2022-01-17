package com.ngmatt.weedmapsandroidcodechallenge.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ngmatt.weedmapsandroidcodechallenge.database.DatabaseInserter
import com.ngmatt.weedmapsandroidcodechallenge.network.model.BusinessResponse
import com.ngmatt.weedmapsandroidcodechallenge.network.model.ReviewResponse
import com.ngmatt.weedmapsandroidcodechallenge.network.repository.Repository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: Repository, application: Application) : AndroidViewModel(application) {
    private val job = CoroutineScope(Dispatchers.IO)

    fun getBusinesses(term: String, lat: Double?, lng: Double?, offset: Int, listener: Repository.OnTaskCompleteListener) {
      repository.getBusinesses(term, lat, lng, offset).enqueue(object : Callback<BusinessResponse>{
          override fun onResponse(call: Call<BusinessResponse>, response: Response<BusinessResponse>) {
              when (response.isSuccessful) {
                  true -> {
                      job.launch {
                          response.body()?.businesses?.forEach {
                              val context = getApplication<Application>().applicationContext
                              DatabaseInserter.insertItem(context, term, it, offset)
                           //   getBusinessReviews(it.id!!)
                            //  delay(5000)
                          }
                          withContext(Dispatchers.Main) {
                              listener.onSuccess(response.body())
                          }
                      }
                  }
                  false -> listener.onError(response.body(), response.code())
              }
          }

          override fun onFailure(call: Call<BusinessResponse>, t: Throwable) {
              listener.onFailure(t)
          }
      })
    }

    fun getBusinessReviews(id: String) {
        repository.getBusinessReviews(id).enqueue(object : Callback<ReviewResponse>{
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                when (response.isSuccessful) {
                    true -> {
                        val topReview = response.body()?.reviews?.first()
                        val context = getApplication<Application>().applicationContext
                        DatabaseInserter.updateReview(context, topReview?.id!!, topReview.text!!)
                       // listener.onSuccess(response.body())
                    }
                    //false -> listener.onError(response.body(), response.code())
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
//                listener.onFailure(t)
            }
        })
    }
}