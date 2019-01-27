package com.example.lindsay.delta5.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class ImageUtils {
    companion object {
        private const val TEMP_IMAGE_NAME = "tempImage"
        private const val MAX_IMAGE_SIZE = 500

        @Throws(IOException::class)
        fun createTempImageFile(context: Context): File {
            return File.createTempFile(TEMP_IMAGE_NAME + "_", ".jpg", context.getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        }

        // Orientates the temporary image correctly
        fun rotateTempImageFromGallery(imageURI: Uri?, imagePath: String, context: Context) {
            val cursor = context.contentResolver.query(imageURI,
                    arrayOf(MediaStore.Images.ImageColumns.ORIENTATION), null, null, null) ?: return

            if (cursor.count != 1) {
                cursor.close()
                return
            }

            cursor.moveToFirst()
            val orientation = cursor.getFloat(0)
            cursor.close()

            val matrix = Matrix()
            matrix.postRotate(orientation)

            val fileBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
            val rotatedBitmap = Bitmap.createBitmap(fileBitmap, 0, 0, fileBitmap.width, fileBitmap.height, matrix, true)

            saveImage(imagePath, rotatedBitmap)
        }

        fun rotateTempImage(imagePath: String) {
            val orientation = ExifInterface(imagePath).getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)

            val matrix = Matrix()
            matrix.postRotate(0f)

            when (orientation) {
                3 -> matrix.postRotate(180f)
                6 -> matrix.postRotate(90f)
                8 -> matrix.postRotate(270f)
            }

            val fileBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), MAX_IMAGE_SIZE, MAX_IMAGE_SIZE)
            val rotatedBitmap = Bitmap.createBitmap(fileBitmap, 0, 0, fileBitmap.width, fileBitmap.height, matrix, true)

            saveImage(imagePath, rotatedBitmap)
        }

        fun saveImage(path: String, bitmap: Bitmap) {
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(path)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun saveTempImageForPlant(context: Context, tempPath: String, imageName: String): String? {
            val path = context.filesDir.absolutePath + "/" + imageName + ".jpg"
            val bitmap = getImageBitmap(tempPath)
            var pathToReturn: String? = null

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(path)
                if (bitmap != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                } else {
                    return pathToReturn
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    fos?.close()
                    deleteImage(tempPath)
                    pathToReturn = path
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            return pathToReturn
        }

        // Images are stored 500x500 so size will be capped at this number
        // Minimum size will be 20x20
        fun getImageBitmap(imagePath: String?, size: Int = MAX_IMAGE_SIZE): Bitmap? {
            if (imagePath == null) {
                return null
            }

            var size = size

            if (size > 500) {
                size = 500
            }

            if (size < 20) {
                size = 20
            }

            return  ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath), size, size)
        }

        fun deleteImage(imagePath: String) {
            File(imagePath).delete()
        }

        fun createImageFile(context: Context, extension: String): File {
            var extension = extension

            if (extension == "jpg") {
                extension = "jpeg"
            }

            val directory = File(context.filesDir, "attachments")
            directory.mkdir()

            // Create an image file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "ATTACH_$timeStamp.$extension"

            return File(directory, imageFileName)
        }
    }



}