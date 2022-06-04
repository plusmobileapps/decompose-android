package com.plusmobileapps.sample.androiddecompose.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.plusmobileapps.sample.androiddecompose.ui.theme.DecomposeAndroidSampleTheme
import com.plusmobileapps.sample.androiddecompose.ui.theme.Typography


@Composable
fun AboutUI() {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        append("This is an Android sample showcasing ")

        pushStringAnnotation("decompose", annotation = "https://github.com/arkivanov/Decompose")
        withStyle(style = SpanStyle(Color.Blue)) {
            append("Decompose")
        }
        pop()

        append(" and ")

        pushStringAnnotation("mvikotlin", annotation = "https://github.com/arkivanov/MVIKotlin")
        withStyle(style = SpanStyle(Color.Blue)) {
            append("MVIKotlin")
        }
        pop()

        append(" with data being supplied by the ")

        pushStringAnnotation(
            "rickandmorty",
            annotation = "https://rickandmortyapi.com/documentation/#restl"
        )
        withStyle(style = SpanStyle(Color.Blue)) {
            append("Rick and Morty API.")
        }
        pop()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClickableText(text = annotatedString, style = Typography.bodyLarge, onClick = { offset ->
            annotatedString.getStringAnnotations("decompose", start = offset, end = offset)
                .firstOrNull()?.let { context.launchWebIntent(it.item) }
            annotatedString.getStringAnnotations("mvikotlin", start = offset, end = offset)
                .firstOrNull()?.let { context.launchWebIntent(it.item) }
            annotatedString.getStringAnnotations("rickandmorty", start = offset, end = offset)
                .firstOrNull()?.let { context.launchWebIntent(it.item) }
        })
    }
}

private fun Context.launchWebIntent(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    })
}

@Preview
@Composable
fun AboutPreview() {
    DecomposeAndroidSampleTheme {
        Surface {
            AboutUI()
        }
    }
}