package cc.reece.cathayhomework_2024.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CathayScaffold(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    val displayCutoutInsets = WindowInsets.displayCutout

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                modifier = Modifier.windowInsetsPadding(
                    displayCutoutInsets.only(WindowInsetsSides.Horizontal)
                )
            )
        }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .windowInsetsPadding(
                    displayCutoutInsets.only(WindowInsetsSides.Horizontal)
                )
        ) {
            content(scaffoldPadding)
        }
    }
}