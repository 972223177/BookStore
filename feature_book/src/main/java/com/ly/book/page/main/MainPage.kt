@file:OptIn(ExperimentalPagerApi::class)

package com.ly.book.page.main

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ly.book.R
import com.ly.book.page.main.feed.FavoritePage
import com.ly.book.page.main.feed.FeedPage
import com.ly.book.page.main.home.HomePage
import com.ly.book.page.main.mine.MinePage
import com.ly.book.page.main.search.SearchPage
import com.ly.book.theme.colorGrayD1DDDF
import com.ly.book.theme.colorGreen00D6D8
import com.ly.book.utils.LocalNavController
import com.ly.book.utils.PreviewProvideNavLocal
import com.ly.book.utils.checkLogin
import com.ly.book.utils.rippleClick
import com.ly.common.utils.UserHelper
import com.ly.core_model.MainMenuItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MainPage(viewModel: MainViewModel = hiltViewModel()) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val menuItems = remember {
        MainMenuItem.values()
    }
    val navController = LocalNavController.current
    LaunchedEffect(key1 = viewModel, block = {
        viewModel.bottomIndex.collectLatest {
            if (it < 0) return@collectLatest
            pagerState.scrollToPage(it)
        }
    })
    Scaffold(bottomBar = {
        Surface(
            elevation = 8.dp, modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(53.dp),
            color = Color.White
        ) {
            val itemClick = { page: Int ->
                scope.launch {
                    pagerState.scrollToPage(page)
                }
                Unit
            }
            Row(modifier = Modifier.fillMaxHeight()) {
                for (item in menuItems) {
                    when (item) {
                        MainMenuItem.Home -> BottomItem(
                            icon = Icons.Rounded.Home,
                            title = item.title,
                            index = 0,
                            currentIndex = pagerState.currentPage,
                            onClick = itemClick
                        )

                        MainMenuItem.Feed -> BottomItem(
                            icon = Icons.Rounded.Menu,
                            title = item.title,
                            index = 1,
                            currentIndex = pagerState.currentPage,
                            onClick = itemClick
                        )

                        MainMenuItem.Search -> BottomItem(
                            icon = Icons.Rounded.Search,
                            title = item.title,
                            index = 2,
                            currentIndex = pagerState.currentPage,
                            onClick = itemClick
                        )

                        MainMenuItem.Favorite -> BottomItem(
                            icon = Icons.Rounded.Favorite,
                            title = item.title,
                            index = 3,
                            currentIndex = pagerState.currentPage,
                            onClick = itemClick
                        )

                        MainMenuItem.Mine -> BottomMineItem(
                            currentIndex = pagerState.currentPage,
                            index = 4,
                            onClick = {
                                checkLogin(navController) {
                                    scope.launch {
                                        pagerState.scrollToPage(it)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }) {
        HorizontalPager(
            count = menuItems.size,
            modifier = Modifier.padding(it),
            state = pagerState,
            key = { page -> menuItems[page].title }) { page ->
            when (menuItems[page]) {
                MainMenuItem.Home -> HomePage()
                MainMenuItem.Feed -> FeedPage()
                MainMenuItem.Search -> SearchPage()
                MainMenuItem.Favorite -> FavoritePage()
                MainMenuItem.Mine -> MinePage(viewModel)
            }
        }
    }
}

@Composable
fun RowScope.BottomItemWrapper(
    currentIndex: Int,
    index: Int,
    child: @Composable () -> Unit,
    onClick: (Int) -> Unit
) {
    Box(modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .rippleClick {
            onClick(index)
        }) {
        child()
        val targetWidth by remember(currentIndex) {
            derivedStateOf { if (currentIndex == index) 1f else 0f }
        }
        val paint = remember {
            Paint().apply {
                color = colorGreen00D6D8
            }
        }
        val percent by animateFloatAsState(targetValue = targetWidth)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(2.dp)
                .fillMaxWidth()
                .drawWithContent {
                    val center = size.width / 2f
                    val halfWidth = size.width * percent / 2f
                    val leftX = center - halfWidth
                    val rightX = center + halfWidth
                    drawContext.canvas.drawRect(
                        leftX,
                        0f,
                        rightX,
                        size.height,
                        paint
                    )

                }
        )
    }
}

@Composable
private fun RowScope.BottomItem(
    icon: ImageVector,
    title: String,
    index: Int,
    currentIndex: Int,
    onClick: (Int) -> Unit
) {
    BottomItemWrapper(currentIndex = currentIndex, index = index, child = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val color by remember(currentIndex) {
                derivedStateOf {
                    if (index == currentIndex)
                        colorGreen00D6D8
                    else
                        colorGrayD1DDDF
                }
            }
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Text(text = title, fontSize = 10.sp, color = color)
        }
    }, onClick = onClick)
}

@Composable
fun RowScope.BottomMineItem(currentIndex: Int, index: Int, onClick: (Int) -> Unit) {
    BottomItemWrapper(currentIndex = currentIndex, index = index, child = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val color by remember(currentIndex) {
                derivedStateOf {
                    if (index == currentIndex)
                        colorGreen00D6D8
                    else
                        colorGrayD1DDDF
                }
            }
            val user by UserHelper.user.collectAsState(initial = null)
            if (user == null || user?.id == 0) {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = MainMenuItem.Mine.title,
                    tint = color
                )
            } else {
                val context = LocalContext.current
                val request = remember(user?.avatar ?: "", context) {
                    ImageRequest.Builder(context)
                        .data(user?.avatar)
                        .allowRgb565(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .crossfade(true)
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .build()
                }
                AsyncImage(
                    model = request,
                    contentDescription = MainMenuItem.Mine.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = color, shape = CircleShape)
                )
            }
            Text(text = MainMenuItem.Mine.title, fontSize = 10.sp, color = color)
        }
    }, onClick = onClick)
}

@Preview
@Composable
fun MainPagePre() {
    PreviewProvideNavLocal {
        MainPage()
    }
}