package com.example.dgs.model.updateprofile

data class UpdateProfileData(var user_id:String,
                             var name:String,
                             var email:String,
                             var mobile:String,
                             var password:String,
                             var location:String,
                             var social_media_user_id:String,
                             var social_media_type:String,
                             var status:String,
                             var reg_date:String,
                             var gcm_token:String,
                             var otp:String,
                             var is_otp_verified:String,
                             var show_my_mobile:String,
                             var firebase_id:String,
                             var profile_pic:String) {
}