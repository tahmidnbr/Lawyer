package com.example.lawyer.presentation.screens.sub

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.lawyer.logic.vm.CaseDetailVm
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.compose.viewmodel.koinViewModel
import java.io.File


@Composable
fun Attachments(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: CaseDetailVm = koinViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val paths = uiState.case?.imagePaths ?: emptyList()

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        vm.addImages(uris)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Add image tile
        item {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PrimaryGold.copy(alpha = 0.1f))
                    .clickable {
                        imagePicker.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Add images",
                        tint = PrimaryGold,
                        modifier = Modifier.size(28.dp)
                    )

                    Text(
                        "Add Image",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryGold
                    )
                }
            }
        }

        items(paths, key = { it }) { path ->
            Image(
                painter = rememberAsyncImagePainter(File(path)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        navController.navigate(
                            "image_viewer/${Uri.encode(path)}"
                        )
                    }
            )
        }
    }
}
