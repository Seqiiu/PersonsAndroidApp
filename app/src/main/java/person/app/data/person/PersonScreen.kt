package person.app.data.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import person.app.data.models.Address
import person.app.data.models.Company
import person.app.data.models.User
import person.app.isInternetAvailable
import person.app.ui.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun PersonScreen(
    modifier: Modifier = Modifier,
    viewModel: DogsViewModel = hiltViewModel(),
    navigationController: NavController
) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is UiState.Success) {
        UserScreen(
            onSave = viewModel::addUser,
            items = (items as UiState.Success).data
        )
    }
}

@Composable
fun UserScreen(
    onSave: (user: User) -> Unit,
    items: List<User>
) {
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isInternetAvailable by remember { mutableStateOf(true) }

    // Retrofit setup
    val retrofit = Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        isInternetAvailable = isInternetAvailable(context)

        if (isInternetAvailable) {
            try {
                // Get users from API
                val fetchedUsers = apiService.getUsers()
                // Save users to the database
                fetchedUsers.forEach { user ->
                    onSave(user)
                }
                users = fetchedUsers
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        } else {
                users = items
                isLoading = false
        }
    }


    // Display loading state or user data
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(top = 80.dp)) {
            items(users.size) { index ->
                UserCard(user = users[index])
            }
        }
    }
}

@Composable
fun UserCard(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = user.name, style = MaterialTheme.typography.titleMedium)
            Text(text = "@${user.username}", style = MaterialTheme.typography.bodyMedium)
            Text(text = user.email, style = MaterialTheme.typography.bodySmall)
            Text(text = user.phone, style = MaterialTheme.typography.bodySmall)
            Text(text = "Address: ${user.address.street}, ${user.address.city}", style = MaterialTheme.typography.bodySmall)
        }
    }
}