import com.example.madcamp_week2.Class.User
import com.example.madcamp_week2.MyLanguage.Strategy
import com.example.madcamp_week2.strategyListToJson
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.Gson

import com.example.madcamp_week2.Class.Stock

object UserDataHolder {
    private var user: User? = null
    private val _favoriteList: MutableSet<Stock> = mutableSetOf()
    private val _strategyList: MutableList<Strategy> = mutableListOf()
//    private val _favoriteNameList: MutableSet<String> = mutableSetOf()
    private val _friendList: MutableList<User> = mutableListOf()

    fun setUser(account: GoogleSignInAccount?) {
        user = account?.let {
            User(
                id = it.id ?: "",
                email = it.email,
                displayName = it.displayName,
                favorites = "[]",
                strategyList = "[]"
            )
        }
    }

    fun getUser(): User? {
        val jsonFavoriteList = Gson().toJson(_favoriteList.toList())
        val jsonStrategyList = strategyListToJson(_strategyList)
        return user?.copy(favorites = jsonFavoriteList, strategyList = jsonStrategyList)
    }

    val favoriteList: List<Stock>
        get() = _favoriteList.toList()

    val strategyList: MutableList<Strategy>
        get() = _strategyList

    fun addFavorite(stock: Stock) {
        _favoriteList.add(stock)
    }

    fun removeFavorite(stock: Stock) {
        _favoriteList.removeAll { it.id == stock.id }
    }

    fun isFavorite(stock: Stock): Boolean {
        return _favoriteList.any { it.id == stock.id }
    }

    fun addAllFavorites(stocks: List<Stock>) {
        _favoriteList.addAll(stocks)
    }

    fun addStrategy(strategy: Strategy) {
        _strategyList.add(strategy)
    }

    fun addAllStrategy(strategyList: List<Strategy>) {
        _strategyList.addAll(strategyList)
    }

    fun updateStrategy(strategy: Strategy, pos: Int) {
        if (pos in _strategyList.indices) {
            _strategyList[pos] = strategy
        }
    }

    fun removeStrategy(pos: Int) {
        if (pos in _strategyList.indices) {
            _strategyList.removeAt(pos)
        }
    }

//    fun addFavoriteName(stockName: String) {
//        _favoriteNameList.add(stockName)
//    }
//
//    fun addAllFavoritesName() {
//        _favoriteNameList.addAll(_favoriteList)
//    }
}
