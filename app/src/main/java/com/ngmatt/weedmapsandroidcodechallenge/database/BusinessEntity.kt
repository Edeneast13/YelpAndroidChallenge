package com.ngmatt.weedmapsandroidcodechallenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BusinessEntity {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    var businessId: String? = null
    var term: String? = null
    var name: String? = null
    var address: String? = null
    var rating: String? = null
    var topReview: String? = null
    var image: String? = null
}