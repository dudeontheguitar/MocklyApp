package com.example.mocklyapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.mocklyapp.presentation.navigation.InterviewRegister
import com.example.mocklyapp.R
import com.example.mocklyapp.models.InterviewItem
import com.example.mocklyapp.models.OngoingInterviewItem
import com.example.mocklyapp.presentation.theme.Poppins

@Composable
fun InterviewScreen(
    navController: NavHostController
) {
    Surface (color = MaterialTheme.colorScheme.onBackground){
        LazyColumn (
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            item{Header()}
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { MyInterviews() }
            item { AvailableInterview(navController) }


        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.zaq),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Let's work,",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                Text(
                    text = "Dulat",
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }


        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_notifications_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}



@Composable
private fun MyInterviews() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Upcoming", "Past")

    val interviews = listOf(
        InterviewItem("Frontend Developer", "Kaspi", "Today, 3:00 PM", false),
        InterviewItem("Product Manager", "Tele2", "20.11.2025, 5:00 PM", false),
        InterviewItem("QA Engineer", "Arbuz", "02.11.2025, 5:00 PM", true),
        InterviewItem("Backend Developer", "Arbuz", "01.11.2025, 2:00 PM", true)
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "My Interviews",
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primaryContainer
        )

        Spacer(Modifier.height(12.dp))

        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primaryContainer,
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier
                        .tabIndicatorOffset(selectedTab)
                        .padding(horizontal = 48.dp)
                        .height(3.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            },
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                ) {
                    Text(
                        text = title,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            fontWeight = if (selectedTab == index) FontWeight.SemiBold else FontWeight.Normal
                        ),
                        color = if (selectedTab == index)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        val upcoming = remember { interviews.filter { !it.isPast } }
        val past = remember { interviews.filter { it.isPast } }
        val currentList = if (selectedTab == 0) upcoming else past

        currentList.forEach { interview ->
            InterviewCard(interview)
            Spacer(Modifier.height(12.dp))
        }
    }
}


@Composable
private fun InterviewCard(item: InterviewItem) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.company,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }

                Text(
                    text = item.dateTime,
                    style = TextStyle(
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    }
}

@Composable
private fun AvailableInterview(navController: NavHostController) {
    val cards = listOf(
        OngoingInterviewItem("Frontend Developer", "Remote", "Waiting"),
        OngoingInterviewItem("Product Manager", "Remote", "Start"),
        OngoingInterviewItem("QA Engineer", "Remote", "Start")
    )

    var showDialog by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<OngoingInterviewItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Ongoing Interview",
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primaryContainer
        )

        Spacer(Modifier.height(12.dp))

        cards.forEach { item ->
            OngoingCard(
                item = item,
                onClick = {
                    if(item.status == "Start"){
                        selectedItem = item
                        showDialog = true
                    }
                }
            )
            Spacer(Modifier.height(12.dp))

        }
    }
    if(showDialog && selectedItem != null){
        InterviewTypeDialog(
            onDismiss = { showDialog = false },
            onRealPerson = {
                showDialog = false
                navController.navigate(InterviewRegister)
            },
            onAi = { /* TODO */ showDialog = false }
        )
    }
}

@Composable
private fun InterviewTypeDialog(
    onDismiss: () -> Unit,
    onRealPerson: () -> Unit,
    onAi: () -> Unit,
){

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Choose Interview Type:",
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primaryContainer
                )

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = onRealPerson,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0300A2)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF3533B6))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.interview3),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Real Person",
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                            ),
                            color = Color.White
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onAi,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A0932)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(35.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF3B3A5B))
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ai),
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "With AI",
                            style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun OngoingCard(item: OngoingInterviewItem, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF060446),
            Color(0xFF0A0932)
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth().clickable{onClick()},
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .height(80.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.location,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(999.dp))
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.status,
                        style = TextStyle(
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
        }
    }
}
