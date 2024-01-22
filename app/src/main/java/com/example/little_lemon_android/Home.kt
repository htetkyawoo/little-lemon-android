package com.example.little_lemon_android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.little_lemon_android.ui.theme.DarkGrey
import com.example.little_lemon_android.ui.theme.White
import com.example.little_lemon_android.ui.theme.Yellow

@Composable
fun Home(navController: NavController, menuItemNetwork: State<List<MenuItemRoom>>) {

    val images = mapOf(
        "bruschetta" to R.drawable.bruschetta,
        "pasta" to R.drawable.pasta,
        "greekSalad" to R.drawable.greek_salad,
        "grilledFish" to R.drawable.grilled_fish,
        "lemonDessert%202" to R.drawable.lemon_dessert,
    )
    val items = menuItemNetwork.value
    val categories = getCategories(items)
    var searchPhase by remember { mutableStateOf("") }


    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 20.dp)
                    .padding(start = 70.dp)
                    .height(50.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Profile",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .clickable { navController.navigate(Profile.route) }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = "Little Lemon",
                style = MaterialTheme.typography.displayMedium,
                color = Yellow,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 5.dp)
            )
            Text(
                text = "Chicago",
                style = MaterialTheme.typography.titleLarge,
                color = White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, bottom = 10.dp)
            )
            Row {
                Text(
                    text = """
                    We are family owned 
                    Mediterranean restaurant,
                    focused on traditional
                    recipes served with a
                     modern twist.""".trimIndent(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = White,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(0.55f)
                )
                Image(
                    alignment = Alignment.Center,
                    painter = painterResource(id = R.drawable.hero_image),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "Hero Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.25f)
                        .clip(RoundedCornerShape(20.dp))
                        .padding(end = 10.dp)
                )
            }
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = MaterialTheme.colorScheme.background)

            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "search",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(36.dp)
                        .align(Alignment.CenterVertically)
                )
                TextField(
                    value = searchPhase,
                    onValueChange = { searchPhase = it },
                    modifier = Modifier.clip(
                        RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                    )
                )
            }

        }
        Text(
            text = "ORDER FOR DELIVERY!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(5.dp)
        )

        if(categories.isNotEmpty()){
            var selectedCategories = remember { mutableStateListOf(*categories.toTypedArray()) }
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                categories.forEach {
                    if (selectedCategories.contains(it)) {
                        Button(
                            onClick = { selectedCategories.remove(it) },
                            modifier = Modifier.padding(2.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Yellow,
                                contentColor = DarkGrey
                            )
                        ) {
                            Text(text = it.uppercase())
                        }
                    } else {
                        OutlinedButton(
                            onClick = { selectedCategories.add(it) },
                            modifier = Modifier.padding(2.dp),
                        ) {
                            Text(text = it.uppercase())
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 10.dp, horizontal = 3.dp)
            ) {
                items.filter { selectedCategories.contains(it.category) }
                    .filter { searchPhase.isEmpty() || it.title.contains(searchPhase, true) }
                    .forEach {
                        Card(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                            )
                            Row(modifier = Modifier.padding(10.dp)) {
                                Column(modifier = Modifier.fillMaxWidth(0.6f)) {
                                    Text(
                                        text = it.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                    Text(
                                        text = "$ ${it.price}",
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }
                                var img = it.image.split("/").last().split(".")[0]
                                println(img)
                                if (img in images) {
                                    Image(
                                        painter = painterResource(id = images[img]!!),
                                        contentDescription = it.title,
                                        contentScale = ContentScale.FillBounds,
                                        modifier = Modifier
                                            .height(100.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(20.dp))
                                    )
                                } else {
                                    AsyncImage(model = it.image, contentDescription = it.title)
                                }
                            }

                        }
                    }
            }
        }
    }


}

fun getCategories(menuItemNetwork: List<MenuItemRoom>): List<String> {
    val categories = mutableListOf<String>()
    menuItemNetwork.forEach {
        if (!categories.contains(it.category)) {
            categories.add(it.category)
        }
    }
    return categories
}