package com.example.chatter.feature.chat

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.auth.oauth2.GoogleCredentials
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.chatter.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONObject
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _message = MutableStateFlow<List<Message>>(emptyList())
    val message = _message.asStateFlow()
    private val db = Firebase.database

    // sending section
    //send text or/and image
    fun sendMessage(channelId: String, messageText: String? = "", image: String? = null) {
        val message = Message(
            id = db.reference.push().key ?: UUID.randomUUID().toString(),
            senderId = Firebase.auth.currentUser?.uid ?: "",
            messageText ?: "",
            System.currentTimeMillis(),
            Firebase.auth.currentUser?.displayName ?: "",
            null,
            image
        )
        db.reference.child("messages").child(channelId).push().setValue(message)
            .addOnCompleteListener {
                //check the success of sending message
                if (it.isSuccessful) {
                    postNotificationToUsers(channelId, message.senderName, messageText?:"")
                }
            }

    }

    fun sendImageMessage(uri: Uri, channelId: String) {
        val imageRef = Firebase.storage.reference.child("images/${UUID.randomUUID()}")
        imageRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                sendMessage(
                    channelId,
                    null,
                    downloadUri.toString()
                )
            }
        }

    }

    fun listenForMessages(channelId: String) {
        db.getReference("messages").child(channelId).orderByChild(
            "createdAt"
        )
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<Message>()
                    snapshot.children.forEach { data ->
                        val message = data.getValue(Message::class.java)
                        message?.let {
                            list.add(it)
                        }
                    }
                    _message.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        subscribeForNotification(channelId)
    }

    private fun subscribeForNotification(channelId: String) {
        FirebaseMessaging.getInstance().subscribeToTopic("group_$channelId")
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("ChatViewModel", "Subscribed to topic:group_$channelId ")
                } else {
                    Log.d("ChatViewModel", "Failed to topic:group_$channelId ")
                }
            }

    }

    private fun postNotificationToUsers(
        channelId: String,
        senderName: String,
        messageContent: String
    ) {
        val fcmUrl = "https://fcm.googleapis.com/v1/projects/chatting-ff84b/messages:send"
        val jsonBody = JSONObject().apply {
            put("message", JSONObject().apply {
                put("topic", "group_$channelId")
                put("notification", JSONObject().apply {
                    put("title", "New message in $channelId")
                    put("body", "$senderName: $messageContent")
                })
            })
        }
        val requestBody = jsonBody.toString()

        val request = object : StringRequest(Method.POST, fcmUrl,
            Response.Listener {
                Log.d("ChatViewModel", "Notification sent successfully")
            },
            Response.ErrorListener {
                Log.e("ChatViewModel", "Failed to send notification")
            }) {
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${getAccessToken()}"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }




    private fun getAccessToken(): String {
        val inputStream = context.resources.openRawResource(R.raw.chatting_key)
        val googleCreeds = GoogleCredentials.fromStream(inputStream)
            .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
        return googleCreeds.refreshAccessToken().tokenValue
    }
}