package com.ngmatt.weedmapsandroidcodechallenge.network.model

class ReviewResponse {
    var reviews: List<Review> = emptyList()

    class Review {
        var id: String? = null
        var rating: Int? = null
        var user: User? = null
        var text: String? = null
        var time_created: String? = null
        var url: String? = null
    }

    class User {
        var id: String? = null
        var profile_url: String? = null
        var image_url: String? = null
        var name: String? = null
    }
}