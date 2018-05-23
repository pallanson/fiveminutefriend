package com.p.fiveminutefriend

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.model.ChatUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*
import kotlinx.android.synthetic.main.fragment_match.*
import java.util.*
import java.util.concurrent.TimeUnit

//https://github.com/bassaer/ChatMessageView
class ChatActivity : AppCompatActivity() {

    var canChat = true
    var timer : Long = 0
    var matchTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val matchId = intent.getStringExtra("matchId")
        val userIcon = BitmapFactory.decodeResource(this.resources, R.drawable.ic_action_add)
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user!!.uid
        val myUser = ChatUser(0, "Me", userIcon)
        val handler = Handler()
        val delay : Long = 500
        var theirUser = ChatUser(1, "Match", userIcon)
        setContentView(R.layout.activity_chat)

        if (matchId != "") {
            val matchRef = FirebaseDatabase.getInstance().reference.child("Users/$matchId")
            val userRef = FirebaseDatabase.getInstance().reference.child("Users/$uid")
            val sentRef = FirebaseDatabase.getInstance().reference.child("Messages/$uid/$matchId").orderByChild("timeSent")
            val receiveRef = FirebaseDatabase.getInstance().reference.child("Messages/$matchId/$uid").orderByChild("timeSent")
            matchRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        theirUser = ChatUser(1, dataSnapshot.child("username").value.toString(), userIcon)
                        canChat = dataSnapshot.hasChild("matches/$uid")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        //matchTime = dataSnapshot.child("matches/$matchId").value as Long
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                timer = 300000 - (System.currentTimeMillis() - matchTime)
                                if (timer > 0) {
                                    text_timer_chat.text = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(timer) % TimeUnit.HOURS.toMinutes(1),
                                            TimeUnit.MILLISECONDS.toSeconds(timer) % TimeUnit.MINUTES.toSeconds(1))
                                    handler.postDelayed(this, delay)
                                }
                            }
                        }, delay)
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
            setOnClickSendButtonListener(View.OnClickListener {
                if (canChat && timer > 0) {
                    val ref = FirebaseDatabase.getInstance().reference.child("Messages/$uid/$matchId")
                    val message = com.p.fiveminutefriend.Model.Message(chatView.inputText, 0, uid, 0, System.currentTimeMillis())
                    val newMessageRef = ref.push()
                    newMessageRef.setValue(message)
                    chatView.inputText = ""
                }
            })
        }

        fab_accept_chat.setOnClickListener {
            Toast.makeText(this, "Accepted!!", Toast.LENGTH_SHORT).show()
        }
    }
}
