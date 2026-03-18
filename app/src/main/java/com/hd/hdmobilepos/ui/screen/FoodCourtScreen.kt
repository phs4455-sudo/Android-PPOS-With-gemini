package com.hd.hdmobilepos.ui.screen


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hd.hdmobilepos.FoodCourtViewModel
import com.hd.hdmobilepos.RightPanelItemUi
import com.hd.hdmobilepos.formatAmount
import com.hd.hdmobilepos.ui.component.PosTopBar

@Composable
fun FoodCourtScreen(navController: NavHostController, vm: FoodCourtViewModel, tableId: Long?) {
    val uiState by vm.uiState.collectAsState()
    var priceEditItem by remember { mutableStateOf<RightPanelItemUi?>(null) }
    var priceInput by remember { mutableStateOf("") }
    var showCancelAllDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(tableId) {
        tableId?.let(vm::selectTable)
    }


    LaunchedEffect(uiState.snackbarMessage) {
        val msg = uiState.snackbarMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(msg)
        vm.consumeSnackbarMessage()
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
    val selectedTable = uiState.selectedTable
    val panelItems = uiState.rightPanel?.items.orEmpty()
    val totalAmount = uiState.rightPanel?.derivedTotalAmount ?: 0

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

