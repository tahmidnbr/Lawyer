package com.example.lawyer.logic

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class ImageStorage(
    private val context: Context
) {
    private val dir: File
        get() = File(context.filesDir, "case_images").also {
            it.mkdirs()
        }

    suspend fun saveImages(uris: List<Uri>): List<String> =
        withContext(Dispatchers.IO) {
            uris.mapNotNull { uri ->
                runCatching {
                    val fileName = "img_${UUID.randomUUID()}.jpg"
                    val destFile = File(dir, fileName)
                    context.contentResolver.openInputStream(uri)?.use{input->
                        destFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    destFile.absolutePath
                }.getOrNull()
            }
        }

    suspend fun deleteImage(path: String) =
        withContext(Dispatchers.IO) {
            runCatching { File(path).delete() }
        }

    suspend fun deleteImages(paths: List<String>) =
        withContext(Dispatchers.IO) {
            paths.forEach { runCatching { File(it).delete() } }
        }
}