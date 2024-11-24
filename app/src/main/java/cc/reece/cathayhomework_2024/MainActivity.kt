package cc.reece.cathayhomework_2024

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import cc.reece.cathayhomework_2024.ui.components.CathayScaffold
import cc.reece.cathayhomework_2024.ui.components.CathayTopAppBar
import cc.reece.cathayhomework_2024.ui.theme.CathayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    CathayTheme {
        CathayScaffold(
            topAppBar = {
                CathayTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    actions = {
                        LanguageButton(
                            onClick = {}
                        )
                    }
                )
            },
            content = {
                MainContent()
            }
        )
    }
}

@Composable
fun LanguageButton(
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_language),
            contentDescription = "language settings",
        )
    }
}

@Composable
fun MainContent() {
    val items = List(100) { "item $it" }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Text(
                    text = item.toString(),
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        }
    }
}