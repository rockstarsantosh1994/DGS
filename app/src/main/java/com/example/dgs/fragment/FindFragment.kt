package com.example.dgs.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgs.*
import com.example.dgs.adapter.*
import com.example.dgs.model.advertismentlist.AdvertsmentList
import com.example.dgs.model.advertismentlist.AdvetismentResponse
import com.example.dgs.model.masterdata.*
import com.google.gson.Gson
import io.paperdb.Paper


class FindFragment : Fragment()  {

    var rvFindData:RecyclerView? = null
    var toolbar:Toolbar?=null
    var createView:View?=null
    var TAG:String="FindFragment"
    var spinCategory:Spinner?=null
    var boardList:ArrayList<BoardList>?=null
    var classList:ArrayList<StandardList>?=null
    var mediumList:ArrayList<MediumList>?=null
    var subjectList:ArrayList<SubjectList>?=null
    var publisherList:ArrayList<PublisherList>?=null
    var authorList:ArrayList<AuthorList>?=null
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
    var tvCategoryType:TextView?=null
    var llNoInternet: LinearLayout?=null
    var llNoData: LinearLayout?=null
    var llNoServerFound: LinearLayout?=null
    var llRecyclerViewData: LinearLayout?=null
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    var findAdsAdapter:FindAdsAdapter?=null
    lateinit var loadMoreItemsCells: ArrayList<AdvertsmentList>
    lateinit var advertisement_list:ArrayList<AdvertsmentList>
    lateinit var advertismentPojo:AdvertsmentList

    var ll_main:LinearLayout?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView= inflater.inflate(R.layout.fragment_find, container, false)

        Paper.init(context)

        Log.e(TAG,"category_type"+arguments?.getString("category_type"))

        //Basic intialisation..
        initViews()

        //setDataToSpinner
        setDataToSpinners()

