package com.example.birdy.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Matches iOS CartManager.shared singleton

data class CartItem(
    val dishName: String,
    val restaurantName: String,
    val price: Double,
    var quantity: Int = 1,
    val imageURL: String = "",
    val specialInstructions: String = "",
    val selectedOptions: List<String> = emptyList()
)

object CartManager {
    var items by mutableStateOf(mutableListOf<CartItem>())
        private set

    val total: Double
        get() = items.sumOf { it.price * it.quantity }

    val itemCount: Int
        get() = items.sumOf { it.quantity }

    fun addItem(item: CartItem) {
        val existing = items.find { it.dishName == item.dishName }
        if (existing != null) {
            existing.quantity += 1
        } else {
            items = (items + item).toMutableList()
        }
    }

    fun decrementItem(dishName: String) {
        val index = items.indexOfFirst { it.dishName == dishName }
        if (index >= 0) {
            val item = items[index]
            if (item.quantity > 1) {
                item.quantity -= 1
                items = items.toMutableList() // trigger recomposition
            } else {
                items = items.toMutableList().also { it.removeAt(index) }
            }
        }
    }

    fun clear() {
        items = mutableListOf()
    }
}