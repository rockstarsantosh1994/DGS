package com.example.dgs.activity.chat

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Log.e
import com.example.dgs.AllKeys
import com.example.dgs.CommonMethods
import com.example.dgs.R
import com.aspl.chat.models.ChatMessage
import com.aspl.chat.models.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*


/**
 * Created by ansh on 04/09/18.
 */
class LatestMessageRow(val chatMessage: ChatMessage, val context: Context) : Item<ViewHolder>() {

    var chatPartnerUser: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {



        Log.e("LatestMessageRow","chat messasge from id"+chatMessage?.fromId)
        Log.e("LatestMessageRow","own id"+CommonMethods.getPrefrence(context,AllKeys.USER_ID))

        if (chatMessage?.fromId== CommonMethods.getPrefrence(context,AllKeys.USER_ID)) {
            viewHolder.itemView.latest_message_textview.setTextAppearance(context,R.style.normalStyle)
        } else {
           // viewHolder.itemView.latest_message_textview.setTextAppearance(context,R.style.boldStyle)
            if(chatMessage.seenStatus.equals("false")){
                viewHolder.itemView.latest_message_textview.setTextAppearance(context,R.style.boldStyle)
            }else{
                viewHolder.itemView.latest_message_textview.setTextAppearance(context,R.style.normalStyle)
                //  viewHolder.itemView.status.setText("Offline")
            }
        }

        if (chatMessage?.fromId== CommonMethods.getPrefrence(context,AllKeys.USER_ID)) {
            viewHolder.itemView.status.setText("")
        } else {
            viewHolder.itemView.status.setText("")
        }

        if(chatMessage.text.contains("https")){
            viewHolder.itemView.latest_message_textview.text

        }else{
            viewHolder.itemView.latest_message_textview.text = chatMessage.text
            viewHolder.itemView.latest_message_textview.setCompoundDrawables(
                null, null, null, null);
        }

        e("LatestMessage","OnCreate chatPartnerUser.status : "+chatPartnerUser?.status)
        e("LatestMessage","OnCreate chatPartnerUser.profilePicUrl : "+chatPartnerUser?.profileImageUrl)
        e("Latest","adid"+chatMessage.advertisement_id)

        val chatPartnerId: String
        e("Latest M","chatMessage.fromId :" +chatMessage.fromId)

        e("LatestMessage","chatPartnerUser.id: "+chatPartnerUser?.uid)
        e("LatestMessage","Common Method id: "+CommonMethods.getPrefrence(context,AllKeys.USER_ID))


        if (chatMessage.fromId == CommonMethods.getPrefrence(context,AllKeys.USER_ID)) {
            chatPartnerId = chatMessage.toId
        } else {
            chatPartnerId = chatMessage.fromId
        }
        e("Latest M","char partner ID :" +chatPartnerId)

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                chatPartnerUser = p0.getValue(User::class.java)
                viewHolder.itemView.username_textview_latest_message.text = chatPartnerUser?.name
                viewHolder.itemView.latest_msg_time.text = DateUtils.getFormattedTime(chatMessage.timestamp)

                //  Log.e("imageUrl",chatPartnerUser?.profileImageUrl)

                val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)

                Glide.with(viewHolder.itemView.imageview_latest_message.context)
                    .load(chatPartnerUser?.profileImageUrl)
                    .apply(requestOptions)
                    .into(viewHolder.itemView.imageview_latest_message)


                viewHolder.itemView.imageview_latest_message.setOnClickListener {

                    e("LatestMessage","OnDataCHanged chatPartnerUser.status : "+chatPartnerUser?.status)
                    e("LatestMessage","OnDataCHanged chatPartnerUser.profilePicUrl : "+chatPartnerUser?.profileImageUrl)

                    if(chatPartnerUser?.profileImageUrl.toString()==null ||

                        chatPartnerUser?.profileImageUrl.toString().equals("")){
                        viewHolder.itemView.imageview_latest_message.isEnabled=false

                    }else{

                        viewHolder.itemView.imageview_latest_message.isEnabled=true
                        BigImageDialog.newInstance(chatPartnerUser?.profileImageUrl!!)
                            .show((context as Activity).fragmentManager, "")
                    }
                }


                /*if (chatPartnerUser?.profileImageUrl!=null || !chatPartnerUser?.profileImageUrl?.isEmpty()!! ||
                       chatPartnerUser?.profileImageUrl!="") {
                       val requestOptions = RequestOptions().placeholder(R.drawable.no_image2)

                       Glide.with(viewHolder.itemView.imageview_latest_message.context)
                               .load(chatPartnerUser?.profileImageUrl)
                               .apply(requestOptions)
                               .into(viewHolder.itemView.imageview_latest_message)

                       viewHolder.itemView.imageview_latest_message.setOnClickListener {
                           BigImageDialog.newInstance(chatPartnerUser?.profileImageUrl!!).show((context as Activity).fragmentManager
                                   , "")
                       }

                   }else{
                       Glide.with(viewHolder.itemView.imageview_latest_message.context)
                           .load(R.drawable.user)
                           .into(viewHolder.itemView.imageview_latest_message)

                       *//*viewHolder.itemView.imageview_latest_message.setOnClickListener {
                        BigImageDialog.newInstance(chatPartnerUser?.profileImageUrl!!).show((context as Activity).fragmentManager
                            , "")
                    } *//*
                }*/
            }

        })


    }

}