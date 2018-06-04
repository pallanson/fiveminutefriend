package com.p.fiveminutefriend

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast

import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.model.ChatUser
import com.google.android.gms.tasks.Continuation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.database.*
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableResult
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import kotlinx.android.synthetic.main.fragment_match.*
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

//https://github.com/bassaer/ChatMessageView
class ChatActivity : AppCompatActivity() {

    var canChat = true
    var isFriend = false
    var timer : Long = 0
    var matchTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val matchId = intent.getStringExtra("matchId")
        isFriend = intent.getBooleanExtra("isFriend", false)
        val userIcon = BitmapFactory.decodeResource(this.resources, R.drawable.mondom)
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid
        val myUser = ChatUser(0, "Me", userIcon)
        val handler = Handler()
        val delay : Long = 500
        var theirUser = ChatUser(1, "Match", userIcon)
        val storageReference = FirebaseStorage
                .getInstance()
                .getReferenceFromUrl(Constants.FIREBASE_STORAGE_REFERENCE +
                        matchId)

        storageReference.child("profilePic")
                .downloadUrl
                .addOnSuccessListener {
                    Picasso.get()
                            .load(it.toString())
                            .resize(90, 90)
                            .centerCrop()
                            .into(object : Target{
                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                }

                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                    if (bitmap != null) {
                                        theirUser.setIcon(bitmap)
                                    }
                                }

                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                }
                            })
                }
        setContentView(R.layout.activity_chat)

        if (matchId != "") {
            val matchRef = FirebaseDatabase.getInstance().reference.child("Users/$matchId")
            val userRef = FirebaseDatabase.getInstance().reference.child("Users/$uid")
            val sentRef = FirebaseDatabase.getInstance().reference.child("Messages/$uid/$matchId").orderByChild("timeSent")
            val receiveRef = FirebaseDatabase.getInstance().reference.child("Messages/$matchId/$uid").orderByChild("timeSent")
            if (isFriend){
                fab_accept_chat.hide()
                text_timer_chat.text = ""
            }
            matchRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        theirUser = ChatUser(1, dataSnapshot.child("username").value.toString(), userIcon)
                        canChat = dataSnapshot.hasChild("friends/$uid")
                        canChat = canChat || (dataSnapshot.hasChild("matches/$uid") && (System.currentTimeMillis() - dataSnapshot.child("matches/$uid").value as Long) <= 300000)
                        if (!canChat) {
                            fab_accept_chat.hide()
                            if (!isFriend){
                                fab_refuse_chat.hide()
                            }
                        }
                        chatView.setEnableSendButton(canChat)
                        if (dataSnapshot.hasChild("friends/$uid")){
                            text_username.text = theirUser.getName()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.hasChild("friends/$matchId")) {
                            fab_accept_chat.hide()
                            text_timer_chat.text = ""
                            isFriend = true
                        }
                        else if (dataSnapshot.hasChild("matches/$matchId")) {
                            matchTime = dataSnapshot.child("matches/$matchId").value as Long
                            handler.postDelayed(object : Runnable {
                                override fun run() {
                                    if (!isFriend) {
                                        timer = 300000 - (System.currentTimeMillis() - matchTime)
                                        if (timer > 0) {
                                            if (canChat) {
                                                text_timer_chat.text = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timer) % TimeUnit.HOURS.toMinutes(1),
                                                        TimeUnit.MILLISECONDS.toSeconds(timer) % TimeUnit.MINUTES.toSeconds(1))
                                            }
                                            handler.postDelayed(this, delay)
                                        }
                                    } else {
                                        text_timer_chat.text = ""
                                    }
                                }
                            }, delay)
                        }
                        else {
                            fab_accept_chat.hide()
                            if (!isFriend){
                                fab_refuse_chat.hide()
                            }
                            canChat = false
                            chatView.send(Message.Builder()
                                    .setRight(false)
                                    .setUser(theirUser)
                                    .setText(theirUser.getName() + " has left the conversation.")
                                    .hideIcon(true)
                                    .build())
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            sentRef.addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    if (p0 != null) {
                        val time : Long = if (p0.child("timeSent").value != null) p0.child("timeSent").value as Long else 0
                        val calendar = Calendar.getInstance()
                        if (time > 0) {
                            calendar.timeInMillis = time
                        }
                        chatView.send(Message.Builder()
                                .setRight(true)
                                .setSendTime(calendar)
                                .setText(p0.child("text").value.toString())
                                .hideIcon(true)
                                .setUser(myUser)
                                .build())
                    }
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            })
            receiveRef.addChildEventListener(object : ChildEventListener{
                override fun onCancelled(p0: DatabaseError?) {
                }

                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    if (p0 != null) {
                        val time : Long = if (p0.child("timeSent").value != null) p0.child("timeSent").value as Long else 0
                        val calendar = Calendar.getInstance()
                        if (time > 0) {
                            calendar.timeInMillis = time
                        }
                        chatView.receive(Message.Builder()
                                .setRight(false)
                                .setSendTime(calendar)
                                .setText(p0.child("text").value.toString())
                                .hideIcon(false)
                                .setUser(theirUser)
                                .build())
                    }
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                }

                override fun onChildRemoved(p0: DataSnapshot?) {
                }
            })
        }

        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500))
        chatView.setLeftBubbleColor(Color.WHITE)
        chatView.setRightMessageTextColor(Color.WHITE)
        chatView.setLeftMessageTextColor(Color.BLACK)
        chatView.setMessageMarginBottom(5)
        chatView.setMessageMarginTop(5)

        with(chatView) {
            setOnClickSendButtonListener(OnClickListener {
                if (chatView.inputText.isNotEmpty()) {
                    val data = HashMap<String, Any>()
                    data["receiverUid"] = matchId
                    data["msgType"] = 0
                    data["text"] = chatView.inputText
                    data["timeSent"] = System.currentTimeMillis()
                    FirebaseFunctions.getInstance()
                            .getHttpsCallable("sendMessage")
                            .call(data)
                            .addOnCompleteListener({
                                if (!it.isSuccessful) {
                                    val ffe: FirebaseFunctionsException = it.exception as FirebaseFunctionsException
                                }
                            })
                    chatView.inputText = ""
                }
            })
        }

        fab_accept_chat.setOnClickListener {
            Toast.makeText(this, "Friend request sent", Toast.LENGTH_SHORT).show()
            val ref = FirebaseDatabase.getInstance().reference.child("FriendRequests/$uid/$matchId")
            ref.setValue(System.currentTimeMillis())
        }

        fab_refuse_chat.setOnClickListener({
            if (!isFriend) {
                val data = HashMap<String, Any>()
                data["matchId"] = matchId
                data["matchTime"] = matchTime
                this.onBackPressed()
                FirebaseFunctions.getInstance()
                        .getHttpsCallable("skipMatch")
                        .call(data)
                        .addOnCompleteListener({
                            if (!it.isSuccessful) {
                                val ffe: FirebaseFunctionsException = it.exception as FirebaseFunctionsException
                            }
                        })
            }
            else {
                AlertDialog.Builder(this)
                        .setTitle("Remove friend")
                        .setMessage("Are you sure you would like to remove ${theirUser.getName()} from your friends list?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, { _: DialogInterface, _: Int ->
                            val data = HashMap<String, Any>()
                            data["friendId"] = matchId
                            this.onBackPressed()
                            FirebaseFunctions.getInstance()
                                    .getHttpsCallable("removeFriend")
                                    .call(data)
                                    .addOnCompleteListener({
                                        if (!it.isSuccessful) {
                                            val ffe: FirebaseFunctionsException = it.exception as FirebaseFunctionsException
                                        }
                                    })
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show()
            }
        })
    }
}
