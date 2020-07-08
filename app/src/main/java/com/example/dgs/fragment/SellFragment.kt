package com.example.dgs.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.dgs.R
import com.example.dgs.adapter.*
import com.example.dgs.fragment.selltab.UploadPicturesFragment
import com.example.dgs.model.masterdata.*
import com.example.dgs.utility.BaseFragmet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import io.paperdb.Paper


class SellFragment : BaseFragmet(),View.OnClickListener {

    var toolbar:androidx.appcompat.widget.Toolbar?=null
    var create_view: View? = null
    var btnNextBookDetails:FloatingActionButton?=null
    var boardList:ArrayList<BoardList>?=null
    var classList:ArrayList<StandardList>?=null
    var mediumList:ArrayList<MediumList>?=null
    var subjectList:ArrayList<SubjectList>?=null
    var publisherList:ArrayList<PublisherList>?=null
    var authorList:ArrayList<AuthorList>?=null
    var spinSelectBoard: Spinner?=null
    var spinSelectMedium: Spinner?=null
    var spinSelectClass: Spinner?=null
    var spinSelectSubject: Spinner?=null
    var spinSelectPublication: Spinner?=null
    var spinSelectAuthor: Spinner?=null
    var stBoardId:String?=null
    var stMediumId:String?=null
    var stClassId:String?=null
    var stSubjectId:String?=null
    var stPublisherId:String?=null
    var stAuthorId:String?=null
    var stBoardName:String?=null
    var stMediumName:String?=null
    var stClassName:String?=null
    var stSubjectName:String?=null
    var stPublisherName:String?=null
    var stAuthorName:String?=null
    val TAG:String="SellFragment"
    var etTitle:TextInputEditText?=null
    var etDesc:TextInputEditText?=null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        create_view=inflater.inflate(R.layout.fragment_sell, container, false)

        //Basic intialisation..
        initView()

        //Toast.makeText(context,"advertisement_type"+arguments?.getString("advertisement_type"),Toast.LENGTH_LONG).show()

        Paper.init(context)

        //setAllAdapters attaching data to spinners....
        setDataToSpinners()

        Log.e(TAG,"When Update \n img1"+arguments?.getString("img1")+"\nimg 2"+arguments?.getString("img2")
        +" image 1 Id "+arguments?.getString("img1_id")+"image 2 id"+arguments?.getString("img2_id"))

