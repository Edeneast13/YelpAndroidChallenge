package com.ngmatt.weedmapsandroidcodechallenge.database

import android.content.Context
import com.ngmatt.weedmapsandroidcodechallenge.network.model.BusinessResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseInserter {
    fun insertItem(context: Context, term: String, business: BusinessResponse.Business, offset: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (offset == 0) {
                //erase the previously cached results for this search term
                AppDatabase.getDatabase(context).businessDao().delete(term)
            }
            val item = BusinessEntity()
            item.name = business.name
            item.businessId = business.id
            item.term = term
            item.address = business.location?.address1 + ", " + business.location?.city + ", " + business.location?.state
            item.image = business.image_url
            item.rating = business.rating.toString()
            AppDatabase.getDatabase(context).businessDao().insert(item)
        }
    }

    fun updateReview(context: Context, id: String, review: String) {
       CoroutineScope(Dispatchers.IO).launch {
           AppDatabase.getDatabase(context).businessDao().updateTopReview(id, review)
       }
    }
}