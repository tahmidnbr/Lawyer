package com.example.lawyer.presentation.screens.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryWhite

@Composable
fun MenuSection(
    title: String ,
    content: @Composable () -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = PrimaryWhite
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(2.dp, PrimaryGold.copy(.05f))
        ) {
            Column(
                modifier = Modifier,
                //verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                content()
            }
        }
    }
}


@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    badgeText: String? = null,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container with Light Background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(PrimaryGold.copy(.15f), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF0F1E36),
                    modifier = Modifier.size(20.dp)
                )
            }

            // Text Content Area
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Title & Badge Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        color = Color(0xFF0F1E36),
                        style = MaterialTheme.typography.headlineSmall,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (badgeText != null) {
                        Text(
                            text = "•  $badgeText",
                            color = Color(0xFF7A7A7A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                // Subtitle
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF7A7A7A),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            // End Chevron Arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF7A7A7A),
                modifier = Modifier.size(14.dp)
            )
        }

//        Spacer(
//            modifier = Modifier
//                .height(16.dp)
//        )
//        HorizontalDivider(
//            modifier = Modifier
//                .fillMaxWidth(),
//            thickness = 1.dp,
//            color = PrimaryGold.copy(.5f)
//        )
    }

}