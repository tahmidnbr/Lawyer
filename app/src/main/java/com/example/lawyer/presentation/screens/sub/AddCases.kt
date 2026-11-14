package com.example.lawyer.presentation.screens.sub

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.lawyer.logic.vm.CaseFormVm
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.extra.CourtDropdown
import com.example.lawyer.presentation.extra.CustomTextField
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun AddCases(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: CaseFormVm = koinViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var checked by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when(event) {
                is UiEvent.NavigateTo -> {
                    navController.navigate(event.route){
                        popUpTo("add") { inclusive = true}
                    }
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }

        }
    }

    Column(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Case Details",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 20.sp,
                letterSpacing = 1.sp
            )

            CustomTextField(
                value = uiState.plaintiff,
                onValueChange = vm::onPlaintiffChange,
                placeholder = "Plaintiff"
            )
            Text(
                "vs",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            CustomTextField(
                value = uiState.defendant,
                onValueChange = vm::onDefendantChange,
                placeholder = "Defendant"
            )

            CustomTextField(
                value = uiState.representing,
                onValueChange = vm::onRepresentingChange,
                placeholder = "Representing"
            )
            CourtDropdown(
                selectedCourt = uiState.courtName,
                onCourtSelected = vm::onCourtNameChange
            )

            CustomTextField(
                value = uiState.caseType,
                onValueChange = vm::onCaseTypeChange,
                placeholder = "Case Type"
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CustomTextField(
                    value = uiState.caseFee,
                    onValueChange = vm::onCaseFeeChange,
                    modifier = Modifier.weight(1f),
                    placeholder = "Case Fee"
                )
                CustomTextField(
                    value = uiState.caseNo,
                    onValueChange = vm::onCaseNoChange,
                    modifier = Modifier.weight(1f),
                    placeholder = "Case No."
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Make as priority",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                    ,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Normal
                )

                Box(
                    modifier = Modifier.height(24.dp), // Forces the vertical height container to remain tight
                    contentAlignment = Alignment.Center
                ) {
                    Switch(
                        checked = uiState.isPriority,
                        onCheckedChange = { vm.onPriorityToggle() },
                        modifier = Modifier
                            .scale(0.75f)
                            // Removes the invisible 48dp padding surrounding standard M3 interactive elements
                            .minimumInteractiveComponentSize(),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrimaryWhite,
                            checkedTrackColor = PrimaryGold,
                            checkedBorderColor = PrimaryGold,
                            uncheckedThumbColor = PrimaryGold,
                            uncheckedTrackColor = PrimaryWhite,
                            uncheckedBorderColor = PrimaryGold

                        )
                    )
                }
            }
            val imagePickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetMultipleContents()
            ) { uris: List<Uri> ->
                vm.onImageSelected(uris)
            }

            ImageUploadSection(
                selectedUris    = uiState.selectedImageUris,
                savedPaths      = uiState.savedImagePaths,
                onPickImages    = { imagePickerLauncher.launch("image/*") },
                onRemoveNew     = vm::removeNewImage,
                onRemoveSaved   = vm::removeSavedImage
            )
        }

        Button(
            enabled = uiState.isFormValid && !uiState.isLoading,
            onClick = vm::saveCase,
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGold
            )
        ) {
            if (uiState.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = PrimaryGold
                )
            } else {
                Text("Add Case", style = MaterialTheme.typography.bodySmall)
            }
        }

    }

}

@Composable
fun ImageUploadSection(
    selectedUris: List<Uri>,
    savedPaths: List<String>,
    onPickImages: () -> Unit,
    onRemoveNew: (Uri) -> Unit,
    onRemoveSaved: (String) -> Unit
){

    val totalCount = selectedUris.size + savedPaths.size

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onPickImages,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, PrimaryGold),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryWhite
            )
        ) {
            Column(
                horizontalAlignment =  Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Image,
                    contentDescription = "Chose Image",
                    modifier = Modifier.fillMaxSize(.25f),
                    tint = PrimaryGold
                )
                Text(
                    if (totalCount == 0) "Upload Images"
                    else "Add More Images ($totalCount selected)",
                    style = MaterialTheme.typography.bodySmall,
                    color = PrimaryGold
                )
            }

        }

        if (totalCount > 0){
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Already saved images
                items(savedPaths, key = { it }) { path ->
                    ImageThumb(
                        painter = rememberAsyncImagePainter(File(path)),
                        onRemove = { onRemoveSaved(path) }
                    )
                }
                // Newly picked
                items(selectedUris, key = { it.toString() }) { uri ->
                    ImageThumb(
                        painter = rememberAsyncImagePainter(uri),
                        onRemove = { onRemoveNew(uri) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ImageThumb(painter: Painter, onRemove: () -> Unit) {
    Box(modifier = Modifier.size(72.dp)) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(PrimaryBlack.copy(alpha = 0.6f))
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = PrimaryWhite,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}