@file:OptIn(ExperimentalPagerApi::class)

package com.ly.book.page.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ly.book.route.args.PicPreData
import com.ly.book.utils.unRippleClick

@Composable
fun PicPreviewPage(argument: PicPreData, popup: () -> Unit) {
    val pageState = rememberPagerState(initialPage = argument.initialIndex)
    Scaffold(topBar = {
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(56.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${pageState.currentPage + 1} / ${pageState.pageCount}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }, backgroundColor = Color.Black) {
        HorizontalPager(
            count = argument.urls.size, modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .unRippleClick { popup() }, state = pageState
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

                PreImage(url = argument.urls[it])
            }
        }
    }
}

@Composable
private fun PreImage(url: String) {
    val image = rememberAsyncImagePainter(
        model = url,
        contentScale = ContentScale.Inside
    )
    var scale by remember {
        mutableStateOf(1f)
    }
    var rotation by remember {
        mutableStateOf(0f)
    }
    var offset by remember {
        mutableStateOf(Offset.Zero)
    }
    val state =
        rememberTransformableState(onTransformation = { zoomChange, offsetChange, rotationChange ->
            scale *= zoomChange
            rotation += rotationChange
            offset += offsetChange
        })
    Image(painter = image, contentDescription = "picture", modifier = Modifier
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
            rotationZ = rotation
            translationX = offset.x
            translationY = offset.y
        }
        .transformable(state)
        .pointerInput(Unit) {
            detectTapGestures(onTap = {
                scale = 1f
                rotation = 0f
                offset = Offset.Zero
            }, onDoubleTap = {
                scale = if (scale > 1f) {
                    1f
                } else {
                    1.5f
                }
            })
        })
}