package com.binduinfo.sports.ui.fragment.profile

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binduinfo.sports.data.model.BasicModel
import com.binduinfo.sports.data.model.ProfileInfo
import com.binduinfo.sports.data.repositores.ProfileRepository
import com.binduinfo.sports.util.imagecompessorsupportmodule.Compressor
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    val progMutable: MutableLiveData<Boolean> = MutableLiveData()
    val serverRequest: MutableLiveData<Boolean> = MutableLiveData()
    var profileHandler: ProfileHandler? = null
    var profileInfo: MutableLiveData<ProfileInfo> =  MutableLiveData<ProfileInfo>().apply {
        CoroutineScope(Main).launch {
           value = repository.loadInfo()
        }
    }


//    private val _text = MutableLiveData<String>().apply {
//        value = "This is notifications Fragment"
//    }
//    val text: LiveData<String> = _text

    fun imageSelect(view: View){
        profileHandler?.profilePic()
    }

    suspend fun uploadImage(context: Context, uri: Uri): BasicModel{
        progMutable.value = true
        return withContext(IO) {
            val compressFile = async {
                imageCompressing(context, uri)
            }.await()

           withContext(IO) {
                imageUploadToServer(compressFile)
            }

        }

    }

    suspend fun imageUploadToServer(compressFile: File): BasicModel{
        return withContext(IO){
            Log.d("image ====", compressFile.toString())
           val multiPart =  MultipartBody.Part.createFormData(
                "image", compressFile.name, compressFile
                    .asRequestBody("image/*".toMediaTypeOrNull()))
            repository.imageUpload(multiPart)
        }
    }

    suspend fun imageCompressing(context: Context, uri: Uri): File{
        return withContext(IO){
            var file = File(uri.path)
            Compressor(context, Compressor.PROFILE_PIC)
                .compressToFile(file)
        }
    }

}