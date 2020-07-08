package com.example.dgs.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.ConfigUrl
import com.example.dgs.R
import com.example.dgs.activity.BottomNavigationActivity
import com.example.dgs.activity.chat.LatestMessagesActivity
import com.example.dgs.adapter.BooksByBoardAdapter
import com.example.dgs.adapter.BooksByClassAdapter
import com.example.dgs.model.masterdata.DataSyncResponse
import com.example.dgs.utility.BaseFragmet
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.collections.HashMap

class DashBoardFragment :BaseFragmet(),View.OnClickListener{

    var rvDataByClass:RecyclerView? = null
    var rvDataByBoard:RecyclerView? = null
    var createView:View?=null
    var toolbar: Toolbar?=null
    var cvClass:CardView?=null
    var cvSubject:CardView?=null
    var cvAuthor:CardView?=null
    var cvBoard:CardView?=null
    var TAG:String="DashBoardFragment"
    var llNoInternet:LinearLayout?=null
    var llNoData:LinearLayout?=null
    var llNoServerFound:LinearLayout?=null
    var llNoInternet1:LinearLayout?=null
    var llNoData1:LinearLayout?=null
    var llNoServerFound1:LinearLayout?=null
    var llDataByClass:LinearLayout?=null
    var llDataByBoard:LinearLayout?=null
    var etSearchBook:EditText?=null
    var et_search:EditText?=null
    var placename:String?=null
    val PERMISSION_ID = 42
    var ivHamberger:ImageView?=null
    var ivNotification:ImageView?=null
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    val AUTOCOMPLETE_REQUEST_CODE=200
    var offsetValClass:Int?=0
    var offsetValBoard:Int?=0
    // var searchBar:MaterialSearchBar ?= null
    var placeFields= Arrays.asList(
       Place.Field.LAT_LNG,
       Place.Field.NAME,
       Place.Field.ADDRESS)
    var isLocationRoot:String="lastlocation"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView = inflater.inflate(R.layout.fragment_dashboard, container, false)

