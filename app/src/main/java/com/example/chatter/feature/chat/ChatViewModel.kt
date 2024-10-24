package com.example.chatter.feature.chat

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _message = MutableStateFlow<List<Message>>(emptyList())
    val message = _message.asStateFlow()
    private val db = Firebase.database


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
    }
}