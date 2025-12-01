package com.vouchervault.mobile.ui

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.vouchervault.mobile.R
import com.vouchervault.mobile.data.LoyaltyRepository
import com.vouchervault.mobile.model.LoyaltyCard
import kotlinx.coroutines.launch

@Composable
fun LoyaltyApp(repository: LoyaltyRepository) {
    LoyaltyHomeScreen(repository)
}

enum class ViewMode { LIST, GRID }

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun LoyaltyHomeScreen(repository: LoyaltyRepository) {
    val cards = repository.cards
    var viewMode by rememberSaveable { mutableStateOf(ViewMode.LIST) }
    var isAddDialogVisible by rememberSaveable { mutableStateOf(false) }
    var selectedCard by remember { mutableStateOf<LoyaltyCard?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result: IntentResult? ->
        if (result != null && result.contents != null) {
            repository.addCardFromScan(result.contents, result.formatName)
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(text = stringResource(id = R.string.cards_title), fontWeight = FontWeight.SemiBold)
                        Text(text = "Frontend mobile per VoucherVault", style = TopAppBarDefaults.topAppBarTitleTextStyle())
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewMode = if (viewMode == ViewMode.LIST) ViewMode.GRID else ViewMode.LIST
                    }) {
                        Icon(
                            imageVector = if (viewMode == ViewMode.LIST) Icons.Filled.GridView else Icons.Filled.List,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            IconButton(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(12.dp),
                onClick = { isAddDialogVisible = true }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(id = R.string.add_card), tint = Color.White)
            }
        }
    ) { innerPadding ->
        if (cards.isEmpty()) {
            EmptyState(modifier = Modifier.padding(innerPadding))
        } else {
            if (viewMode == ViewMode.LIST) {
                LoyaltyList(
                    cards = cards,
                    onCardClick = { selectedCard = it },
                    contentPadding = innerPadding
                )
            } else {
                LoyaltyGrid(
                    cards = cards,
                    onCardClick = { selectedCard = it },
                    contentPadding = innerPadding
                )
            }
        }
    }

    if (isAddDialogVisible) {
        AddCardDialog(
            onDismiss = { isAddDialogVisible = false },
            onScan = {
                barcodeLauncher.launch(buildScanOptions())
                isAddDialogVisible = false
            },
            onSave = { name, number, format ->
                repository.addCard(storeName = name, number = number, format = format)
                isAddDialogVisible = false
            }
        )
    }

    if (selectedCard != null) {
        LoyaltyCardBottomSheet(
            card = selectedCard!!,
            sheetState = sheetState,
            onDismiss = {
                coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        selectedCard = null
                    }
                }
            }
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.CreditCard,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Text(text = stringResource(id = R.string.empty_state), color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun LoyaltyList(
    cards: List<LoyaltyCard>,
    onCardClick: (LoyaltyCard) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        items(cards) { card ->
            LoyaltyCardRow(card = card, onCardClick = onCardClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LoyaltyGrid(
    cards: List<LoyaltyCard>,
    onCardClick: (LoyaltyCard) -> Unit,
    contentPadding: PaddingValues
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(160.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(cards, key = { it.id }) { card ->
            LoyaltyCardCell(card = card, onCardClick = onCardClick)
        }
    }
}

@Composable
private fun LoyaltyCardRow(card: LoyaltyCard, onCardClick: (LoyaltyCard) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(card.color)),
        onClick = { onCardClick(card) }
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Storefront,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
                    .padding(10.dp)
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = card.storeName, fontWeight = FontWeight.SemiBold)
                Text(text = "${card.cardNumber} • ${card.barcodeFormat}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun LoyaltyCardCell(card: LoyaltyCard, onCardClick: (LoyaltyCard) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(card.color)),
        onClick = { onCardClick(card) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Storefront,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.5f), CircleShape)
                    .padding(12.dp)
            )
            Text(text = card.storeName, fontWeight = FontWeight.SemiBold, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(text = card.cardNumber, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoyaltyCardBottomSheet(
    card: LoyaltyCard,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = card.storeName, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(text = "${card.cardNumber} • ${card.barcodeFormat}")
            BarcodePreview(cardNumber = card.cardNumber, formatName = card.barcodeFormat)
        }
    }

    LaunchedEffect(Unit) {
        sheetState.show()
    }
}

@Composable
private fun BarcodePreview(cardNumber: String, formatName: String) {
    val bitmap = remember(cardNumber, formatName) { generateBarcode(cardNumber, formatName) }
    if (bitmap != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = cardNumber,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )
        }
    } else {
        Text(text = "Barcode non disponibile")
    }
}

private fun generateBarcode(value: String, formatName: String): Bitmap? {
    return try {
        val format = BarcodeFormat.valueOf(formatName)
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(value, format, 800, 300)
        BarcodeEncoder().createBitmap(bitMatrix)
    } catch (ex: Exception) {
        null
    }
}

@Composable
private fun AddCardDialog(
    onDismiss: () -> Unit,
    onScan: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var storeName by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var format by rememberSaveable { mutableStateOf("CODE_128") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.add_card)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FormTextField(value = storeName, onValueChange = { storeName = it }, label = stringResource(id = R.string.store_name))
                FormTextField(value = cardNumber, onValueChange = { cardNumber = it }, label = stringResource(id = R.string.card_number))
                FormTextField(value = format, onValueChange = { format = it.uppercase() }, label = stringResource(id = R.string.barcode_format))
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(storeName.ifBlank { "Nuova carta" }, cardNumber, format.ifBlank { "CODE_128" })
            }) {
                Text(text = stringResource(id = R.string.save))
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel))
                }
                TextButton(onClick = onScan) {
                    Text(text = stringResource(id = R.string.scan_barcode))
                }
            }
        }
    )
}

@Composable
private fun FormTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors()
    )
}

private fun buildScanOptions(): ScanOptions {
    return ScanOptions()
        .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        .setPrompt("Allinea il codice a barre nel riquadro")
        .setBeepEnabled(false)
        .setBarcodeImageEnabled(true)
        .setOrientationLocked(false)
}
