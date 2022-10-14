package com.ly.book.page.other

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun WaitingPage() {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center).size(50.dp))
    }
}

@Preview
@Composable
fun WaitingPagePre() {
    WaitingPage()
}