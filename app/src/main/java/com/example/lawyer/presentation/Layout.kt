package com.example.lawyer.presentation

import android.os.Build
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.lawyer.presentation.extra.CalendarEmpty
import com.example.lawyer.presentation.extra.CalendarEmptyFilled
import com.example.lawyer.presentation.extra.HomeRegular
import com.example.lawyer.R
import com.example.lawyer.presentation.extra.FluentuiSystemIconsFolder
import com.example.lawyer.presentation.extra.FluentuiSystemIconsFolderOpen
import com.example.lawyer.presentation.extra.FluentuiSystemIconsHome
import com.example.lawyer.presentation.screens.Screens
import com.example.lawyer.ui.theme.DangerRed
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import java.util.UUID

val screens = listOf(
    Screens("home","Home", FluentuiSystemIconsHome, HomeRegular),
    Screens("cases","Cases", FluentuiSystemIconsFolderOpen, FluentuiSystemIconsFolder),
    Screens("dates","Dates", CalendarEmptyFilled, CalendarEmpty),
    Screens("menu","Menu", Icons.AutoMirrored.Rounded.MenuOpen, Icons.Outlined.Menu),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Topbar(){
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet  by remember { mutableStateOf(false) }

    var avatarSeed by remember { mutableStateOf(UUID.randomUUID().toString()) }

    var isLinked by remember { mutableStateOf(false) }

    if (showBottomSheet){
        BottomSheet(
            onDismiss = {showBottomSheet = false},
            sheetState = sheetState,
            avatarSeed =  avatarSeed
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(PrimaryWhite)
            .statusBarsPadding()
            .fillMaxWidth()

    ) {
        Image(
            painterResource(R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(38.dp)
                .clip(CircleShape)
                .clickable{
                    showBottomSheet =  true
                }
        )

        IconButton(
            onClick = {
                isLinked = !isLinked
                Toast.makeText(context, "In Development", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(
                imageVector = if (isLinked) Icons.Default.LinkOff else Icons.Default.Link,
                contentDescription = null
            )
        }
    }
}

@Composable
fun CommonTopBar(
    navController: NavController,
    content : @Composable () -> Unit = {}
){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight(.1f)
            .background(PrimaryWhite)
            .fillMaxWidth()
            .statusBarsPadding()

    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBackIosNew,
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    onClick = {
                        navController.popBackStack()
                    }
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(18.dp)
        )

        content()
    }
}

@Composable
fun Bottombar(
    selectedIndex: Any,
    onSelect: (Int) -> Unit = {},
    hazeState: HazeState
) {
    val blurModifier = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Modifier.hazeEffect(
            state = hazeState,
            style = HazeStyle(
                tint = HazeTint(PrimaryBlack.copy(alpha = 0.75f)),
                blurRadius = 8.dp,
                noiseFactor = 0.01f
            )
        )
    } else {
        Modifier.background(PrimaryBlack)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Move insets here to push the floating bar above the navigation handle
            .padding(start = 12.dp, end = 12.dp, bottom = 6.dp) // Adds the floating margins
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
                .clip(RoundedCornerShape(12.dp)) // Clips the blur and background to a pill shape
                .then(blurModifier)
                .padding(8.dp), // Padding inside the floating bar
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            screens.forEachIndexed { index, screen -> // Fixed shadow variable naming conflict
                val isSelected = selectedIndex == index
                Column(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { onSelect(index) },
                            indication = null
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isSelected) screen.selected else screen.unSelected,
                        contentDescription = null,
                        tint = if (isSelected) PrimaryGold else PrimaryWhite.copy(alpha = .5f),
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = screen.text,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) PrimaryGold else PrimaryWhite.copy(.5f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    avatarSeed: String,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PrimaryWhite,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painterResource(R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .size(110.dp)
                        .clip(CircleShape)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        avatarSeed,
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Sync your profile with cloud",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(.85f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGreen
                    ),
                ) {
                    Text(
                        "Sync with Google",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryWhite,
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(.85f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, PrimaryGold)
                ) {
                    Text(
                        "Import Existing Case",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryGold,
                    )
                }
            }


        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoadingIcon(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_pulse")

    val alphaValue by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "opacity"
    )

    Icon(
        painter = painterResource(R.drawable.lucide_scale),
        contentDescription = "Loading",
        modifier = modifier
            .size(48.dp)
            // CRITICAL FIX: Pass a lambda block {} instead of assigning directly
            .graphicsLayer {
                alpha = alphaValue
            },
        tint = PrimaryGreen,
    )
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )
        },
        text = {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    "Confirm",
                    color = DangerRed
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    "Dismiss",
                    color = PrimaryGreen.copy(.5f)
                )
            }
        },
        shape = RoundedCornerShape(12.dp),
        containerColor = PrimaryWhite,
    )
}