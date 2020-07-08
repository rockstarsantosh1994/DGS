package com.example.dgs.model.signup

data class SignUpData(
    var user_id: String,
    var name: String,
    var email: String,
    var mobile: String,
    var password: String,
    var profile_pic: String,
    var location: String,
    var latitude: String,
    var longitude: String,
    var social_media_user_id: String,
    var social_media_type: String,
    var status: String,
    var reg_date: String,
    var gcm_token: String,
    //var reward_points: String,
    var otp: String,
    var show_my_mobile: String,
    var firebase_id: String,
    var is_otp_verified: String
) {
}