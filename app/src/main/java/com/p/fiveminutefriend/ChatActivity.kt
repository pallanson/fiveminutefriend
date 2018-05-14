package com.p.fiveminutefriend

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast

import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.model.ChatUser

import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.content_chat.*

//https://github.com/bassaer/ChatMessageView
class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val userIcon = BitmapFactory.decodeResource(this.resources, R.drawable.ic_action_add)
        val user = ChatUser(1, "Friend", userIcon)
        val me = ChatUser(2, "You", userIcon)
        chatView.setRightBubbleColor(ContextCompat.getColor(this, R.color.green500))
        chatView.setLeftBubbleColor(Color.WHITE)
        chatView.setRightMessageTextColor(Color.WHITE)
        chatView.setLeftMessageTextColor(Color.BLACK)
        chatView.setMessageMarginBottom(5)
        chatView.setMessageMarginTop(5)

        with(chatView) {
            receive(Message.Builder()
                .setRight(false)
                .setText("This would be a friend message")
                .setPicture(userIcon)
                .hideIcon(true)
                .setUser(user)
                .build())

            setOnClickSendButtonListener(View.OnClickListener {
                val message = Message.Builder()
                    .setRight(true)
                    .setText(chatView.inputText)
                    .hideIcon(true)
                    .setUser(me)
                    .build()
                chatView.send(message)
                chatView.inputText = ""
            })
        }

        fab_accept_chat.setOnClickListener {
            Toast.makeText(this, "Accepted!!", Toast.LENGTH_SHORT).show()
        }
    }
}
