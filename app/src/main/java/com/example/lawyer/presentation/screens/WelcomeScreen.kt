package com.example.lawyer.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lawyer.AppPreferences
import com.example.lawyer.R
import com.example.lawyer.ui.theme.PrimaryBlack
import com.example.lawyer.ui.theme.PrimaryGold
import com.example.lawyer.ui.theme.PrimaryWhite
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun WelcomeScreen(
    navController: NavController,
    prefs: AppPreferences = koinInject()
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff16302B))
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ){
            Icon(
                imageVector = Icons.Rounded.ArrowBackIosNew,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clickable(
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                    .size(18.dp),
                contentDescription = null,
                tint = PrimaryWhite
            )

            Image(
                painter = painterResource(R.drawable.download),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(.95f)
                    .align(Alignment.BottomCenter)
                ,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(PrimaryWhite)
                .navigationBarsPadding(),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
            ) {
                Column(
                    modifier = Modifier,
                ) {
                    Text(
                        "Get Started",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                    Text(
                        "Select your preferred method to continue",
                        color = PrimaryBlack.copy(alpha = .5f),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        Toast.makeText(context, "This feature is in development", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryGold
                    )
                ) {
                    Text(
                        "Continue with google",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            prefs.setOnboarded()
                            navController.navigate("screenGraph"){
                                popUpTo("welcome") {inclusive = true}
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, PrimaryGold)
                ) {
                    Text(
                        "Continue without sync",
                        style = MaterialTheme.typography.bodySmall,
                        color = PrimaryGold,
                    )
                }

                Text(
                    "I agree with the Terms & Conditions and Privacy Policy",
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = PrimaryBlack.copy(alpha = .5f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall
                )

            }

        }
    }
}