package com.hd.hdmobilepos.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd.hdmobilepos.SuperHomeViewModel
import com.hd.hdmobilepos.LastTransactionSummaryUi
import com.hd.hdmobilepos.formatAmount
import com.hd.hdmobilepos.ui.component.PosTopBar

// --- 현대백화점 프리미엄 컬러 팔레트 ---
private val PosGreen = Color(0xFF005645)
private val PosBeige = Color(0xFFC1A57A)
private val PosLilac = Color(0xFFD8CCD2)
private val PosSlate = Color(0xFF5A6B7A)
private val PosBg = Color(0xFFF8F9FA)
private val PosSurface = Color.White
private val PosBorder = Color(0xFFE0E0E0)
private val PosGold = Color(0xFFFFD54F) 

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperHomeScreen(
    superHomeVm: SuperHomeViewModel,
    onNavigateToProductRegister: (String?) -> Unit,
    onNavigateToRestaurant: () -> Unit
) {
    val uiState by superHomeVm.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val gridState = rememberLazyGridState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { PosTopBar() },
        containerColor = PosBg
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- Left Content (65%) ---
            Column(
                modifier = Modifier.weight(0.65f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ModernSearchBar(
                    value = uiState.barcodeInput,
                    onValueChange = superHomeVm::onBarcodeInputChanged,
                    onSearch = {
                        val barcode = superHomeVm.consumeBarcodeForNavigation() ?: return@ModernSearchBar
                        onNavigateToProductRegister(barcode)
                        keyboardController?.hide()
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ModernHeroTile("상품판매", "Sales", Icons.Filled.AddShoppingCart, PosGreen, Modifier.weight(1.2f), { onNavigateToProductRegister(null) })
                    ModernHeroTile("식당관리", "Tables", Icons.Filled.TableRestaurant, PosBeige, Modifier.weight(1f), onNavigateToRestaurant)
                    ModernHeroTile("통합조회", "Search", Icons.Filled.Analytics, PosSlate, Modifier.weight(1f), {})
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    color = PosSurface,
                    border = BorderStroke(1.dp, PosBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = PosGold, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("빠른 실행 메뉴", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PosSlate)
                        }
                        
                        Box(modifier = Modifier.weight(1f).padding(top = 12.dp)) {
                            LazyVerticalGrid(
                                state = gridState,
                                columns = GridCells.Fixed(4),
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val menuItems = listOf(
                                    "상품재등록" to Icons.Filled.History,
                                    "거스름돈" to Icons.Filled.PriceChange,
                                    "식수" to Icons.Filled.Restaurant,
                                    "사은행사참여" to Icons.Filled.Redeem,
                                    "H.Point조회" to Icons.Filled.Loyalty,
                                    "매출현황" to Icons.Filled.BarChart,
                                    "주차정산" to Icons.Filled.LocalParking,
                                    "영수증조회" to Icons.Filled.ReceiptLong,
                                    "환경설정" to Icons.Filled.Settings,
                                    "시스템 정보" to Icons.Filled.Info
                                )
                                items(menuItems) { (label, icon) ->
                                    ModernGridItem(label = label, icon = icon, onClick = {})
                                }
                            }
                            if (gridState.canScrollForward) {
                                BouncingScrollHint(Modifier.align(Alignment.BottomCenter))
                            }
                        }
                    }
                }

                // 4. 공지 및 행사 (가로 배치, 폰트 크기 14sp 동기화)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        title = "공지사항", 
                        content = "현대백화점 멤버십 등급 산정 기준 변경", 
                        icon = Icons.AutoMirrored.Filled.VolumeUp,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        title = "행사안내", 
                        content = "H.Point 앱 신규 가입 시 5,000P 증정", 
                        icon = Icons.Filled.LocalOffer,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // --- Right Content (35%) ---
            Column(
                modifier = Modifier.weight(0.35f),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ModernReceiptCard(uiState.lastTransaction)

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickSideButton("직전 영수증 출력", Icons.Filled.Print, PosSlate)
                    QuickSideButton("직전 거래 환불", Icons.Filled.Replay, Color(0xFFD32F2F))
                    QuickSideButton("보류 내역 리스트", Icons.Filled.ListAlt, PosSlate)
                    
                    Spacer(Modifier.weight(1f))
                    
                    // SIGN-OFF 버튼 (1.2배 크기 = 120dp, 딥그린 컬러)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        Surface(
                            onClick = { /* SIGN-OFF 액션 */ },
                            modifier = Modifier.size(120.dp), // 1.2배 상향
                            shape = RoundedCornerShape(20.dp),
                            color = PosGreen, // 딥그린 변경
                            shadowElevation = 6.dp
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Filled.Logout, contentDescription = null, tint = Color.White, modifier = Modifier.size(38.dp))
                                Spacer(Modifier.height(10.dp))
                                Text("SIGN-OFF", color = Color.White, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, content: String, icon: ImageVector, modifier: Modifier) {
    Surface(
        modifier = modifier.height(84.dp),
        shape = RoundedCornerShape(12.dp),
        color = PosLilac.copy(alpha = 0.15f),
        border = BorderStroke(1.dp, PosLilac.copy(alpha = 0.4f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = PosSlate, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                // 폰트 크기를 메뉴 버튼과 동일하게 14sp로 조정
                Text(title, style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp), color = PosSlate.copy(alpha = 0.7f))
                Text(content, style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp), fontWeight = FontWeight.Bold, color = PosSlate, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

@Composable
private fun BouncingScrollHint(modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scroll")
    val dy by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 8f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dy"
    )
    Icon(Icons.Filled.KeyboardArrowDown, null, tint = PosGreen, modifier = modifier.size(32.dp).offset(y = dy.dp))
}

@Composable
private fun ModernSearchBar(value: String, onValueChange: (String) -> Unit, onSearch: () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp), color = PosSurface, border = BorderStroke(2.dp, PosGreen.copy(alpha = 0.5f)), shadowElevation = 4.dp) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.QrCodeScanner, null, tint = PosGreen, modifier = Modifier.size(28.dp))
            OutlinedTextField(
                value = value, onValueChange = onValueChange, modifier = Modifier.weight(1f),
                placeholder = { Text("바코드를 스캔하거나 상품명을 입력하세요", fontSize = 16.sp) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() })
            )
            IconButton(onClick = onSearch) { Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = PosGreen) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernHeroTile(label: String, subLabel: String, icon: ImageVector, primaryColor: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = modifier.height(140.dp), shape = RoundedCornerShape(20.dp), color = primaryColor, shadowElevation = 8.dp) {
        Box(Modifier.fillMaxSize()) {
            Icon(icon, null, tint = Color.White.copy(alpha = 0.1f), modifier = Modifier.size(100.dp).align(Alignment.BottomEnd).offset(20.dp, 20.dp))
            Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Surface(shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.2f), modifier = Modifier.size(44.dp)) { Icon(icon, null, tint = Color.White, modifier = Modifier.padding(10.dp)) }
                Column {
                    Text(subLabel, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(label, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernGridItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = Modifier.height(96.dp), shape = RoundedCornerShape(14.dp), color = PosBg, border = BorderStroke(1.dp, PosBorder)) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = PosSlate, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(8.dp))
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF424242), textAlign = TextAlign.Center, lineHeight = 16.sp)
        }
    }
}

