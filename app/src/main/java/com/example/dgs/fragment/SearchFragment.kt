package com.example.dgs.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
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
import com.example.dgs.adapter.FindAdsAdapter
import com.example.dgs.model.advertismentlist.AdvetismentResponse
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson

class SearchFragment : Fragment() {

    var createView:View?=null
    var etSearch:TextInputEditText?=null
    var rvSearchBooks:RecyclerView?=null
    var TAG:String?="SearchFragment"
    var advetismentResponse:AdvetismentResponse?=null
    var llNoInternet: LinearLayout?=null
    var llNoData: LinearLayout?=null
    var llNoServerFound: LinearLayout?=null
    var llRecyclerViewData: LinearLayout?=null
    var toolbar: Toolbar? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        createView=inflater.inflate(R.layout.fragment_search, container, false)

        //Basic intialisation...
        initViews()

        //load ads based on search
        if(CommonMethods.isNetworkAvailable(context as AppCompatActivity)){
            //load Ads
            loadAds()
        }else{
            llRecyclerViewData?.visibility=View.GONE
            llNoInternet?.visibility=View.VISIBLE
        }

        etSearch?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                //filterBook(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        etSearch?.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                if(etSearch?.text.toString().equals("")){
                       etSearch?.setError("Invalid search!")
                       etSearch?.requestFocus()
                }else{
                    loadAds()
                }
                true
            } else {
                false
            }
        }

        return createView
    }

    fun initViews(){
        //Toolbar intialisation...
        toolbar = createView?.findViewById(R.id.toolbar_search) as Toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).title = "Search"

        //EditText intialisation....
        etSearch=createView?.findViewById(R.id.et_searchbook)
        etSearch?.setText(arguments?.getString("search_string"))

        //RecyclerView intialisation...
        rvSearchBooks=createView?.findViewById(R.id.rv_searchbooks)
        rvSearchBooks?.layoutManager = GridLayoutManager(context,2)

        //LinearLayout intialisation...
        llNoData=createView?.findViewById(R.id.ll_nodata)
        llNoInternet=createView?.findViewById(R.id.ll_nointernet)
        llNoServerFound=createView?.findViewById(R.id.ll_servernotfound)
        llRecyclerViewData=createView?.findViewById(R.id.ll_recyclerview)

    }

    fun loadAds() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("DigiShare")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val stringRequest=object: StringRequest(Request.Method.POST, ConfigUrl.VIEW_ADS_LIST, Response.Listener {
                    response->
                val gson= Gson()
                Log.e(TAG,"response $response")
                advetismentResponse=gson.fromJson(response, AdvetismentResponse::class.java)

                if(advetismentResponse?.StatusCode.equals("1")) {
                    if (advetismentResponse?.data?.advertisement_list?.size != 0) {
                        progressDialog.dismiss()
                        llRecyclerViewData?.visibility = View.VISIBLE
                        llNoData?.visibility = View.GONE
                        var findAdsActivity = context?.let {
                            advetismentResponse?.data?.advertisement_list?.let { it1 ->
                                FindAdsAdapter(it,
                                    it1,"other")
                            }
                        }
                        rvSearchBooks!!.adapter = findAdsActivity

                    } else {
                        progressDialog.dismiss()
                        llRecyclerViewData?.visibility = View.GONE
                        llNoData?.visibility = View.VISIBLE
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
                params.put("standard_id","")
                params.put("board_id","")
                params.put("subject_id","")
                params.put("author_id","")
                params.put("advertisement_id","")
                params.put("publisher_id","")
                params.put("user_id","")
                params.put("title",etSearch?.text.toString())
                params.put("offset","0")
                params.put("location",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.CURRENT_ADDRESS).toString())
                params.put("latitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LATITUDE).toString())
                params.put("longitude",CommonMethods.getPrefrence(context as AppCompatActivity, AllKeys.LONGITUDE).toString())

                Log.e(TAG,"response $params")
                return params
            }
        }
        val mQueue= Volley.newRequestQueue(context)
        mQueue.add(stringRequest)
    }

    /*private fun filterBook(text:String){
        val filteredList=ArrayList<AdvertsmentList>()
        for (AdvertsmentList item : advetismentResponse?.data?.advertisement_list? )
    }*/
}
