package com.hd.hdmobilepos

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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "ppos.db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
        val repo = PosRepository(db.posDao())

        setContent {
            PPOSTheme {
                val factory = remember {
                    object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(repo) as T
                    }
                }
                val vm: MainViewModel = viewModel(factory = factory)
                MainNavHost(vm = vm)
            }
        }
    }
}

data class RightPanelItemUi(
    val orderItemId: Long,
    val itemName: String,
    val priceSnapshot: Int,
    val qty: Int,
    val lineTotal: Int
)

data class RightOrderPanelUi(
    val orderId: Long,
    val orderStatus: String,
    val elapsedLabel: String,
    val items: List<RightPanelItemUi>,
    val orderTotalAmount: Int,
    val derivedTotalAmount: Int,
    val isTotalMismatch: Boolean
)


data class PaymentOrderItemUi(
    val name: String,
    val qty: Int,
    val price: Int
)

data class PaymentOrderSnapshot(
    val tableId: Long?,
    val tableName: String,
    val items: List<PaymentOrderItemUi>,
    val totalAmount: Int,
    val receivedAmount: Int
)

enum class PaymentMethod(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    CARD_MOBILE("카드/모바일", Icons.Filled.CreditCard),
    CASH("현금", Icons.Filled.Payments),
    GIFT_CARD("상품권", Icons.Filled.CardGiftcard),
    H_POINT("H-POINT", Icons.Filled.Loyalty),
    PARTNER_CARD("제휴카드", Icons.Filled.CreditCard),
    SIMPLE_PAY("간편결제", Icons.Filled.PhoneAndroid)
}

data class PaymentUiState(
    val tableName: String = "",
    val items: List<PaymentOrderItemUi> = emptyList(),
    val totalAmount: Int = 0,
    val receivedAmount: Int = 0,
    val selectedMethod: PaymentMethod? = null,
    val keypadInput: String = "",
    val methodAmounts: Map<PaymentMethod, Int> = emptyMap()
)

class PaymentViewModel(initialSnapshot: PaymentOrderSnapshot) : ViewModel() {
    private val _uiState = MutableStateFlow(
        PaymentUiState(
            tableName = initialSnapshot.tableName,
            items = initialSnapshot.items,
            totalAmount = initialSnapshot.totalAmount,
            receivedAmount = initialSnapshot.receivedAmount
        )
    )
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedMethod = method, keypadInput = "") }
    }

    fun onKeypadPressed(key: String) {
        when (key) {
            "Clear" -> _uiState.update { it.copy(keypadInput = "") }
            "Backspace" -> _uiState.update { state ->
                state.copy(keypadInput = state.keypadInput.dropLast(1))
            }
            "만원" -> _uiState.update { state ->
                val next = if (state.keypadInput.isBlank()) "10000" else state.keypadInput + "0000"
                state.copy(keypadInput = next)
            }
            "Enter" -> applyEnteredAmount()
            else -> {
                if (key.all { it.isDigit() }) {
                    _uiState.update { state -> state.copy(keypadInput = state.keypadInput + key) }
                }
            }
        }
    }

    private fun applyEnteredAmount() {
        _uiState.update { state ->
            val method = state.selectedMethod ?: return@update state
            val amount = state.keypadInput.toIntOrNull() ?: 0
            val updatedMap = state.methodAmounts.toMutableMap().apply { this[method] = amount }
            state.copy(
                methodAmounts = updatedMap,
                receivedAmount = updatedMap.values.sum(),
                keypadInput = ""
            )
        }
    }

    fun removePaymentMethod(method: PaymentMethod) {
        _uiState.update { state ->
            val updatedMap = state.methodAmounts.toMutableMap().apply { remove(method) }
            state.copy(
                methodAmounts = updatedMap,
                receivedAmount = updatedMap.values.sum()
            )
        }
    }
}


data class LastTransactionSummaryUi(
    val itemCount: Int = 0,
    val purchaseAmount: Int = 0,
    val discountAmount: Int = 0,
    val receivedAmount: Int = 0,
    val changeAmount: Int = 0
)

data class SuperHomeUiState(
    val barcodeInput: String = "",
    val lastTransaction: LastTransactionSummaryUi = LastTransactionSummaryUi()
)

class SuperHomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SuperHomeUiState())
    val uiState: StateFlow<SuperHomeUiState> = _uiState.asStateFlow()

    fun onBarcodeInputChanged(value: String) {
        _uiState.update { it.copy(barcodeInput = value) }
    }

    fun consumeBarcodeForNavigation(): String? {
        val barcode = _uiState.value.barcodeInput.trim()
        if (barcode.isBlank()) return null
        _uiState.update { it.copy(barcodeInput = "") }
        return barcode
    }
}

private const val ROUTE_RESTAURANT = "restaurant"
private const val ROUTE_SUPER_HOME = "super_home"
private const val ROUTE_PRODUCT_REGISTER = "product_register"
private const val ROUTE_PRODUCT_REGISTER_WITH_ARG = "product_register?barcode={barcode}"
private const val ARG_BARCODE = "barcode"

private fun productRegisterRoute(barcode: String? = null): String {
    return if (barcode.isNullOrBlank()) ROUTE_PRODUCT_REGISTER else "$ROUTE_PRODUCT_REGISTER?$ARG_BARCODE=${Uri.encode(barcode)}"
}

enum class UiMode {
    NORMAL,
    SELECT_TARGET_FOR_MOVE,
    SELECT_TARGET_FOR_MERGE
}

enum class TableActionType {
    MOVE,
    MERGE
}

data class PendingTableAction(
    val type: TableActionType,
    val sourceTableId: Long,
    val targetTableId: Long
)

data class MainUiState(
    val areas: List<Area> = emptyList(),
    val selectedAreaId: Long? = null,
    val tables: List<TableSummary> = emptyList(),
    val selectedTableId: Long? = null,
    val selectedSourceTableId: Long? = null,
    val selectedTargetTableId: Long? = null,
    val uiMode: UiMode = UiMode.NORMAL,
    val pendingAction: PendingTableAction? = null,
    val snackbarMessage: String? = null,
    val rightPanel: RightOrderPanelUi? = null,
    val isReseeding: Boolean = false,
    val reseedMessage: String? = null
)

