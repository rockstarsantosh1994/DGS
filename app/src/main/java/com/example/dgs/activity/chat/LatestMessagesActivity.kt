package com.example.dgs.activity.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log.e
import android.view.View
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.example.dgs.activity.chat.NewMessageActivity.Companion.MY_ID
import com.example.dgs.activity.chat.NewMessageActivity.Companion.MY_IMAGE
import com.example.dgs.activity.chat.NewMessageActivity.Companion.MY_NAME
import com.example.dgs.activity.chat.NewMessageActivity.Companion.PARTNER_ID
import com.example.dgs.activity.chat.NewMessageActivity.Companion.PARTNER_IMAGE
import com.example.dgs.activity.chat.NewMessageActivity.Companion.PARTNER_NAME
import com.aspl.chat.models.ChatMessage
import com.aspl.chat.models.User
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlin.collections.HashMap

class LatestMessagesActivity : AppCompatActivity() {

    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessagesMap = HashMap<String, ChatMessage>()
    var toolbar:androidx.appcompat.widget.Toolbar?=null
    var adver_id:String?=null
    var toId:String?=null
    var fcm_child_id:String?=""

    companion object {
        var currentUser: User? = null
        val TAG = LatestMessagesActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)
        //verifyUserIsLoggedIn()

        toolbar= this.findViewById(R.id.toolbar_latestmsg) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)

        Log.e("data","OnCreate "+intent.getStringExtra("notification"))

        if(intent.getStringExtra("notification").equals("Notification",false)){
            supportActionBar?.title = "Notification"
        }else{
            supportActionBar?.title = "Chat"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerview_latest_messages.adapter = adapter
        e("latestmessagemap","recycler count"+recyclerview_latest_messages.adapter?.itemCount)
        e("latestmessagemap","latest message map count"+latestMessagesMap.size)
        swiperefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        fetchCurrentUser()
        listenForLatestMessages()

        adapter.setOnItemClickListener { item, _ ->
            //val userItem = item as UserItem
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as LatestMessageRow

            e("LatestMessage","Item name : "+row.chatPartnerUser);
            e("LatestMessage","Item name : "+item.chatPartnerUser?.name);
            e("LatestMessage","Item profile : "+item.chatPartnerUser?.profileImageUrl);
            e("LatestMessage","Item id : "+item.chatPartnerUser?.uid);
            e("LatestMessage","advertid :"+item.chatMessage.advertisement_id)
            e("LatestMessage","seenstatus :"+item.chatMessage.seenStatus)
            e("LatestMessage","id :"+item.chatMessage.id)
            e("LatestMessage","text:"+item.chatMessage.text)
            e("LatestMessage","timestamp :"+item.chatMessage.timestamp)
            // intent.putExtra(USER_KEY,  User(item.chatPartnerUser?.uid.toString(),
            // item.chatPartnerUser?.name.toString(),item.chatPartnerUser?.profileImageUrl.toString()))
            intent.putExtra(MY_ID,""+CommonMethods.getPrefrence(this,AllKeys.FIREBASE_USER_ID))
            intent.putExtra("advertisement_id",item.chatMessage.advertisement_id)
            intent.putExtra("gcm_token",item.chatPartnerUser?.gcm_token)
            intent.putExtra("id",item.chatMessage.id)
            intent.putExtra("text",item.chatMessage.text)
            intent.putExtra("fromId",item.chatMessage.fromId)
            intent.putExtra("toId",item.chatMessage.toId)
            intent.putExtra("timestamp",item.chatMessage.timestamp)

            Log.e("advertisment_id","advertismentid"+item.chatMessage.advertisement_id)

            if(CommonMethods.getPrefrence(this,AllKeys.MEDIA_TYPE)!!.equals("e")){
                intent.putExtra(MY_NAME,""+CommonMethods.getPrefrence(this,AllKeys.EMAIL_NAME))
                intent.putExtra(MY_IMAGE,""+CommonMethods.getPrefrence(this,AllKeys.EMAIL_PIC))

            }else if(CommonMethods.getPrefrence(this,AllKeys.MEDIA_TYPE)!!.equals("f")){
                intent.putExtra(MY_NAME,""+CommonMethods.getPrefrence(this,AllKeys.FACEBOOK_NAME))
                intent.putExtra(MY_IMAGE,""+CommonMethods.getPrefrence(this,AllKeys.FACEBOOK_PIC))

            }else{
                intent.putExtra(MY_NAME,""+CommonMethods.getPrefrence(this,AllKeys.GMAIL_NAME))
                intent.putExtra(MY_IMAGE,""+CommonMethods.getPrefrence(this,AllKeys.GMAIL_PIC))

            }

            intent.putExtra(PARTNER_NAME,item.chatPartnerUser?.name)
            //intent.putExtra(PARTNER_ID,item.chatPartnerUser?.uid)
            intent.putExtra(PARTNER_ID,item.chatPartnerUser?.uid)
            intent.putExtra(PARTNER_IMAGE,item.chatPartnerUser?.profileImageUrl)
            startActivity(intent)
        }


        /*new_message_fab.setOnClickListener {
            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)
        }*/

        swiperefresh.setOnRefreshListener {
            //verifyUserIsLoggedIn()
            fetchCurrentUser()
            listenForLatestMessages()
        }
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        e("latestmessagemap","before RefreshRecyclerVIewMessages latest message map count"+
                latestMessagesMap.size)

        if(latestMessagesMap.size==0){
            ll_nodata.visibility= View.VISIBLE
            swiperefresh.visibility=View.GONE
        }else{
            ll_nodata.visibility= View.GONE
            swiperefresh.visibility=View.VISIBLE
        }

        if(currentUser?.status.equals("online")){

            latestMessagesMap.values
                .asSequence()
                .sortedByDescending { it.timestamp }
                .forEach {
                    adapter.add(LatestMessageRow(it,this))
                    //adapter.notifyDataSetChanged()
                    adver_id = it.advertisement_id
                    Log.e("advertisment_id","inside for each advertismentid"+adver_id)
                    Log.e("advertisment_id","ChatMessage"+it.text)
                    //toId=it.toId

                }

        }

        else{

            latestMessagesMap.values
                .asSequence()
                .sortedByDescending { it.timestamp }
                .forEach {
                    adapter.add(LatestMessageRow(it,this))
                    //again only if you want to sort them

                    /// adapter.notifyDataSetChanged()
                    adver_id=it.advertisement_id

                }


        }


        swiperefresh.isRefreshing = false

        e("latestmessagemap","after RefreshRecyclerVIewMessages latest message map count"+latestMessagesMap.size)
    }

    private fun listenForLatestMessages() {
        swiperefresh.isRefreshing = true
        val fromId = CommonMethods.getPrefrence(this,AllKeys.USER_ID)
//        val fromId = FirebaseAuth.getInstance().uid ?: return

        e("LM","FromID : "+fromId)

        var ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        e("latestmessage","ref : "+ref)


        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                e(TAG, "database error: " + databaseError.message)

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                e(TAG, "has children: " + dataSnapshot.hasChildren() +" "+dataSnapshot.value)
                if (!dataSnapshot.hasChildren()) {
                    swiperefresh.isRefreshing = false
                    e("dataSnapshot",dataSnapshot.toString())

                }
                else
                {
                    latestMessagesMap.clear()


                    for(ds in dataSnapshot.children)
                    {
                        fcm_child_id= ds.key
                        e("TAGVAL",fcm_child_id)
                        e("dataSnapshot",dataSnapshot.toString())
                        //ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$fcm_child_id")
                        // e("TAGVAL2",ds.)
                        //e("refindatasnapshot",ref.toString())
                        ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$fcm_child_id")
                        e("latestmessage","ref : "+ref)
                        ref.addChildEventListener(object : ChildEventListener {
                            override fun onCancelled(databaseError: DatabaseError) {
                            }

                            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                            }

                            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                                    latestMessagesMap[dataSnapshot.key!!] = it
                                    refreshRecyclerViewMessages()

                                }
                            }

                            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

                                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                                    latestMessagesMap[dataSnapshot.key!!] = it
                                    refreshRecyclerViewMessages()

                                }
                            }

                            override fun onChildRemoved(p0: DataSnapshot) {
                            }

                        })
                    }
                    e("VALLL",dataSnapshot.value.toString())

                }
            }

        })



    }

    private fun fetchCurrentUser() {

        val uid = CommonMethods.getPrefrence(this,AllKeys.USER_ID)

        e("LM","uid : "+uid)

        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentUser = dataSnapshot.getValue(User::class.java)
                e("LatestMessage","Item name : "+currentUser?.name)
                e("LatestMessage","Item profile : "+currentUser?.profileImageUrl)
                e("LatestMessage","Item id : "+currentUser?.uid)
                e("LatestMessage","stuas  : "+currentUser?.status)
            }

        })
    }

    /*private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }*/

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    /*override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
*/

    private fun status(staus:String){

        val ref = FirebaseDatabase.getInstance().getReference("users").child(""+
                CommonMethods.getPrefrence(this,AllKeys.USER_ID))

        val hashmap = java.util.HashMap<String, String>()
        hashmap.put("status", staus)

        ref.updateChildren(hashmap as Map<String, Any>)

    }


    override fun onResume() {
        super.onResume()

        status("online")

    }

    override fun onPause() {
        super.onPause()

        status("offline")
    }

}