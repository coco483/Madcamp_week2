import com.example.madcamp_week2.Stock

data class User(
    val id: String,
    val email: String?,
    val displayName: String?,
    var favorites: String? // Assuming only IDs are sent
)
