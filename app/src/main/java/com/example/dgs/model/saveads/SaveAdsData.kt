package com.example.dgs.model.saveads

data class SaveAdsData(
    var advertisement_id:String,
    var title:String,
    var description:String,
    var board_id:String,
    var medium_id:String,
    var standard_id:String,
    var subject_id:String,
    var publisher_id:String,
    var author_id:String,
    var price:String,
    var is_negotiable:String,
    var location:String,
    var latitude:String,
    var longitude:String,
    var mobile:String,
    var name:String,
    var show_mobile:String,
    var show_location:String,
    var user_id:String,
    var status:String,
    var posted_date:String,
    var sell_date:String,
    var delete_date:String
) {
}