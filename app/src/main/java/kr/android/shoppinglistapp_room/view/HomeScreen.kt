package kr.android.shoppinglistapp_room.view

import android.util.Log.i
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kr.android.shoppinglistapp_room.model.ShoppingItem
import kr.android.shoppinglistapp_room.navigation.Screens
import kr.android.shoppinglistapp_room.ui.theme.GreenPrimaryContainerDark
import kr.android.shoppinglistapp_room.ui.theme.ShoppingListApp_RoomTheme
import kr.android.shoppinglistapp_room.ui.theme.ThemeMode

@Composable
fun HomeScreen(
    themeMode: ThemeMode,
    isDark : Boolean,
    onThemeChange : (ThemeMode) -> Unit,
    navController: NavHostController
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    ShoppingListApp_RoomTheme (darkTheme = isDark) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppBar(
                    title = "Shopping List",
                    themeMode = themeMode,
                    onThemeChange = onThemeChange
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(Screens.AddEditScreen.route) },
                    modifier = Modifier
                        .padding(30.dp)
                        .size(70.dp)
                        .border(
                            width = 1.dp,
                            color = GreenPrimaryContainerDark,
                            CircleShape
                        ),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "add item",
                        modifier = Modifier.size(35.dp)
                    )
                }
            },
            snackbarHost = { SwipeableSnackBar(snackBarHostState) }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(15){
                    ShoppingItemView(
                        ShoppingItem(
                            0L,
                            "Eggs",
                            "30"
                        ),
                        isDark = isDark,
                        {}
                    )
                }
            }
        }
    }

}

@Composable
fun ShoppingItemView (
    item : ShoppingItem,
    isDark: Boolean,
    onClick : () -> Unit
){

    val haptic = LocalHapticFeedback.current

    var checked by remember { mutableStateOf(false) }

    val containerColor = if (checked) {
        if (isDark) Color.Gray else Color.LightGray
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    val textColor = if (checked) {
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
    } else {
        MaterialTheme.colorScheme.onPrimary
    }

    val strikeColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clickable{ onClick() }
            .padding(top = 12.dp)
            .drawWithContent {
                drawContent()
                if (checked) {
                    val strokeWidth = 2.dp.toPx()
                    drawLine(
                        color = strikeColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = strokeWidth,
                        cap = StrokeCap.Round
                    )
                }
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = textColor
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Checkbox(
                checked = checked,
                onCheckedChange = {
                    checked = it

                    haptic.performHapticFeedback(
                        HapticFeedbackType.TextHandleMove
                    )
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    checkmarkColor = MaterialTheme.colorScheme.background
                )
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Name:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.name
                )
            }

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Quantity:",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.quantity
                )
            }

        }
    }

}