        Log.e(TAG,"is_negotiable "+arguments?.getString("is_negotiable"))
        Log.e(TAG,"lattitude "+arguments?.getString("lat"))
        Log.e(TAG,"longitude "+arguments?.getString("lng"))
        Log.e(TAG,"location "+arguments?.getString("location"))
     /*   Toast.makeText(context,arguments?.getString("title"),Toast.LENGTH_LONG).show()
        Toast.makeText(context,arguments?.getString("title"),Toast.LENGTH_LONG).show()
       */ //Is user want to update advetisement
        if(arguments?.getString("advertisement_type").equals("update")){
            setupdateAdvertismentData()
        }
        return create_view
    }

    fun initView(){

        //Toolbar intialisation..
        toolbar= this.create_view!!.findViewById(R.id.toolbar_sell) as androidx.appcompat.widget.Toolbar
        val activity = activity as AppCompatActivity?
        activity?.setSupportActionBar(toolbar)
        activity?.supportActionBar?.title = "Sell"


        //FloatingActionButton intialisation...
        btnNextBookDetails=create_view!!.findViewById(R.id.btn_nextbookdetails) as FloatingActionButton

        //setOnClickListener intialisation..
        btnNextBookDetails?.setOnClickListener(this)

        //Spinner intialisation...
        spinSelectBoard=create_view?.findViewById(R.id.spin_selectboard)
        spinSelectMedium=create_view?.findViewById(R.id.spin_selectmedium)
        spinSelectClass=create_view?.findViewById(R.id.spin_selectclass)
        spinSelectSubject=create_view?.findViewById(R.id.spin_select_book_subject_wise)
        spinSelectPublication=create_view?.findViewById(R.id.spin_select_publications)
        spinSelectAuthor=create_view?.findViewById(R.id.spin_select_author)

        //Edittext intialisation..
        etTitle=create_view?.findViewById(R.id.et_addtitle)
        etDesc=create_view?.findViewById(R.id.et_add_description)


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_nextbookdetails->{
                if(isValidated()){
                    Log.e("click","Clicked")
                    if(arguments?.getString("advertisement_type").equals("update")){
                        val uploadPictureFragment: Fragment = UploadPicturesFragment()
                        val data = Bundle() //Use bundle to pass data
                        data.putString("advertisement_id",arguments?.getString("advertisement_id"))
                        data.putString("title",arguments?.getString("title"))
                        data.putString("description",etDesc?.text.toString())
                        data.putString("board_id", stBoardId)
                        data.putString("medium_id", stMediumId)
                        data.putString("standard_id",stClassId)
                        data.putString("subject_id", stSubjectId)
                        data.putString("publisher_id",stPublisherId)
                        data.putString("author_id",stAuthorId)
                        data.putString("price", arguments?.getString("price"))
                        data.putString("is_negotiable",arguments?.getString("is_negotiable"))
                        data.putString("location",arguments?.getString("location"))
                        data.putString("lat",arguments?.getString("lat"))
                        data.putString("lng",arguments?.getString("lng"))
                        data.putString("mobile",arguments?.getString("mobile"))
                        data.putString("name",arguments?.getString("name"))
                        data.putString("show_mobile",arguments?.getString("show_mobile"))
                        data.putString("show_location",arguments?.getString("show_location"))
                        data.putString("user_id",arguments?.getString("user_id"))
                        data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                        data.putString("stBoardName", stBoardName)
                        data.putString("stMediumName",stMediumName)
                        data.putString("stClassName",stClassName)
                        data.putString("stSubjectName", stSubjectName)
                        data.putString("stPublisherName", stPublisherName)
                        data.putString("stAuthorName", stAuthorName)
                        data.putString("img1", arguments?.getString("img1"))
                        data.putString("img1_id",arguments?.getString("img1_id"))
                        data.putString("img2", arguments?.getString("img2"))
                        data.putString("img2_id",arguments?.getString("img2_id"))
                        //data.putSerializable("images",arguments?.getSerializable("images"))
                        uploadPictureFragment.arguments = data
                        addFragmentWithoutRemove(R.id.frame_container,uploadPictureFragment, "UploadPictures")
                    }
                    else if(arguments?.getString("advertisement_type").equals("new")){
                        val uploadPictureFragment: Fragment = UploadPicturesFragment()
                        val data = Bundle() //Use bundle to pass data
                        data.putString("stBoardId", stBoardId)
                        data.putString("stMediumId",stMediumId)
                        data.putString("stClassId", stClassId)
                        data.putString("stSubjectId", stSubjectId)
                        data.putString("stPublisherId", stPublisherId)
                        data.putString("stAuthorId", stAuthorId)
                        data.putString("stBoardName", stBoardName)
                        data.putString("stMediumName",stMediumName)
                        data.putString("stClassName",stClassName)
                        data.putString("stSubjectName", stSubjectName)
                        data.putString("stPublisherName", stPublisherName)
                        data.putString("stAuthorName", stAuthorName)
                        data.putString("title",etTitle?.text.toString())
                        data.putString("desc",etDesc?.text.toString())
                        data.putString("advertisement_type",arguments?.getString("advertisement_type"))
                        uploadPictureFragment.arguments = data
                        //addFragmentWithoutRemove(R.id.frame_container,uploadPictureFragment, "UploadPictures")
                        replaceFragmentWithBack(R.id.frame_container,uploadPictureFragment, "UploadPictures","Sell")
                        Log.e("stack","Sell Fragment")
                        //replaceFragmentWithBack(R.id.frame_container,uploadPictureFragment, "UploadPictures","SellFragment")
                    }
                }
            }
        }
    }

    fun setDataToSpinners(){
        //Board List Data.......
        boardList= Paper.book().read("board_list")
        Log.e(TAG,"board_List"+boardList)
        var boardListAdapter= context?.let { BoardListAdapter(it,boardList) }
        spinSelectBoard?.adapter=boardListAdapter

        spinSelectBoard?.onItemSelectedListener= object:AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stBoardId=boardList?.get(p2)?.board_id
                stBoardName=boardList?.get(p2)?.board
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }

        //Medium List Data.......
        mediumList=Paper.book().read("medium_list")
        var mediumListAdapter=context?.let { MediumListAdapter(it,mediumList) }
        spinSelectMedium?.adapter=mediumListAdapter

        spinSelectMedium?.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stMediumId=mediumList?.get(p2)?.medium_id
                stMediumName=mediumList?.get(p2)?.medium
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        //Class/Standard List Data.......
        classList=Paper.book().read("standard_list")
        var classListAdapter=context?.let { ClassListAdapter(it,classList) }
        spinSelectClass?.adapter=classListAdapter

        spinSelectClass?.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stClassId=classList?.get(p2)?.standard_id
                stClassName=classList?.get(p2)?.standard
            }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        //Subject List Data.......
        subjectList=Paper.book().read("subject_list")
        var subjectListAdapter=context?.let { SubjectListAdapter(it,subjectList) }
        spinSelectSubject?.adapter=subjectListAdapter

        spinSelectSubject?.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stSubjectId=subjectList?.get(p2)?.subject_id
                stSubjectName=subjectList?.get(p2)?.subject
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        //Publisher List Data.....
        publisherList=Paper.book().read("publisher_list")
        var publicationListAdapter=context?.let { PublicationListAdapter(it,publisherList) }
        spinSelectPublication?.adapter=publicationListAdapter

        spinSelectPublication?.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stPublisherId=publisherList?.get(p2)?.publisher_id
                stPublisherName=publisherList?.get(p2)?.publisher
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        //Author List Data.....
        authorList=Paper.book().read("author_list")
        var authorListAdapter=context?.let { AuthorListAdapter(it,authorList) }
        spinSelectAuthor?.adapter=authorListAdapter

        spinSelectAuthor?.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                stAuthorId=authorList?.get(p2)?.author_id
                stAuthorName=authorList?.get(p2)?.author
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
    }


    fun setupdateAdvertismentData(){
        if(!arguments?.getString("title").equals("")){
            etTitle?.setText(arguments?.getString("title"))
        }
        if(!arguments?.getString("description").equals("")){
            etDesc?.setText(arguments?.getString("description"))
        }
        if(!arguments?.getString("board_id").equals("")){
            boardList= Paper.book().read("board_list")
            var selectedDeptIndex=0
            for(i in boardList!!.indices){
                if(boardList?.get(i)?.board_id.equals(arguments?.getString("board_id")))
                {
                    selectedDeptIndex=i
                }
            }
            spinSelectBoard?.setSelection(selectedDeptIndex)
        }

        if(!arguments?.getString("medium_id").equals("")){
            mediumList=Paper.book().read("medium_list")
            var selectedDeptIndex=0
            for(i in mediumList!!.indices){
                if(mediumList?.get(i)?.medium_id.equals(arguments?.getString("medium_id"))){
                    selectedDeptIndex=i
                }
            }
            spinSelectMedium?.setSelection(selectedDeptIndex)
        }

        if(!arguments?.getString("standard_id").equals("")){
            classList=Paper.book().read("standard_list")
            var selectedDeptIndex=0
            for(i in classList!!.indices){
                if(classList?.get(i)?.standard_id.equals(arguments?.getString("standard_id"))){
                    selectedDeptIndex=i
                }
            }
            spinSelectClass?.setSelection(selectedDeptIndex)
        }

        if(!arguments?.getString("subject_id").equals("")){
            subjectList=Paper.book().read("subject_list")
            var selectedDeptIndex=0
            for(i in subjectList!!.indices){
                if(subjectList?.get(i)?.subject_id.equals(arguments?.getString("subject_id"))){
                    selectedDeptIndex=i
                }
            }
            spinSelectSubject?.setSelection(selectedDeptIndex)
        }

        if(!arguments?.getString("publisher_id").equals("")){
            publisherList=Paper.book().read("publisher_list")
            var selectedDeptIndex=0
            for(i in publisherList!!.indices){
                if(publisherList?.get(i)?.publisher_id.equals(arguments?.getString("publisher_id"))){
                    selectedDeptIndex=i
                }
            }
            spinSelectPublication?.setSelection(selectedDeptIndex)
        }

        if(!arguments?.getString("author_id").equals("")){
            authorList=Paper.book().read("author_list")
            var selectedDeptIndex=0
            for(i in authorList!!.indices){
                if(authorList?.get(i)?.author_id.equals(arguments?.getString("author_id"))){
                    selectedDeptIndex=i
                }
            }
            spinSelectAuthor?.setSelection(selectedDeptIndex)
        }

    }
    fun isValidated():Boolean{

        if(etTitle?.text.toString().isEmpty()){
            etTitle?.error = "Title required!"
            etTitle?.requestFocus()
            return false
        }/*else if(etDesc?.text.toString().isEmpty()){
            etDesc?.error = "Description required!"
            etDesc?.requestFocus()
            return false
        }else if(stBoardName.toString().equals("Select Board")){
            Toast.makeText(context,"Please select board!",Toast.LENGTH_SHORT).show()
            return false
        }else if(stMediumName.toString().equals("Select Medium")){
            Toast.makeText(context,"Please select medium!",Toast.LENGTH_SHORT).show()
            return false
        }else if(stClassName.toString().equals("Select Class")){
            Toast.makeText(context,"Please select Class!",Toast.LENGTH_SHORT).show()
            return false
        }else if(stClassName.toString().equals("Select Standard")){
            Toast.makeText(context,"Please select Class!",Toast.LENGTH_SHORT).show()
            return false
        }else if(stSubjectName.toString().equals("Select Subject")){
            Toast.makeText(context,"Please select subject!",Toast.LENGTH_SHORT).show()
            return false
        }else if(stPublisherName.toString().equals("Select Publisher")){
            Toast.makeText(context,"Please select publisher!",Toast.LENGTH_SHORT).show()
            return false
        }else if(stAuthorName.toString().equals("Select Author")){
            Toast.makeText(context,"Please select author!",Toast.LENGTH_SHORT).show()
            return false
        }*/
        return true
    }
}
