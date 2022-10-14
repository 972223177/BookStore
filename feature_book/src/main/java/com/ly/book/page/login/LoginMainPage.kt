package com.ly.book.page.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ly.book.R
import com.ly.book.route.NavRoute
import com.ly.book.theme.colorGreen00D6D8
import com.ly.book.utils.LocalNavController
import com.ly.book.utils.zeroBtnElevation

@Composable
fun LoginMainPage() {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(key1 = systemUiController, effect = {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = false)
        onDispose {
            systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)
        }
    })
    Scaffold(backgroundColor = colorGreen00D6D8) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_login_main),
                contentDescription = "bg_login",
                contentScale = ContentScale.Crop,
                modifier = Modifier.align(Alignment.Center)
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp, vertical = 15.dp)
            ) {
                val navController = LocalNavController.current
                Button(
                    onClick = {
                        navController.navigate(NavRoute.Login.SignIn)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(90.dp),
                    elevation = zeroBtnElevation,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(
                        text = "登录",
                        fontSize = 17.sp,
                        color = colorResource(id = R.color.green00D6D8),
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = {
                        navController.navigate(NavRoute.Login.SignUp)
                    },
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(90.dp),
                    elevation = zeroBtnElevation,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow)
                ) {
                    Text(
                        text = "注册",
                        fontSize = 17.sp,
                        color = colorResource(id = R.color.green00D6D8),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
