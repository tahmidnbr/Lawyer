package com.example.lawyer.presentation.screens.sub

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.lawyer.logic.vm.CaseDetailVm
import com.example.lawyer.logic.vm.HearingVm
import com.example.lawyer.presentation.AlertDialog
import com.example.lawyer.presentation.UiEvent
import com.example.lawyer.presentation.model.Hearing
import com.example.lawyer.ui.theme.DangerRed
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CaseDetails(
    caseId: Long,
    modifier: Modifier = Modifier,
    navController: NavController,
    detailVm: CaseDetailVm = koinViewModel(),
    hearingVm: HearingVm = koinViewModel()
) {
    val uiState by detailVm.uiState.collectAsStateWithLifecycle()
    val hearingState by hearingVm.uiState.collectAsStateWithLifecycle()
    val case = uiState.caseWithHearings?.case
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        detailVm.events.collect { event ->
            when (event) {
                is UiEvent.NavigateTo -> navController.navigate(event.route)
                is UiEvent.NavigateBack -> navController.popBackStack()
                is UiEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                else -> Unit
            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PrimaryGold)
        }
        return
    }

    val hearings = hearingState.hearings

    LazyColumn(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CaseHeadings(
                caseNo = case?.caseNo,
                title = "${case?.plaintiff ?: "—"} vs ${case?.defendant ?: "—"}",
                caseFee = case?.caseFee?.toString(),
                imageUri = case?.imageUri,
                isPriority = case?.isPriority,
                onEdit = { navController.navigate("write/$caseId") },
                onStar = { detailVm.togglePriority() },
                onShare = {},
                vm = detailVm
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Case History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = PrimaryBlack,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Proceedings in hearing sequence",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        color = PrimaryBlack.copy(alpha = 0.5f)
                    )
                }

                IconButton(
                    onClick = { navController.navigate("write/$caseId") },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Proceeding",
                        tint = PrimaryGold,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        if (hearings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No proceedings recorded yet.\nTap '+' to add the first proceeding.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = PrimaryBlack.copy(alpha = 0.4f)
                    )
                }
            }
        }

        itemsIndexed(
            items = hearings,
            key = { _, p -> p.id }
        ) { index, hearing ->
            HearingCard(
                hearing = hearing,
                isLatest = index == 0,
                isLast = index == hearings.lastIndex,
                onDelete = { hearingVm.deleteHearing(hearing.id) }
            )
        }
    }
}
@Composable
fun CaseHeadings(
    caseNo: String?,
    title: String,
    onEdit: () -> Unit,
    onStar: () -> Unit,
    onShare: () -> Unit,
    caseFee: String?,
    imageUri: String?,
    isPriority: Boolean?,
    vm: CaseDetailVm
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // ── Top Bar: Case Badge + Action Buttons ─────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(PrimaryGold, RoundedCornerShape(6.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "CASE NO. ${caseNo?.ifBlank { "—" } ?: "—"}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlack,
                    fontSize = 12.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = onStar,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PrimaryBlack.copy(alpha = 0.05f)
                    )
                ) {
                    Icon(
                        imageVector = if (isPriority == true) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryGold
                    )
                }
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PrimaryBlack.copy(alpha = 0.05f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share",
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryBlack
                    )
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PrimaryBlack.copy(alpha = 0.05f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = "Edit",
                        modifier = Modifier.size(20.dp),
                        tint = PrimaryBlack
                    )
                }
            }
        }

        // ── Title & Meta Description ──────────────────────────────
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = title.ifBlank { "— vs —" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                letterSpacing = 0.5.sp,
                color = PrimaryBlack
            )

            Text(
                text = "Confidential legal proceeding record. Synchronized with internal case management database protocols.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 12.sp,
                color = PrimaryBlack.copy(alpha = 0.5f)
            )
        }

        // ── Optional Document Preview Attachment ──────────────────
        if (!imageUri.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, PrimaryBlack.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            ) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Case Document Attachment",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // ── Case Fee & Delete Row ─────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Money,
                    contentDescription = "Case Fee",
                    modifier = Modifier.size(20.dp),
                    tint = PrimaryGold
                )
                Text(
                    text = "Fee: BDT ${caseFee?.ifBlank { "0.0" } ?: "0.0"}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = PrimaryBlack
                )
            }

            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Case",
                    modifier = Modifier.size(20.dp),
                    tint = DangerRed
                )
            }
        }

        HorizontalDivider(
            color = PrimaryBlack.copy(alpha = 0.08f),
            thickness = 1.dp
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Case Record?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = "This action will permanently erase all hearings, documents, and logs associated with this case. This cannot be undone.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.deleteCase()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Delete", color = DangerRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = PrimaryBlack)
                }
            }
        )
    }
}

@Composable
fun HearingCard(
    isLatest: Boolean,
    isLast: Boolean,
    onDelete: () -> Unit,
    hearing: Hearing
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height( IntrinsicSize.Min)
    ) {
        // ── Timeline Marker (Dot + Vertical Line) ──────────────────
        Column(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(12.dp)
                    .then(
                        if (isLatest)
                            Modifier.background(PrimaryGold, CircleShape)
                        else
                            Modifier.border(2.dp, PrimaryGold, CircleShape)
                    )
            )

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(PrimaryGold.copy(alpha = 0.3f))
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // ── Proceeding Card Container ──────────────────────────────
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isLatest) PrimaryGold else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dateFormatter.format(Date(hearing.hearingDate)).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (isLatest) PrimaryBlack.copy(alpha = 0.7f) else PrimaryBlack.copy(alpha = 0.5f)
                    )

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Proceeding",
                            modifier = Modifier.size(14.dp),
                            tint = if (isLatest) PrimaryBlack.copy(alpha = 0.6f) else PrimaryBlack.copy(alpha = 0.4f)
                        )
                    }
                }

                Text(
                    text = hearing.purposeOrProceeding,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = PrimaryBlack
                )
            }
        }
    }
}