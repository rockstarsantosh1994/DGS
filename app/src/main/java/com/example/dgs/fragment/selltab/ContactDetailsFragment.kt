package com.example.dgs.fragment.selltab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.example.dgs.utility.BaseFragmet
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class ContactDetailsFragment : BaseFragmet(), View.OnClickListener {

    var btnContactDetails: FloatingActionButton? = null
    var btnPrevious:FloatingActionButton?=null
    var toolbar: Toolbar? = null
    val TAG: String = "ContactDetailsFragment"
    var createView: View? = null
    var etMobileNumber: TextInputEditText? = null
    var etLocation:TextInputEditText?=null
   /* var latititue:String?=null
    var longitude:String?=null
    var LatLng:String?=null

 */
    var stLatitude:String?=null
    var stLongitude:String?=null
    var location:String?=null

    val AUTOCOMPLETE_REQUEST_CODE=200
    // var searchBar:MaterialSearchBar ?= null
    var placeFields= Arrays.asList(
        Place.Field.LAT_LNG,
        Place.Field.NAME,
        Place.Field.ADDRESS

    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView = inflater.inflate(R.layout.activity_contact_details, container, false)

       // Toast.makeText(context,"advertisement_type "+arguments?.getString("advertisement_type"),Toast.LENGTH_LONG).show()

        //init views...
        initViews()

        Log.e(TAG,"is_negotiable"+arguments?.getString("is_negotiable"))

        Log.e(TAG, "Boardid ${arguments?.getString("stBoardId")} \nMediumId ${arguments?.getString("stMediumId")} " +"\n ClassId ${arguments?.getString("stClassId")} \nSubjectId ${arguments?.getString("stSubjectId")}" +" \nPublisher ID${arguments?.getString("stPublisherId")} \nAuthorId ${arguments?.getString("stAuthorId")}" + "\n Title ${arguments?.getString("ettitle")} \n Desc ${arguments?.getString("desc")} \n Price ${arguments?.getString("price")}")
        Log.e(TAG,"lattitude "+arguments?.getString("lat"))
        Log.e(TAG,"longitude "+arguments?.getString("lng"))
        Log.e(TAG,"location "+arguments?.getString("location"))

        return createView
    }

    fun initViews() {
        //Button intialisation..
        btnContactDetails = createView?.findViewById(R.id.btn_contactdetails) as FloatingActionButton
        btnContactDetails?.setOnClickListener(this)
        btnPrevious=createView?.findViewById(R.id.btn_prevcontactdetails) as FloatingActionButton
        btnPrevious?.setOnClickListener(this)

        /*location text click*/



       /* etLocation?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //Your code here

            }})*/


        //Toolbar intialisation...
        toolbar = createView?.findViewById(R.id.toolbar_contact) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Contact details"

        //Edittext intialisation...
        etMobileNumber = createView?.findViewById(R.id.et_add_contact_number)
        etLocation=createView?.findViewById(R.id.spin_selectlocation)
        etLocation?.requestFocus()
        etLocation?.setOnClickListener(this)

        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).toString().equals("f")){
            etMobileNumber?.setText(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_MOBILE))
        }

        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).toString().equals("g")){
            etMobileNumber?.setText(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_MOBILE))
        }

        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).toString().equals("e")){
            etMobileNumber?.setText(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MOBILE_NUMBER))
        }

       /* if(!CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS).toString().equals(AllKeys.DNF)){
            etLocation?.setText(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS))
        }*/
        if(arguments?.getString("advertisement_type").equals("update")){
            if(!arguments?.getString("mobile").equals("")){
                etMobileNumber?.setText(arguments?.getString("mobile"))
            }
            if(!arguments?.getString("location").equals("")){
                etLocation?.setText(arguments?.getString("location"))
            }
        }else if(arguments?.getString("advertisement_type").equals("new")){
            if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).toString().equals("e")){
                etMobileNumber?.setText(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MOBILE_NUMBER))
            }
            if(!CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS).toString().equals(AllKeys.DNF)){
                 etLocation?.setText(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.CURRENT_ADDRESS))
             }
        }

      //  Toast.makeText(context as AppCompatActivity,"stLongitude"+stLongitude,Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.spin_selectlocation->{
               // Toast.makeText(context,"Clicked on Editext",Toast.LENGTH_LONG).show()
               /* val intent= Intent(context, PlacesActivity::class.java)
                startActivity(intent)*/
               // finish()

                val intent = activity?.let {
                    Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, placeFields)
                        .build(it)
                }
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }

            R.id.btn_contactdetails -> {
                if (isValidated()) {
                    if(arguments?.getString("advertisement_type").equals("update")){
                        val postNowFragment: Fragment = PostNowFragment()
                        val data = Bundle()
                        data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                        data.putString("advertisement_id",arguments?.getString("advertisement_id"))
                        data.putString("title",arguments?.getString("title"))
                        data.putString("description",arguments?.getString("description"))
                        data.putString("board_id", arguments?.getString("board_id"))
                        data.putString("medium_id", arguments?.getString("medium_id"))
                        data.putString("standard_id",arguments?.getString("standard_id"))
                        data.putString("subject_id", arguments?.getString("subject_id"))
                        data.putString("publisher_id",arguments?.getString("publisher_id"))
                        data.putString("author_id",arguments?.getString("author_id"))
                        data.putString("price",arguments?.getString("price"))
                        data.putString("is_negotiable",arguments?.getString("is_negotiable"))
                        data.putString("location",etLocation?.text.toString())
                        if(stLatitude==null && stLongitude==null){
                            data.putString("lat",arguments?.getString("lat"))
                            data.putString("lng",arguments?.getString("lng"))
                        }else{
                            data.putString("lat",stLatitude)
                            data.putString("lng",stLongitude)
                        }
                        data.putString("mobile",etMobileNumber?.text.toString())
                        data.putString("name",arguments?.getString("name"))
                        data.putString("show_mobile",arguments?.getString("show_mobile"))
                        data.putString("show_location",arguments?.getString("show_location"))
                        data.putString("user_id",arguments?.getString("user_id"))
                        data.putString("stBoardName",  arguments?.getString("stBoardName"))
                        data.putString("stMediumName", arguments?.getString("stMediumName"))
                        data.putString("stClassName", arguments?.getString("stClassName"))
                        data.putString("stSubjectName",  arguments?.getString("stSubjectName"))
                        data.putString("stPublisherName",  arguments?.getString("stPublisherName"))
                        data.putString("stAuthorName",  arguments?.getString("stAuthorName"))
                        data.putString("gallery_img1",arguments?.getString("gallery_img1"))
                        data.putString("gallery_img2",arguments?.getString("gallery_img2"))
                        data.putString("img1path",arguments?.getString("img1path"))
                        data.putString("img2path",arguments?.getString("img2path"))
                        data.putString("image_type1",arguments?.getString("image_type1"))
                        data.putString("image_type2",arguments?.getString("image_type2"))
                        postNowFragment.arguments = data
                        addFragmentWithoutRemove(R.id.frame_container, postNowFragment, "PostNow")

                    }

                    else if(arguments?.getString("advertisement_type").equals("new")){
                        val postNowFragment: Fragment = PostNowFragment()
                        val data = Bundle()
                        data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                        data.putString("title",arguments?.getString("title"))
                        data.putString("stBoardId", arguments?.getString("stBoardId"))
                        data.putString("stMediumId", arguments?.getString("stMediumId"))
                        data.putString("stClassId", arguments?.getString("stClassId"))
                        data.putString("stSubjectId", arguments?.getString("stSubjectId"))
                        data.putString("stPublisherId", arguments?.getString("stPublisherId"))
                        data.putString("stAuthorId", arguments?.getString("stAuthorId"))
                        data.putString("stBoardName", arguments?.getString("stBoardName"))
                        data.putString("stMediumName", arguments?.getString("stMediumName"))
                        data.putString("stClassName", arguments?.getString("stClassName"))
                        data.putString("stSubjectName", arguments?.getString("stSubjectName"))
                        data.putString("stPublisherName", arguments?.getString("stPublisherName"))
                        data.putString("stAuthorName", arguments?.getString("stAuthorName"))
                        //data.putString("ettitle", arguments?.getString("ettitle"))
                        data.putString("desc", arguments?.getString("desc"))
                        data.putString("price", arguments?.getString("price"))
                        data.putString("is_negotiable", arguments?.getString("is_negotiable"))
                        data.putString("addcontactno", etMobileNumber?.text.toString())
                        data.putString("location", etLocation?.text.toString())
                        data.putString("gallery_img1",arguments?.getString("gallery_img1"))
                        data.putString("gallery_img2",arguments?.getString("gallery_img2"))
                        if(stLatitude==null && stLongitude==null){
                            data.putString("lat",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LATITUDE))
                            data.putString("lng",CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LONGITUDE))
                        }else{
                            data.putString("lat",stLatitude)
                            data.putString("lng",stLongitude)
                        }
                        Log.e(TAG,"location editext"+etLocation?.text.toString())
                        Log.e(TAG,"location editext"+stLatitude)
                        Log.e(TAG,"location editext"+stLongitude)
                        Log.e(TAG,"Common location editext"+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LATITUDE))
                        Log.e(TAG,"Common location editext"+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.LONGITUDE))
                        data.putString("img1path",arguments?.getString("img1path"))
                        data.putString("img2path",arguments?.getString("img2path"))
                        //data.putString("image_type",arguments?.getString("image_type"))
                        data.putString("image_type1","local")
                        data.putString("image_type2","local")
                       /* data.putString("img3path", arguments?.getString("img3path"))
                        data.putString("img4path", arguments?.getString("img4path"))*/

                        /* data.putByteArray("bitmap",arguments?.getByteArray("bitmap"));
                         data.putByteArray("bitmap2",arguments?.getByteArray("bitmap"));*/
                        /*data.putString("camera_img1", arguments?.getString("camera_img1"))
                        data.putString("gallery_img1",  arguments?.getString("gallery_img1"))
                        data.putString("camera_img2",  arguments?.getString("camera_img2"))
                        data.putString("gallery_img2",  arguments?.getString("gallery_img2"))*/
                        postNowFragment.arguments = data
                        addFragmentWithoutRemove(R.id.frame_container, postNowFragment, "PostNow")
                    }

                }
            }

            R.id.btn_prevcontactdetails->{
                /*val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
                val fragmentManager:FragmentManager=activity?.supportFragmentManager!!
                val count:Int=fragmentManager.backStackEntryCount
                fragmentManager.popBackStack()
            }


        }
    }

    fun isValidated(): Boolean {
        var mob: String = etMobileNumber?.text.toString()

        if (etMobileNumber?.text.toString().isEmpty()) {
            etMobileNumber?.error = "Mobile number required!"
            etMobileNumber?.requestFocus()
            return false
        } else if (mob.isEmpty() || mob.length != 10 || mob.startsWith("0") || mob.startsWith("1") || mob.startsWith("2") || mob.startsWith("3") || mob.startsWith("4") || mob.startsWith("5") || mob.startsWith("6")) {
            etMobileNumber?.error = "Invalid mobile number!"
            etMobileNumber?.requestFocus()
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE
            && resultCode == AppCompatActivity.RESULT_OK
        ) {
            //setImageViewWithImage()
            val place = data?.let {
                Autocomplete.getPlaceFromIntent(it)
            }

         //   Log.e("Datatat", "Place: " + place?.name)
           // Log.e("lat","Place: " + place?.latLng)

          //  LatLng= place?.latLng.toString()
/*
            latititue = LatLngla.toString()
            longitude = place?.latLng?.longitude.toString()*/
            Log.e("","lat: "+place?.latLng)
            Log.e("","log " + place?.latLng)

            var latLng:LatLng= place?.latLng!!
            stLatitude= latLng.latitude.toString()
            stLongitude= latLng.longitude.toString()

            Log.e("LatLong","Latitude $stLatitude"+"Longitude $stLongitude")

            location =place?.name
            etLocation?.setText(location)

        }
    }
}
