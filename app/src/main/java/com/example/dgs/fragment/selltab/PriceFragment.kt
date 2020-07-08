package com.example.dgs.fragment.selltab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dgs.R
import com.example.dgs.utility.BaseFragmet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class PriceFragment : BaseFragmet() ,View.OnClickListener{

    var btnPrice: FloatingActionButton? = null
    var btnPrev:FloatingActionButton?=null
    var toolbar:Toolbar?=null
    val TAG:String="PriceFragment"
    var etPrice:TextInputEditText?=null
    var chNegotiable:CheckBox?=null
    var createView:View?=null
    var stChNegotiable:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView= inflater.inflate(R.layout.activity_price, container, false)

        //basic intilaisation...
        initViews()

       // Toast.makeText(context,"advertisement_type"+arguments?.getString("advertisement_type"),Toast.LENGTH_LONG).show()
        Log.e(TAG, "advertisement type ${arguments?.getString("advertisement_type")} \nMediumId ${arguments?.getString("stMediumId")} " +
                    "\n ClassId ${arguments?.getString("stClassId")} \nSubjectId ${arguments?.getString("stSubjectId")}" +
                    " \nPublisher ID${arguments?.getString("stPublisherId")} \nAuthorId ${arguments?.getString("stAuthorId")}" +
                    "\n Title ${arguments?.getString("ettitle")} \n Desc ${arguments?.getString("desc")} \nAuthor name ${arguments?.getString("stAuthorName")}")


        Log.e(TAG, "When Updater parameters are comming \n advertisement type  ${arguments?.getString("advertisement_type")} \nMediumId ${arguments?.getString("medium_id")} " +
                "\n ClassId ${arguments?.getString("standard_id")} \nSubjectId ${arguments?.getString("subject_id")}" +
                " \nPublisher ID${arguments?.getString("publisher_id")} \nAuthorId ${arguments?.getString("author_id")}" +
                "\n Price ${arguments?.getString("price")} \n Desc ${arguments?.getString("desc")} \nAuthor name ${arguments?.getString("stAuthorName")}")

        /*  Log.e(TAG, "\ngallery_img1"+arguments?.getByteArray("gallery_img1")+
                  "\ngallery_img2"+arguments?.getByteArray("gallery_img2"))
  */
        Log.e(TAG,"camera imageeee"+arguments?.getByteArray("bitmap")+
                "\ncamera_img2"+arguments?.getByteArray("bitmap"))

        Log.e(TAG,"is_negotiable "+arguments?.getString("is_negotiable"))
        Log.e(TAG,"lattitude "+arguments?.getString("lat"))
        Log.e(TAG,"longitude "+arguments?.getString("lng"))
        Log.e(TAG,"location "+arguments?.getString("location"))

        return createView

    }

    fun initViews(){

        //btn intialisation...
        btnPrice= createView?.findViewById(R.id.btn_pricedetails) as FloatingActionButton
        btnPrice?.setOnClickListener(this)
        btnPrev=createView?.findViewById(R.id.btn_prevpricedetails) as FloatingActionButton
        btnPrev?.setOnClickListener(this)

        //toolbar intialisation..
        toolbar=createView?.findViewById(R.id.toolbar_price) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Price"

        //CheckBox intialisation...
        chNegotiable=createView?.findViewById(R.id.ch_negotiable)
        chNegotiable?.setOnClickListener(this)

        //TextInputEditText intialisation...
        etPrice=createView?.findViewById(R.id.et_price)

        if(arguments?.getString("advertisement_type").equals("update")){
           if(!arguments?.getString("price").equals("")){
               etPrice?.setText(arguments?.getString("price"))
           }
           if(!arguments?.getString("is_negotiable").equals("")){
               chNegotiable?.isChecked = arguments?.getString("is_negotiable").equals("1")
           }
        }
        if(chNegotiable?.isChecked==true){
            stChNegotiable="1"
        }else {
            stChNegotiable="0"
        }
        //Toast.makeText(context,"st_negotiable" +stChNegotiable,Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_pricedetails->{
                if(isValidated()) {
                    if(arguments?.getString("advertisement_type").equals("update",false)){
                        var contactDetailsFragment: Fragment =ContactDetailsFragment()
                        val data = Bundle() //Use bundle to pass data
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
                        data.putString("price",etPrice?.text.toString())
                        data.putString("is_negotiable",stChNegotiable)
                        data.putString("location",arguments?.getString("location"))
                        data.putString("lat",arguments?.getString("lat"))
                        data.putString("lng",arguments?.getString("lng"))
                        data.putString("mobile",arguments?.getString("mobile"))
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
                        /*data.putString("image_type_server2",arguments?.getString("image_type_server"))
                        data.putString("image_type_local2",arguments?.getString("image_type_local"))*/
                        contactDetailsFragment.arguments = data
                        addFragmentWithoutRemove(R.id.frame_container, contactDetailsFragment, "Contact Details")

                    }

                    else if(arguments?.getString("advertisement_type").equals("new",false)){
                        val contactDetailsFragment = ContactDetailsFragment()
                        val data = Bundle()
                       // Toast.makeText(context,"When sending from if condition  advertisement_type "+arguments?.getString("advertisement_type"),Toast.LENGTH_LONG).show()
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
                        data.putString("ettitle", arguments?.getString("ettitle"))
                        data.putString("desc", arguments?.getString("desc"))
                        data.putString("price", etPrice?.text.toString())
                        data.putString("is_negotiable", stChNegotiable)
                        data.putString("gallery_img1",arguments?.getString("gallery_img1"))
                        data.putString("gallery_img2",arguments?.getString("gallery_img2"))
                        data.putString("img1path",arguments?.getString("img1path"))
                        data.putString("img2path",arguments?.getString("img2path"))
                        data.putString("image_type1","local")
                        data.putString("image_type2","local")
                       /* data.putString("img3path", arguments?.getString("img3path"))
                        data.putString("img4path", arguments?.getString("img4path"))*/


                        /*data.putByteArray("bitmap",arguments?.getByteArray("bitmap"));
                        data.putByteArray("bitmap2",arguments?.getByteArray("bitmap"));*/
                        contactDetailsFragment.arguments = data
                        addFragmentWithoutRemove(R.id.frame_container, contactDetailsFragment, "Contact Details")
                    }

/*
                    data.putByteArray("camera_img2",arguments?.getByteArray("camera_img2"))
                    data.putByteArray("gallery_img2",arguments?.getByteArray("gallery_img2"))
                    data.putByteArray("camera_img2",arguments?.getByteArray("camera_img2"))
*/                  }

            }

            R.id.btn_prevpricedetails->{
               /* val fragmentManager: FragmentManager = activity?.supportFragmentManager!!
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);*/
                val fragmentManager:FragmentManager=activity?.supportFragmentManager!!
                fragmentManager.popBackStack()
            }

            R.id.ch_negotiable->{
                    if(chNegotiable?.isChecked!!){
                        stChNegotiable="1"
                        //Toast.makeText(context,""+stChNegotiable.toString(),Toast.LENGTH_SHORT).show()
                    }else
                    {
                        stChNegotiable="0"
                       // Toast.makeText(context,""+stChNegotiable.toString(),Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun isValidated():Boolean{

        if(etPrice?.text.toString().equals("")){
            etPrice?.error = "Price required!"
            etPrice?.requestFocus()
            return false
        }
        return true
    }
}
