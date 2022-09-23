package com.ly.book.page.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ly.book.R


@Composable
fun LoginTextField(
    hint: String,
    modifier: Modifier = Modifier,
    defaultValue: String = "",
    keyboardType: KeyboardType,
    hideInput: Boolean = false,
    textChange: (String) -> Unit
) {
    var defaultInput by remember(defaultValue) {
        mutableStateOf(defaultValue)
    }
    BasicTextField(
        value = defaultInput,
        onValueChange = {
            if (it.length <= 20) {
                defaultInput = it
                textChange(it)
            }
        },
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.grayEDEFF3_50),
                shape = RoundedCornerShape(90.dp)
            ),
        visualTransformation = if (hideInput) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        cursorBrush = SolidColor(colorResource(id = R.color.green00D6D8)),
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (defaultInput.isEmpty()) {
                    Text(
                        text = hint,
                        fontSize = 17.sp,
                        color = colorResource(id = R.color.grayAFC1C4)
                    )
                }
                it()
            }
        },
        textStyle = TextStyle(
            fontSize = 17.sp, color = colorResource(id = R.color.black242126),
        )
    )
}