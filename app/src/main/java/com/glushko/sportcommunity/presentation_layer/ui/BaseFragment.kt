package com.glushko.sportcommunity.presentation_layer.ui

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.BuildConfig
import com.glushko.sportcommunity.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

abstract class BaseFragment : Fragment(){

    abstract val layoutId: Int

    open val titleToolbar = R.string.app_name
    open val showToolbar = true

    var photoFile: File? = null
    var photoUri: Uri? = null
    var navigator: Navigator = Navigator()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)

    }

    override fun onResume() {
        super.onResume()

        base {
            if (showToolbar)
                supportActionBar?.show()
            else
                supportActionBar?.hide()
            supportActionBar?.title = getString(titleToolbar)
        }
    }
    open fun onBackPressed() {}


    open fun updateProgress(status: Boolean?) {
        if (status == true) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    companion object{
        const val TAKE_PICTURE_REQUEST = 4343
        var currentPhotoPath = ""
    }

    fun takePhotoIntent(photoUri: Uri?){
       val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activity?.let{_activity ->
            if(intent.resolveActivity(_activity.packageManager)!=null){

                photoUri?.let {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(intent, TAKE_PICTURE_REQUEST)
                }
            }
        }
    }

    fun createImageFile(): File?{
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        activity?.let {
            val storageDir: File? = it.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile("JPEG_${timeStamp}",
            ".jpg",
            storageDir)
            currentPhotoPath = file.absolutePath
            return file
        }
        return null
    }

    fun createUri(photoFile: File?): Uri?{
        activity?.let{_activity ->
            photoFile?.let {
                val photoUri = FileProvider.getUriForFile(
                    _activity,
                    BuildConfig.APPLICATION_ID+".provider",
                    it
                )
                return photoUri
            }
        }
        return null
    }
    fun saveImage(bitmap: Bitmap, name: String){
        CoroutineScope(Dispatchers.IO).launch {
            var fos: OutputStream? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                activity?.let {
                    val resolver: ContentResolver = it.contentResolver
                    val contentValues = ContentValues()
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + "SportCommunity")
                    val imageUri =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    imageUri?.let {uri->
                        fos = resolver.openOutputStream(uri)
                    }
                }
            }else{
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+File.separator+"SportCommunity"
                val file = File(imagesDir)
                if(!file.exists()){
                    file.mkdir()
                }
                val image = File(imagesDir, "$name.jpeg")
                fos = FileOutputStream(image)
            }
            fos?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                it.flush()
                it.close()
            }
        }
    }

    //показ сообщений, метож выполняется в activity
    fun showMessage(message: String) = base { showMessage(message) }
    //Показываем ProgressBar
    fun showProgress() = base { progressStatus(View.VISIBLE) }
    //Убираем ProgressBAr
    fun hideProgress() = base { progressStatus(View.GONE) }
    //Убираем клаву
    fun hideSoftKeyboard() = base { hideSoftKeyboard() }

    inline fun base(block: BaseActivity.() -> Unit) {
        activity.base(block)
    }
}

