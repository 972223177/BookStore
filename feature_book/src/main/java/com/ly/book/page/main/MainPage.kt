@file:OptIn(ExperimentalPagerApi::class)

package com.ly.book.page.main

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
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
            Row(modifier = Modifier.fillMaxHeight()) {
                menuItems.forEachIndexed { index, menu ->
                    BottomItem(
                        icon = when (menu) {
                            MainMenuItem.Home -> Icons.Rounded.Home
                            MainMenuItem.Feed -> Icons.Rounded.Menu
                            MainMenuItem.Search -> Icons.Rounded.Search
                            MainMenuItem.Favorite -> Icons.Rounded.Favorite
                            MainMenuItem.Mine -> Icons.Rounded.Person
                        },
                        title = menu.title,
                        index = index,
                        currentIndex = pagerState.currentPage,
                        onClick = {
                            val switch = {
                                scope.launch {
                                    pagerState.scrollToPage(it)
                                }
                            }
                            if (menu == MainMenuItem.Mine) {
                                checkLogin(navController) {
                                    switch()
                                }
                            } else {
                                switch()
                            }
                        }
                    )
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
private fun RowScope.BottomItem(
    icon: ImageVector,
    title: String,
    index: Int,
    currentIndex: Int,
    onClick: (Int) -> Unit
) {
    Box(modifier = Modifier
        .weight(1f)
        .fillMaxHeight()
        .rippleClick {
            onClick(index)
        }) {
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
                tint = color
            )
            Text(text = title, fontSize = 10.sp, color = color)
        }

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

@Preview
@Composable
fun MainPagePre() {
    PreviewProvideNavLocal {
        MainPage()
    }
}