       /* Places.initialize(context as AppCompatActivity,getString(R.string.google_api_key))
        placeClient=Places.createClient(context as AppCompatActivity)*/
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context as AppCompatActivity)

        //Basic intialisation....
        initViews()

        /*get current location*/
        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.IS_LOCATION_ROOT).equals(AllKeys.DNF) || CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.IS_LOCATION_ROOT).equals("lastlocation")){
            getLastLocation()
        }

        Log.e("location",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LONGITUDE))
        Log.e("location",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LATITUDE))
        Log.e("location",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS))
        Log.e("location",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.IS_LOCATION_ROOT))
        /*if(!et_search?.text.toString().isEmpty()){

        }*/

        //load data ByClass and ByBoard
        if(!CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS).equals(AllKeys.DNF)){
            if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)){
                llNoInternet?.visibility=View.GONE
                llNoInternet1?.visibility=View.GONE
                et_search?.setText(CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS))
                loadData()
            }else{
                llNoInternet?.visibility=View.VISIBLE
                llNoInternet1?.visibility=View.VISIBLE
                llDataByBoard?.visibility=View.GONE
                llDataByClass?.visibility=View.GONE
            }
        }


        etSearchBook?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                if(etSearchBook?.text.toString().equals("")){
                    etSearchBook?.setError("Invalid Search!")
                    etSearchBook?.requestFocus()
                }else{
                    val searchFragment: Fragment = SearchFragment()
                    val data = Bundle() //Use bundle to pass data
                    data.putString("search_string", etSearchBook?.text.toString())
                    searchFragment.arguments = data
                    addFragmentWithoutRemove(R.id.frame_container,searchFragment, "SearchFragment")
                    CommonMethods.setPreference(context as AppCompatActivity, AllKeys.IS_FIRST,"0")
                }
                true

            } else {
                false
            }
        }

        val activity = activity as Context
        //Places.initialize(activity, resources.getString(R.string.google_api_key))
        Places.initialize(activity,"AIzaSyAbymbEYWHWU1NjdGNbVRK0v5uY_MbO8ic")
        //Places.initialize(activity,"AIzaSyBPx1d9YYprWtkkIKGQJFKzIh44Ihik8ds")

        return createView
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {

        if (checkPermissions()) {
                if (isLocationEnabled()) {

                    mFusedLocationClient.lastLocation.addOnCompleteListener(context as AppCompatActivity) { task ->
                        var location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                           /* findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
                            findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()*/

                            Log.e("latitudeDash","latitude"+ location.latitude)
                            Log.e("longitudeDash","longitude"+ location.longitude)

                            getAddressFromLatLng(location.latitude,location.longitude)
                            /*if(CommonMethods.isNetworkAvailable(context as Activity)){

                            }else{
                                Toast.makeText(context as AppCompatActivity,"No Internet Connection",Toast.LENGTH_SHORT).show()
                            }*/
                        }
                    }
                } else {
                    Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(intent,301)
                    //et_search?.setText(placename)
                }
            } else {
                requestPermissions()
            }
    }

    private fun getAddressFromLatLng(latitude: Double, longitude: Double) {
        try {
            var geocoder: Geocoder? = null
            var addresses: List<Address>? = null
            geocoder = Geocoder(context, Locale.getDefault())
            addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address: String = addresses.get(0).getAddressLine(0)
            val city: String = addresses.get(0).locality
            val knownname = addresses.get(0).featureName

            Log.e("DB","local : "+city)
            for(i in addresses){
                Log.e("DB","i ki value : "+i)
            }
            Log.e("DB","addresses : "+addresses)
            Log.e("DB","local : "+city)


            if (knownname != null || !knownname.equals("")) {
                placename = knownname
                //CommonMethods.setPreference(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS,placename)
                if (CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS).equals(AllKeys.DNF)) {
                    et_search?.setText(knownname)
                } else {
                    et_search?.setText(CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS))
                }



                CommonMethods.setPreference(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS, et_search?.text.toString())
                CommonMethods.setPreference(context as AppCompatActivity,AllKeys.LATITUDE,latitude.toString())
                CommonMethods.setPreference(context as AppCompatActivity,AllKeys.LONGITUDE,longitude.toString())

                //call update location api
                updatelocation()

                Log.e(
                    "commonmethodlatlong",
                    "Place: " + CommonMethods.getPrefrence(
                        context as AppCompatActivity,
                        AllKeys.CURRENT_ADDRESS
                    )
                )
                if (CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                    loadData()
                } else {
                    llNoInternet?.visibility = View.VISIBLE
                    llNoInternet1?.visibility = View.VISIBLE
                    llDataByBoard?.visibility = View.GONE
                    llDataByClass?.visibility = View.GONE
                }
            } else {
                Toast.makeText(context, "Please select location manually", Toast.LENGTH_LONG).show()
            }



           /* if (CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                loadData()
            } else {
                llNoInternet?.visibility = View.VISIBLE
                llNoInternet1?.visibility = View.VISIBLE
                llDataByBoard?.visibility = View.GONE
                llDataByClass?.visibility = View.GONE
            }*/

            Log.e("Address", "Address $address" + "\n city $city" + "\n knownname $knownname")
        }catch (e:InvocationTargetException){
            e.cause?.printStackTrace()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context as AppCompatActivity)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            /*findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()*/
            Log.e("latitude","latitude"+ mLastLocation.latitude)
            Log.e("longitude","longitude"+ mLastLocation.longitude)
            CommonMethods.setPreference(context as AppCompatActivity,AllKeys.LATITUDE,mLastLocation.latitude.toString())
            CommonMethods.setPreference(context as AppCompatActivity,AllKeys.LONGITUDE,mLastLocation.longitude.toString())
            if(CommonMethods.isNetworkAvailable(context as Activity)){
                getAddressFromLatLng(mLastLocation.latitude,mLastLocation.longitude)
            }else{
                Toast.makeText(context as AppCompatActivity,"No Internet Connection",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(context as AppCompatActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context as AppCompatActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            context as AppCompatActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    fun initViews(){
        //Toolbar intialisation..
        toolbar=createView?.findViewById(R.id.toolbar)
        toolbar?.title = ""
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)

        //CardView intialisation..
        cvClass=createView?.findViewById(R.id.cv_category_class)
        cvSubject=createView?.findViewById(R.id.cv_category_subject)
        cvAuthor=createView?.findViewById(R.id.cv_category_author)
        cvBoard=createView?.findViewById(R.id.cv_category_board)
        et_search = createView?.findViewById(R.id.et_search)
        et_search?.requestFocus()
        cvClass?.setOnClickListener(this)
        cvSubject?.setOnClickListener(this)
        cvAuthor?.setOnClickListener(this)
        cvBoard?.setOnClickListener(this)
        et_search?.setOnClickListener(this)

        //ImageVIew intialisation...
        ivHamberger=createView?.findViewById(R.id.ic_hamberger)
        ivHamberger?.setOnClickListener(this)
        ivNotification=createView?.findViewById(R.id.iv_notification)
        ivNotification?.setOnClickListener(this)

        //LinearLayout intialisation...
        llNoData=createView?.findViewById(R.id.ll_nodata)
        llNoInternet=createView?.findViewById(R.id.ll_nointernet)
        llNoServerFound=createView?.findViewById(R.id.ll_servernotfound)
        llNoData1=createView?.findViewById(R.id.ll_nodata1)
        llNoInternet1=createView?.findViewById(R.id.ll_nointernet1)
        llNoServerFound1=createView?.findViewById(R.id.ll_servernotfound1)
        llDataByClass=createView?.findViewById(R.id.ll_databyclass)
        llDataByBoard=createView?.findViewById(R.id.ll_databyboard)

        //Recycler view intialisation...
        rvDataByClass= createView?.findViewById(R.id.rv_databyclass) as RecyclerView
        rvDataByBoard= createView?.findViewById(R.id.rv_databyboard) as RecyclerView
        rvDataByClass!!.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        rvDataByBoard!!.layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        //EditText intialisation
        etSearchBook=createView?.findViewById(R.id.et_find) as EditText
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
                R.id.cv_category_board->{
                    val findFragment: Fragment = FindFragment()
                    val data = Bundle() //Use bundle to pass data
                    data.putString("category_type", "board")
                    data.putString("location", et_search?.text.toString())
                    findFragment.arguments = data
                    CommonMethods.setPreference(context as AppCompatActivity,AllKeys.IS_FIRST,"0")
                    addFragmentWithoutRemove(R.id.frame_container,findFragment, "UploadPictures")
                }
                R.id.cv_category_subject->{
                    val findFragment: Fragment = FindFragment()
                    val data = Bundle() //Use bundle to pass data
                    data.putString("category_type", "subject")
                    data.putString("location", et_search?.text.toString())
                    findFragment.arguments = data
                    CommonMethods.setPreference(context as AppCompatActivity,AllKeys.IS_FIRST,"0")
                    addFragmentWithoutRemove(R.id.frame_container,findFragment, "UploadPictures")
                }
                R.id.cv_category_author->{
                    val findFragment: Fragment = FindFragment()
                    val data = Bundle() //Use bundle to pass data
                    data.putString("category_type", "author")
                    data.putString("location", et_search?.text.toString())
                    findFragment.arguments = data
                    CommonMethods.setPreference(context as AppCompatActivity,AllKeys.IS_FIRST,"0")
                    addFragmentWithoutRemove(R.id.frame_container,findFragment, "UploadPictures")
                }
                R.id.cv_category_class->{
                    val findFragment: Fragment = FindFragment()
                    val data = Bundle() //Use bundle to pass data
                    data.putString("category_type", "class")
                    data.putString("location", et_search?.text.toString())
                    findFragment.arguments = data
                    CommonMethods.setPreference(context as AppCompatActivity,AllKeys.IS_FIRST,"0")
                    addFragmentWithoutRemove(R.id.frame_container,findFragment, "UploadPictures")
                }

                R.id.et_search->{
                    Log.e("placeapi","clicked")
                    val intent = activity?.let {
                        Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN, placeFields)
                            .build(it)
                    }
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }

                R.id.ic_hamberger->{
                    (activity as BottomNavigationActivity).openDrawer()
                }

                R.id.iv_notification->{
                    val intent=Intent(context, LatestMessagesActivity::class.java)
                    intent.putExtra("notification","Notification")
                    startActivity(intent)
                }
        }
    }

    fun loadData(){
        val stringRequest=object : StringRequest(Request.Method.POST, ConfigUrl.DASHBOARD_DATA,
            Response.Listener {
                response ->
            val gson= Gson()
            //Log.e(TAG,"response $response")
            val dataSyncResponse=gson.fromJson(response, DataSyncResponse::class.java)
            if(dataSyncResponse.StatusCode.equals("1")){
                Log.e("DataSync","response $response")
                if(dataSyncResponse.data.by_class.size!=0) {
                    llDataByClass?.visibility=View.VISIBLE
                    llNoData?.visibility=View.GONE
                    var dataAdapter= context?.let { BooksByClassAdapter(it,dataSyncResponse.data.by_class) }
                    rvDataByClass!!.adapter=dataAdapter
                }else{
                    llNoData?.visibility=View.VISIBLE
                    llDataByClass?.visibility=View.GONE
                }
                if(dataSyncResponse.data.by_board.size!=0){
                    llDataByBoard?.visibility=View.VISIBLE
                    llNoData1?.visibility=View.GONE
                    var dataAdapter= context?.let { BooksByBoardAdapter(it,dataSyncResponse.data.by_board) }
                    rvDataByBoard!!.adapter=dataAdapter
                }else{
                    llNoData1?.visibility=View.VISIBLE
                    llDataByBoard?.visibility=View.GONE
                }


            }else{
                Toast.makeText(context,dataSyncResponse.StatusCode, Toast.LENGTH_LONG).show()
            }

        }, Response.ErrorListener {
            llDataByClass?.visibility=View.GONE
            llDataByBoard?.visibility=View.GONE
            llNoServerFound?.visibility=View.VISIBLE
            llNoServerFound1?.visibility=View.VISIBLE
            Log.e("DataSync","Data master error $it")
        }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params=HashMap<String,String>()

                params.put("user_id", context?.let { CommonMethods.getPrefrence(it,AllKeys.USER_ID).toString() }.toString())
                params.put("location",et_search?.text.toString())
                params.put("latitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LATITUDE).toString())
                params.put("longitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LONGITUDE).toString())


                Log.e("DataSync","load data params\n $params")

                return params
            }
        }
        val mQueue= Volley.newRequestQueue(activity as AppCompatActivity)
        mQueue.add(stringRequest)
    }

    private fun updatelocation() {
        val stringRequest=object : StringRequest(Request.Method.POST, ConfigUrl.UPDATELOCATION,
            Response.Listener {
                    response ->
                val gson= Gson()
                Log.e("update_location","update_response $response")
                val dataSyncResponse=gson.fromJson(response, DataSyncResponse::class.java)
                if(dataSyncResponse.StatusCode.equals("1")){
                    Log.e(TAG,"response $response")
                }else{
                    Toast.makeText(context,dataSyncResponse.StatusCode, Toast.LENGTH_LONG).show()
                }

            }, Response.ErrorListener {

                Log.e(TAG,"Data master error $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params=HashMap<String,String>()
                    params.put("user_id",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.USER_ID).toString())
                    params.put("location",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS).toString())
                    params.put("latitude",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LATITUDE).toString())
                    params.put("longitude",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LONGITUDE).toString())

                Log.e("update","Download master params\n $params")

                return params
            }
        }
        val mQueue= Volley.newRequestQueue(context as Activity)
        mQueue.add(stringRequest)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e("placeapi","inside on activity on result \n"+data)
        Log.e("placeapi","requestcode \n"+AUTOCOMPLETE_REQUEST_CODE)
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            Log.e("placeapi","result ok")
            //setImageViewWithImage()
            val place = data?.let {
                Autocomplete.getPlaceFromIntent(it)
            }

            placename=place?.name
            et_search?.setText(placename)
            if (placename != null) {
            var latLng:LatLng  = place?.latLng!!
            /*val mStringLatitude = latLng.latitude
            val mStringLongitude = latLng.longitude*/
             CommonMethods.setPreference(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS,et_search?.text.toString())
             CommonMethods.setPreference(context as AppCompatActivity,AllKeys.LATITUDE,latLng.latitude.toString())
             CommonMethods.setPreference(context as AppCompatActivity,AllKeys.LONGITUDE,latLng.longitude.toString())

             isLocationRoot="placeapi"
             CommonMethods.setPreference(context as AppCompatActivity,AllKeys.IS_LOCATION_ROOT,isLocationRoot)
             //Location will update
             updatelocation()

             Log.e("location","Place current location"+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS))
             Log.e("location","Place latitude"+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LATITUDE))
             Log.e("location","Place longitude"+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LONGITUDE))

        }
            //CommonMethods.setPreference(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS,placename)
            //


            Log.e("Datatat", "Place: " + place?.name)
            Log.e("Datatat", "Place: " + place?.name)
            //  var string: String? =place?.name


            Log.e("commonmethod", "Place: " + CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS))
            //load data ByClass and ByBoard
              if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)){
                  loadData()
              }else{
                  llNoInternet?.visibility=View.VISIBLE
                  llNoInternet1?.visibility=View.VISIBLE
                  llDataByBoard?.visibility=View.GONE
                  llDataByClass?.visibility=View.GONE
              }
        }

        if(requestCode==301 ){
            //replaceFragmentWithoutBack(R.id.frame_container,DashBoardFragment(), "DashBoardFragment")
            getLastLocation()
            Log.e("onActivityResult","data\n")
            Log.e("onActivityResult","data\n"+data)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("location",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LONGITUDE))
        Log.e("locationLAT",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LATITUDE))
        Log.e("locationONG",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS))
    }
}


