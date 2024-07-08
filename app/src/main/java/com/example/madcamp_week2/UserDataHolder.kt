import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.example.madcamp_week2.Stock

object UserDataHolder {
    private var user: User? = null
    private val _favoriteList: MutableSet<Stock> = mutableSetOf()

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

    val favoriteList: List<Stock>
        get() = _favoriteList.toList()


    fun addFavorite(stock: Stock) {
        _favoriteList.add(stock)
    }

    fun addAllFavorites(stocks: List<Stock>) {
        _favoriteList.addAll(stocks)
    }

}


//object UserDataHolder {
//    private var user: User? = null
//    private val _favoriteList: MutableSet<String> = mutableSetOf()
//
//    fun setUser(account: GoogleSignInAccount?) {
//        user = if (account != null) {
//            User(
//                id = account.id ?: "",
//                email = account.email,
//                displayName = account.displayName,
//                favorites = ""
//            )
//        } else {
//            null
//        }
//    }
//
//    fun getUser(): User? {
//        return user
//    }
//
//    val favoriteList: List<String>
//        get() = _favoriteList.toList()
//
//
//    fun addFavorite(stockId: String) {
//        _favoriteList.add(stockId)
//    }
//
//    fun addAllFavorites(stockIds: List<String>) {
//        _favoriteList.addAll(stockIds)
//    }
//
//}
