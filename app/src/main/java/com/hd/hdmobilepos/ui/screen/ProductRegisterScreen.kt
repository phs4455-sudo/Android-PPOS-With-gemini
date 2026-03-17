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

fun ProductRegisterScreen(
    barcode: String?,
    onNavigateHome: () -> Unit
) {
    val productRegisterVm: ProductRegisterViewModel = viewModel(
        key = "product_register_${barcode ?: "default"}",
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val lookupGateway = InMemoryProductLookupGateway()
                val scannerGateway = NoOpBarcodeScannerGateway()
                return ProductRegisterViewModel(
                    productLookupGateway = lookupGateway,
                    salesCartGateway = InMemorySalesCartGateway(),
                    barcodeScannerGateway = scannerGateway,
                    initialBarcode = barcode
                ) as T
            }
        }
    )
    val uiState by productRegisterVm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        productRegisterVm.events.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = { PosTopBar() },
        containerColor = Color(0xFFF6F2E9),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProductRegisterLeftPane(
                uiState = uiState,
                onDecrease = productRegisterVm::decreaseQty,
                onIncrease = productRegisterVm::increaseQty,
                onDelete = productRegisterVm::deleteItem,
                onClearAll = productRegisterVm::clearCart,
                onNavigateHome = onNavigateHome,
                modifier = Modifier.weight(0.45f)
            )
            ProductRegisterRightPane(
                uiState = uiState,
                onSelectCategory = productRegisterVm::selectCategory,
                onAddProduct = { productRegisterVm.addProduct(it.id) },
                onScanSubmit = productRegisterVm::simulateScan,
                modifier = Modifier.weight(0.55f)
            )
        }
    }
}

data class ProductItemUi(
    val id: String,
    val name: String,
    val price: Int,
    val category: String,
    val barcode: String
)

data class CartItemUi(
    val product: ProductItemUi,
    val qty: Int
) {
    val lineAmount: Int get() = product.price * qty
}

data class ProductRegisterUiState(
    val categories: List<String> = listOf("즐겨찾기", "채소", "과일", "생수", "종량제 봉투", "기타"),
    val selectedCategory: String = "즐겨찾기",
    val categoryProducts: List<ProductItemUi> = emptyList(),
    val cartItems: List<CartItemUi> = emptyList(),
    val totalAmount: Int = 0,
    val discountAmount: Int = 0,
    val receivedAmount: Int = 0,
    val barcodeInputState: String = ""
)

interface ProductLookupGateway {
    suspend fun findByBarcode(barcode: String): ProductItemUi?
    fun productsByCategory(category: String): List<ProductItemUi>
}

interface SalesCartGateway {
    suspend fun mergeItem(current: List<CartItemUi>, product: ProductItemUi): List<CartItemUi>
}

interface BarcodeScannerGateway {
    val scannedBarcodes: StateFlow<String?>
}

class InMemorySalesCartGateway : SalesCartGateway {
    override suspend fun mergeItem(current: List<CartItemUi>, product: ProductItemUi): List<CartItemUi> {
        val index = current.indexOfFirst { it.product.id == product.id }
        if (index < 0) return current + CartItemUi(product = product, qty = 1)
        return current.mapIndexed { i, cartItem ->
            if (i == index) cartItem.copy(qty = cartItem.qty + 1) else cartItem
        }
    }
}

class NoOpBarcodeScannerGateway : BarcodeScannerGateway {
    override val scannedBarcodes: StateFlow<String?> = MutableStateFlow(null)
}

