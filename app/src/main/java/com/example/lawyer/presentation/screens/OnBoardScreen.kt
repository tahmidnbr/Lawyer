package com.example.lawyer.presentation.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lawyer.R
import com.example.lawyer.presentation.extra.LucideScale
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryGreen
import com.example.lawyer.ui.theme.PrimaryWhite


@Composable
fun OnBoardScreen(
    navController: NavController,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryGreen)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryWhite,
                    letterSpacing = 2.sp
                )
                Text(
                    "An everything record for a lawman",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryWhite.copy(alpha = 0.5f)
                )
            }

            Icon(
                imageVector = LucideScale,
                contentDescription = "Logo",
                modifier = Modifier.fillMaxSize(.5f),
                tint = PrimaryWhite
            )

//            Image(
//                painter = painterResource(R.drawable.lawyer),
//                contentDescription = null,
//                modifier = Modifier.fillMaxSize(.75f)
//            )

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate("welcome"){
                            popUpTo("splash"){ inclusive = true}
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGold
                    )
                ) {
                    Text(
                        "Continue",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Text(
                    "By Continuing, you are agree to ${stringResource(id = R.string.app_name)}'s Terms of services and Privacy Policy",
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = PrimaryWhite.copy(alpha = .5f)
                )
            }
        }
    }
}