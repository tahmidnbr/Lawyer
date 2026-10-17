package com.example.lawyer.presentation.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.FolderCopy
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.DriveFileRenameOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FolderCopy
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lawyer.presentation.LoadingIcon
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite

@Composable
fun MenuScreen(
    contentPaddingValues: PaddingValues
){
    LazyColumn(
        contentPadding = contentPaddingValues,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(PrimaryWhite),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            MenuSection(
                "General"
            ){
                SettingItem(
                    Icons.Outlined.Edit,
                    "Case Naming Convention",
                    "Set case naming convention"
                )

                SettingItem(
                    Icons.Outlined.FolderCopy,
                    "Documents & Pleadings",
                    "Documents attached with cases"
                )


                SettingItem(
                    Icons.Outlined.AccountBalanceWallet,
                    "Fees & Billings",
                    "Fees and other billings"
                )
            }
        }

        item {
            MenuSection(
                "Application"
            ){
                SettingItem(
                    Icons.Outlined.Sync,
                    "Auto Sync",
                    "Turn on auto sync"
                )

                SettingItem(
                    Icons.Outlined.Update,
                    "Check For Update",
                    "Checks for new update"
                )

            }
        }

        item {
            MenuSection(
                "Others"
            ){
                SettingItem(
                    Icons.Outlined.Notifications,
                    "Notifications",
                    "Turn on notifications"
                )

                SettingItem(
                    Icons.Outlined.Download,
                    "Profile Info Download",
                    "Download profile information"
                )

            }
        }
    }
}