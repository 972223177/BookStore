package com.ly.book.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.ly.book.R

@Composable
fun EmptyHolder(modifier: Modifier) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.align { size, space, _ ->
                IntOffset(
                    (space.width * 0.5f - size.width * 0.5f).toInt(),
                    (space.height * 0.45f - size.height * 0.5f).toInt()
                )
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_empty_normal),
                contentDescription = "Empty"
            )
            Text(text = "空空的~", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}


@Composable
fun LoadingHolder(modifier: Modifier) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading))

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(composition = composition, progress = {
        progress
    }, modifier = modifier)
}

@Preview
@Composable
fun EmptyHolderPre() {
    Surface {
        EmptyHolder(modifier = Modifier.fillMaxSize())
    }
}

@Preview
@Composable
fun LoadingHolderPre() {
    Surface {
        LoadingHolder(modifier = Modifier.size(160.dp))
    }
}





