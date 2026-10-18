package com.example.lawyer.presentation.screens.sub

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.lawyer.logic.vm.CaseFormVm
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.screens.home.CustomTextField
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.androidx.compose.koinViewModel
import coil3.compose.AsyncImage

@Composable
fun AddCases(
    modifier: Modifier = Modifier,
    navController: NavController,
    vm: CaseFormVm = koinViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> {
                    navController.navigate(event.route) {
                        popUpTo("add") { inclusive = true }
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
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        // ── FIXED TOP HEADER (Title & Priority Switch) ──────────────────
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Case Details",
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 26.sp,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Make as priority",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Switch(
                    checked = uiState.isPriority,
                    onCheckedChange = { vm.onPriorityToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = PrimaryGold,
                        checkedTrackColor = PrimaryGold.copy(alpha = 0.5f)
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 4.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        }

        // ── SCROLLABLE FORM FIELDS ──────────────────────────────────────
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            CustomOutlinedTextField(
                value = uiState.plaintiff,
                onValueChange = vm::onPlaintiffChange,
                label = "Plaintiff"
            )

            Text(
                "vs",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            CustomOutlinedTextField(
                value = uiState.defendant,
                onValueChange = vm::onDefendantChange,
                label = "Defendant"
            )

            CustomOutlinedTextField(
                value = uiState.representing,
                onValueChange = vm::onRepresentingChange,
                label = "Representing"
            )

            CustomOutlinedTextField(
                value = uiState.courtName,
                onValueChange = vm::onCourtNameChange,
                label = "Court"
            )

            CustomOutlinedTextField(
                value = uiState.caseType,
                onValueChange = vm::onCaseTypeChange,
                label = "Case Type"
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomOutlinedTextField(
                    value = uiState.caseFee,
                    onValueChange = vm::onCaseFeeChange,
                    modifier = Modifier.weight(1f),
                    label = "Case Fee"
                )
                CustomOutlinedTextField(
                    value = uiState.caseNo,
                    onValueChange = vm::onCaseNoChange,
                    modifier = Modifier.weight(1f),
                    label = "Case No."
                )
            }

            ImagePickerField(
                selectedImageUri = uiState.selectedImageUri,
                onImageSelected = vm::onImageSelected
            )

            Spacer(Modifier.height(8.dp))
        }

        // ── FIXED BOTTOM BUTTON ────────────────────────────────────────
        Button(
            enabled = uiState.isFormValid && !uiState.isLoading,
            onClick = vm::saveCase,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryGold,
                contentColor = PrimaryBlack,
                disabledContainerColor = PrimaryGold.copy(alpha = 0.4f),
                disabledContentColor = PrimaryBlack.copy(alpha = 0.5f)
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = PrimaryBlack
                )
            } else {
                Text(
                    text = "Add Case",
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlack
                )
            }
        }
    }
}
@Composable
fun ImagePickerField(
    selectedImageUri: String?,
    onImageSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        onImageSelected(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val uri = saveBitmapToCache(context, it)
            onImageSelected(uri)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Image Source", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = { Text("Choose where you want to import the case document/image from.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Gallery", color = PrimaryGold, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    cameraLauncher.launch(null)
                }) {
                    Text("Camera", color = PrimaryGold, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .clickable { showDialog = true },
        contentAlignment = Alignment.Center
    ) {
        if (!selectedImageUri.isNullOrEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Case Image",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text("Image Attached", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                    Text("Tap to change image", fontSize = 13.sp, color = Color.Black.copy(alpha = 0.5f))
                }
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "📎", fontSize = 18.sp)
                Text(
                    text = "Attach Document / Case Image",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun saveBitmapToCache(context: android.content.Context, bitmap: android.graphics.Bitmap): Uri {
    val cacheFile = java.io.File(context.cacheDir, "case_image_${System.currentTimeMillis()}.jpg")
    val fos = java.io.FileOutputStream(cacheFile)
    bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, fos)
    fos.flush()
    fos.close()
    return androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        cacheFile
    )
}

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(
                text = label,
                fontSize = 15.sp,
                color = Color.Black.copy(alpha = 0.5f)
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryGold,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            focusedLabelColor = PrimaryGold,
            cursorColor = PrimaryGold
        )
    )
}