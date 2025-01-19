package person.app.data.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import person.app.data.models.Address
import person.app.data.models.Company
import person.app.data.models.User

@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    viewModel: DogsViewModel = hiltViewModel(),
    navigationController: NavController
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is UiState.Success) {
        PersonTypeScreen(
            items = (items as UiState.Success).data,
            onSave = viewModel::addUser,
            onRowClick = { navigationController.navigate("details") },
            modifier = modifier
        )
    }
}

@Composable
internal fun PersonTypeScreen(
    items: List<User>,
    onSave: (user: User) -> Unit,
    onRowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        var dog by remember { mutableStateOf("Czarek") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = dog,
                onValueChange = { dog = it }
            )
            val user = User(1,"s","s","s",
                Address("s","s","s,","s")
                ,"s","s", Company("s","d")
            )

            Button(modifier = Modifier.width(96.dp), onClick = { onSave(user) }) {
                Text("Save")
            }
        }

        items.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.clickable {
                    onRowClick()
                }
            ) {
                Text("🐕", modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush = mainGradient())
                    .padding(8.dp)
                )

                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(it.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(it.phone, fontSize = 12.sp, fontWeight = FontWeight.Light)
                }

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

private fun mainGradient(): Brush {
    return Brush.linearGradient(
        colors = listOf(Color(0xFF65558F), Color(0xFFEEB6E8))
    )
}