@file:OptIn(ExperimentalMaterialApi::class)

package com.ly.book.page.main.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ly.book.page.main.MainViewModel
import com.ly.book.theme.colorBlack242126
import com.ly.book.theme.colorGrayAFC1C4
import com.ly.book.theme.colorGrayD1DDDF
import com.ly.book.theme.colorGreen00D6D8
import com.ly.book.utils.PreviewProvideNavLocal
import com.ly.book.utils.rippleClick
import com.ly.book.utils.unRippleClick
import kotlinx.coroutines.launch

@Composable
fun MinePage(viewModel: MainViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val density = LocalDensity.current
    val peekHeight = remember(density) {
        with(density) {
            (context.resources.displayMetrics.heightPixels * (406f / 812)).toDp()
        }
    }
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(scaffoldState = scaffoldState, sheetContent = {
        BottomSheetContent(logout = {
            viewModel.logout()
        }, onSwitch = {
            scope.launch {
                with(scaffoldState.bottomSheetState) {
                    if (isCollapsed) {
                        expand()
                    } else {
                        collapse()
                    }
                }
            }
        })
    }, sheetPeekHeight = peekHeight) {
        Column(
            modifier = Modifier
                .padding(it)
                .statusBarsPadding()
        ) {
            Text(
                text = "我的",
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold,
                color = colorBlack242126,
                modifier = Modifier.padding(top = 20.dp, start = 20.dp)
            )
            UserInfoPanel(modifier = Modifier.padding(20.dp))
        }
    }
}

@Composable
private fun UserInfoPanel(modifier: Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {

    }
}

@Composable
private fun BottomSheetContent(logout: () -> Unit, onSwitch: () -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 10.dp)
                .height(6.dp)
                .width(50.dp)
                .background(color = colorGrayAFC1C4, shape = RoundedCornerShape(20.dp))
                .unRippleClick(onSwitch)
        )
        MenuCategoryTitle(
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp, start = 20.dp),
            title = "我的订阅"
        )
        MenuItem(title = "已更新", suffix = "0", suffixColor = colorGreen00D6D8) {}
        MenuItem(title = "所有书籍", suffix = "0") {}
        MenuItem(title = "有声读物", suffix = "0") {

        }
        MenuCategoryTitle(
            modifier = Modifier.padding(top = 40.dp, bottom = 10.dp, start = 20.dp),
            title = "账号"
        )
        MenuItem(title = "修改信息") {

        }
        MenuItem(title = "分享你的用户卡片") {

        }

        MenuItem(title = "退出登录", onClick = logout)

        Box(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun MenuCategoryTitle(modifier: Modifier, title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = colorGrayD1DDDF,
        modifier = modifier
    )
}

@Composable
private fun MenuItem(
    title: String,
    suffix: String = "",
    suffixColor: Color = colorGrayD1DDDF,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(45.dp)
            .fillMaxWidth()
            .rippleClick(onClick)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 17.sp, color = colorBlack242126, fontWeight = FontWeight.Bold)
        Text(text = suffix, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = suffixColor)
    }
}

@Preview
@Composable
fun MinePagePre() {
    PreviewProvideNavLocal {
        MinePage(hiltViewModel())
    }
}