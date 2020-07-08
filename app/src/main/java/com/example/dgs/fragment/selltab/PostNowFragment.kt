package com.example.dgs.fragment.selltab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.example.dgs.utility.BaseFragmet
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostNowFragment :BaseFragmet(),View.OnClickListener {

    var btnPostNow: AppCompatButton? = null
    var btnPrev: AppCompatButton? = null
    var toolbar:Toolbar?=null
    val TAG:String="PostNowFragment"
    var createView:View?=null
    var etName:EditText?=null
    var chShowMyPhone:CheckBox?=null
    var chShowMyLocation:CheckBox?=null
    var stShowMyLocation:String?=null
    var stShowMyPhone:String?=null
    var ivNavProfilePic: CircleImageView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        createView = inflater.inflate(R.layout.activity_post_now, container, false)

        //Basic intialsiation...
        initViews()

        Log.e(TAG, "Boardid ${arguments?.getString("stBoardId")} \nMediumId ${arguments?.getString("stMediumId")} " +
                "\n ClassId ${arguments?.getString("stClassId")} \nSubjectId ${arguments?.getString("stSubjectId")}" +
                " \nPublisher ID${arguments?.getString("stPublisherId")} \nAuthorId ${arguments?.getString("stAuthorId")}" +
                "\n Title ${arguments?.getString("ettitle")} \n Desc ${arguments?.getString("desc")}")


        Log.e(TAG,"location"+arguments?.getString("location"))
        Log.e(TAG,"location"+arguments?.getString("lat"))
        Log.e(TAG,"location"+arguments?.getString("lng"))
        Log.e(TAG,"profile_pic"+CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC))

        return createView
    }

    fun initViews(){
        //Button intialisation....
        btnPostNow=createView?.findViewById(R.id.btn_postnow) as AppCompatButton
        btnPostNow?.setOnClickListener(this)
        btnPrev=createView?.findViewById(R.id.btn_prevpostnow1)
        btnPrev?.setOnClickListener(this)

        //Toolbar intialisation...
        toolbar=createView?.findViewById(R.id.toolbar_postnow) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Post now"

        //Edittext intialisation...
        etName=createView?.findViewById(R.id.et_yourname)
        if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("f")){
            etName?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.FACEBOOK_NAME))
            etName?.isEnabled=false
        }else if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("g")){
            etName?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.GMAIL_NAME))
            etName?.isEnabled=false
        }else if(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("e")){
            etName?.setText(CommonMethods.getPrefrence(activity as AppCompatActivity,AllKeys.EMAIL_NAME))
            etName?.isEnabled=false
        }

        /*set img*/
        ivNavProfilePic=createView?.findViewById(R.id.iv_profilepic)

        if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("g")){

            if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC).equals("")){
                Picasso.with(context as AppCompatActivity).load(R.drawable.user).into(ivNavProfilePic)
            }else{
                Picasso.with(context as AppCompatActivity).load(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.GMAIL_PIC)).into(ivNavProfilePic)
            }

        }else if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("f") ){

            if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC).equals("")){
                Picasso.with(context as AppCompatActivity).load(R.drawable.user).into(ivNavProfilePic)
            }else{
                Picasso.with(context as AppCompatActivity).load(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.FACEBOOK_PIC)).into(ivNavProfilePic)
            }

        }else if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.MEDIA_TYPE).equals("e")){
            if(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_PIC).equals(AllKeys.DNF) ||
                CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_PIC).equals("")){
                Picasso.with(context as AppCompatActivity).load(R.drawable.user).into(ivNavProfilePic)
            }else{
                Picasso.with(context as AppCompatActivity).load(CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.EMAIL_PIC)).into(ivNavProfilePic)
            }

        }

        //Checkbox intialisation..
        chShowMyPhone=createView?.findViewById(R.id.ch_showmyphonenoads)
        chShowMyLocation=createView?.findViewById(R.id.ch_showmylocationonads)
        chShowMyLocation?.isChecked=true
        chShowMyPhone?.setOnClickListener(this)
        chShowMyLocation?.setOnClickListener(this)

        //check show my phone condition
        chShowMyPhone?.isChecked = CommonMethods.getPrefrence(context as AppCompatActivity,AllKeys.SHOW_MOBILE_VISIBLITY).equals("1")
        //Toast.makeText(context,"Value of checkbox "+chShowMyPhone?.isChecked,Toast.LENGTH_LONG).show()

        if(chShowMyPhone?.isChecked==true){
            stShowMyPhone="1"
        }else{
            stShowMyPhone="0"
        }

        if(chShowMyLocation?.isChecked==true){
            stShowMyLocation="1"
        }else{
            stShowMyLocation="0"
        }

        if(arguments?.getString("advertisement_type").equals("update")){
            if(!arguments?.getString("name").equals("")){
                etName?.setText(arguments?.getString("name"))
            }
            if(!arguments?.getString("show_mobile").equals("")){
                chShowMyPhone?.isChecked = arguments?.getString("show_mobile").equals("1")
            }
            if(!arguments?.getString("show_location").equals("")){
                chShowMyLocation?.isChecked = arguments?.getString("show_location").equals("1")
            }
        }

        if(chShowMyPhone?.isChecked==true){
            stShowMyPhone="1"
        }else{
            stShowMyPhone="0"
        }
        //Toast.makeText(context,"Show my phone" +stShowMyPhone,Toast.LENGTH_SHORT).show()


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_postnow->{
                if(arguments?.getString("advertisement_type").equals("update")){
                    val preViewFragment = PreViewFragment()
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
                    data.putString("location",arguments?.getString("location"))
                    data.putString("lat",arguments?.getString("lat"))
                    data.putString("lng",arguments?.getString("lng"))
                    data.putString("mobile",arguments?.getString("mobile"))
                    data.putString("name",etName?.text.toString())
                    data.putString("show_mobile",stShowMyPhone)
                    data.putString("show_location",stShowMyLocation)
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
                    preViewFragment.arguments = data
                    addFragmentWithoutRemove(R.id.frame_container,preViewFragment, "PreView")
                }

                else if(arguments?.getString("advertisement_type").equals("new")){
                    val preViewFragment = PreViewFragment()
                    val data = Bundle()
                    data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                    data.putString("title",arguments?.getString("title"))
                    data.putString("stBoardId", arguments?.getString("stBoardId"))
                    data.putString("stMediumId", arguments?.getString("stMediumId"))
                    data.putString("stClassId", arguments?.getString("stClassId"))
                    data.putString("stSubjectId", arguments?.getString("stSubjectId"))
                    data.putString("stPublisherId", arguments?.getString("stPublisherId"))
                    data.putString("stAuthorId", arguments?.getString("stAuthorId"))
                    data.putString("stBoardName",  arguments?.getString("stBoardName"))
                    data.putString("stMediumName", arguments?.getString("stMediumName"))
                    data.putString("stClassName", arguments?.getString("stClassName"))
                    data.putString("stSubjectName",  arguments?.getString("stSubjectName"))
                    data.putString("stPublisherName",  arguments?.getString("stPublisherName"))
                    data.putString("stAuthorName",  arguments?.getString("stAuthorName"))
                    //data.putString("ettitle", arguments?.getString("ettitle"))
                    data.putString("desc", arguments?.getString("desc"))
                    data.putString("price", arguments?.getString("price"))
                    data.putString("is_negotiable", arguments?.getString("is_negotiable"))
                    data.putString("addcontactno", arguments?.getString("addcontactno"))
                    data.putString("etyourname",etName?.text.toString())
                    data.putString("show_mobile",stShowMyPhone)
                    data.putString("show_location",stShowMyLocation)
                    data.putString("gallery_img1",arguments?.getString("gallery_img1"))
                    data.putString("gallery_img2",arguments?.getString("gallery_img2"))
                    data.putString("location",arguments?.getString("location"))
                    data.putString("lat",arguments?.getString("lat"))
                    data.putString("lng",arguments?.getString("lng"))

                    data.putString("img1path",arguments?.getString("img1path"))
                    data.putString("img2path",arguments?.getString("img2path"))
                    //data.putString("image_type",arguments?.getString("image_type"))
                    data.putString("image_type1","local")
                    data.putString("image_type2","local")

                    preViewFragment.arguments = data

                    addFragmentWithoutRemove(R.id.frame_container,preViewFragment, "PreView")
                }

            }

            R.id.ch_showmylocationonads->{
                if(chShowMyLocation?.isChecked==true){
                    stShowMyLocation="1"
                }else{
                    stShowMyLocation="0"
                }

            }

            R.id.ch_showmyphonenoads->{
               // Toast.makeText(context,"Show_1 my phone " +stShowMyPhone,Toast.LENGTH_SHORT).show()
                if(chShowMyPhone?.isChecked==true){
                    stShowMyPhone="1"
                }else{
                    stShowMyPhone="0"
                }
            }

            R.id.btn_prevpostnow1->{
                Toast.makeText(context,"Clicked",Toast.LENGTH_SHORT).show()
                val fragmentManager:FragmentManager=activity?.supportFragmentManager!!
                //val count:Int=fragmentManager.backStackEntryCount
                fragmentManager.popBackStack()
            }
        }
    }

}
