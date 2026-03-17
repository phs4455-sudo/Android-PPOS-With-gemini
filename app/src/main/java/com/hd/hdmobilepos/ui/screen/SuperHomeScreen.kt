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

@Composable
fun SuperHomeScreen(
    superHomeVm: SuperHomeViewModel,
    onNavigateToProductRegister: (String?) -> Unit,
    onNavigateToRestaurant: () -> Unit
) {
    val uiState by superHomeVm.uiState.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var barcodeHasFocus by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(barcodeHasFocus) {
        if (!barcodeHasFocus) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PosTopBar() },
        containerColor = Color(0xFFF1F3F4), // Professional light grey background
        bottomBar = {
            SuperUtilityBar(
                labels = listOf("고정 메뉴", "시재 점검", "셀프계산대 전환", "전달노트", "따라하기", "직원외 할인")
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Content: 65% (Quick Actions & Grid)
            Column(
                modifier = Modifier.weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Barcode Search Bar
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0))
                ) {
                    OutlinedTextField(
                        value = uiState.barcodeInput,
                        onValueChange = superHomeVm::onBarcodeInputChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { barcodeHasFocus = it.isFocused },
                        placeholder = { Text("바코드를 스캔하거나 입력하세요") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF005645)) },
                        singleLine = true,
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            val barcode = superHomeVm.consumeBarcodeForNavigation() ?: return@KeyboardActions
                            onNavigateToProductRegister(barcode)
                        })
                    )
                }

                // Hero Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NewHeroTile(
                        label = "상품판매",
                        icon = Icons.Filled.AddShoppingCart,
                        color = Color(0xFF005645),
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToProductRegister(null) }
                    )
                    NewHeroTile(
                        label = "식당관리",
                        icon = Icons.Filled.TableRestaurant,
                        color = Color(0xFFC1A57A),
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToRestaurant
                    )
                    NewHeroTile(
                        label = "통합조회",
                        icon = Icons.Filled.Search,
                        color = Color(0xFF5A6B7A),
                        modifier = Modifier.weight(1f),
                        onClick = {}
                    )
                }

                // Secondary Actions Grid
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0))
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.fillMaxSize().padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        val items = listOf("즐겨찾기", "행사보관", "영수증 조회", "소비기한 등록", "상품조회", "포인트 조회", "보류/복원", "도중회수")
                        gridItems(items) { label ->
                            OutlinedButton(
                                onClick = {
                                    if (label == "즐겨찾기") {
                                        onNavigateToRestaurant()
                                    }
                                },
                                modifier = Modifier.height(88.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F3F4)),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3C4043))
                            ) {
                                Text(label, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Right Content: 35% (Summary & History)
            Column(
                modifier = Modifier.weight(0.35f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Receipt Style Transaction Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0)),
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.AccessTime, contentDescription = null, tint = Color(0xFF005645), modifier = Modifier.size(24.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("직전 결제 요약", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Divider(color = Color(0xFFF1F3F4))
                        SummaryLine("상품 수량", "${uiState.lastTransaction.itemCount} EA")
                        SummaryLine("총 금액", "${formatAmount(uiState.lastTransaction.purchaseAmount)} 원")
                        SummaryLine("할인 금액", "- ${formatAmount(uiState.lastTransaction.discountAmount)} 원")
                        Divider(color = Color(0xFFF1F3F4))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("거스름돈", fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F), style = MaterialTheme.typography.titleLarge)
                            Text("${formatAmount(uiState.lastTransaction.changeAmount)} 원", fontWeight = FontWeight.ExtraBold, color = Color(0xFFD32F2F), style = MaterialTheme.typography.headlineMedium)
                        }
                    }
                }

                // Utility Buttons
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val sideButtons = listOf("직전 영수증 출력", "직전 거래 환불", "보류 내역", "도중 회수")
                    sideButtons.forEach { label ->
                        Button(
                            onClick = {},
                            modifier = Modifier.fillMaxWidth().height(64.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A6B7A), contentColor = Color.White)
                        ) {
                            Text(label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { keyboardController?.hide() }
    }

    fun navigateWithBarcode() {
        val barcode = superHomeVm.consumeBarcodeForNavigation() ?: return
        onNavigateToProductRegister(barcode)
        focusRequester.requestFocus()
    }

    LaunchedEffect(uiState.barcodeInput) {
        if (uiState.barcodeInput.contains("\n") || uiState.barcodeInput.contains("\r")) {
            navigateWithBarcode()
        }
    }
}

@Composable
private fun NewHeroTile(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(16.dp),
        color = color,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
            Spacer(Modifier.height(10.dp))
            Text(label, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun SummaryLine(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color(0xFF5F6368), style = MaterialTheme.typography.bodyLarge)
        Text(value, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun SuperUtilityBar(labels: List<String>) {
    Surface(color = Color(0xFFE8EAED)) {
        Column {
            Divider(color = Color(0xFFDADCE0), thickness = 1.dp)
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                labels.forEach { label ->
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B), contentColor = Color.White)
                    ) {
                        Text(label, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
