package com.ly.book.page.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ly.book.R
import com.ly.book.utils.zeroBtnElevation

@Composable
fun SignUpPage(viewModel: LoginViewModel, navToMain: () -> Unit) {
    Scaffold {
        Surface(modifier = Modifier.padding(it)) {
            DisposableEffect(viewModel) {
                onDispose {
                    viewModel.deleteSignInInfo()
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
                        color = colorResource(R.color.black242126),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 70.dp)
                    )
                    LoginTextField(
                        "Name",
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp),
                        keyboardType = KeyboardType.Text
                    ) { account ->
                        viewModel.updateName(account)
                    }
                    LoginTextField(
                        "Email",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 20.dp),
                        keyboardType = KeyboardType.Email
                    ) { account ->
                        viewModel.updateAccount(account)
                    }

                    LoginTextField(
                        "Password",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 20.dp),
                        hideInput = true,
                        keyboardType = KeyboardType.Password
                    ) { pwd ->
                        viewModel.updatePwd(pwd)
                    }
                    SignUpBtn(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 50.dp),
                        onClick = {
                            viewModel.register(navToMain)
                        })
                }
                Box {
                    SignProtocol(
                        checked = viewModel.protocolAccept,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 15.dp),
                        onChecked = { accept ->
                            viewModel.protocolAccept = accept
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
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.green00D6D8_10)),
        elevation = zeroBtnElevation
    ) {
        Text(
            text = "注册",
            fontSize = 17.sp,
            color = colorResource(id = R.color.green00D6D8),
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun SignUpPre() {
    SignUpPage(viewModel = hiltViewModel()) {}
}