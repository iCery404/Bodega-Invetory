package com.bodega.inventory.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bodega.inventory.data.Item
import com.bodega.inventory.viewmodel.InventoryViewModel

@Composable
fun ItemListSheet(
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = viewModel()
) {
    val items by viewModel.allItems.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Items",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Search field
        SearchField(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        // Items list
        val filteredItems = items.filter { item ->
            item.name.contains(searchQuery, ignoreCase = true)
        }
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredItems) { item ->
                ItemRow(item)
            }
        }
    }
}

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        placeholder = { Text("Search items...") },
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

@Composable
fun ItemRow(item: Item) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Quantity: ${item.quantity}",
                fontSize = 14.sp,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AddItemSheet(
    onDismiss: () -> Unit,
    onItemAdded: (String, Int) -> Unit,
    viewModel: InventoryViewModel = viewModel()
) {
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Add New Item",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )
        
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray
                )
            ) {
                Text("Cancel")
            }
            
            Button(
                onClick = {
                    if (itemName.isNotEmpty() && quantity.isNotEmpty()) {
                        onItemAdded(itemName, quantity.toIntOrNull() ?: 0)
                        onDismiss()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Text("Add")
            }
        }
    }
}
