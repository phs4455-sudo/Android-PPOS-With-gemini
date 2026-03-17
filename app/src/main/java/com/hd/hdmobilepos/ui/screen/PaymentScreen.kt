package com.hd.hdmobilepos.ui.screen

import com.hd.hdmobilepos.*
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.derivedStateOf
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.hd.hdmobilepos.data.ActiveOrderDetails
import com.hd.hdmobilepos.data.Area
import com.hd.hdmobilepos.data.AppDatabase
import com.hd.hdmobilepos.data.PosRepository
import com.hd.hdmobilepos.data.TableSummary
import com.hd.hdmobilepos.ui.component.PosTopBar
import com.hd.hdmobilepos.ui.theme.PPOSTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import java.util.Locale
import java.util.concurrent.TimeUnit

fun PaymentScreen(navController: NavHostController, paymentVm: PaymentViewModel) {
    val uiState by paymentVm.uiState.collectAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PosTopBar() },
        containerColor = Color(0xFFF6F2E9)
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val paymentLines = uiState.methodAmounts.entries
                .filter { it.value > 0 }
                .sortedBy { it.key.ordinal }

            // Left: Order Summary (White Card)
            Surface(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(uiState.tableName, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp), fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("결제수단", modifier = Modifier.weight(0.45f), color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                        Text("결제금액", modifier = Modifier.weight(0.35f), textAlign = TextAlign.End, color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(vertical = 8.dp)) {
                        items(paymentLines) { line ->
                            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Text(line.key.label, modifier = Modifier.weight(0.45f), fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                                Text("${formatAmount(line.value)}원", modifier = Modifier.weight(0.35f), textAlign = TextAlign.End, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                                FilledTonalIconButton(
                                    onClick = { paymentVm.removePaymentMethod(line.key) },
                                    modifier = Modifier.size(36.dp),
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color(0xFFF5F5F5))
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "결제 취소", tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("총 결제금액", style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp), fontWeight = FontWeight.Bold)
                            Text("${formatAmount(uiState.totalAmount)}원", style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp), color = Color(0xFFD32F2F), fontWeight = FontWeight.ExtraBold)
                        }
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("받은 금액", style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp), fontWeight = FontWeight.Bold)
                            Text("${formatAmount(uiState.receivedAmount)}원", style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp), color = Color(0xFF005645), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Right: Payment Input (Beige Background Area with its own cards)
            Column(
                modifier = Modifier.weight(1.1f).fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PaymentMethodGrid(
                    selected = uiState.selectedMethod,
                    methodAmounts = uiState.methodAmounts,
                    onSelect = paymentVm::selectPaymentMethod
                )
                
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("선택된 수단: ${uiState.selectedMethod?.label ?: "미선택"}", color = Color(0xFF005645), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("입력 금액", style = MaterialTheme.typography.titleLarge)
                            Text(
                                if (uiState.keypadInput.isBlank()) "0원" else "${formatAmount(uiState.keypadInput.toIntOrNull() ?: 0)}원",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }
                    }
                }
                
                NumericKeypad(
                    modifier = Modifier.weight(1f),
                    onKeyPress = paymentVm::onKeypadPressed
                )
                
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B))
                ) {
                    Text("결제 화면 닫기", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun PaymentMethodGrid(
    selected: PaymentMethod?,
    methodAmounts: Map<PaymentMethod, Int>,
    onSelect: (PaymentMethod) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth().height(210.dp), // Height adjusted for 2 rows
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        gridItems(PaymentMethod.values().toList()) { method ->
            val isSelected = selected == method
            val amount = methodAmounts[method] ?: 0
            Surface(
                onClick = { onSelect(method) },
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) Color(0xFF005645) else Color.White,
                contentColor = if (isSelected) Color.White else Color.Black,
                border = BorderStroke(1.dp, if (isSelected) Color(0xFF005645) else Color(0xFFDADCE0)),
                shadowElevation = if (isSelected) 4.dp else 1.dp,
                modifier = Modifier.height(100.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(method.icon, contentDescription = method.label, modifier = Modifier.size(28.dp))
                    Spacer(Modifier.height(6.dp))
                    Text(method.label, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 15.sp)
                    if (amount > 0) {
                        Text("${formatAmount(amount)}원", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color(0xFF005645))
                    }
                }
            }
        }
    }
}

@Composable
private fun NumericKeypad(modifier: Modifier = Modifier, onKeyPress: (String) -> Unit) {
    val keys = listOf(
        listOf("7", "8", "9", "Clear"),
        listOf("4", "5", "6", "Backspace"),
        listOf("1", "2", "3", "Enter"),
        listOf("0", "00", "만원")
    )
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        keys.forEachIndexed { rowIndex, rowKeys ->
            Row(modifier = Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                rowKeys.forEach { key ->
                    val isAction = key in listOf("Enter", "Clear", "Backspace")
                    Surface(
                        onClick = { onKeyPress(key) },
                        modifier = Modifier.weight(if (rowIndex == 3 && key == "만원") 1.6f else 1f).fillMaxHeight(),
                        shape = RoundedCornerShape(10.dp),
                        color = when (key) {
                            "Enter" -> Color(0xFF005645)
                            "Clear", "Backspace" -> Color(0xFF5A6B7A)
                            else -> Color.White
                        },
                        contentColor = if (isAction) Color.White else Color.Black,
                        border = if (!isAction) BorderStroke(1.dp, Color(0xFFDADCE0)) else null,
                        shadowElevation = 1.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (key == "Backspace") {
                                Icon(Icons.Filled.Backspace, contentDescription = key, modifier = Modifier.size(24.dp))
                            } else {
                                Text(key, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

