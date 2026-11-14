package com.example.lawyer.presentation.screens.sub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import com.example.lawyer.ui.theme.PrimaryBlack
import java.io.File

@Composable
fun ImageViewer(
    modifier: Modifier = Modifier,
    imagePath: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBlack)
    ) {
        Image(
            painter = rememberAsyncImagePainter(File(imagePath)),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}