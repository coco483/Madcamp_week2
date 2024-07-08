import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object UserDataHolder {
    private var user: User? = null
    private val _favoriteList: MutableSet<String> = mutableSetOf()
//    private val _favoriteNameList: MutableSet<String> = mutableSetOf()

    fun setUser(account: GoogleSignInAccount?) {
        user = if (account != null) {
            User(
                id = account.id ?: "",
                email = account.email,
                displayName = account.displayName,
                favorites = ""
            )
        } else {
            null
        }
    }

    fun getUser(): User? {
        return user
    }

    fun clearUser() {
        user = null
    }

    val favoriteList: List<String>
        get() = _favoriteList.toList()

//    val favoriteNameList: List<String>
//        get() = _favoriteNameList.toList()

    fun addFavorite(stockId: String) {
        _favoriteList.add(stockId)
    }

    fun addAllFavorites(stockIds: List<String>) {
        _favoriteList.addAll(stockIds)
    }

//    fun addFavoriteName(stockName: String) {
//        _favoriteNameList.add(stockName)
//    }
//
//    fun addAllFavoritesName() {
//        _favoriteNameList.addAll(_favoriteList)
//    }
}
