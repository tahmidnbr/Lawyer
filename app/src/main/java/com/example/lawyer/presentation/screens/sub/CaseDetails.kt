package com.example.lawyer.presentation.screens.sub

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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
    val uiState  by detailVm.uiState.collectAsStateWithLifecycle()
    val hearingState by hearingVm.uiState.collectAsStateWithLifecycle()
    val case = uiState.caseWithHearings?.case
    val context  = LocalContext.current

    LaunchedEffect(Unit) {
        detailVm.events.collect { event ->
            when (event) {
                is UiEvent.NavigateTo   -> navController.navigate(event.route)
                is UiEvent.NavigateBack -> navController.popBackStack()
                is UiEvent.ShowToast    -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (uiState.isLoading) return

    val hearings = hearingState.hearings  // already sorted by date DESC from DAO

    LazyColumn(
        modifier = modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 32.dp),
        //verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CaseHeadings(
                caseFee = case?.caseFee.toString(),
                caseNo = case?.caseNo ,
                title = "${case?.plaintiff} vs ${case?.defendant}",
                isPriority = case?.isPriority,
                onEdit = { navController.navigate("write/$caseId") },
                onStar = {detailVm.togglePriority()},
                onShare = {}
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // ── Section label ─────────────────────────────────────
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Case History",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "Proceedings in hearing sequence",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryBlack.copy(.5f)
                    )
                }

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Proceeding",
                    modifier = Modifier
                        .clickable(
                            onClick = {navController.navigate("write/$caseId")}
                        )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ── Empty state ───────────────────────────────────────
        if (hearings.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No proceedings yet.\nTap '+' to begin.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryBlack.copy(.4f)
                    )
                }
            }
        }

        // ── Timeline ──────────────────────────────────────────
        itemsIndexed(
            items = hearings,
            key   = { _, p -> p.id }
        ) { index, hearing ->
            HearingCard(
                hearing = hearing,
                isLatest   = index == 0,
                isLast     = index == hearings.lastIndex,
                onDelete   = { hearingVm.deleteHearing(hearing.id) }
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
    isPriority: Boolean?,
    vm: CaseDetailVm  =  koinViewModel ()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Column {

        // ── Case No + action icons ────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                Modifier.background(PrimaryGold, RoundedCornerShape(4.dp))
            ) {
                Text(
                    "CASE NO. ${caseNo?.ifBlank { "—" }}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = PrimaryWhite
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                IconButton(
                    onClick = onStar ,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PrimaryBlack.copy(alpha = .05f)
                    )
                ) {
                    Icon(
                        imageVector = if (isPriority == true) Icons.Filled.Star else Icons.Outlined.StarOutline,
                        contentDescription = "Favourite",
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryGold
                    )
                }
                IconButton(
                    onClick = onShare,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryBlack.copy(alpha = .05f))
                ) {
                    Icon(Icons.Outlined.Share,       contentDescription = "Share",     modifier = Modifier.size(18.dp))
                }
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = PrimaryBlack.copy(alpha = .05f))
                ) {
                    Icon(Icons.Outlined.EditNote,    contentDescription = "Edit",      modifier = Modifier.size(18.dp))
                }
            }
        }

        // Title
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title.ifBlank { "— vs —" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                letterSpacing = 1.sp,
                color = PrimaryGreen
            )
            Text(
                text = "This case record contains confidential legal proceedings. All timelines, milestones, and hearing dates displayed are synchronized in real-time with the official court database protocols.",
                style = MaterialTheme.typography.labelSmall,
                color = PrimaryBlack.copy(alpha = .5f), // A muted gold/brown that matches your theme accent
                //modifier = Modifier.padding(horizontal = 12.dp) // Adds breathing room before Case History
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = "Case Fee",
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryGreen
                    )

                    Text(
                        caseFee ?:  "0.0",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryGreen
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Completed",
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    showDeleteDialog = true
                                }
                            )
                            .size(18.dp),
                        tint = DangerRed,
                    )
                }

                if (showDeleteDialog){
                    AlertDialog(
                        onDismissRequest = {showDeleteDialog =  false},
                        onConfirmation = {
                            vm.deleteCase()
                            showDeleteDialog  = false
                        },
                        dialogTitle =  "Are your sure?",
                        dialogText = "Confirming will erase all the data about these case and this can't be undone",
                        icon = Icons.Default.Delete
                    )
                }
            }

        }
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
        modifier = Modifier.fillMaxWidth()
    ) {
        // ── Timeline column: dot + line ───────────────────────
        Column(
            modifier = Modifier
                .weight(.1f)
                .padding(top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .then(
                        if (isLatest)
                            Modifier.background(PrimaryGreen, CircleShape)
                        else
                            Modifier.border(2.dp, PrimaryGreen, CircleShape)
                    )
            )
            // Connector line — hidden on last item
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(72.dp)           // matches card height roughly
                        .background(PrimaryGreen.copy(alpha = 0.25f))
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // ── Proceeding card ───────────────────────────────────
        Card(
            modifier = Modifier
                .clickable{}
                .weight(.9f)
                .padding(bottom = if (isLast) 0.dp else 12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isLatest) PrimaryGold else PrimaryBlack.copy(alpha = 0.06f)
            ),
            shape = RoundedCornerShape(8.dp)
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
                        dateFormatter.format(Date(hearing.hearingDate)).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isLatest) PrimaryWhite.copy(.75f) else PrimaryBlack.copy(.5f)
                    )
                    // Delete on long press feel — small icon
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .size(13.dp)
                            .clickable { onDelete() },
                        tint = if (isLatest) PrimaryWhite.copy(.5f) else PrimaryBlack.copy(.3f)
                    )
                }
                Text(
                    hearing.purposeOrProceeding,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isLatest) PrimaryWhite else PrimaryBlack
                )
            }
        }
    }
}