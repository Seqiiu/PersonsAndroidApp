package person.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

import dagger.hilt.android.AndroidEntryPoint
import person.app.MainNavigation
import person.app.data.models.User
import person.app.ui.theme.PersonAppTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


// Define API service interface
interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

// Main activity


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonAppTheme {
               // UserScreen()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}

/*@Composable
fun UserScreen() {
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
                    personRepository.add(user)
                }
                users = fetchedUsers
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        } else {
            // If no internet, fetch users from database
            personRepository.users.collect { usersFromDb ->
                if (usersFromDb.isNotEmpty()) {
                    users = usersFromDb
                }
                isLoading = false
            }
        }
    }


    // Display loading state or user data
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
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
*/