class InMemoryProductLookupGateway : ProductLookupGateway {
    private val products = listOf(
        ProductItemUi("fav_apple", "사과", 2300, "즐겨찾기", "880000100001"),
        ProductItemUi("fav_water", "생수 2L", 1100, "즐겨찾기", "880000100002"),
        ProductItemUi("veg_cabbage", "양배추", 3800, "채소", "880000100101"),
        ProductItemUi("veg_onion", "양파 1망", 4500, "채소", "880000100102"),
        ProductItemUi("veg_spinach", "시금치", 2100, "채소", "880000100103"),
        ProductItemUi("veg_lettuce", "상추", 2600, "채소", "880000100104"),
        ProductItemUi("veg_potato", "감자 1봉", 3900, "채소", "880000100105"),
        ProductItemUi("veg_sweet_potato", "고구마", 4200, "채소", "880000100106"),
        ProductItemUi("veg_cucumber", "오이 2입", 1800, "채소", "880000100107"),
        ProductItemUi("veg_carrot", "당근", 1500, "채소", "880000100108"),
        ProductItemUi("veg_radish", "무", 2700, "채소", "880000100109"),
        ProductItemUi("veg_green_onion", "대파", 1900, "채소", "880000100110"),
        ProductItemUi("veg_mushroom", "느타리버섯", 3200, "채소", "880000100111"),
        ProductItemUi("veg_paprika", "파프리카", 3400, "채소", "880000100112"),
        ProductItemUi("veg_broccoli", "브로콜리", 2900, "채소", "880000100113"),
        ProductItemUi("veg_tomato", "토마토", 4100, "채소", "880000100114"),
        ProductItemUi("veg_garlic", "깐마늘", 5300, "채소", "880000100115"),
        ProductItemUi("veg_pumpkin", "단호박", 3600, "채소", "880000100116"),
        ProductItemUi("veg_bean_sprout", "콩나물", 1200, "채소", "880000100117"),
        ProductItemUi("veg_chili", "청양고추", 1700, "채소", "880000100118"),
        ProductItemUi("veg_eggplant", "가지", 2400, "채소", "880000100119"),
        ProductItemUi("veg_zucchini", "애호박", 2200, "채소", "880000100120"),
        ProductItemUi("fruit_banana", "바나나", 4200, "과일", "880000100201"),
        ProductItemUi("fruit_orange", "오렌지", 5500, "과일", "880000100202"),
        ProductItemUi("water_small", "생수 500ml", 700, "생수", "880000100301"),
        ProductItemUi("water_box", "생수 20입", 9800, "생수", "880000100302"),
        ProductItemUi("bag_5l", "종량제봉투 5L", 500, "종량제 봉투", "880000100401"),
        ProductItemUi("bag_10l", "종량제봉투 10L", 900, "종량제 봉투", "880000100402"),
        ProductItemUi("etc_battery", "건전지 AA 2입", 2000, "기타", "880000100501"),
        ProductItemUi("etc_tissue", "물티슈", 1800, "기타", "880000100502")
    )

    override suspend fun findByBarcode(barcode: String): ProductItemUi? =
        products.firstOrNull { it.barcode == barcode }

    override fun productsByCategory(category: String): List<ProductItemUi> =
        products.filter { it.category == category || (category == "즐겨찾기" && it.category == "즐겨찾기") }
}

