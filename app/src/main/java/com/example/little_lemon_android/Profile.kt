package com.example.little_lemon_android

import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.little_lemon_android.ui.theme.DarkGrey
import com.example.little_lemon_android.ui.theme.Yellow

@Composable
fun Profile(
    navController: NavController,
    registered: SharedPreferences,

    ){
    var firstName by remember { mutableStateOf(registered.getString(User.FIRST_NAME, "") ?: "") }
    var lastName by remember { mutableStateOf(registered.getString(User.LAST_NAME, "") ?: "") }
    var email by remember { mutableStateOf(registered.getString(User.EMAIL, "") ?: "") }

    var firstNameError by remember { mutableStateOf(firstName.isBlank()) }
    var lastNameError by remember { mutableStateOf(lastName.isBlank()) }
    var emailError by remember { mutableStateOf(email.isBlank()) }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(50.dp)
        )
        Text(
            text = "Personal information",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )

        OutlinedTextField(
            singleLine = true,
            label = { Text(text = "first name") },
            placeholder = { Text(text = "Please enter your first Name") },
            value = firstName,
            onValueChange = {
                firstName = it
                registered.edit().putString(User.FIRST_NAME, firstName).commit()
                firstNameError = firstName.isBlank()
            })
        OutlinedTextField(
            singleLine = true,
            label = { Text(text = "last name") },
            placeholder = { Text(text = "Please enter your last Name") },
            value = lastName,
            onValueChange = {
                lastName = it
                registered.edit().putString(User.LAST_NAME, lastName).commit()
                lastNameError = lastName.isBlank()
            })
        OutlinedTextField(
            singleLine = true,
            label = { Text(text = "email") },
            placeholder = { Text(text = "Please enter your email") },
            value = email,
            onValueChange = {
                email = it
                registered.edit().putString(User.EMAIL, email).commit()
                emailError = !validEmail(email)
            })

        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxHeight()
                .padding(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    registered.edit().clear().commit()
                    navController.navigate(Onboarding.route){
                        navController.backQueue.clear()
                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Yellow,
                    contentColor = DarkGrey
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = !firstNameError && !lastNameError && !emailError
            ) {
                Text(text = "Log out",style = MaterialTheme.typography.titleLarge)
            }
        }

    }
}