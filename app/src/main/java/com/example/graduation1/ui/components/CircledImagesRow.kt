package com.example.graduation1.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun CircledImagesRow(images : List<Any>) {

    images.forEach { image ->
        Image(
            rememberAsyncImagePainter(image),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(10.dp)
                .clip(shape = CircleShape)
        )
    }
}