package com.demir.gibitayfa.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demir.gibitayfa.model.gibiModel
import com.demir.gibitayfa.util.Resource
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.shortLinkAsync
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(private val firestore: FirebaseFirestore)
    :ViewModel() {
    private val _yilmaz= MutableStateFlow<Resource<List<gibiModel>>>(Resource.unspecifed())
    val yilmaz=_yilmaz.asStateFlow()
    private val _ilkkan= MutableStateFlow<Resource<List<gibiModel>>>(Resource.unspecifed())
    val ilkkan=_ilkkan.asStateFlow()
    private val _ersoy= MutableStateFlow<Resource<List<gibiModel>>>(Resource.unspecifed())
    val ersoy=_ersoy.asStateFlow()

    init {
        fetchYilmaz()
        fetchErsoy()
        fetchIlkkan()
    }


    private fun fetchYilmaz() {
        viewModelScope.launch {
            _yilmaz.emit(Resource.loading())
        }
        firestore.collection("records").whereEqualTo("user","yilmaz")
            .get().addOnSuccessListener { result->
                val bestDealList=result.toObjects(gibiModel::class.java)
                viewModelScope.launch {
                    _yilmaz.emit(Resource.Success(bestDealList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _yilmaz.emit(Resource.Error(it.message.toString()))
                }
            }

    }
    private fun fetchIlkkan() {
        viewModelScope.launch {
            _ilkkan.emit(Resource.loading())
        }
        firestore.collection("records").whereEqualTo("user","ilkkan")
            .get().addOnSuccessListener { result->
                val bestDealList=result.toObjects(gibiModel::class.java)
                viewModelScope.launch {
                    _ilkkan.emit(Resource.Success(bestDealList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _ilkkan.emit(Resource.Error(it.message.toString()))
                }
            }

    }
    private fun fetchErsoy() {
        viewModelScope.launch {
            _ersoy.emit(Resource.loading())
        }
        firestore.collection("records").whereEqualTo("user","ersoy")
            .get().addOnSuccessListener { result->
                val bestDealList=result.toObjects(gibiModel::class.java)
                viewModelScope.launch {
                    _ersoy.emit(Resource.Success(bestDealList))
                }
            }.addOnFailureListener{
                viewModelScope.launch {
                    _ersoy.emit(Resource.Error(it.message.toString()))
                }
            }

    }
}