class ProductRegisterViewModel(
    private val productLookupGateway: ProductLookupGateway,
    private val salesCartGateway: SalesCartGateway,
    barcodeScannerGateway: BarcodeScannerGateway,
    initialBarcode: String?
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductRegisterUiState())
    val uiState: StateFlow<ProductRegisterUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<String>()
    val events: SharedFlow<String> = _events.asSharedFlow()

    init {
        selectCategory(_uiState.value.selectedCategory)
        if (!initialBarcode.isNullOrBlank()) {
            processBarcode(initialBarcode)
        }
        viewModelScope.launch {
            barcodeScannerGateway.scannedBarcodes.collectLatest { scanned ->
                if (!scanned.isNullOrBlank()) {
                    processBarcode(scanned)
                }
            }
        }
    }

    fun selectCategory(category: String) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                categoryProducts = productLookupGateway.productsByCategory(category)
            )
        }
    }

    fun addProduct(productId: String) {
        val product = _uiState.value.categoryProducts.firstOrNull { it.id == productId } ?: return
        viewModelScope.launch {
            val nextCart = salesCartGateway.mergeItem(_uiState.value.cartItems, product)
            updateCart(nextCart)
        }
    }

    fun increaseQty(productId: String) = addProduct(productId)

    fun decreaseQty(productId: String) {
        val next = _uiState.value.cartItems.mapNotNull {
            if (it.product.id != productId) it
            else if (it.qty <= 1) null else it.copy(qty = it.qty - 1)
        }
        updateCart(next)
    }

    fun deleteItem(productId: String) {
        updateCart(_uiState.value.cartItems.filterNot { it.product.id == productId })
    }

    fun clearCart() {
        updateCart(emptyList())
    }

    fun simulateScan(barcode: String) {
        _uiState.update { it.copy(barcodeInputState = "") }
        processBarcode(barcode.trim())
    }

    private fun processBarcode(barcode: String) {
        if (barcode.isBlank()) return
        viewModelScope.launch {
            val product = productLookupGateway.findByBarcode(barcode)
            if (product == null) {
                _events.emit("등록되지 않은 바코드입니다: $barcode")
                return@launch
            }
            val nextCart = salesCartGateway.mergeItem(_uiState.value.cartItems, product)
            updateCart(nextCart)
            _events.emit("${product.name} 상품이 추가되었습니다.")
        }
    }

    private fun updateCart(cartItems: List<CartItemUi>) {
        val total = cartItems.sumOf { it.lineAmount }
        _uiState.update {
            it.copy(
                cartItems = cartItems,
                totalAmount = total,
                discountAmount = 0,
                receivedAmount = total
            )
        }
    }
}

@Composable
private fun CartHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F3F4))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("No", modifier = Modifier.width(32.dp), style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Bold, color = Color.Gray)
        Text("상품명", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Bold, color = Color.Gray)
        Text("단가", modifier = Modifier.width(80.dp), textAlign = TextAlign.End, style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(Modifier.width(12.dp)) // Added gap as requested
        Text("수량", modifier = Modifier.width(110.dp), textAlign = TextAlign.Center, style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Bold, color = Color.Gray)
        Text("금액", modifier = Modifier.width(90.dp), textAlign = TextAlign.End, style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(Modifier.width(44.dp))
    }
}

@Composable
private fun CompactCartRow(
    index: Int,
    item: CartItemUi,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp) // Reduced height for 6-row visibility
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$index", modifier = Modifier.width(32.dp), style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp), color = Color.Gray)
        
        Column(modifier = Modifier.weight(1f)) {
            Text(item.product.name, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp), fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(item.product.barcode, style = MaterialTheme.typography.labelSmall.copy(fontSize = 12.sp), color = Color.LightGray)
        }

        Text(formatAmount(item.product.price), modifier = Modifier.width(80.dp), textAlign = TextAlign.End, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp))

        Spacer(Modifier.width(12.dp)) // Added gap as requested

        Row(
            modifier = Modifier.width(110.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material3.IconButton(onClick = onDecrease, modifier = Modifier.size(36.dp)) {
                Icon(androidx.compose.material.icons.Icons.Filled.Remove, contentDescription = null, modifier = Modifier.size(18.dp))
            }
            Text("${item.qty}", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp), fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp))
            androidx.compose.material3.IconButton(onClick = onIncrease, modifier = Modifier.size(36.dp)) {
                Icon(androidx.compose.material.icons.Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }

        Text(
            formatAmount(item.lineAmount),
            modifier = Modifier.width(90.dp),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF005645)
        )

        androidx.compose.material3.IconButton(onClick = onDelete, modifier = Modifier.width(44.dp)) {
            Icon(androidx.compose.material.icons.Icons.Filled.Close, contentDescription = null, tint = Color(0xFFDADCE0), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun BoxScope.ScrollHintIcon(show: Boolean, isUp: Boolean = false) {
    if (show) {
        val infiniteTransition = rememberInfiniteTransition(label = "scrollHint")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = if (isUp) -8f else 8f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "bounce"
        )

        Icon(
            // KeyboardArrowUp 대신 범용적인 ExpandLess 사용 (KeyboardArrowDown의 짝꿍)
            imageVector = if (isUp) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF005645),
            modifier = Modifier
                .align(if (isUp) Alignment.TopCenter else Alignment.BottomCenter)
                .offset(y = offsetY.dp)
                .size(44.dp)
                // Modifier를 인자로 전달하던 오류 수정: Dp 값을 직접 할당
                .padding(top = if (isUp) 6.dp else 0.dp, bottom = if (isUp) 0.dp else 6.dp)
        )
    }
}