class MainViewModel(private val repository: PosRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private var tableObserverJob: Job? = null
    private var rightPanelObserverJob: Job? = null
    private val canceledPriceMemory = mutableMapOf<Long, Int>()

    init {
        viewModelScope.launch {
            bootstrapData()
            repository.observeAreas().collectLatest { areas ->
                val selectedArea = _uiState.value.selectedAreaId ?: repository.findFirstAreaIdWithTables()
                _uiState.update { it.copy(areas = areas, selectedAreaId = selectedArea) }
                selectedArea?.let(::observeTables)
            }
        }
    }

    fun selectArea(areaId: Long) {
        _uiState.update {
            it.copy(
                selectedAreaId = areaId,
                selectedTableId = null,
                selectedSourceTableId = null,
                selectedTargetTableId = null,
                uiMode = UiMode.NORMAL,
                pendingAction = null,
                rightPanel = null
            )
        }
        observeTables(areaId)
    }

    fun selectTable(tableId: Long) {
        _uiState.update {
            it.copy(
                selectedTableId = tableId,
                selectedSourceTableId = tableId,
                selectedTargetTableId = null,
                uiMode = UiMode.NORMAL,
                pendingAction = null
            )
        }
        observeRightPanel(tableId)
    }


    fun buildPaymentSnapshot(tableId: Long?): PaymentOrderSnapshot {
        val state = _uiState.value
        val resolvedTableId = tableId ?: state.selectedTableId
        val table = state.tables.firstOrNull { it.tableId == resolvedTableId }
        val panel = state.rightPanel
        val items = panel?.items?.map { PaymentOrderItemUi(name = it.itemName, qty = it.qty, price = it.lineTotal) }.orEmpty()
        val total = panel?.derivedTotalAmount ?: 0
        return PaymentOrderSnapshot(
            tableId = resolvedTableId,
            tableName = table?.tableName ?: "선택된 테이블 없음",
            items = items,
            totalAmount = total,
            receivedAmount = total
        )
    }

    fun startMoveMode() {
        val sourceId = _uiState.value.selectedTableId
        if (sourceId == null) {
            pushSnackbar("먼저 원본 테이블을 선택하세요")
            return
        }
        if (_uiState.value.rightPanel == null) {
            pushSnackbar("이동할 활성 주문이 없습니다")
            return
        }
        _uiState.update {
            it.copy(
                uiMode = UiMode.SELECT_TARGET_FOR_MOVE,
                selectedSourceTableId = sourceId,
                selectedTargetTableId = null,
                pendingAction = null
            )
        }
    }

    fun startMergeMode() {
        val sourceId = _uiState.value.selectedTableId
        if (sourceId == null) {
            pushSnackbar("먼저 원본 테이블을 선택하세요")
            return
        }
        if (_uiState.value.rightPanel == null) {
            pushSnackbar("합석할 활성 주문이 없습니다")
            return
        }
        _uiState.update {
            it.copy(
                uiMode = UiMode.SELECT_TARGET_FOR_MERGE,
                selectedSourceTableId = sourceId,
                selectedTargetTableId = null,
                pendingAction = null
            )
        }
    }

    fun onTableTileClicked(tableId: Long, status: String) {
        val state = _uiState.value
        if (state.uiMode == UiMode.NORMAL) {
            selectTable(tableId)
            return
        }

        val sourceId = state.selectedSourceTableId
        if (sourceId == null) {
            _uiState.update { it.copy(uiMode = UiMode.NORMAL) }
            return
        }
        if (status == "DISABLED") {
            pushSnackbar("사용불가 테이블은 선택할 수 없습니다")
            return
        }
        if (sourceId == tableId) {
            pushSnackbar("다른 대상 테이블을 선택하세요")
            return
        }

        viewModelScope.launch {
            if (state.uiMode == UiMode.SELECT_TARGET_FOR_MERGE && !repository.hasActiveOrder(tableId)) {
                pushSnackbar("합석은 양쪽 테이블에 활성 주문이 있어야 합니다")
                return@launch
            }
            val type = if (state.uiMode == UiMode.SELECT_TARGET_FOR_MOVE) TableActionType.MOVE else TableActionType.MERGE
            _uiState.update {
                it.copy(
                    selectedTargetTableId = tableId,
                    pendingAction = PendingTableAction(
                        type = type,
                        sourceTableId = sourceId,
                        targetTableId = tableId
                    )
                )
            }
        }
    }

    fun dismissPendingAction() {
        _uiState.update { it.copy(pendingAction = null, selectedTargetTableId = null, uiMode = UiMode.NORMAL) }
    }

    fun confirmPendingAction() {
        val action = _uiState.value.pendingAction ?: return
        viewModelScope.launch {
            when (action.type) {
                TableActionType.MOVE -> {
                    val moved = repository.moveActiveOrder(action.sourceTableId, action.targetTableId)
                    if (!moved) pushSnackbar("이동할 활성 주문이 없습니다")
                }

                TableActionType.MERGE -> {
                    repository.mergeTables(action.sourceTableId, action.targetTableId)
                    pushSnackbar("합석 처리되었습니다")
                }
            }
            _uiState.update {
                it.copy(
                    pendingAction = null,
                    selectedTargetTableId = null,
                    uiMode = UiMode.NORMAL,
                    selectedTableId = action.targetTableId,
                    selectedSourceTableId = action.targetTableId
                )
            }
            observeRightPanel(action.targetTableId)
        }
    }

    fun consumeSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun onTableDropped(sourceTableId: Long, targetTableId: Long) {
        if (sourceTableId == targetTableId) return
        val targetTable = _uiState.value.tables.firstOrNull { it.tableId == targetTableId } ?: return
        if (targetTable.status == "DISABLED") {
            pushSnackbar("사용불가 테이블은 대상이 될 수 없습니다")
            return
        }

        viewModelScope.launch {
            if (!repository.hasActiveOrder(sourceTableId)) {
                pushSnackbar("원본 테이블에 활성 주문이 없습니다")
                return@launch
            }
            val targetHasActiveOrder = repository.hasActiveOrder(targetTableId)
            if (targetHasActiveOrder) {
                _uiState.update {
                    it.copy(
                        selectedTargetTableId = targetTableId,
                        pendingAction = PendingTableAction(
                            type = TableActionType.MERGE,
                            sourceTableId = sourceTableId,
                            targetTableId = targetTableId
                        )
                    )
                }
                return@launch
            }

            val moved = repository.moveActiveOrder(sourceTableId, targetTableId)
            if (!moved) {
                pushSnackbar("이동할 활성 주문이 없습니다")
                return@launch
            }
            pushSnackbar("이동 처리되었습니다")

            _uiState.update {
                it.copy(
                    selectedTableId = targetTableId,
                    selectedSourceTableId = targetTableId,
                    selectedTargetTableId = null,
                    uiMode = UiMode.NORMAL,
                    pendingAction = null
                )
            }
            observeRightPanel(targetTableId)
        }
    }

    fun addMenuToSelectedTable(menuName: String, price: Int) {
        val tableId = _uiState.value.selectedTableId ?: return
        viewModelScope.launch {
            repository.addMenuToTable(tableId = tableId, menuName = menuName, price = price)
        }
    }

    fun increaseOrderItemQty(orderItemId: Long) {
        val orderId = _uiState.value.rightPanel?.orderId ?: return
        viewModelScope.launch {
            repository.changeOrderItemQty(orderId = orderId, orderItemId = orderItemId, delta = 1)
        }
    }

    fun decreaseOrderItemQty(orderItemId: Long) {
        val panel = _uiState.value.rightPanel ?: return
        val target = panel.items.firstOrNull { it.orderItemId == orderItemId } ?: return
        if (target.qty <= 1) {
            pushSnackbar("수량은 1 이상이어야 합니다")
            return
        }
        val orderId = panel.orderId
        viewModelScope.launch {
            repository.changeOrderItemQty(orderId = orderId, orderItemId = orderItemId, delta = -1)
        }
    }

    fun changeOrderItemUnitPrice(orderItemId: Long, newPrice: Int) {
        if (newPrice <= 0) {
            pushSnackbar("금액은 1원 이상이어야 합니다")
            return
        }
        val panel = _uiState.value.rightPanel ?: return
        val orderId = panel.orderId
        viewModelScope.launch {
            repository.changeOrderItemUnitPrice(orderId = orderId, orderItemId = orderItemId, newPrice = newPrice)
        }
    }

    fun toggleOrderItemCanceled(orderItemId: Long) {
        val panel = _uiState.value.rightPanel ?: return
        val item = panel.items.firstOrNull { it.orderItemId == orderItemId } ?: return
        val orderId = panel.orderId
        viewModelScope.launch {
            if (item.priceSnapshot > 0) {
                canceledPriceMemory[orderItemId] = item.priceSnapshot
                repository.changeOrderItemUnitPrice(orderId = orderId, orderItemId = orderItemId, newPrice = 0)
                pushSnackbar("상품 지정취소 처리되었습니다")
            } else {
                val restorePrice = canceledPriceMemory[orderItemId] ?: 8000
                repository.changeOrderItemUnitPrice(orderId = orderId, orderItemId = orderItemId, newPrice = restorePrice)
                pushSnackbar("상품 지정취소가 해제되었습니다")
            }
        }
    }

    fun cancelAllCurrentOrderItems() {
        val orderId = _uiState.value.rightPanel?.orderId ?: return
        viewModelScope.launch {
            repository.cancelAllOrderItems(orderId)
            pushSnackbar("주문내역이 전체 취소되었습니다")
        }
    }

    private fun pushSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun reseedDemoData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isReseeding = true, reseedMessage = null) }
            try {
                repository.forceReseedDemoData()
                applyOneShotState("재생성 완료")
            } catch (t: Throwable) {
                _uiState.update { it.copy(reseedMessage = "재생성 실패: ${t.message ?: "unknown"}") }
            } finally {
                _uiState.update { it.copy(isReseeding = false) }
            }
        }
    }

    private suspend fun bootstrapData() {
        try {
            repository.seedIfNeeded()
            if (repository.getTableCount() == 0) {
                repository.forceReseedDemoData()
            }
            applyOneShotState("초기 로드 완료")
        } catch (t: Throwable) {
            _uiState.update { it.copy(reseedMessage = "초기 로드 실패: ${t.message ?: "unknown"}") }
        }
    }

    private suspend fun applyOneShotState(prefix: String) {
        val areas = repository.getAreasOnce()
        val selectedArea = repository.findFirstAreaIdWithTables()
        val tables = selectedArea?.let { repository.getTablesOnce(it) }.orEmpty()
        val selectedTable = tables.firstOrNull()?.tableId
        val areaCount = repository.getAreaCount()
        val tableCount = repository.getTableCount()
        _uiState.update {
            it.copy(
                areas = areas,
                selectedAreaId = selectedArea,
                tables = tables,
                selectedTableId = selectedTable,
                reseedMessage = "${prefix}: 구역 ${areaCount}개 / 테이블 ${tableCount}개"
            )
        }
        selectedTable?.let(::observeRightPanel)
    }

    private fun observeTables(areaId: Long) {
        tableObserverJob?.cancel()
        tableObserverJob = viewModelScope.launch {
            repository.observeTables(areaId).collectLatest { tables ->
                val selected = _uiState.value.selectedTableId?.takeIf { id -> tables.any { it.tableId == id } }
                    ?: tables.firstOrNull()?.tableId
                _uiState.update {
                    it.copy(
                        tables = tables,
                        selectedTableId = selected,
                        selectedSourceTableId = it.selectedSourceTableId?.takeIf { id -> tables.any { t -> t.tableId == id } },
                        selectedTargetTableId = it.selectedTargetTableId?.takeIf { id -> tables.any { t -> t.tableId == id } }
                    )
                }
                selected?.let(::observeRightPanel)
            }
        }
    }

    private fun observeRightPanel(tableId: Long) {
        rightPanelObserverJob?.cancel()
        rightPanelObserverJob = viewModelScope.launch {
            repository.observeActiveOrderDetails(tableId).collectLatest { activeOrder ->
                _uiState.update { state ->
                    state.copy(
                        rightPanel = activeOrder?.toRightPanelUi()
                    )
                }
            }
        }
    }
}


