package com.example.birdy.ui.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.birdy.data.CartItem
import com.example.birdy.data.CartManager
import com.example.birdy.data.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

// MARK: - Data Models (matching backend StoreDetailResponse)

data class StoreBrandInfo(
    val name: String,
    val logo_url: String,
    val banner_image_url: String,
    val rating: Double,
    val review_count: String,
    val cuisine: String,
    val tags: List<String>
)

data class StoreLocationInfo(
    val distance: String,
    val delivery_fee: Double,
    val delivery_time_est: String,
    val address: String
)

data class StoreMenuCategory(
    val category_name: String,
    val items: List<StoreMenuItem>
)

data class StoreMenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val image_url: String,
    val is_available: Boolean,
    val modifier_groups: List<StoreModifierGroup>
)

data class StoreModifierGroup(
    val id: String,
    val name: String,
    val min_selection: Int,
    val max_selection: Int,
    val is_required: Boolean,
    val options: List<StoreModifierOption>
)

data class StoreModifierOption(
    val id: String,
    val name: String,
    val extra_price: Double,
    val is_default: Boolean
)

data class StoreData(
    val restaurant_id: String,
    val brand_info: StoreBrandInfo,
    val location_info: StoreLocationInfo,
    val menu: List<StoreMenuCategory>
)

// MARK: - API Fetcher

suspend fun fetchStoreDetail(restaurantId: String): StoreData? {
    return withContext(Dispatchers.IO) {
        try {
            val url = "${Config.API_BASE_URL}/restaurants/$restaurantId"
            val jsonStr = java.net.URL(url).readText()
            parseStoreJson(JSONObject(jsonStr))
        } catch (e: Exception) {
            null
        }
    }
}

// MARK: - JSON Parser (shared by API and local file)

fun parseStoreJson(root: JSONObject): StoreData {
    val brandObj = root.getJSONObject("brand_info")
    val locObj = root.getJSONObject("location_info")
    val menuArr = root.getJSONArray("menu")

    val brandInfo = StoreBrandInfo(
        name = brandObj.getString("name"),
        logo_url = brandObj.optString("logo_url", ""),
        banner_image_url = brandObj.optString("banner_image_url", ""),
        rating = brandObj.optDouble("rating", 0.0),
        review_count = brandObj.optString("review_count", "New"),
        cuisine = brandObj.optString("cuisine", ""),
        tags = parseStringArray(brandObj.optJSONArray("tags"))
    )

    val locationInfo = StoreLocationInfo(
        distance = locObj.optString("distance", ""),
        delivery_fee = locObj.optDouble("delivery_fee", 2.99),
        delivery_time_est = locObj.optString("delivery_time", "20-35 min"),
        address = locObj.optString("address", "")
    )

    val menu = (0 until menuArr.length()).map { i ->
        val catObj = menuArr.getJSONObject(i)
        val itemsArr = catObj.getJSONArray("items")
        StoreMenuCategory(
            category_name = catObj.getString("category_name"),
            items = (0 until itemsArr.length()).map { j ->
                val itemObj = itemsArr.getJSONObject(j)
                val modArr = itemObj.optJSONArray("modifier_groups")
                StoreMenuItem(
                    id = itemObj.optString("id", ""),
                    name = itemObj.getString("name"),
                    description = itemObj.optString("description", ""),
                    price = itemObj.getDouble("price"),
                    image_url = itemObj.optString("image_url", ""),
                    is_available = itemObj.optBoolean("is_available", true),
                    modifier_groups = if (modArr != null) parseModifierGroups(modArr) else emptyList()
                )
            }
        )
    }

    return StoreData(
        restaurant_id = root.optString("restaurant_id", ""),
        brand_info = brandInfo,
        location_info = locationInfo,
        menu = menu
    )
}

