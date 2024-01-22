package com.example.little_lemon_android

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.little_lemon_android.ui.theme.LittlelemonandroidTheme
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(contentType = ContentType("text", "plain"))
        }
    }
    private val database by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database").build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val registered = getSharedPreferences("user", MODE_PRIVATE)

        setContent {
            LittlelemonandroidTheme {
                val databaseMenuItems = database.menuItemDao().getAll().observeAsState(
                    emptyList()
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(registered = registered, menuItemNetwork = databaseMenuItems)
                }
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            if (database.menuItemDao().isEmpty()) {
                val menuItemNetwork = getMenu()
                saveMenuToDatabase(menuItemNetwork)
            }
        }
    }

    private suspend fun getMenu(): List<MenuItemNetwork> {
        val response =
            client.get("https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json")
                .body<MenuNetworkData>();
        return response.menu
    }

    private fun saveMenuToDatabase(menuItemNetwork: List<MenuItemNetwork>) {
        val menuItemRoom = menuItemNetwork.map { it.toMenuItemRoom() }
        database.menuItemDao().insertAll(*menuItemRoom.toTypedArray())
    }
}

@Composable
fun Navigation(registered: SharedPreferences, menuItemNetwork: State<List<MenuItemRoom>>) {
    val navController = rememberNavController()
    var startDistinations = if (registered.getBoolean(User.IS_REGISTERED, false)) {
        Home.route
    } else {
        Onboarding.route
    }
    NavHost(navController = navController, startDestination = startDistinations) {
        composable(Onboarding.route) {
            Onboarding(
                navController = navController,
                registered = registered,
            )
        }
        composable(Home.route) {
            Home(navController = navController, menuItemNetwork = menuItemNetwork)
        }
        composable(Profile.route) {
            Profile(
                navController = navController,
                registered = registered,
            )
        }

    }
}