@Composable
private fun ModernReceiptCard(transaction: LastTransactionSummaryUi) {
    Surface(shape = RoundedCornerShape(16.dp), color = PosSurface, border = BorderStroke(1.dp, PosBorder), shadowElevation = 4.dp) {
        Column(Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth().background(PosSlate.copy(alpha = 0.05f)).padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.History, null, tint = PosGreen, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("직전 결제 요약", fontWeight = FontWeight.Black, color = PosSlate)
                }
            }
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ReceiptLine("상품 수량", "${transaction.itemCount} 개") // 단위 '개' 변경
                ReceiptLine("구매 금액", "${formatAmount(transaction.purchaseAmount)} 원")
                ReceiptLine("할인 금액", "- ${formatAmount(transaction.discountAmount)} 원", Color(0xFFD32F2F))
                ReceiptLine("받은 금액", "${formatAmount(transaction.receivedAmount)} 원", PosGreen)
                HorizontalDivider(Modifier.padding(vertical = 4.dp), color = PosBorder, thickness = 1.dp)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("거스름돈", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF616161))
                    Text("${formatAmount(transaction.changeAmount)} 원", fontWeight = FontWeight.Black, color = Color(0xFFD32F2F), style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
    }
}

@Composable
private fun ReceiptLine(label: String, value: String, valueColor: Color = Color(0xFF212121)) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color(0xFF757575), fontSize = 15.sp, fontWeight = FontWeight.Medium)
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = valueColor)
    }
}

@Composable
private fun QuickSideButton(label: String, icon: ImageVector, color: Color) {
    Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(64.dp), shape = RoundedCornerShape(14.dp), colors = ButtonDefaults.buttonColors(containerColor = color)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(16.dp))
            Text(label, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
        }
    }
}
