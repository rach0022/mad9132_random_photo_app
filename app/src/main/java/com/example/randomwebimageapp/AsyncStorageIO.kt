import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import com.example.randomwebimageapp.R
import com.example.randomwebimageapp.TheApp
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

/*
 * Created by Tony Davidson on November 18, 2020
*/

class AsyncStorageIO(private var bitmap: Bitmap, private val savingLastImage: Boolean = false) :
        AsyncTask<Any, Any, Any>() {

    // region Properties
    var completed = false
        private set

    private val fileName = TheApp.context.getString(R.string.last_image_file_name)

    private val folderName = TheApp.context.getString(R.string.gallery_folder_name)

    // endregion

    // region Background Method overrides
    override fun onPreExecute() {
        super.onPreExecute()
        completed = false
    }

    override fun doInBackground(vararg p0: Any?): Any? {
        if (savingLastImage) {
            saveLastImage()
        } else {
            saveImage(TheApp.context)
        }
        return null
    }

    override fun onPostExecute(result: Any?) {
        super.onPostExecute(result)
        completed = true
        TheApp.context.toast("Image saved")
    }
// endregion

    // region Save Last Image to Internal Storage

    private fun saveLastImage() {

        val file = TheApp.context.getFileStreamPath(fileName)

        if (file.exists()) {
            file.delete()
        }

        saveImageToInternalStorage()
    }

    private fun saveImageToInternalStorage() {

        val file = File(TheApp.context.filesDir, fileName)
        saveImageToStream(file)
    }

    private fun saveImageToStream(file: File) {

        try {
            val outputStream = FileOutputStream(file)
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                TheApp.context.toast(e.message ?: "Bitmap Compress Error")
                e.printStackTrace()
            }
        } catch (e: Exception) {
            TheApp.context.toast(e.message ?: "File Stream Error")
            e.printStackTrace()
        }
    }

    // endregion

    // region Extension methods
    // Extension function to show toast message
    private fun Context.toast(message: String) {
        Toast.makeText(TheApp.context, message, Toast.LENGTH_SHORT).show()
    }

// region Save Image to Gallery

    // for gallery saving
    private fun saveImage(
            context: Context
    ) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING introduced in API 29.

            val uri: Uri? =
                    context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else { // less than Q
            val directory = File(

                    Environment.getExternalStorageDirectory().toString()
                            + File.separator + folderName
            )

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val timeStampFileName = System.currentTimeMillis().toString() + ".jpg"

            val file = File(directory, timeStampFileName)

            saveImageToStream(file)

            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)//deprecated API 29

            context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
            )
        }
    }

    private fun saveImageToStream(outputStream: OutputStream?) {

        try {
            if (outputStream != null) {
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    TheApp.context.toast(e.message ?: "error")
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            TheApp.context.toast(e.message ?: "stream error")
            e.printStackTrace()
        }
    }

// endregion

// endregion

}