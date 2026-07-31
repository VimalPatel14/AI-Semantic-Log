package com.vimal.theaisemanticlog.ui.log.extenstion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.noRippleClickable(onClick: (() -> Unit)?): Modifier = composed {
    onClick?.let { nonNullableOnClick ->
        clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
            nonNullableOnClick()
        }
    } ?: Modifier
}
