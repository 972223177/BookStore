@file:OptIn(ExperimentalMaterialApi::class)

package com.ly.book.page.main.mine

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ly.book.R
import com.ly.book.page.main.MainAction
import com.ly.book.page.main.MainViewModel
import com.ly.book.theme.colorBlack242126
import com.ly.book.theme.colorGrayD1DDDF
import com.ly.book.theme.colorGreen00D6D8
import com.ly.book.theme.colorGreen00D6D8_10
import com.ly.book.utils.PreviewProvideNavLocal
import com.ly.book.utils.rippleClick
import com.ly.book.utils.unRippleClick
import com.ly.core_model.MainMenuItem
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun MinePage(
    mainViewModel: MainViewModel,
    mineViewModel: MineViewModel = hiltViewModel(),
    goPicPre: (List<String>) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Expanded
        )
    )
    LaunchedEffect(key1 = mineViewModel, block = {
        mineViewModel.mineEvent.collect {
            when (it) {
                MineEvent.ToHome -> mainViewModel.dispatch(MainAction.SwitchBottomBar(MainMenuItem.Home))
            }
        }
    })
    val density = LocalDensity.current
    val dp10Px = remember(density) {
        with(density) {
            10.dp.toPx()
        }
    }
    val dp15Px = remember(key1 = density) {
        with(density) {
            15.dp.toPx()
        }
    }
    val dp50Px = remember(density) {
        with(density) {
            50.dp.toPx()
        }
    }
    BottomSheetScaffold(scaffoldState = scaffoldState, sheetContent = {
        BottomSheetContent(scaffoldState.bottomSheetState, mineViewModel, logout = {
            mineViewModel.dispatch(MineAction.Logout)
        })
    }, sheetPeekHeight = 300.dp, sheetShape = object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {
            val path = Path().apply {
                val roundRect = RoundRect(
                    Rect(0f, dp15Px, size.width, size.height),
                    topLeft = CornerRadius(dp10Px),
                    topRight = CornerRadius(dp10Px)
                )
                addRoundRect(roundRect)
            }
            val btnPath = Path().apply {
                val centerX = size.width / 2f
                val halfWidth = dp50Px / 2f
                val roundRect = RoundRect(
                    Rect(centerX - halfWidth, 0f, centerX + halfWidth, halfWidth),
                    topLeft = CornerRadius(dp10Px),
                    topRight = CornerRadius(dp10Px)
                )
                addRoundRect(roundRect)
            }
            return Outline.Generic(Path.combine(PathOperation.Union, path, btnPath))
        }

    }) {
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
                modifier = Modifier.padding(top = 10.dp, start = 20.dp)
            )
            Row(
                modifier = Modifier
                    .padding(top = 15.dp, start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(width = 1.dp, color = colorGreen00D6D8_10, shape = CircleShape)
                ) {
                    val avatarUrl = mineViewModel.state.avatar
                    val avatarPainter = rememberAsyncImagePainter(
                        model = avatarUrl, placeholder = painterResource(
                            id = R.drawable.default_avatar
                        ), error = painterResource(
                            id = R.drawable.default_avatar
                        ),
                        contentScale = ContentScale.Crop
                    )
                    Image(
                        painter = avatarPainter,
                        contentDescription = "avatar",
                        modifier = Modifier
                            .clip(CircleShape)
                            .unRippleClick {
                                goPicPre(listOf(avatarUrl))
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                Box(modifier = Modifier.padding(start = 15.dp)) {
                    Text(
                        text = mineViewModel.state.nickname,
                        fontSize = 24.sp,
                        color = colorBlack242126,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        textAlign = TextAlign.Start,
                    )
                }
            }

            Box(modifier = Modifier.padding(start = 115.dp)) {
                Row {
                    FollowItem(
                        modifier = Modifier.width(100.dp),
                        title = "关注我的",
                        count = mineViewModel.state.followCount
                    ) {

                    }
                    FollowItem(
                        modifier = Modifier.width(100.dp),
                        title = "已关注",
                        count = mineViewModel.state.followingCount
                    ) {

                    }
                }
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(96.dp),
                backgroundColor = with(mineViewModel.state) {
                    catchOrNull {
                        Color(bookModel?.bookPrimaryColor ?: 0L)
                    } ?: colorGreen00D6D8
                }
            ) {
                Column {
                    Text(
                        text = "最近阅读",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                    )
                    if (mineViewModel.state.bookModel == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "还没有阅读过的图书哦~",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                        ) {
                            val bookModel by remember(mineViewModel.state) {
                                derivedStateOf { mineViewModel.state.bookModel }
                            }
                            val cover = rememberAsyncImagePainter(
                                model = bookModel?.bookCover ?: "",
                                contentScale = ContentScale.Crop
                            )
                            Image(
                                painter = cover,
                                contentDescription = "cover",
                                modifier = Modifier
                                    .size(49.5.dp, 66.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop,
                            )
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = bookModel?.bookName ?: "",
                                    maxLines = 1,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${((bookModel?.readInfo?.readProgress ?: 0.0) * 100).roundToInt()}%",
                                    fontSize = 14.sp,
                                    color = colorGrayD1DDDF,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun FollowItem(modifier: Modifier, title: String, count: Int, onClick: () -> Unit) {
    Column(modifier = modifier.rippleClick(onClick)) {
        Text(text = title, fontSize = 12.sp, color = colorGrayD1DDDF, fontWeight = FontWeight.Bold)
        Text(
            text = count.toString(),
            fontSize = 17.sp,
            color = colorBlack242126,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun BottomSheetContent(
    bottomSheetState: BottomSheetState,
    viewModel: MineViewModel,
    logout: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(state = scrollState),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(50.dp)
                .unRippleClick {
                    scope.launch {
                        with(bottomSheetState) {
                            if (isCollapsed) {
                                expand()
                            } else {
                                collapse()
                            }
                        }
                    }
                }
        ) {
            val isExpanded by remember {
                derivedStateOf {
                    bottomSheetState.targetValue == BottomSheetValue.Expanded
                }
            }

            Icon(
                imageVector = Icons.Sharp.KeyboardArrowUp,
                contentDescription = "switch",
                modifier = Modifier
                    .align(Alignment.Center)
                    .rotate(if (isExpanded) 180f else 0f),
                tint = Color.LightGray,
            )
        }
        MenuCategoryTitle(
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp, start = 20.dp),
            title = "我的订阅"
        )
        MenuItem(
            title = "已更新",
            suffix = "${viewModel.state.updatedBookCount}",
            suffixColor = colorGreen00D6D8
        ) {}
        MenuItem(title = "所有书籍", suffix = "${viewModel.state.bookCount}") {}
        MenuItem(title = "有声读物", suffix = "${viewModel.state.audioBookCount}") {

        }
        MenuCategoryTitle(
            modifier = Modifier.padding(top = 30.dp, bottom = 10.dp, start = 20.dp),
            title = "账号"
        )
        MenuItem(title = "修改信息") {

        }
        MenuItem(title = "分享你的用户卡片") {

        }

        MenuItem(title = "退出登录", onClick = logout)

        Box(modifier = Modifier.height(5.dp))
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
        MinePage(hiltViewModel(), hiltViewModel(), goPicPre = {})
    }
}