@Composable
private fun ProductRegisterLeftPane(
    uiState: ProductRegisterUiState,
    onDecrease: (String) -> Unit,
    onIncrease: (String) -> Unit,
    onDelete: (String) -> Unit,
    onClearAll: () -> Unit,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    
    // Auto-focus on new items
    LaunchedEffect(uiState.cartItems.size) {
        if (uiState.cartItems.isNotEmpty()) {
            listState.animateScrollToItem(uiState.cartItems.size - 1)
        }
    }

    val showScrollDownHint by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) false 
            else visibleItems.last().index < totalItems - 1
        }
    }

    val showScrollUpHint by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }
    }

    Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Main Cart Container
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0))
        ) {
            Column(Modifier.fillMaxSize()) {
                CartHeader()
                Box(modifier = Modifier.weight(1f)) {
                    if (uiState.cartItems.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = Color(0xFFEEEEEE), modifier = Modifier.size(80.dp))
                                Spacer(Modifier.height(16.dp))
                                Text("주문 상품이 없습니다", color = Color.LightGray, style = MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp))
                                Spacer(Modifier.height(24.dp))
                                Button(
                                    onClick = onNavigateHome,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005645))
                                ) {
                                    Icon(Icons.Filled.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("홈으로 돌아가기", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                }
                            }
                        }
                    } else {
                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(uiState.cartItems, key = { _, item -> item.product.id }) { index, item ->
                                CompactCartRow(
                                    index = index + 1,
                                    item = item,
                                    onDecrease = { onDecrease(item.product.id) },
                                    onIncrease = { onIncrease(item.product.id) },
                                    onDelete = { onDelete(item.product.id) }
                                )
                                Divider(color = Color(0xFFF1F3F4), thickness = 1.dp)
                            }
                        }
                    }
                    ScrollHintIcon(show = showScrollUpHint, isUp = true)
                    ScrollHintIcon(show = showScrollDownHint, isUp = false)
                }

                // Left Action Row
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f).height(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3C4043))
                    ) { Text("행사적용", fontWeight = FontWeight.Bold, fontSize = 17.sp) }
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f).height(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3C4043))
                    ) { Text("주문 보류", fontWeight = FontWeight.Bold, fontSize = 17.sp) }
                    Button(
                        onClick = onClearAll,
                        modifier = Modifier.weight(1f).height(60.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE), contentColor = Color(0xFFD32F2F))
                    ) { Text("전체취소", fontWeight = FontWeight.Bold, fontSize = 17.sp) }
                }
            }
        }

        // Summary Card: The Hyundai Premium Green Theme
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF003D32), // Deep Premium Hyundai Green
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 6.dp
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("총 매출", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp))
                    Text("${formatAmount(uiState.totalAmount)}", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp))
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("할인 금액", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp))
                    Text("-${formatAmount(uiState.discountAmount)}", color = Color(0xFFA5D6A7), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp), fontWeight = FontWeight.Medium)
                }
                Divider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text("받을 금액", color = Color.White, style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp), fontWeight = FontWeight.ExtraBold)
                    Text(
                        "${formatAmount(uiState.receivedAmount)} 원",
                        color = Color(0xFFFFD54F), // Elegant Gold Accent
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun NewCartItemRow(
    item: CartItemUi,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF202124))
            Text(item.product.barcode, style = MaterialTheme.typography.labelSmall, color = Color(0xFF70757A), modifier = Modifier.padding(top = 2.dp))
            Text("${formatAmount(item.product.price)} 원", style = MaterialTheme.typography.bodySmall, color = Color(0xFF5F6368), modifier = Modifier.padding(top = 2.dp))
        }
        
        // High-Speed Stepper with Large Touch Targets
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(Color(0xFFF1F3F4), RoundedCornerShape(10.dp))
                .padding(2.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            androidx.compose.material3.IconButton(onClick = onDecrease, modifier = Modifier.size(42.dp)) {
                Icon(Icons.Filled.Remove, contentDescription = "감소", modifier = Modifier.size(20.dp), tint = Color(0xFF3C4043))
            }
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0)),
                modifier = Modifier.width(48.dp).height(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "${item.qty}",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF202124)
                    )
                }
            }
            androidx.compose.material3.IconButton(onClick = onIncrease, modifier = Modifier.size(42.dp)) {
                Icon(Icons.Filled.Add, contentDescription = "증가", modifier = Modifier.size(20.dp), tint = Color(0xFF3C4043))
            }
        }

        Text(
            "${formatAmount(item.lineAmount)}",
            modifier = Modifier.width(90.dp),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF005645)
        )

        androidx.compose.material3.IconButton(onClick = onDelete, modifier = Modifier.padding(start = 8.dp)) {
            Icon(Icons.Filled.Close, contentDescription = "삭제", tint = Color(0xFFDADCE0), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun ProductRegisterRightPane(
    uiState: ProductRegisterUiState,
    onSelectCategory: (String) -> Unit,
    onAddProduct: (ProductItemUi) -> Unit,
    onScanSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    var showManualKeypad by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val gridState = rememberLazyGridState()
    
    val showScrollDownHint by remember {
        derivedStateOf {
            val layoutInfo = gridState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) false 
            else visibleItems.last().index < totalItems - 1
        }
    }

    val showScrollUpHint by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
        }
    }
    
    Column(modifier = modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Menu Grid Container
        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0))
        ) {
            Column(Modifier.fillMaxSize()) {
                // Category Tabs: Scaled 1.1x
                ScrollableTabRow(
                    selectedTabIndex = uiState.categories.indexOf(uiState.selectedCategory).coerceAtLeast(0),
                    containerColor = Color.White,
                    contentColor = Color(0xFF005645),
                    edgePadding = 16.dp,
                    divider = {}
                ) {
                    uiState.categories.forEachIndexed { index, category ->
                        Tab(
                            selected = uiState.selectedCategory == category,
                            onClick = { onSelectCategory(category) },
                            modifier = Modifier.height(44.dp) // Reduced from 48dp
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                if (index == 0) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFF2C94C), modifier = Modifier.size(16.dp))
                                }
                                Text(category, fontWeight = if (uiState.selectedCategory == category) FontWeight.ExtraBold else FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                // Barcode Manual Search Area
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 2.dp), // Minimal vertical padding for balance
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it.filter { c -> c.isDigit() } },
                        modifier = Modifier.weight(1f).height(52.dp), // Increased from 42dp
                        placeholder = { Text("바코드 수기입력", fontSize = 15.sp) }, // Increased font size
                        leadingIcon = { Icon(Icons.Filled.Keyboard, contentDescription = null, tint = Color(0xFF5A6B7A), modifier = Modifier.size(20.dp)) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF005645),
                            unfocusedBorderColor = Color(0xFFDADCE0)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            onScanSubmit(input)
                            input = ""
                            keyboardController?.hide()
                        })
                    )
                    Button(
                        onClick = { showManualKeypad = true },
                        modifier = Modifier.height(52.dp), // Match TextField height
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A6B7A))
                    ) {
                        Icon(Icons.Filled.PointOfSale, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("키패드", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        gridItems(uiState.categoryProducts) { product ->
                            Surface(
                                onClick = { onAddProduct(product) },
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F3F4)),
                                shadowElevation = 1.dp
                            ) {
                                Column(
                                    Modifier.padding(8.dp).height(80.dp), // Adjusted height for better grid fit with larger input
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        product.name, 
                                        maxLines = 2, 
                                        textAlign = TextAlign.Center, 
                                        fontSize = 15.sp, 
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 18.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        formatAmount(product.price), 
                                        color = Color(0xFF005645), 
                                        fontWeight = FontWeight.Black,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                    ScrollHintIcon(show = showScrollUpHint, isUp = true)
                    ScrollHintIcon(show = showScrollDownHint, isUp = false)
                }
            }
        }

        // Tenders Row: Harmonious 3-Column Color Rhythm aligned with Left Summary
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                ProductRegisterActionButton("장바구니할인", Icons.Filled.LocalOffer, Color(0xFF5A6B7A), Modifier.weight(1f))
                ProductRegisterActionButton("상품권", Icons.Filled.CardGiftcard, Color(0xFFC1A57A), Modifier.weight(1f))
                ProductRegisterActionButton("현금", Icons.Filled.Payments, Color(0xFF005645), Modifier.weight(1.2f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                ProductRegisterActionButton("기타시재", Icons.Filled.PointOfSale, Color(0xFF5A6B7A), Modifier.weight(1f))
                ProductRegisterActionButton("H.Point 사용", Icons.Filled.Stars, Color(0xFFC1A57A), Modifier.weight(1f))
                ProductRegisterActionButton("카드/모바일", Icons.Filled.CreditCard, Color(0xFF005645), Modifier.weight(1.2f))
            }
        }
    }

    if (showManualKeypad) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showManualKeypad = false }) {
            ManualBarcodeKeypad(
                value = input,
                onValueChange = { input = it },
                onConfirm = {
                    onScanSubmit(input)
                    input = ""
                    showManualKeypad = false
                },
                onClose = { showManualKeypad = false }
            )
        }
    }
}

