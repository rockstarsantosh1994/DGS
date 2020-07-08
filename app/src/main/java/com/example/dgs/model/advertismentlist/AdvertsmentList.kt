package com.example.dgs.model.advertismentlist

import java.io.Serializable

data class AdvertsmentList (var advertisement_id:String,
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
                            var board:String,
                            var medium:String,
                            var standard:String,
                            var subject:String,
                            var publisher:String,
                            var author:String,
                            var email:String,
                            var profile_pic:String,
                            var social_media_user_id:String,
                            var social_media_type:String,
                            var reg_date:String,
                            var firebase_id:String,
                            var gcm_token:String,
                            var images:ArrayList<ImagesList>
                           // var images:ArrayList<MediaStore.Images>

                            ) :Serializable{
    constructor() : this("","","" ,
            "","","","",""
    ,"","","","","","","","",""
    ,"","","","","","","","","","",""
    ,"","","","","","","", ArrayList<ImagesList>()
    )


}
