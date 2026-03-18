package com.hd.hdmobilepos.ui.screen


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.hd.hdmobilepos.RestaurantViewModel
import com.hd.hdmobilepos.TableActionType
import com.hd.hdmobilepos.UiMode
import com.hd.hdmobilepos.formatAmount
import com.hd.hdmobilepos.ui.component.PosTopBar
import java.util.concurrent.TimeUnit

@Composable
fun RestaurantScreen(navController: NavHostController, vm: RestaurantViewModel) {
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
internal fun BouncingArrow(isUp: Boolean, modifier: Modifier = Modifier) {
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
