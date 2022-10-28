@file:Suppress("unused")

package com.ly.book.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.ly.book.utils.clickWithoutRipple
import kotlin.math.roundToInt

private const val DEFAULT_ANIM_DURATION = 300

/**
 * 弹窗
 * 必须与Page级别的Composable同级，保证约束是允许填充整个屏幕
 */
@Immutable
class XSheetDialogProperties(
    val cancelable: Boolean = true,
    val dimAmount: Float = 0.5f,
    val canceledOnTouchOutside: Boolean = true
)

@Composable
fun TopSheetDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    properties: XSheetDialogProperties = XSheetDialogProperties(),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    DialogBg(
        visible = visible,
        cancelable = properties.cancelable,
        dimAmount = properties.dimAmount,
        canceledOnTouchOutside = properties.canceledOnTouchOutside,
        onDismissRequest = onDismissRequest
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIM_DURATION,
                    easing = LinearOutSlowInEasing
                ),
                initialOffsetY = {
                    -it
                }
            ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIM_DURATION,
                    easing = LinearOutSlowInEasing
                ),
                targetOffsetY = {
                    -it
                }
            ),
            modifier = modifier.clickWithoutRipple { }
        ) {
            content()
        }
    }
}

@Composable
fun BottomSheetDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    properties: XSheetDialogProperties = XSheetDialogProperties(),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    DialogBg(
        visible = visible,
        cancelable = properties.cancelable,
        dimAmount = properties.dimAmount,
        canceledOnTouchOutside = properties.canceledOnTouchOutside,
        onDismissRequest = onDismissRequest
    ) {
        var offsetY by remember {
            mutableStateOf(0f)
        }
        val offsetYAnim by animateFloatAsState(targetValue = offsetY)
        var bottomSheetHeight by remember { mutableStateOf(0f) }
        DisposableEffect(visible) {
            onDispose {
                offsetY = 0f
            }
        }
        AnimatedVisibility(
            modifier = modifier.clickWithoutRipple { }
                .align(Alignment.BottomCenter)
                .offset {
                    IntOffset(0, offsetYAnim.roundToInt())
                }
                .onGloballyPositioned {
                    bottomSheetHeight = it.size.height.toFloat()
                }
                .draggable(state = rememberDraggableState {
                    offsetY = (offsetY + it.toInt()).coerceAtLeast(0f)
                }, orientation = Orientation.Vertical, onDragStopped = {
                    if (properties.cancelable && offsetY > bottomSheetHeight / 2) {
                        onDismissRequest()
                    } else {
                        offsetY = 0f
                    }
                }),
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIM_DURATION,
                    easing = LinearOutSlowInEasing,
                ),
                initialOffsetY = {
                    it
                }
            ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIM_DURATION,
                    easing = LinearOutSlowInEasing
                ),
                targetOffsetY = {
                    it
                }
            )
        ) {
            content()
        }
    }

}


@Composable
private fun DialogBg(
    visible: Boolean,
    cancelable: Boolean,
    dimAmount: Float,
    canceledOnTouchOutside: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BackHandler(enabled = visible) {
        if (cancelable) {
            onDismissRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIM_DURATION,
                    easing = LinearEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = DEFAULT_ANIM_DURATION,
                    easing = LinearEasing
                )
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = Color.Black.copy(alpha = dimAmount)).clickWithoutRipple {
                        if (canceledOnTouchOutside) {
                            onDismissRequest()
                        }
                    })
        }
        content()
    }
}