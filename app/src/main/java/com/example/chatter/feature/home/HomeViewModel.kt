package com.example.chatter.feature.home

import androidx.lifecycle.ViewModel
import com.example.chatter.model.Channel
import com.google.firebase.Firebase
import com.google.firebase.database.database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val firebaseDatabase = Firebase.database
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels = _channels.asStateFlow()
    init {
        getChannels()
    }


    private fun getChannels(){
        firebaseDatabase.getReference("channel").get().addOnSuccessListener {
            val list = mutableListOf<Channel>()
            it.children.forEach { data ->
                val channel = Channel(data.key!!,data.value.toString())
                list.add(channel)
        }

            _channels.value = list
    }
    }
    fun addChannel(name:String) {
        val key = firebaseDatabase.getReference("channel").push().key
        firebaseDatabase.getReference("channel").child(key!!).setValue(name)
            .addOnSuccessListener {
                getChannels()
            }
    }
}

//@Module
//@InstallIn(SingletonComponent::class)
//object module{
//
//    @Provides
//    @Singleton
//    fun getDao(
//
//    ):String{
//        return ""
//    }
//}