private fun parseModifierGroups(arr: JSONArray): List<StoreModifierGroup> {
    return (0 until arr.length()).map { i ->
        val g = arr.getJSONObject(i)
        val optsArr = g.optJSONArray("options") ?: JSONArray()
        StoreModifierGroup(
            id = g.getString("id"),
            name = g.getString("name"),
            min_selection = g.optInt("min_selection", 0),
            max_selection = g.optInt("max_selection", 1),
            is_required = g.optBoolean("is_required", false),
            options = (0 until optsArr.length()).map { j ->
                val o = optsArr.getJSONObject(j)
                StoreModifierOption(
                    id = o.getString("id"),
                    name = o.getString("name"),
                    extra_price = o.optDouble("extra_price", 0.0),
                    is_default = o.optBoolean("is_default", false)
                )
            }
        )
    }
}

private fun parseStringArray(arr: JSONArray?): List<String> {
    if (arr == null) return emptyList()
    return (0 until arr.length()).map { arr.getString(it) }
}

// Legacy loader (backward compat)
fun loadStoreData(inputStream: InputStream): StoreData? {
    return try {
        parseStoreJson(JSONObject(inputStream.bufferedReader().use { it.readText() }))
    } catch (e: Exception) {
        null
    }
}

// MARK: - Store Screen

@Composable
fun StoreScreen(
    onBack: () -> Unit = {},
    onViewCart: () -> Unit = {},
    restaurantId: String = "",
    jsonInputStream: InputStream? = null
) {
    var storeData by remember { mutableStateOf<StoreData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf("Delivery") }
    var selectedItem by remember { mutableStateOf<StoreMenuItem?>(null) }

    // Load data: API if restaurantId provided, else local JSON
    LaunchedEffect(restaurantId) {
        isLoading = true
        loadError = false
        try {
            if (restaurantId.isNotEmpty()) {
                storeData = fetchStoreDetail(restaurantId)
            } else if (jsonInputStream != null) {
                storeData = loadStoreData(jsonInputStream)
            }
            if (storeData == null) loadError = true
        } catch (e: Exception) {
            loadError = true
        }
        isLoading = false
    }

    // Loading skeleton
    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(Color(0xFFF0F0F0))
            )
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(200.dp)
                    .height(28.dp)
                    .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeaderCircleButton(icon = Icons.Default.Close, onClick = onBack)
            }
        }
        return
    }

    // Error state
    if (loadError || storeData == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Failed to load restaurant", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Please try again", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Go Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color.Gray, RoundedCornerShape(12.dp))
                        .clickable { onBack() }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
                Text(
                    text = "Retry",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(colors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C))),
                            RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            isLoading = true
                            loadError = false
                        }
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
        return
    }

    val data = storeData!!

    Box(modifier = Modifier.fillMaxSize()) {
        // Scrollable content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // 1. BANNER with header buttons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                AsyncImage(
                    model = data.brand_info.banner_image_url,
                    contentDescription = "Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent)
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HeaderCircleButton(icon = Icons.Default.Close, onClick = onBack)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        HeaderCircleButton(icon = Icons.Default.Search) { /* TODO */ }
                        HeaderCircleButton(icon = Icons.Default.FavoriteBorder) { /* TODO */ }
                        HeaderCircleButton(icon = Icons.Default.MoreVert) { /* TODO */ }
                    }
                }
            }

            // 2. MAIN CONTENT
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                // Logo overlapping banner
                Box(modifier = Modifier.offset(y = (-40).dp)) {
                    AsyncImage(
                        model = data.brand_info.logo_url,
                        contentDescription = "Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(84.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .shadow(8.dp, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(-30.dp))

                // Title & Info
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = data.brand_info.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "More",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = String.format("%.1f", data.brand_info.rating),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = Color.Black
                        )
                        Text(
                            text = "(${data.brand_info.review_count}) • ${data.brand_info.tags.joinToString(" • ")} • ${data.brand_info.cuisine} • ${data.location_info.distance}",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // 3. DELIVERY / PICKUP TOGGLE
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(50))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        ToggleBtn("Delivery", selectedMode == "Delivery") { selectedMode = "Delivery" }
                        ToggleBtn("Pickup", selectedMode == "Pickup") { selectedMode = "Pickup" }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Group Order",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.08f), RoundedCornerShape(50))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }

                // 4. DELIVERY INFO BOX
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF0F0))
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (data.location_info.delivery_fee == 0.0) "Free delivery"
                            else "$${String.format("%.2f", data.location_info.delivery_fee)} delivery fee",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCC1111)
                        )
                        Text(text = "pricing & fees", fontSize = 13.sp, color = Color.Gray)
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(50.dp)
                            .align(Alignment.CenterVertically),
                        color = Color.Gray.copy(alpha = 0.3f)
                    )
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.Gray.copy(alpha = 0.03f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = data.location_info.delivery_time_est,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(text = "delivery time", fontSize = 13.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // 5. MENU CATEGORIES
                data.menu.forEachIndexed { index, category ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = category.category_name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black,
                            modifier = Modifier.padding(
                                top = if (index == 0) 0.dp else 30.dp,
                                bottom = 16.dp
                            )
                        )
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            category.items.forEach { item ->
                                StoreFoodCard(
                                    menuItem = item,
                                    restaurantName = data.brand_info.name,
                                    onItemTap = { selectedItem = item }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(if (CartManager.items.isEmpty()) 20.dp else 100.dp))
            }
        }

        // 6. FLOATING CART BAR
        if (CartManager.items.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .shadow(20.dp, RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(colors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C))),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-6).dp)
                                .background(Color.Red, CircleShape)
                                .size(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${CartManager.itemCount}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "${CartManager.itemCount} item${if (CartManager.itemCount == 1) "" else "s"}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "$${String.format("%.2f", CartManager.total)}",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "View Cart",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF388E3C),
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(50))
                            .clickable { onViewCart() }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }

    // 7. ITEM DETAIL SHEET (matches iOS ItemDetailSheet)
    selectedItem?.let { item ->
        ItemDetailSheet(
            item = item,
            restaurantName = data.brand_info.name,
            onDismiss = { selectedItem = null },
            onAddToCart = { cartItem ->
                CartManager.addItem(cartItem)
                selectedItem = null
            }
        )
    }
}

