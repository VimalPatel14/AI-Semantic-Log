package com.vimal.theaisemanticlog.ui.log.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.vimal.theaisemanticlog.ui.theme.TheAISemanticLogTheme

@Composable
fun LogListShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 15.dp)
    ) {
        items(20){
            LogItemShimmer()
        }
    }
}

@Composable
fun LogItemShimmer() {
    val shimmer = rememberShimmer(
        shimmerBounds = ShimmerBounds.View
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .shimmer(shimmer)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
    }
}

@PreviewLightDark
@Composable
private fun LogItemShimmerPreview() {
    TheAISemanticLogTheme {
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding()
        ) {
            repeat(10) {
                LogItemShimmer()
            }
        }
    }

}