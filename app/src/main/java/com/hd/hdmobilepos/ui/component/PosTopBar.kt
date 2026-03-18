package com.hd.hdmobilepos.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PosTopBar() {
    val now = rememberCurrentDateTime()
    // 1) 년도 및 초를 포함한 포맷 정의
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy.MM.dd (E)") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm:ss") }

    Surface(
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 브랜드 로고 영역
            Text(
                "THE HYUNDAI",
                fontWeight = FontWeight.Black,
                color = Color(0xFF005645),
                style = MaterialTheme.typography.titleLarge.copy(
                    letterSpacing = 1.2.sp,
                    fontSize = 22.sp
                )
            )

            Spacer(Modifier.width(40.dp))

            // 1) 일시 정보 영역: 세로 배치(Column) 및 폰트 크기 조정
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFF005645),
                    modifier = Modifier.size(24.dp) // 세로 배치에 맞춰 아이콘 크기 소폭 상향
                )
                Spacer(Modifier.width(10.dp))
                Column(verticalArrangement = Arrangement.Center) {
                    Text(
                        text = now.format(dateFormatter),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp, 
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF5F6368)
                    )
                    Text(
                        text = now.format(timeFormatter),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 19.sp, // 시간 강조를 위해 더 크게
                            fontWeight = FontWeight.Black
                        ),
                        color = Color(0xFF202124)
                    )
                }
            }

            Spacer(Modifier.width(32.dp))

            // 시스템 정보
            InfoChip(label = "POS번호", value = "5556")
            Spacer(Modifier.width(10.dp))
            InfoChip(label = "거래번호", value = "0014")

            Spacer(Modifier.weight(1f))

            // 액션 버튼
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PosTopActionButton("점검", Icons.Filled.CheckCircle, Color(0xFF5A6B7A))
                PosTopActionButton("조회", Icons.Filled.Search, Color(0xFF5A6B7A))
                PosTopActionButton("재출력", Icons.Filled.Print, Color(0xFF005645))
                PosTopActionButton("더보기", Icons.Filled.MoreVert, Color(0xFF757575))
            }
        }
    }
}

@Composable
private fun InfoChip(label: String, value: String) {
    Surface(
        color = Color(0xFFF1F3F4),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF5F6368)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                ),
                color = Color(0xFF202124)
            )
        }
    }
}

@Composable
private fun PosTopActionButton(
    label: String,
    icon: ImageVector,
    baseColor: Color
) {
    FilledTonalButton(
        onClick = {},
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = baseColor.copy(alpha = 0.15f),
            contentColor = baseColor 
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        modifier = Modifier.height(48.dp)
    ) {
        Icon(
            icon, 
            contentDescription = label, 
            modifier = Modifier.size(20.dp),
            tint = baseColor 
        )
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold 
            ),
            color = baseColor 
        )
    }
}

@Composable
private fun rememberCurrentDateTime(): LocalDateTime {
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            kotlinx.coroutines.delay(1000)
        }
    }
    return now
}