@Composable
private fun ProductRegisterActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier
) {
    Button(
        onClick = {},
        modifier = modifier.height(84.dp), // Increased for larger fonts
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(6.dp))
            Text(label, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, fontSize = 17.sp)
        }
    }
}

@Composable
private fun ManualBarcodeKeypad(
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onClose: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 16.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0))
    ) {
        Column(
            modifier = Modifier.width(360.dp).padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("바코드 수기입력", fontWeight = FontWeight.Black, fontSize = 22.sp, color = Color(0xFF202124))
                androidx.compose.material3.IconButton(onClick = onClose) { Icon(Icons.Filled.Close, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(28.dp)) }
            }
            
            // Input Display
            Surface(
                modifier = Modifier.fillMaxWidth().height(72.dp),
                color = Color(0xFFF1F3F4),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0))
            ) {
                Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = if (value.isEmpty()) "0" else value,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                        fontWeight = FontWeight.Black,
                        color = if (value.isEmpty()) Color.LightGray else Color(0xFF005645)
                    )
                }
            }

            // Numeric Grid
            val keys = listOf(
                listOf("7", "8", "9"),
                listOf("4", "5", "6"),
                listOf("1", "2", "3"),
                listOf("C", "0", "OK")
            )
            
            keys.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { key ->
                        val isOK = key == "OK"
                        val isC = key == "C"
                        Button(
                            onClick = {
                                when (key) {
                                    "C" -> onValueChange("")
                                    "OK" -> onConfirm()
                                    else -> onValueChange(value + key)
                                }
                            },
                            modifier = Modifier.weight(1f).height(80.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOK) Color(0xFF005645) else if (isC) Color(0xFFFFEBEE) else Color.White,
                                contentColor = if (isOK) Color.White else if (isC) Color(0xFFD32F2F) else Color(0xFF3C4043)
                            ),
                            border = if (!isOK && !isC) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDADCE0)) else null,
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            Text(key, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

@Composable