private fun formatAmount(value: Int): String = String.format(Locale.KOREA, "%,d", value)

private fun formatElapsed(createdAt: Long): String {
    val elapsedMillis = (System.currentTimeMillis() - createdAt).coerceAtLeast(0)
    return "${TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)}분"
}

private fun ActiveOrderDetails.toRightPanelUi(): RightOrderPanelUi {
    return RightOrderPanelUi(
        orderId = orderId,
        orderStatus = status,
        elapsedLabel = formatElapsed(createdAt),
        items = items.map { line ->
            RightPanelItemUi(
                orderItemId = line.orderItemId,
                itemName = line.itemName,
                priceSnapshot = line.priceSnapshot,
                qty = line.qty,
                lineTotal = line.lineTotal
            )
        },
        orderTotalAmount = orderTotalAmount,
        derivedTotalAmount = derivedTotalAmount,
        isTotalMismatch = orderTotalAmount != derivedTotalAmount
    )
}

@Composable
fun MainNavHost(vm: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ROUTE_SUPER_HOME) {
        composable(ROUTE_SUPER_HOME) {
            val superHomeVm: SuperHomeViewModel = viewModel()
            SuperHomeScreen(
                superHomeVm = superHomeVm,
                onNavigateToProductRegister = { barcode ->
                    navController.navigate(productRegisterRoute(barcode))
                },
                onNavigateToRestaurant = {
                    navController.navigate(ROUTE_RESTAURANT)
                }
            )
        }
        composable(ROUTE_RESTAURANT) { RestaurantScreen(navController, vm) }
        composable("food/{tableId}") { backStackEntry ->
            val tableId = backStackEntry.arguments?.getString("tableId")?.toLongOrNull()
            FoodCourtScreen(navController = navController, vm = vm, tableId = tableId)
        }
        composable(ROUTE_PRODUCT_REGISTER_WITH_ARG) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString(ARG_BARCODE)
            ProductRegisterScreen(
                barcode = barcode,
                onNavigateHome = {
                    navController.navigate(ROUTE_SUPER_HOME) {
                        popUpTo(ROUTE_SUPER_HOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("payment/{tableId}") { backStackEntry ->
            val tableId = backStackEntry.arguments?.getString("tableId")?.toLongOrNull()
            val snapshot = remember(tableId, vm.uiState.collectAsState().value.rightPanel) { vm.buildPaymentSnapshot(tableId) }
            val paymentVm: PaymentViewModel = viewModel(
                key = "payment_${tableId ?: -1}",
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T = PaymentViewModel(snapshot) as T
                }
            )
            PaymentScreen(navController = navController, paymentVm = paymentVm)
        }
    }
}

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
fun RestaurantScreen(navController: NavHostController, vm: MainViewModel) {
    val uiState by vm.uiState.collectAsState()
    val selectedTable = uiState.tables.firstOrNull { it.tableId == uiState.selectedTableId }
    val snackbarHostState = remember { SnackbarHostState() }
    val tableBounds = remember { mutableStateMapOf<Long, Rect>() }
    var draggingTableId by remember { mutableStateOf<Long?>(null) }
    var draggingOffset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(uiState.snackbarMessage) {
        val msg = uiState.snackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        vm.consumeSnackbarMessage()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PosTopBar() },
        containerColor = Color(0xFFF6F2E9),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Pane: Table Grid
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                if (uiState.areas.isNotEmpty()) {
                    ScrollableTabRow(
                        selectedTabIndex = uiState.areas.indexOfFirst { it.id == uiState.selectedAreaId }.coerceAtLeast(0),
                        containerColor = Color.Transparent,
                        edgePadding = 0.dp,
                        indicator = { tabPositions ->
                            if (uiState.areas.indexOfFirst { it.id == uiState.selectedAreaId } >= 0) {
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[uiState.areas.indexOfFirst { it.id == uiState.selectedAreaId }.coerceAtLeast(0)]),
                                    color = Color(0xFF005645)
                                )
                            }
                        },
                        divider = {}
                    ) {
                        uiState.areas.forEach { area ->
                            val isSelected = area.id == uiState.selectedAreaId
                            Tab(
                                selected = isSelected,
                                onClick = { vm.selectArea(area.id) },
                                text = {
                                    Text(
                                        area.name,
                                        style = if (isSelected) MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp) else MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color(0xFF005645) else Color(0xFF757575)
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                if (uiState.uiMode != UiMode.NORMAL) {
                    val modeLabel = if (uiState.uiMode == UiMode.SELECT_TARGET_FOR_MOVE) "이동 대상 테이블 선택" else "합석 대상 테이블 선택"
                    Surface(
                        color = Color(0xFF005645),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = modeLabel,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (uiState.tables.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("테이블 데이터가 없습니다", color = Color.Gray)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            gridItems(uiState.tables) { table ->
                                val selected = table.tableId == selectedTable?.tableId
                                val isTargetMode = uiState.uiMode != UiMode.NORMAL
                                val isTargetCandidate = isTargetMode &&
                                    table.tableId != uiState.selectedSourceTableId &&
                                    table.status != "DISABLED"
                                val isSelectedTarget = table.tableId == uiState.selectedTargetTableId
                                val isSelectedSource = table.tableId == uiState.selectedTableId
                                val isDragActiveSource = draggingTableId == table.tableId
                                
                                val containerColor = when {
                                    table.status == "DISABLED" -> Color(0xFFE0E0E0)
                                    table.status == "MERGED" -> Color(0xFFC7A97E)
                                    selected -> Color(0xFF005645)
                                    else -> Color.White
                                }
                                val contentColor = if (selected || table.status == "MERGED") Color.White else Color(0xFF202124)
                                
                                Card(
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = containerColor,
                                        contentColor = contentColor
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(154.dp)
                                        .onGloballyPositioned { coordinates ->
                                            tableBounds[table.tableId] = coordinates.boundsInWindow()
                                        }
                                        .offset {
                                            if (draggingTableId == table.tableId) {
                                                IntOffset(draggingOffset.x.roundToInt(), draggingOffset.y.roundToInt())
                                            } else {
                                                IntOffset.Zero
                                            }
                                        }
                                        .zIndex(if (draggingTableId == table.tableId) 10f else 0f)
                                        .border(
                                            width = when {
                                                isDragActiveSource -> 4.dp
                                                selected || isSelectedTarget -> 2.dp
                                                else -> 1.dp
                                            },
                                            color = when {
                                                isDragActiveSource -> Color(0xFFFFB300)
                                                isSelectedTarget -> Color(0xFF1E88E5)
                                                isSelectedSource -> Color(0xFF005645)
                                                isTargetCandidate -> Color(0xFF8BC34A)
                                                else -> Color(0xFFDADCE0)
                                            },
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .pointerInput(table.tableId) {
                                            detectDragGesturesAfterLongPress(
                                                onDragStart = {
                                                    draggingTableId = table.tableId
                                                    draggingOffset = Offset.Zero
                                                },
                                                onDrag = { _, dragAmount ->
                                                    if (draggingTableId == table.tableId) {
                                                        draggingOffset += dragAmount
                                                    }
                                                },
                                                onDragEnd = {
                                                    val sourceId = draggingTableId
                                                    val sourceBounds = sourceId?.let { tableBounds[it] }
                                                    val dropPoint = sourceBounds?.center?.plus(draggingOffset)
                                                    if (sourceId != null && dropPoint != null) {
                                                        val targetId = tableBounds.entries
                                                            .firstOrNull { (id, rect) -> id != sourceId && rect.contains(dropPoint) }
                                                            ?.key
                                                        if (targetId != null) {
                                                            vm.onTableDropped(sourceId, targetId)
                                                        }
                                                    }
                                                    draggingTableId = null
                                                    draggingOffset = Offset.Zero
                                                },
                                                onDragCancel = {
                                                    draggingTableId = null
                                                    draggingOffset = Offset.Zero
                                                }
                                            )
                                        }
                                        .clickable {
                                            vm.onTableTileClicked(table.tableId, table.status)
                                            if (uiState.uiMode == UiMode.NORMAL && table.status == "EMPTY") {
                                                navController.navigate("food/${table.tableId}")
                                            }
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        verticalArrangement = Arrangement.SpaceBetween,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            table.tableName, 
                                            fontWeight = FontWeight.ExtraBold, 
                                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 26.sp)
                                        )
                                        
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(
                                                "${formatAmount(table.totalAmount)}원", 
                                                fontWeight = FontWeight.Bold, 
                                                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 19.sp),
                                                color = if (selected || table.status == "MERGED") Color.White else Color(0xFF005645)
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Filled.AccessTime, contentDescription = null, modifier = Modifier.size(16.dp), tint = contentColor.copy(alpha = 0.7f))
                                                Text(formatElapsed(table.createdAt), style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), color = contentColor.copy(alpha = 0.7f))
                                                Spacer(Modifier.width(6.dp))
                                                Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = contentColor.copy(alpha = 0.7f))
                                                Text("${table.capacity}명", style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), color = contentColor.copy(alpha = 0.7f))
                                            }
                                        }

                                        Surface(
                                            color = if (selected || table.status == "MERGED") Color.White.copy(alpha = 0.2f) else Color(0xFFF1F3F4),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                formatTableStatus(table.status) + (if (table.status == "MERGED") " (합석)" else ""),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp),
                                                fontWeight = FontWeight.Bold,
                                                color = contentColor
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Right Pane: Order Summary
            Surface(
                modifier = Modifier
                    .width(360.dp)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (selectedTable == null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("테이블을 선택하세요", color = Color.Gray)
                        }
                    } else {
                        // Table Info Header
                        Surface(
                            color = Color(0xFF005645),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val chipColors = statusChipColors(selectedTable.status)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        selectedTable.tableName, 
                                        color = Color.White, 
                                        fontWeight = FontWeight.ExtraBold, 
                                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 29.sp)
                                    )
                                    Surface(
                                        color = chipColors.first,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = formatTableStatus(selectedTable.status),
                                            color = chipColors.second,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 13.sp),
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Filled.AccessTime, contentDescription = null, modifier = Modifier.size(19.dp), tint = Color.White.copy(alpha = 0.8f))
                                        Text(uiState.rightPanel?.elapsedLabel ?: "0분", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp))
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(19.dp), tint = Color.White.copy(alpha = 0.8f))
                                        Text("${selectedTable.capacity}명", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp))
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        val panel = uiState.rightPanel
                        val visiblePanelItems = panel?.items?.filter { it.priceSnapshot > 0 }.orEmpty()
                        
                        if (panel == null || visiblePanelItems.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("활성 주문이 없습니다", color = Color.Gray)
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), 
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("상품명", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), modifier = Modifier.weight(0.50f))
                                Text("수량", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), textAlign = TextAlign.Center, modifier = Modifier.weight(0.15f))
                                Text("금액", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 14.sp), textAlign = TextAlign.End, modifier = Modifier.weight(0.35f))
                            }
                            HorizontalDivider(color = Color(0xFFEEEEEE))
                            
                            val listState = rememberLazyListState()
                            val showScrollDownHint by remember(visiblePanelItems, listState) {
                                derivedStateOf {
                                    val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
                                    lastVisible < visiblePanelItems.lastIndex
                                }
                            }
                            val showScrollUpHint by remember(listState) {
                                derivedStateOf {
                                    listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
                                }
                            }
                            
                            Box(modifier = Modifier.weight(1f)) {
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(visiblePanelItems) { item ->
                                        Column {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 15.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(item.itemName, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp), fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.50f))
                                                Text("${item.qty}", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp), textAlign = TextAlign.Center, modifier = Modifier.weight(0.15f))
                                                Text("${formatAmount(item.lineTotal)}원", style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp), fontWeight = FontWeight.Bold, color = Color(0xFF005645), textAlign = TextAlign.End, modifier = Modifier.weight(0.35f))
                                            }
                                            HorizontalDivider(color = Color(0xFFF5F5F5))
                                        }
                                    }
                                }
                                
                                if (showScrollUpHint) {
                                    BouncingArrow(isUp = true, modifier = Modifier.align(Alignment.TopCenter))
                                }
                                if (showScrollDownHint) {
                                    BouncingArrow(isUp = false, modifier = Modifier.align(Alignment.BottomCenter))
                                }
                            }

                            Spacer(Modifier.height(16.dp))
                            Surface(
                                color = Color(0xFFF8F9FA),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("주문 합계", color = Color(0xFF202124), style = MaterialTheme.typography.titleMedium.copy(fontSize = 19.sp), fontWeight = FontWeight.Bold)
                                    Text("${formatAmount(panel.orderTotalAmount)}원", color = Color(0xFFD32F2F), style = MaterialTheme.typography.headlineSmall.copy(fontSize = 28.sp), fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("food/${selectedTable.tableId}") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(68.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF607D8B),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Filled.AddShoppingCart, contentDescription = null, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("추가 주문", style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp), fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { navController.navigate("payment/${selectedTable.tableId}") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(68.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF005645),
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(Icons.Filled.Payment, contentDescription = null, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("결제하기", style = MaterialTheme.typography.titleSmall.copy(fontSize = 17.sp), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    val pending = uiState.pendingAction
    if (pending != null) {
        val sourceName = uiState.tables.firstOrNull { it.tableId == pending.sourceTableId }?.tableName ?: "T-${pending.sourceTableId}"
        val targetName = uiState.tables.firstOrNull { it.tableId == pending.targetTableId }?.tableName ?: "T-${pending.targetTableId}"
        val message = if (pending.type == TableActionType.MOVE) {
            "${sourceName}의 주문을 ${targetName}로 이동할까요?"
        } else {
            "${sourceName}과 ${targetName}를 합석할까요?"
        }
        AlertDialog(
            onDismissRequest = { vm.dismissPendingAction() },
            title = { Text(if (pending.type == TableActionType.MOVE) "이동 확인" else "합석 확인") },
            text = { Text(message) },
            confirmButton = {
                Button(onClick = { vm.confirmPendingAction() }) { Text("확인") }
            },
            dismissButton = {
                OutlinedButton(onClick = { vm.dismissPendingAction() }) { Text("취소") }
            }
        )
    }
}

@Composable
private fun BouncingArrow(isUp: Boolean, modifier: Modifier = Modifier) {
    val bounceTransition = rememberInfiniteTransition(label = "scrollHint")
    val bounceOffset by bounceTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isUp) -6f else 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scrollHintOffset"
    )
    Icon(
        imageVector = if (isUp) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
        contentDescription = null,
        tint = Color(0xFF005645),
        modifier = modifier
            .size(40.dp)
            .offset(y = bounceOffset.dp)
    )
}


@Composable
fun FoodCourtScreen(navController: NavHostController, vm: MainViewModel, tableId: Long?) {
    val uiState by vm.uiState.collectAsState()
    var priceEditItem by remember { mutableStateOf<RightPanelItemUi?>(null) }
    var priceInput by remember { mutableStateOf("") }
    var showCancelAllDialog by remember { mutableStateOf(false) }

    LaunchedEffect(tableId) {
        tableId?.let(vm::selectTable)
    }

    val menusByCategory = remember {
        linkedMapOf(
            "오므&커리" to listOf("오므라이스", "비프카레", "치킨카레", "새우카레"),
            "본까스" to listOf("등심 돈까스", "치즈 돈까스", "매운 돈까스", "생선까스"),
            "해천죽" to listOf("전복죽", "소고기죽", "야채죽", "해물죽"),
            "이심사철기" to listOf("철판 불고기", "철판 제육", "철판 낙지", "철판 우동"),
            "미코" to listOf("열무보리비빔밥", "사리기름고기비빔밥", "한우 안심 스테이크", "두레 갈치조림")
        )
    }
    val categories = remember(menusByCategory) { menusByCategory.keys.toList() }
    val favoriteTabTitle = "즐겨찾기"
    val favoriteAddCardLabel = "__favorite_add_card__"
    val displayCategories = remember(categories) { listOf(favoriteTabTitle) + categories }
    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var showFavoritePickerDialog by remember { mutableStateOf(false) }
    var selectedFavoriteDialogCategoryIndex by remember { mutableIntStateOf(0) }
    val favoriteMenus = remember { mutableStateListOf<String>() }
    val selectedFavoriteCandidates = remember { mutableStateListOf<String>() }
    val allMenusWithCategory = remember(menusByCategory) {
        menusByCategory.flatMap { (category, menus) -> menus.map { menu -> "$category|$menu" } }
    }

    val currentCategory = displayCategories.getOrElse(selectedCategoryIndex) { favoriteTabTitle }
    val isFavoriteTab = currentCategory == favoriteTabTitle
    val currentMenus = if (isFavoriteTab) {
        listOf(favoriteAddCardLabel) + favoriteMenus
    } else {
        menusByCategory[currentCategory].orEmpty()
    }
    val selectedTable = uiState.tables.firstOrNull { it.tableId == uiState.selectedTableId }
    val panelItems = uiState.rightPanel?.items.orEmpty()
    val totalAmount = uiState.rightPanel?.derivedTotalAmount ?: 0

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
            // Left Pane: Active Order List (Widened by 1.2x)
            Surface(
                modifier = Modifier.weight(1.2f).fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Surface(
                        color = Color(0xFF005645),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                selectedTable?.tableName ?: "테이블 선택",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 26.sp),
                                fontWeight = FontWeight.ExtraBold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Filled.AccessTime, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White.copy(alpha = 0.8f))
                                    Text(uiState.rightPanel?.elapsedLabel ?: "0분", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp))
                                }
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Filled.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White.copy(alpha = 0.8f))
                                    Text("${selectedTable?.capacity ?: 0}명", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp))
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("No.", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp), modifier = Modifier.width(28.dp))
                        Text("상품명", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp), modifier = Modifier.weight(1f))
                        Text("단가", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp), textAlign = TextAlign.End, modifier = Modifier.width(64.dp))
                        Spacer(Modifier.width(12.dp))
                        Text("수량", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp), textAlign = TextAlign.Center, modifier = Modifier.width(80.dp))
                        Text("금액", color = Color(0xFF757575), style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp), textAlign = TextAlign.End, modifier = Modifier.width(72.dp))
                        Spacer(modifier = Modifier.width(32.dp))
                    }
                    HorizontalDivider(color = Color(0xFFEEEEEE))

                    val leftListState = rememberLazyListState()
                    val showLeftScrollDownHint by remember {
                        derivedStateOf {
                            val layoutInfo = leftListState.layoutInfo
                            val visibleItemsInfo = layoutInfo.visibleItemsInfo
                            if (visibleItemsInfo.isEmpty()) {
                                false
                            } else {
                                val lastVisibleItem = visibleItemsInfo.last()
                                lastVisibleItem.index < layoutInfo.totalItemsCount - 1 ||
                                        lastVisibleItem.offset + lastVisibleItem.size > layoutInfo.viewportEndOffset
                            }
                        }
                    }
                    val showLeftScrollUpHint by remember {
                        derivedStateOf {
                            leftListState.firstVisibleItemIndex > 0 || leftListState.firstVisibleItemScrollOffset > 0
                        }
                    }

                    Box(modifier = Modifier.weight(1f)) {
                        LazyColumn(state = leftListState, modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(panelItems) { index, item ->
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val isCanceled = item.priceSnapshot == 0
                                        Text(
                                            "${index + 1}",
                                            modifier = Modifier.width(28.dp),
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                            color = Color.Gray
                                        )
                                        Text(
                                            item.itemName,
                                            modifier = Modifier.weight(1f),
                                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            fontWeight = FontWeight.SemiBold,
                                            textDecoration = if (isCanceled) TextDecoration.LineThrough else TextDecoration.None,
                                            color = if (isCanceled) Color(0xFFD32F2F) else Color(0xFF202124),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = formatAmount(item.priceSnapshot),
                                            modifier = Modifier.width(64.dp),
                                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                                            textAlign = TextAlign.End,
                                            color = Color.Gray
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Row(
                                            modifier = Modifier.width(80.dp),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            FilledTonalIconButton(
                                                onClick = { vm.decreaseOrderItemQty(item.orderItemId) },
                                                modifier = Modifier.size(28.dp),
                                                colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color(0xFFF5F5F5))
                                            ) { Icon(Icons.Filled.Remove, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                            Text("${item.qty}", modifier = Modifier.padding(horizontal = 4.dp), style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp), fontWeight = FontWeight.Bold)
                                            FilledTonalIconButton(
                                                onClick = { vm.increaseOrderItemQty(item.orderItemId) },
                                                modifier = Modifier.size(28.dp),
                                                colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = Color(0xFFF5F5F5))
                                            ) { Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                        }
                                        Text(
                                            text = formatAmount(item.lineTotal),
                                            modifier = Modifier.width(72.dp).clickable { priceEditItem = item; priceInput = item.priceSnapshot.toString() },
                                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                            textAlign = TextAlign.End,
                                            color = if (isCanceled) Color(0xFFD32F2F) else Color(0xFF005645),
                                            fontWeight = FontWeight.Bold
                                        )
                                        IconButton(onClick = { vm.toggleOrderItemCanceled(item.orderItemId) }, modifier = Modifier.size(32.dp)) {
                                            Icon(if (isCanceled) Icons.Filled.Undo else Icons.Filled.Close, contentDescription = null, tint = Color(0xFFDADCE0), modifier = Modifier.size(18.dp))
                                        }
                                    }
                                    HorizontalDivider(color = Color(0xFFF5F5F5))
                                }
                            }
                        }
                        if (showLeftScrollUpHint) BouncingArrow(isUp = true, modifier = Modifier.align(Alignment.TopCenter).padding(top = 4.dp))
                        if (showLeftScrollDownHint) BouncingArrow(isUp = false, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp))
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { /* 행사할인 로직 */ },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF607D8B))
                        ) { Text("행사할인", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                        OutlinedButton(
                            onClick = { showCancelAllDialog = true },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                        ) { Text("전체취소", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
                    }

                    Spacer(Modifier.height(16.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF003D32), // Deep Premium Hyundai Green
                        modifier = Modifier.fillMaxWidth(),
                        shadowElevation = 4.dp
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("총 매출", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp))
                                Text("${formatAmount(totalAmount)}원", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp))
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("할인 금액", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp))
                                Text("-0원", color = Color(0xFFA5D6A7), style = MaterialTheme.typography.bodyLarge.copy(fontSize = 17.sp), fontWeight = FontWeight.Medium)
                            }
                            HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                                Text("받을 금액", color = Color.White, style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp), fontWeight = FontWeight.ExtraBold)
                                Text(
                                    "${formatAmount(totalAmount)}원",
                                    color = Color(0xFFFFD54F), // Elegant Gold Accent
                                    style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp),
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // Right Pane: Menu Selection (Adjusted to maintain 4x4 grid)
            Surface(
                modifier = Modifier.weight(1.6f).fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedCategoryIndex,
                        containerColor = Color.Transparent,
                        edgePadding = 0.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedCategoryIndex]),
                                color = Color(0xFF005645)
                            )
                        },
                        divider = {}
                    ) {
                        displayCategories.forEachIndexed { index, category ->
                            Tab(
                                selected = index == selectedCategoryIndex,
                                onClick = { selectedCategoryIndex = index },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        if (index == 0) Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFF2C94C), modifier = Modifier.size(18.dp))
                                        Text(
                                            category,
                                            style = if (index == selectedCategoryIndex) MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp) else MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                                            fontWeight = if (index == selectedCategoryIndex) FontWeight.Bold else FontWeight.Normal,
                                            color = if (index == selectedCategoryIndex) Color(0xFF005645) else Color(0xFF757575)
                                        )
                                    }
                                }
                            )
                        }
                    }

                    val gridState = rememberLazyGridState()
                    val showGridScrollDownHint by remember {
                        derivedStateOf {
                            val layoutInfo = gridState.layoutInfo
                            val visibleItemsInfo = layoutInfo.visibleItemsInfo
                            if (visibleItemsInfo.isEmpty()) {
                                false
                            } else {
                                val lastVisibleItem = visibleItemsInfo.last()
                                lastVisibleItem.index < layoutInfo.totalItemsCount - 1 ||
                                        lastVisibleItem.offset.y + lastVisibleItem.size.height > layoutInfo.viewportEndOffset
                            }
                        }
                    }
                    val showGridScrollUpHint by remember {
                        derivedStateOf {
                            gridState.firstVisibleItemIndex > 0 || gridState.firstVisibleItemScrollOffset > 0
                        }
                    }

                    Box(modifier = Modifier.weight(1f).padding(top = 12.dp)) {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(4),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            gridItems(currentMenus) { menuName ->
                                val isFavoriteAddCard = isFavoriteTab && menuName == favoriteAddCardLabel
                                Card(
                                    modifier = Modifier.fillMaxWidth().height(100.dp).clickable {
                                        if (isFavoriteAddCard) {
                                            selectedFavoriteCandidates.clear()
                                            favoriteMenus.forEach { fav -> allMenusWithCategory.firstOrNull { it.endsWith("|$fav") }?.let { selectedFavoriteCandidates.add(it) } }
                                            selectedFavoriteDialogCategoryIndex = 0
                                            showFavoritePickerDialog = true
                                        } else {
                                            vm.addMenuToSelectedTable(menuName = menuName, price = 8000)
                                        }
                                    },
                                    colors = CardDefaults.cardColors(containerColor = if (isFavoriteAddCard) Color(0xFFF1F3F4) else Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize().padding(8.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (isFavoriteAddCard) {
                                            Icon(Icons.Filled.Add, contentDescription = null, tint = Color(0xFF005645), modifier = Modifier.size(32.dp))
                                            Text("추가", style = MaterialTheme.typography.labelMedium, color = Color(0xFF005645))
                                        } else {
                                            Text(menuName, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
                                            Spacer(Modifier.height(6.dp))
                                            Text("${formatAmount(8000)}원", color = Color(0xFF005645), fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp))
                                        }
                                    }
                                }
                            }
                        }
                        if (showGridScrollUpHint) BouncingArrow(isUp = true, modifier = Modifier.align(Alignment.TopCenter).padding(top = 4.dp))
                        if (showGridScrollDownHint) BouncingArrow(isUp = false, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 4.dp))
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // 1, 1, 2x2 Layout: Left two are single large buttons, right is a 2x2 grid
                        FoodCourtActionButton("테이블 화면 이동", Icons.Filled.TableRestaurant, Color(0xFFD8CCD2), Modifier.weight(1f).height(154.dp)) {
                            navController.popBackStack()
                        }
                        FoodCourtActionButton("기타시재", Icons.Filled.PointOfSale, Color(0xFF5A6B7A), Modifier.weight(1f).height(154.dp)) { }
                        
                        Column(modifier = Modifier.weight(2.2f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                FoodCourtActionButton("상품권", Icons.Filled.CardGiftcard, Color(0xFFC1A57A), Modifier.weight(1f)) { }
                                FoodCourtActionButton("현금", Icons.Filled.Payments, Color(0xFF005645), Modifier.weight(1.2f)) {
                                    navController.navigate("payment/${selectedTable?.tableId ?: (tableId ?: -1)}")
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                                FoodCourtActionButton("H.Point 사용", Icons.Filled.Stars, Color(0xFFC1A57A), Modifier.weight(1f)) { }
                                FoodCourtActionButton("카드/모바일", Icons.Filled.CreditCard, Color(0xFF005645), Modifier.weight(1.2f)) {
                                    navController.navigate("payment/${selectedTable?.tableId ?: (tableId ?: -1)}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showFavoritePickerDialog) {
        val dialogCategory = categories.getOrElse(selectedFavoriteDialogCategoryIndex) { categories.firstOrNull().orEmpty() }
        val dialogMenus = menusByCategory[dialogCategory].orEmpty()
        AlertDialog(
            onDismissRequest = { showFavoritePickerDialog = false },
            title = { Text("즐겨찾기 상품 선택", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ScrollableTabRow(selectedTabIndex = selectedFavoriteDialogCategoryIndex, containerColor = Color.Transparent) {
                        categories.forEachIndexed { index, category ->
                            Tab(selected = index == selectedFavoriteDialogCategoryIndex, onClick = { selectedFavoriteDialogCategoryIndex = index }, text = { Text(category) })
                        }
                    }
                    LazyColumn(modifier = Modifier.height(300.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        items(dialogMenus) { menu ->
                            val entry = "$dialogCategory|$menu"
                            val checked = entry in selectedFavoriteCandidates
                            Row(
                                modifier = Modifier.fillMaxWidth().clickable { if (checked) selectedFavoriteCandidates.remove(entry) else selectedFavoriteCandidates.add(entry) }.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(checked = checked, onCheckedChange = { if (it) selectedFavoriteCandidates.add(entry) else selectedFavoriteCandidates.remove(entry) })
                                Text(menu, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            },
            confirmButton = { Button(onClick = { favoriteMenus.clear(); favoriteMenus.addAll(selectedFavoriteCandidates.map { it.substringAfter("|") }.distinct()); showFavoritePickerDialog = false; selectedCategoryIndex = 0 }) { Text("적용하기") } },
            dismissButton = { OutlinedButton(onClick = { showFavoritePickerDialog = false }) { Text("취소") } }
        )
    }

    priceEditItem?.let { target ->
        AlertDialog(
            onDismissRequest = { priceEditItem = null },
            title = { Text("단가 변경", fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(target.itemName, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = priceInput, onValueChange = { priceInput = it.filter { c -> c.isDigit() } }, label = { Text("변경할 단가") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                }
            },
            confirmButton = { Button(onClick = { priceInput.toIntOrNull()?.let { vm.changeOrderItemUnitPrice(target.orderItemId, it) }; priceEditItem = null }) { Text("확인") } },
            dismissButton = { OutlinedButton(onClick = { priceEditItem = null }) { Text("취소") } }
        )
    }

    if (showCancelAllDialog) {
        AlertDialog(
            onDismissRequest = { showCancelAllDialog = false },
            title = { Text("전체취소 확인", fontWeight = FontWeight.Bold) },
            text = { Text("현재 주문된 모든 내역을 취소하시겠습니까?") },
            confirmButton = { Button(onClick = { vm.cancelAllCurrentOrderItems(); showCancelAllDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))) { Text("전체취소 실행") } },
            dismissButton = { OutlinedButton(onClick = { showCancelAllDialog = false }) { Text("돌아가기") } }
        )
    }
}

@Composable
private fun FoodCourtActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = if (color == Color(0xFFD8CCD2)) Color(0xFF3C4043) else Color.White),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(4.dp))
            Text(label, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center, fontSize = 15.sp)
        }
    }
}

@Composable
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

private fun statusChipColors(status: String): Pair<Color, Color> = when (status) {
    "OCCUPIED" -> Color(0xFFE2F3EC) to Color(0xFF005645)
    "EMPTY" -> Color(0xFFF0F0F0) to Color(0xFF5E5E5E)
    "BILLING" -> Color(0xFFFFEED1) to Color(0xFF9A6300)
    "DISABLED" -> Color(0xFFE6E6E6) to Color(0xFF8A8A8A)
    "MERGED" -> Color(0xFFEFE3D0) to Color(0xFF6B4B2A)
    else -> Color(0xFFE2F3EC) to Color(0xFF005645)
}

private fun formatTableStatus(status: String): String = when (status) {
    "OCCUPIED" -> "식사중"
    "EMPTY" -> "빈자리"
    "BILLING" -> "결제대기"
    "DISABLED" -> "사용불가"
    "MERGED" -> "합석됨"
    else -> status
}

private fun formatElapsed(createdAt: Long?): String {
    if (createdAt == null) return "0분"
    val elapsedMillis = (System.currentTimeMillis() - createdAt).coerceAtLeast(0)
    return "${TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)}분"
}
