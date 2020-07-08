package com.example.dgs

class ConfigUrl {
    companion object {
        val BASE_URL: String = "http://123.201.36.137:9192/digishare/trunk/api/"
        val FIREBASE_NOTIFICATION_URL="https://fcm.googleapis.com/fcm/send"

        val LOGIN_URL:String=BASE_URL+"user/login/format/json"
        val VERIFY_URL:String=BASE_URL+"user/verify/format/json"
        val RESEND_URL:String=BASE_URL+"user/resend/format/json"
        val SIGNUP_URL:String=BASE_URL+"user/register/format/json"
        val UPDATE_PROFILE:String=BASE_URL+"user/update/format/json"
        val DATA_SYNC:String=BASE_URL+"user/data_sync/format/json"
        val DASHBOARD_DATA:String=BASE_URL+"user/dashboard_data/format/json"
        val SAVE_ADS:String=BASE_URL+"advertisement/add/format/json"
        val UPLOAD_IMG:String= BASE_URL+"advertisement/upload_images/format/json/"
        val UPDATE_ADS:String=BASE_URL+"advertisement/update/format/json"
        val VIEW_ADS_LIST:String=BASE_URL+"advertisement/list/format/json"
        val DELETE_ADS:String=BASE_URL+"advertisement/delete/format/json"
        val MARK_AS_SOLD_URL:String=BASE_URL+"advertisement/mark_as_sold/format/json"
        val VERSION_CHECK:String="https://www.asmobisoft.com/apps/digishare/api/version_check.php"
        val DELETE_IMAGE:String=BASE_URL+"advertisement/delete_image/format/json"
        val UPDATELOCATION:String=BASE_URL+"user/update_location/format/json"
        val CHECK_PHONE_NUMBER_VISIBILITY:String=BASE_URL+"user/update_mobile_visibility/format/json"
        val CHAT_SAVED:String=BASE_URL+"advertisement/chat/format/json"
        val MY_ORDERS:String=BASE_URL+"advertisement/my_orders/format/json"
        val FORGOTPASSWORD:String=BASE_URL+"user/forgot/format/json"
        val UPDATEPASSWORD:String=BASE_URL+"user/update_password/format/json"
    }
}