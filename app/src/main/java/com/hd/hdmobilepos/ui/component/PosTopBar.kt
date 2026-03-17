package com.hd.hdmobilepos.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PosTopBar() {
    val now = rememberCurrentDateTime()
    val dateFormatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd (E)") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm:ss") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "THE HYUNDAI",
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF005645),
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.width(6.dp))
        Column {
            Text(now.format(dateFormatter), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(now.format(timeFormatter), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(6.dp))
        Surface(color = Color(0xFFE7E7E7), shape = MaterialTheme.shapes.small) {
            Text("포스: 5556", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
        }
        Spacer(Modifier.width(4.dp))
        Surface(color = Color(0xFFE7E7E7), shape = MaterialTheme.shapes.small) {
            Text("거래: 0014", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
        }
        Spacer(Modifier.weight(1f))
        PosTopActionButton("점검", Icons.Filled.CheckCircle)
        PosTopActionButton("조회", Icons.Filled.Search)
        PosTopActionButton("영수증 재출력", Icons.Filled.Print)
        PosTopActionButton("더보기", Icons.Filled.MoreVert)
    }
}

@Composable
private fun PosTopActionButton(label: String, icon: ImageVector) {
    OutlinedButton(
        onClick = {},
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black.copy(alpha = 0.35f)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.padding(end = 4.dp), tint = Color.Black)
        Text(label, color = Color.Black)
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