// MARK: - Item Detail Sheet (matches iOS ItemDetailView)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailSheet(
    item: StoreMenuItem,
    restaurantName: String,
    onDismiss: () -> Unit,
    onAddToCart: (CartItem) -> Unit,
    editingCartItem: CartItem? = null,  // If set, we're editing an existing cart item (matches iOS)
    initialQuantity: Int = 1,
    initialSelectedOptionNames: List<String> = emptyList(),
    initialSpecialInstructions: String = ""
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var quantity by remember { mutableStateOf(editingCartItem?.quantity ?: initialQuantity) }
    var specialInstructions by remember { mutableStateOf(editingCartItem?.specialInstructions ?: initialSpecialInstructions) }

    // Track selected options per modifier group: groupId → list of selected option IDs
    val selectedOptions = remember {
        mutableStateListOf<Pair<String, String>>().also { list ->
            if (editingCartItem != null) {
                // When editing, pre-fill from the cart item's selected options by matching option names
                val editOptionNames = editingCartItem.selectedOptions.map { it.substringBefore(" +$") }
                item.modifier_groups.forEach { group ->
                    group.options.forEach { opt ->
                        if (editOptionNames.contains(opt.name)) {
                            list.add(group.id to opt.id)
                        }
                    }
                }
            } else if (initialSelectedOptionNames.isNotEmpty()) {
                // Pre-fill from initial option names
                item.modifier_groups.forEach { group ->
                    group.options.forEach { opt ->
                        if (initialSelectedOptionNames.contains(opt.name)) {
                            list.add(group.id to opt.id)
                        }
                    }
                }
            } else {
                // Pre-select default options
                item.modifier_groups.forEach { group ->
                    group.options.forEach { opt ->
                        if (opt.is_default) {
                            list.add(group.id to opt.id)
                        }
                    }
                }
            }
        }
    }

    // Calculate extra price from selected modifiers
    val modifierExtraPrice = remember(selectedOptions.size) {
        var extra = 0.0
        item.modifier_groups.forEach { group ->
            val selectedForGroup = selectedOptions.filter { it.first == group.id }
            selectedForGroup.forEach { (_, optId) ->
                group.options.find { it.id == optId }?.let { extra += it.extra_price }
            }
        }
        extra
    }
    val totalPrice = (item.price + modifierExtraPrice) * quantity

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .verticalScroll(rememberScrollState())
        ) {
            // Close button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color.Gray.copy(alpha = 0.1f), CircleShape)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(16.dp),
                        tint = Color.Black
                    )
                }
            }

            // Item image
            if (item.image_url.isNotEmpty()) {
                AsyncImage(
                    model = item.image_url,
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Name & price
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = item.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%.2f", item.price)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCC5500)
                )
            }

            // Description
            if (item.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Modifier groups
            item.modifier_groups.forEach { group ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = group.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        if (group.is_required) {
                            Text(
                                text = "Required",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red,
                                modifier = Modifier
                                    .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = if (group.max_selection == 1) "Choose 1"
                        else "Choose up to ${group.max_selection}",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Options
                    group.options.forEach { option ->
                        val isSelected = selectedOptions.any { it.first == group.id && it.second == option.id }
                        val isRadio = group.max_selection <= 1

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isRadio) {
                                        // Radio: deselect others in same group
                                        selectedOptions.removeAll { it.first == group.id }
                                        selectedOptions.add(group.id to option.id)
                                    } else {
                                        // Checkbox: toggle
                                        val existing = selectedOptions.indexOfFirst { it.first == group.id && it.second == option.id }
                                        if (existing >= 0) {
                                            selectedOptions.removeAt(existing)
                                        } else {
                                            val countForGroup = selectedOptions.count { it.first == group.id }
                                            if (countForGroup < group.max_selection) {
                                                selectedOptions.add(group.id to option.id)
                                            }
                                        }
                                    }
                                }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isRadio) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        selectedOptions.removeAll { it.first == group.id }
                                        selectedOptions.add(group.id to option.id)
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFCC5500))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            if (isSelected) Color(0xFFCC5500) else Color.Transparent,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .border(
                                            width = if (isSelected) 0.dp else 1.5.dp,
                                            color = if (isSelected) Color(0xFFCC5500) else Color.Gray,
                                            shape = RoundedCornerShape(6.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = option.name,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = Color.Black,
                                modifier = Modifier.weight(1f)
                            )

                            if (option.extra_price > 0) {
                                Text(
                                    text = "+$${String.format("%.2f", option.extra_price)}",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Special instructions
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Special Instructions",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = specialInstructions,
                    onValueChange = { specialInstructions = it },
                    placeholder = {
                        Text("Add a note (e.g. no onions, extra sauce)", color = Color.Gray, fontSize = 14.sp)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3,
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp, color = Color.Black)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quantity stepper + Add to cart button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Stepper
                Row(
                    modifier = Modifier
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(50))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .clickable { if (quantity > 1) quantity-- },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("−", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                    Text(
                        text = "$quantity",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.width(36.dp),
                        textAlign = TextAlign.Center
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White, CircleShape)
                            .clickable { quantity++ },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                // Add to cart button
                Text(
                    text = "Add to Cart • $${String.format("%.2f", totalPrice)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            Brush.horizontalGradient(colors = listOf(Color(0xFF4CAF50), Color(0xFF388E3C))),
                            RoundedCornerShape(50.dp)
                        )
                        .clickable {
                            val selectedNames = mutableListOf<String>()
                            item.modifier_groups.forEach { group ->
                                selectedOptions.filter { it.first == group.id }.forEach { (_, optId) ->
                                    group.options.find { it.id == optId }?.let { opt ->
                                        val label = if (opt.extra_price > 0) "${opt.name} +$${String.format("%.2f", opt.extra_price)}"
                                         else opt.name
                                        selectedNames.add(label)
                                    }
                                }
                            }
                            onAddToCart(
                                CartItem(
                                    dishName = item.name,
                                    restaurantName = restaurantName,
                                    price = item.price + modifierExtraPrice,
                                    quantity = quantity,
                                    imageURL = item.image_url,
                                    specialInstructions = specialInstructions,
                                    selectedOptions = selectedNames,
                                    menuItem = item  // Store full menu item for cart re-editing (matches iOS)
                                )
                            )
                        }
                        .padding(vertical = 14.dp),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// MARK: - Store Food Card (matches iOS StoreFoodCardView)

@Composable
fun StoreFoodCard(
    menuItem: StoreMenuItem,
    restaurantName: String,
    onItemTap: () -> Unit = {}
) {
    val cartQuantity = CartManager.items
        .filter { it.dishName == menuItem.name }
        .sumOf { it.quantity }

    val hasModifiers = menuItem.modifier_groups.isNotEmpty()

    Column(
        modifier = Modifier.width(180.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Image with add/stepper button
        Box(
            modifier = Modifier
                .size(172.dp, 170.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onItemTap() }
        ) {
            AsyncImage(
                model = menuItem.image_url,
                contentDescription = menuItem.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            )

            if (hasModifiers) {
                // Show "Add" button that opens detail sheet
                if (cartQuantity == 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color.White, CircleShape)
                            .clickable { onItemTap() }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Customize",
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                } else {
                    // Stepper for items with modifiers (still opens detail on image tap)
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                            .shadow(3.dp, RoundedCornerShape(50))
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(horizontal = 6.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(Color(0xFFCC5500), CircleShape)
                                .clickable { CartManager.decrementItem(menuItem.name) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("−", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Text(
                            text = "$cartQuantity",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.width(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(Color(0xFFCC5500), CircleShape)
                                .clickable { onItemTap() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            } else {
                // No modifiers: direct add/stepper
                if (cartQuantity == 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .shadow(2.dp, CircleShape)
                            .background(Color.White, CircleShape)
                            .clickable {
                                CartManager.addItem(
                                    CartItem(
                                        dishName = menuItem.name,
                                        restaurantName = restaurantName,
                                        price = menuItem.price,
                                        imageURL = menuItem.image_url
                                    )
                                )
                            }
                            .padding(8.dp)
                    ) {
                        Text(text = "+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(6.dp)
                            .shadow(3.dp, RoundedCornerShape(50))
                            .background(Color.White, RoundedCornerShape(50))
                            .padding(horizontal = 6.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(Color(0xFFCC5500), CircleShape)
                                .clickable { CartManager.decrementItem(menuItem.name) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("−", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Text(
                            text = "$cartQuantity",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.width(22.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .background(Color(0xFFCC5500), CircleShape)
                                .clickable {
                                    CartManager.addItem(
                                        CartItem(
                                            dishName = menuItem.name,
                                            restaurantName = restaurantName,
                                            price = menuItem.price,
                                            imageURL = menuItem.image_url
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("+", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Name
        Text(
            text = menuItem.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { onItemTap() }
        )

        // Description (if present, show 1 line)
        if (menuItem.description.isNotEmpty()) {
            Text(
                text = menuItem.description,
                fontSize = 12.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { onItemTap() }
            )
        }

        // Price
        Text(
            text = "$${String.format("%.2f", menuItem.price)}",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

// MARK: - Toggle Button

@Composable
fun ToggleBtn(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (isSelected) Color.White else Color.Gray,
        modifier = Modifier
            .background(if (isSelected) Color.Black else Color.Transparent, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 8.dp)
    )
}

// MARK: - Header Circle Button

@Composable
fun HeaderCircleButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .shadow(4.dp, CircleShape)
            .background(Color.White, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Black
        )
    }
}

