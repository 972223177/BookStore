package com.ly.book.page.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ly.book.route.NavRoute
import com.ly.book.theme.colorBlack242126
import com.ly.book.theme.colorGreen00D6D8
import com.ly.book.theme.colorGreen00D6D8_10
import com.ly.book.utils.LocalNavController
import com.ly.book.utils.toast
import com.ly.book.utils.zeroBtnElevation

@Composable
fun SignUpPage(viewModel: LoginViewModel, navToMain: () -> Unit) {
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.event.collect {
            when (it) {
                is LoginEvent.ErrorMsg -> toast(it.value)
                LoginEvent.RegisterSuccess -> navToMain()
                else -> {}
            }
        }
    })
    Scaffold(floatingActionButton = {
        val navController = LocalNavController.current
        FloatingActionButton(onClick = {
            val hasLoginDest =
                navController.backQueue.firstOrNull { it.destination.route == NavRoute.Login.SignIn } != null
            if (hasLoginDest) {
                navController.popBackStack()
            } else {
                navController.navigate(NavRoute.Login.SignIn) {
                    popUpTo(NavRoute.Login.SignUp) {
                        inclusive = true
                    }
                }
            }
        }, modifier = Modifier.padding(bottom = 300.dp, end = 10.dp)) {
            Text(text = "登录", fontSize = 12.sp, color = Color.White)
        }
    }) {
        Surface(modifier = Modifier.padding(it)) {
            DisposableEffect(viewModel) {
                onDispose {
                    viewModel.dispatch(LoginAction.DeleteInfo)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        "注册",
                        fontSize = 24.sp,
                        color = colorBlack242126,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 70.dp)
                    )
                    LoginTextField(
                        "昵称",
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp),
                        keyboardType = KeyboardType.Text
                    ) { name ->
                        viewModel.dispatch(LoginAction.UpdateName(name))
                    }
                    LoginTextField(
                        "邮箱",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 20.dp),
                        keyboardType = KeyboardType.Email
                    ) { account ->
                        viewModel.dispatch(LoginAction.UpdateAccount(account))
                    }

                    LoginTextField(
                        "密码",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 20.dp),
                        hideInput = true,
                        keyboardType = KeyboardType.Password
                    ) { pwd ->
                        viewModel.dispatch(LoginAction.UpdatePwd(pwd))
                    }
                    SignUpBtn(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 50.dp),
                        onClick = {
                            viewModel.dispatch(LoginAction.Register)
                        })
                }
                Box {
                    SignProtocol(
                        checked = viewModel.state.protocolAccept,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                        onChecked = { accept ->
                            viewModel.dispatch(LoginAction.UpdateProtocolAccept(accept))
                        }, onProtocolClick = {
                            //todo
                        })
                }
            }
        }
    }
}

@Composable
private fun SignUpBtn(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(90.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorGreen00D6D8_10),
        elevation = zeroBtnElevation
    ) {
        Text(
            text = "注册",
            fontSize = 17.sp,
            color = colorGreen00D6D8,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun SignUpPre() {
    SignUpPage(viewModel = hiltViewModel()) {}
}