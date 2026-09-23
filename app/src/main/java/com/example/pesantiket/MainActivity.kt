package com.example.pesantiket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pesantiket.ui.theme.PesanTiketTheme
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PesanTiketTheme {
                TicketScreen()
            }
        }
    }
}

@Composable
fun TicketScreen() {

    var ticketPrice by rememberSaveable {
        mutableStateOf(50000)
    }

    var ticketCount by rememberSaveable {
        mutableStateOf(1)
    }

    var buyerName by rememberSaveable {
        mutableStateOf("")
    }

    var statusMessage by rememberSaveable {
        mutableStateOf("Silakan pesan tiket")
    }

    var isProcessing by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(isProcessing) {

        if (isProcessing) {

            delay(5000)

            statusMessage = "Tiket telah dipesan"
            isProcessing = false
        }
    }

    TicketContent(
        ticketPrice = ticketPrice,
        ticketCount = ticketCount,
        buyerName = buyerName,
        statusMessage = statusMessage,
        isProcessing = isProcessing,

        onBuyerNameChange = {
            buyerName = it
        },

        onDecrease = {
            if (ticketCount > 1) {
                ticketCount--
            }
        },

        onIncrease = {
            ticketCount++
        },

        onOrderClick = {

            if (buyerName.isBlank()) {
                statusMessage = "Nama masih kosong"

            } else {
                statusMessage = "Memproses pesanan..."
                isProcessing = true
            }
        }
    )
}

@Composable
fun TicketContent(
    ticketPrice: Int,
    ticketCount: Int,
    buyerName: String,
    statusMessage: String,
    isProcessing: Boolean,
    onBuyerNameChange: (String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onOrderClick: () -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "Pemesanan Tiket",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Text(
                text = "Nama Pembeli",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = buyerName,
                onValueChange = onBuyerNameChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Masukkan nama Anda")
                },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Harga Tiket",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rp${formatRupiah(ticketPrice)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Jumlah Tiket",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Tombol Kurang
                Button(
                    onClick = onDecrease,
                    enabled = ticketCount > 1,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0000FF)
                    )
                ) {
                    Text(text = "-")
                }

                // Jumlah Tiket
                Text(
                    text = "$ticketCount",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Tombol Tambah
                Button(
                    onClick = onIncrease,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0000FF)
                    )
                ) {
                    Text(text = "+")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Total Harga
            Text(
                text = "Total Harga",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rp${formatRupiah(ticketPrice * ticketCount)}",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Pesan
            Button(
                onClick = onOrderClick,
                enabled = !isProcessing,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0000FF)
                )
            ) {
                Text(
                    text = if (isProcessing) {
                        "Memproses..."
                    } else {
                        "Pesan Tiket"
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {

                Text(
                    text = "Status: $statusMessage",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}

fun formatRupiah(value: Int): String {
    return "%,d".format(value).replace(',', '.')
}