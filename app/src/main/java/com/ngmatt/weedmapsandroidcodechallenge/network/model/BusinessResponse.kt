package com.ngmatt.weedmapsandroidcodechallenge.network.model

class BusinessResponse {
    var total: Int? = null
    var businesses: List<Business> = emptyList()
    var region: Region? = null

    class Business {
        var rating: Double? = null
        var price: String? = null
        var phone: String? = null
        var id: String? = null
        var alias: String? = null
        var is_closed: Boolean? = false
        var categories: List<Category> = emptyList()
        var review_count: Int? = null
        var name: String? = null
        var url: String? = null
        var coordinates: Coordinates? = null
        var image_url: String? = null
        var location: Location? = null
        var distance: Double? = null
        var transactions: List<String> = emptyList()
    }

    class Category {
        var alias: String? = null
        var title: String? = null
    }

    class Coordinates {
        var latitude: Double? = null
        var longitude: Double? = null
    }

    class Location {
        var city: String? = null
        var country: String? = null
        var address2: String? = null
        var address3: String? = null
        var state: String? = null
        var address1: String? = null
        var zip_code: String? = null
    }

    class Region {
        var center: Center? = null
    }

    class Center {
        var latitude: Double? = null
        var longitude: Double? = null
    }
}