package com.example.mocklyapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mocklyapp.R
import com.example.mocklyapp.ui.theme.Poppins
import com.google.android.gms.internal.base.zaq

@Composable
fun LoginScreen (
    onSignInClick: () -> Unit = {},
    onNoAccountClick: () -> Unit = {}
){

    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}

    Surface(color = MaterialTheme.colorScheme.onBackground) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp)
                .padding(top = 300.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Login",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primaryContainer,
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 45.sp
                )
            )
            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField (

                value = email,
                onValueChange = {email = it},
                label = { Text(text = "Email", color = MaterialTheme.colorScheme.background) },
                placeholder = { Text("Enter your email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer
                )

            )

            OutlinedTextField (

                value = password,
                onValueChange = {password = it},
                label = { Text(text = "Password", color = MaterialTheme.colorScheme.background) },
                placeholder = { Text("Enter your password") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                    cursorColor = MaterialTheme.colorScheme.primaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer
                ),
            )

            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(65.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                onClick = {onSignInClick()}
            ) {
                Text(
                    text = "Sign In",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    ),
                    color = Color.White
                )
            }

            TextButton(
                onClick = {onNoAccountClick()}
            ){
                Text(
                    text = "Don't have an account?",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = "Sign in using",
                style = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                ),
                color = MaterialTheme.colorScheme.secondary
            )
            Row (
                modifier = Modifier.fillMaxWidth().padding(start = 165.dp, end = 165.dp).padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Image(
                    painter = painterResource(R.drawable.login1),
                    contentDescription = "",
                    modifier = Modifier.height(17.dp).width(17.dp)
                )
                Image(
                    painter = painterResource(R.drawable.login2),
                    contentDescription = "",
                    modifier = Modifier.height(17.dp).width(17.dp)
                )
                Image(
                    painter = painterResource(R.drawable.login3),
                    contentDescription = "",
                    modifier = Modifier.height(17.dp).width(17.dp)
                )
            }
        }
    }
}