package com.ly.book.page.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ly.book.theme.colorBlack242126
import com.ly.book.theme.colorGrayAFC1C4
import com.ly.book.theme.colorGreen00D6D8
import com.ly.book.theme.colorGreen00D6D8_10
import com.ly.book.utils.rippleClick
import com.ly.book.utils.zeroBtnElevation

@Composable
fun SignInPage(viewModel: LoginViewModel, navToMain: () -> Unit = {}, initFirst: () -> Unit = {}) {
    Scaffold {
        Surface(modifier = Modifier.padding(it)) {
            DisposableEffect(viewModel, initFirst) {
                initFirst()
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
                        "登录",
                        fontSize = 24.sp,
                        color = colorBlack242126,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 20.dp, top = 70.dp)
                    )
                    LoginTextField(
                        "Email",
                        defaultValue = viewModel.rememberAccount,
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp),
                        keyboardType = KeyboardType.Email
                    ) { account ->
                        viewModel.updateAccount(account)
                    }

                    LoginTextField(
                        "Password",
                        defaultValue = viewModel.rememberPwd,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 20.dp),
                        hideInput = true,
                        keyboardType = KeyboardType.Password
                    ) { pwd ->
                        viewModel.updatePwd(pwd)
                    }
                    Box {
                        SignInMenu(
                            checked = viewModel.rememberMe,
                            modifier = Modifier.padding(horizontal = 25.dp, vertical = 10.dp),
                            onRemember = { remember ->
                                viewModel.rememberMe = remember
                            },
                            onForget = {

                            })
                    }
                    SignInBtn(modifier = Modifier.padding(horizontal = 20.dp), onClick = {
                        viewModel.login(navToMain)
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
private fun SignInMenu(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onRemember: (Boolean) -> Unit,
    onForget: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var rememberMe by remember(checked) {
                mutableStateOf(checked)
            }
            Checkbox(
                checked = rememberMe,
                onCheckedChange = {
                    rememberMe = it
                    onRemember(it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorGreen00D6D8,
                    uncheckedColor = colorGrayAFC1C4
                ),
                modifier = Modifier.scale(0.7f)
            )
            Text(
                text = "记住密码",
                fontSize = 12.sp,
                color = colorGrayAFC1C4,
                modifier = Modifier.rippleClick {
                    rememberMe = !rememberMe
                }
            )
        }
        Button(
            onClick = onForget, colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = zeroBtnElevation
        ) {
            Text(
                text = "忘记密码",
                fontSize = 12.sp,
                color = colorGrayAFC1C4,
            )
        }

    }
}

@Composable
private fun SignInBtn(modifier: Modifier = Modifier, onClick: () -> Unit) {
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
            text = "登录",
            fontSize = 17.sp,
            color = colorGreen00D6D8,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SignProtocol(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onChecked: (Boolean) -> Unit,
    onProtocolClick: (String) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        var accept by remember(checked) {
            mutableStateOf(checked)
        }
        Checkbox(
            checked = accept,
            onCheckedChange = {
                accept = it
                onChecked(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = colorGreen00D6D8,
                uncheckedColor = colorGrayAFC1C4
            ),
            modifier = Modifier.scale(0.7f)
        )
        ProtocolText(onClick = onProtocolClick)
    }
}

@Composable
private fun ProtocolText(onClick: (String) -> Unit) {

    val normalSpanStyle = remember {
        SpanStyle(fontSize = 11.sp, color = Color.LightGray)
    }
    val protoSpanStyle = remember {
        SpanStyle(fontSize = 11.sp, color = Color.Black)
    }
    val tag = remember {
        "Url"
    }
    val protocol = buildAnnotatedString {
        withStyle(normalSpanStyle) {
            append("登录之前需要创建账号，您也可以已游客身份访问，但在这之前需要您先同意")
        }
        pushStringAnnotation(tag, "Terms of Use")
        withStyle(protoSpanStyle) {
            append("使用条款")
        }
        pop()

        withStyle(normalSpanStyle) {
            append("和")
        }

        pushStringAnnotation(tag, "Privacy Policy")
        withStyle(protoSpanStyle) {
            append("隐私协议")
        }
        pop()
    }
    ClickableText(text = protocol, onClick = {
        val url = protocol.getStringAnnotations(tag, it, it).firstOrNull()?.item
        if (!url.isNullOrEmpty()) {
            onClick(url)
        }
    })
}

@Preview
@Composable
fun SignInPre() {
    SignInPage(hiltViewModel()) {}
}