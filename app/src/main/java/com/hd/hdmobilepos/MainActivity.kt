package com.hd.hdmobilepos


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.hd.hdmobilepos.data.ActiveOrderDetails
import com.hd.hdmobilepos.data.Area
import com.hd.hdmobilepos.data.AppDatabase
import com.hd.hdmobilepos.data.PosRepository
import com.hd.hdmobilepos.data.TableSummary
import com.hd.hdmobilepos.navigation.MainNavHost
import com.hd.hdmobilepos.ui.theme.PPOSTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
                val restaurantVm: RestaurantViewModel = viewModel(
                    key = "restaurant_vm",
                    factory = remember {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T = RestaurantViewModel(repo) as T
                        }
                    }
                )
                val foodCourtVm: FoodCourtViewModel = viewModel(
                    key = "food_court_vm",
                    factory = remember {
                        object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T = FoodCourtViewModel(repo) as T
                        }
                    }
                )
                MainNavHost(restaurantVm = restaurantVm, foodCourtVm = foodCourtVm)
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

class RestaurantViewModel(private val repository: PosRepository) : ViewModel() {
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

data class FoodCourtUiState(
    val selectedTableId: Long? = null,
    val selectedTable: TableSummary? = null,
    val rightPanel: RightOrderPanelUi? = null,
    val snackbarMessage: String? = null
)

class FoodCourtViewModel(private val repository: PosRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(FoodCourtUiState())
    val uiState: StateFlow<FoodCourtUiState> = _uiState.asStateFlow()

    private var selectedTableObserverJob: Job? = null
    private var rightPanelObserverJob: Job? = null
    private val canceledPriceMemory = mutableMapOf<Long, Int>()

    fun selectTable(tableId: Long) {
        if (_uiState.value.selectedTableId == tableId) return
        _uiState.update { it.copy(selectedTableId = tableId) }
        observeSelectedTable(tableId)
        observeRightPanel(tableId)
    }

    fun buildPaymentSnapshot(tableId: Long?): PaymentOrderSnapshot {
        val state = _uiState.value
        val resolvedTableId = tableId ?: state.selectedTableId
        val items = state.rightPanel?.items?.map { PaymentOrderItemUi(name = it.itemName, qty = it.qty, price = it.lineTotal) }.orEmpty()
        val total = state.rightPanel?.derivedTotalAmount ?: 0
        return PaymentOrderSnapshot(
            tableId = resolvedTableId,
            tableName = state.selectedTable?.tableName ?: "선택된 테이블 없음",
            items = items,
            totalAmount = total,
            receivedAmount = total
        )
    }

    fun consumeSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }


    private fun observeSelectedTable(tableId: Long) {
        selectedTableObserverJob?.cancel()
        selectedTableObserverJob = viewModelScope.launch {
            repository.observeTableSummaryById(tableId).collectLatest { table ->
                _uiState.update { it.copy(selectedTable = table) }
            }
        }
    }

    private fun observeRightPanel(tableId: Long) {
        rightPanelObserverJob?.cancel()
        rightPanelObserverJob = viewModelScope.launch {
            repository.observeActiveOrderDetails(tableId).collectLatest { activeOrder ->
                _uiState.update { it.copy(rightPanel = activeOrder?.toRightPanelUi()) }
            }
        }
    }


    private fun observeSelectedTable(tableId: Long) {
        selectedTableObserverJob?.cancel()
        selectedTableObserverJob = viewModelScope.launch {
            repository.observeTableSummaryById(tableId).collectLatest { table ->
                _uiState.update { it.copy(selectedTable = table) }
            }
        }
    }

    private fun observeRightPanel(tableId: Long) {
        rightPanelObserverJob?.cancel()
        rightPanelObserverJob = viewModelScope.launch {
            repository.observeActiveOrderDetails(tableId).collectLatest { activeOrder ->
                _uiState.update { it.copy(rightPanel = activeOrder?.toRightPanelUi()) }
            }
        }
    }


    private fun observeSelectedTable(tableId: Long) {
        selectedTableObserverJob?.cancel()
        selectedTableObserverJob = viewModelScope.launch {
            repository.observeTableSummaryById(tableId).collectLatest { table ->
                _uiState.update { it.copy(selectedTable = table) }
            }
        }
    }

    private fun observeRightPanel(tableId: Long) {
        rightPanelObserverJob?.cancel()
        rightPanelObserverJob = viewModelScope.launch {
            repository.observeActiveOrderDetails(tableId).collectLatest { activeOrder ->
                _uiState.update { it.copy(rightPanel = activeOrder?.toRightPanelUi()) }
            }
        }
    }

    private fun pushSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }
}

data class FoodCourtUiState(
    val selectedTableId: Long? = null,
    val selectedTable: TableSummary? = null,
    val rightPanel: RightOrderPanelUi? = null,
    val snackbarMessage: String? = null
)

class FoodCourtViewModel(private val repository: PosRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(FoodCourtUiState())
    val uiState: StateFlow<FoodCourtUiState> = _uiState.asStateFlow()

    private var selectedTableObserverJob: Job? = null
    private var rightPanelObserverJob: Job? = null
    private val canceledPriceMemory = mutableMapOf<Long, Int>()

    fun selectTable(tableId: Long) {
        if (_uiState.value.selectedTableId == tableId) return
        _uiState.update { it.copy(selectedTableId = tableId) }
        observeSelectedTable(tableId)
        observeRightPanel(tableId)
    }

    fun buildPaymentSnapshot(tableId: Long?): PaymentOrderSnapshot {
        val state = _uiState.value
        val resolvedTableId = tableId ?: state.selectedTableId
        val items = state.rightPanel?.items?.map { PaymentOrderItemUi(name = it.itemName, qty = it.qty, price = it.lineTotal) }.orEmpty()
        val total = state.rightPanel?.derivedTotalAmount ?: 0
        return PaymentOrderSnapshot(
            tableId = resolvedTableId,
            tableName = state.selectedTable?.tableName ?: "선택된 테이블 없음",
            items = items,
            totalAmount = total,
            receivedAmount = total
        )
    }

    fun consumeSnackbarMessage() {
        _uiState.update { it.copy(snackbarMessage = null) }
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
        viewModelScope.launch {
            repository.changeOrderItemQty(orderId = panel.orderId, orderItemId = orderItemId, delta = -1)
        }
    }

    fun changeOrderItemUnitPrice(orderItemId: Long, newPrice: Int) {
        if (newPrice <= 0) {
            pushSnackbar("금액은 1원 이상이어야 합니다")
            return
        }
        val orderId = _uiState.value.rightPanel?.orderId ?: return
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

    private fun observeSelectedTable(tableId: Long) {
        selectedTableObserverJob?.cancel()
        selectedTableObserverJob = viewModelScope.launch {
            repository.observeTableSummaryById(tableId).collectLatest { table ->
                _uiState.update { it.copy(selectedTable = table) }
            }
        }
    }

    private fun observeRightPanel(tableId: Long) {
        rightPanelObserverJob?.cancel()
        rightPanelObserverJob = viewModelScope.launch {
            repository.observeActiveOrderDetails(tableId).collectLatest { activeOrder ->
                _uiState.update { it.copy(rightPanel = activeOrder?.toRightPanelUi()) }
            }
        }
    }

    private fun pushSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }
}


internal fun formatAmount(value: Int): String = String.format(Locale.KOREA, "%,d", value)

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
