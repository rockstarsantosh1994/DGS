package com.example.dgs.activity.chat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.aspl.chat.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    companion object {
        const val USER_KEY = "USER_KEY"
        const val MY_NAME = "MY_NAME"
        const val MY_ID = "MY_ID"
        const val MY_IMAGE = "MY_IMAGE"

        const val PARTNER_NAME = "PARTNER_NAME"
        const val PARTNER_ID = "PARTNER_ID"
        const val PARTNER_IMAGE = "PARTNER_IMAGE"

        private val TAG = NewMessageActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        swiperefresh.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent))

        supportActionBar?.title = "Select User"

        fetchUsers()
        //Todo - Add more users and messages for screenshots

        swiperefresh.setOnRefreshListener {
            fetchUsers()
        }
    }

    private fun fetchUsers() {
        swiperefresh.isRefreshing = true

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                dataSnapshot.children.forEach {
                    Log.d(TAG, it.toString())
                    @Suppress("NestedLambdaShadowedImplicitParameter")
                    it.getValue(User::class.java)?.let {
                        if (it.uid != CommonMethods.getPrefrence(this as AppCompatActivity,AllKeys.USER_ID)) {
                            adapter.add(UserItem(it, this@NewMessageActivity))
                        }
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    Log.e("NewMessage","uid"+userItem.user.uid)
                    Log.e("NewMessage","uname"+userItem.user.name)
                    Log.e("NewMessage","profile"+userItem.user.profileImageUrl)
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, User(userItem.user.uid,userItem.user.name,
                        "",userItem.user.profileImageUrl,CommonMethods.getPrefrence(applicationContext,AllKeys.FCM_TOKEN)))
                   // intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)
                    finish()
                }

                recyclerview_newmessage.adapter = adapter
                swiperefresh.isRefreshing = false
            }

        })
    }
}

class UserItem(val user: User, val context: Context) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.name

        Log.e("imageurl","image url"+user.profileImageUrl);

        /*if (user.profileImageUrl!!!=null|| !user.profileImageUrl!!.isEmpty()) {
            val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)


            Glide.with(viewHolder.itemView.imageview_new_message.context)
                    .load(user.profileImageUrl)
                    .apply(requestOptions)
                    .into(viewHolder.itemView.imageview_new_message)

            viewHolder.itemView.imageview_new_message.setOnClickListener {
                BigImageDialog.newInstance(user?.profileImageUrl!!).show((context as Activity).fragmentManager
                        , "")
            }
        }else{
            Toast.makeText(context,"No Image Available",Toast.LENGTH_SHORT).show()
        }*/

        if (user.profileImageUrl!! == null|| user.profileImageUrl!!.isEmpty()) {
            Toast.makeText(context,"No Image Available",Toast.LENGTH_SHORT).show()
            Toast.makeText(context,"if",Toast.LENGTH_SHORT).show()

        }else{
            Toast.makeText(context,"else",Toast.LENGTH_SHORT).show()
            val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)

            Glide.with(viewHolder.itemView.imageview_new_message.context)
                .load(user.profileImageUrl)
                .apply(requestOptions)
                .into(viewHolder.itemView.imageview_new_message)

            viewHolder.itemView.imageview_new_message.setOnClickListener {
                BigImageDialog.newInstance(user?.profileImageUrl!!).show((context as Activity).fragmentManager, "")
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

}