        return createView
    }

    var isLastPage: Boolean = false
    var isLoading: Boolean = false

    fun initViews() {

        advertisement_list=ArrayList<AdvertsmentList>()

        //recycler view intialisation...
        rvFindData = createView?.findViewById(R.id.rv_findData) as RecyclerView
        rvFindData?.layoutManager = GridLayoutManager(context, 2)

        //toolbar intialisation....
        toolbar = createView?.findViewById(R.id.toolbar_find) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Category Search"

        //LinearLayout intialisation...
        llNoData = createView?.findViewById(R.id.ll_nodata)
        llNoInternet = createView?.findViewById(R.id.ll_nointernet)
        llNoServerFound = createView?.findViewById(R.id.ll_servernotfound)
        llRecyclerViewData = createView?.findViewById(R.id.ll_recyclerview)


        //Spinner intialisation..
        spinCategory = createView?.findViewById(R.id.spin_category)

        //TextView intialsiation
        tvCategoryType = createView?.findViewById(R.id.tv_categorytype)
        if (arguments?.getString("category_type").equals("board")) {
            tvCategoryType?.text = "Find Board"
            loadAds()
        }
        if (arguments?.getString("category_type").equals("class")) {
            tvCategoryType?.text = "Find Class"
        }
        if (arguments?.getString("category_type").equals("subject")) {
            tvCategoryType?.text = "Find Subject"
        }
        if (arguments?.getString("category_type").equals("author")) {
            tvCategoryType?.text = "Find Author"
        }


        rvFindData?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView?.canScrollVertically(1)!!) {
                    //mPresenter.getData(++mPage, ANDROID)
                    e("Findfragment","size"+advertisement_list.size)
                    offsetVal=offsetVal?.plus(10)
                    e("Findfragment","offsetval"+offsetVal)
                    if(offsetVal!! > advertisement_list.size){
                        //Toast.makeText(context,"Limit exceed",Toast.LENGTH_SHORT).show()
                    }else{
                        loadAds()
                    }
                 }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
        })

    }



       /* rvFindData?.addOnScrollListener(object : RecyclerView.OnScrollListener(rvFindData?.layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call loadmore items to get more data
                getMoreItems()
            }
        })*/

    fun getMoreItems() {
        //after fetching your data assuming you have fetched list in your
        // recyclerview adapter assuming your recyclerview adapter is
        //rvAdapter
       // after getting your data you have to assign false to isLoading
        isLoading = false

        findAdsAdapter?.addData(advetismentResponse?.data?.advertisement_list!!)
    }

    var offsetVal:Int?=0

    var advetismentResponse:AdvetismentResponse?=null
    fun loadAds() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("DigiShare")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object:
            StringRequest(Request.Method.POST, ConfigUrl.VIEW_ADS_LIST, Response.Listener {
                    response->
                val gson= Gson()
                Log.e(TAG,"response $response")
                 advetismentResponse=gson.fromJson(response, AdvetismentResponse::class.java)

                if(advetismentResponse?.StatusCode.equals("1")) {

                    Log.e(TAG,"arraylist size  ${advetismentResponse?.data?.advertisement_list?.size}")
                    advertisement_list.addAll(advetismentResponse?.data?.advertisement_list!!)
                    e("FindFragment","List Size ${advertisement_list.size}")

                    if (!advertisement_list.isEmpty()!! ||
                        advertisement_list.size != 0) {
                        progressDialog.dismiss()
                        llRecyclerViewData?.visibility = View.VISIBLE
                        llNoData?.visibility = View.GONE
                        llNoServerFound?.visibility=View.GONE

                        if(offsetVal==0){
                            findAdsAdapter = context?.let {
                                FindAdsAdapter(it, advertisement_list!!,arguments?.getString("category_type").toString())
                            }
                            rvFindData!!.adapter = findAdsAdapter

                        }else{
                            findAdsAdapter?.notifyDataSetChanged()
                        }


                    } else {
                        progressDialog.dismiss()
                        llRecyclerViewData?.visibility = View.GONE
                        llNoData?.visibility = View.VISIBLE
                        llNoServerFound?.visibility=View.GONE
                        Toast.makeText(context, advetismentResponse?.StatusCode, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }, Response.ErrorListener {
                progressDialog.dismiss()
                llRecyclerViewData?.visibility=View.GONE
                llNoServerFound?.visibility=View.VISIBLE
                Log.e(TAG,"error $it")
            }){
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()

                params.put("medium_id","")
                if(arguments?.getString("category_type").equals("class")) {
                    params.put("standard_id",stClassId.toString())
                }else{
                    params.put("standard_id","0")
                }

                if(arguments?.getString("category_type").equals("board")){
                    params.put("board_id",stBoardId.toString())
                }else{
                    params.put("board_id","0")
                }

                if(arguments?.getString("category_type").equals("subject")) {
                    params.put("subject_id", stSubjectId.toString())
                }else{
                    params.put("subject_id","0")
                }
                if(arguments?.getString("category_type").equals("author")) {
                    params.put("author_id",stAuthorId.toString())
                }else{
                    params.put("author_id","0")
                }
                params.put("advertisement_id","")
                params.put("publisher_id","")
                params.put("user_id","")
                params.put("title","")
                params.put("offset",offsetVal.toString())
                params.put("location",arguments?.getString("location").toString())
                params.put("latitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LATITUDE).toString())
                params.put("longitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LONGITUDE).toString())

                Log.e(TAG,"params $params")
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(context)
        mQueue.add(stringRequest)
    }

  /*  private fun LoadMoreData() {
        //Add the Loading View
        findAdsAdapter.addLoadingView()
        //Create the loadMoreItemsCells Arraylist
        loadMoreItemsCells = ArrayList()
        //Get the number of the current Items of the main Arraylist
        val start = adapterLinear.itemCount
        //Load 16 more items
        val end = start + 16
        //Use Handler if the items are loading too fast.
        //If you remove it, the data will load so fast that you can't even see the LoadingView
        Handler().postDelayed({
            for (i in start..end) {
                //Get data and add them to loadMoreItemsCells ArrayList
                //loadMoreItemsCells.add("Item $i")
            }
            //Remove the Loading View
            adapterLinear.removeLoadingView()
            //We adding the data to our main ArrayList
            adapterLinear.addData(loadMoreItemsCells)
            //Change the boolean isLoading to false
            scrollListener.setLoaded()
            //Update the recyclerView in the main thread
            items_linear_rv.post {
                adapterLinear.notifyDataSetChanged()
            }
        }, 3000)

    }*/

    fun setDataToSpinners(){

        if(arguments?.getString("category_type").equals("class")) {
            //Class/Standard List Data.......
            classList = Paper.book().read("standard_list")
            var classListAdapter = context?.let { ClassListAdapter(it, classList) }
            spinCategory?.adapter = classListAdapter

            spinCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    stClassId = classList?.get(p2)?.standard_id
                    stClassName = classList?.get(p2)?.standard

                    Log.e("spintype ","class_name "+stClassName)

                    if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                        if (!stClassName.toString().equals("Select Class") || !stClassName.toString().equals("Select Standard")) {
                                advertisement_list.clear()
                                offsetVal = 0
                                //load Ads
                                loadAds()
                        } else {
                            advertisement_list.clear()
                            offsetVal = 0
                            loadAds()
                        }
                    }else{
                        Log.e("INELSE","INSELSE")
                        llRecyclerViewData?.visibility = View.GONE
                        llNoInternet?.visibility = View.VISIBLE
                        llNoServerFound?.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }

        else if(arguments?.getString("category_type").equals("board")) {
            Log.e("internetCheck",CommonMethods.isNetworkAvailable(context as AppCompatActivity).toString())
            //Board List Data.......
            boardList = Paper.book().read("board_list")
            Log.e(TAG, "board_List" + boardList)
            var boardListAdapter = context?.let { BoardListAdapter(it, boardList) }
            spinCategory?.adapter = boardListAdapter

            spinCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    stBoardId = boardList?.get(p2)?.board_id
                    stBoardName = boardList?.get(p2)?.board
                    if(!CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                        llRecyclerViewData?.visibility = View.GONE
                        llNoInternet?.visibility = View.VISIBLE
                        llNoServerFound?.visibility = View.GONE
                    }else{

                        if (!stBoardName.toString().equals("Select Board")) {
                            advertisement_list.clear()
                            offsetVal = 0
                            //load Ads
                            if(CommonMethods.isNetworkAvailable(context as AppCompatActivity))
                            {
                                advertisement_list.clear()
                                offsetVal = 0
                                loadAds()
                            }

                        } else {
                            if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                                advertisement_list.clear()
                                offsetVal = 0
                                loadAds()
                            }
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }


        else if(arguments?.getString("category_type").equals("subject")) {
            //Subject List Data.......
            subjectList = Paper.book().read("subject_list")
            var subjectListAdapter = context?.let { SubjectListAdapter(it, subjectList) }
            spinCategory?.adapter = subjectListAdapter

            spinCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    stSubjectId = subjectList?.get(p2)?.subject_id
                    stSubjectName = subjectList?.get(p2)?.subject
                    if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                        if (!stSubjectName.toString().equals("Select Subject")) {
                                advertisement_list.clear()
                                offsetVal = 0
                                //load Ads
                                loadAds()
                        } else {
                            advertisement_list.clear()
                            offsetVal = 0
                            loadAds()
                        }
                    }else {
                        llRecyclerViewData?.visibility = View.GONE
                        llNoInternet?.visibility = View.VISIBLE
                        llNoServerFound?.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }
        else if(arguments?.getString("category_type").equals("author")) {
            //Author List Data.....
            authorList = Paper.book().read("author_list")
            var authorListAdapter = context?.let { AuthorListAdapter(it, authorList) }
            spinCategory?.adapter = authorListAdapter

            spinCategory?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    stAuthorId = authorList?.get(p2)?.author_id
                    stAuthorName = authorList?.get(p2)?.author
                    Toast.makeText(context,"Subject name"+stAuthorName,Toast.LENGTH_SHORT).show()
                    if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)) {
                        if (!stAuthorName.toString().equals("Select Author")) {
                                advertisement_list.clear()
                                offsetVal = 0
                                //load Ads
                                loadAds()
                        } else {
                            advertisement_list.clear()
                            offsetVal = 0
                            //load Ads
                            loadAds()
                        }
                    }else{
                        llRecyclerViewData?.visibility = View.GONE
                        llNoInternet?.visibility = View.VISIBLE
                        llNoServerFound?.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        }
    }
}
