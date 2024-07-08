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

    fun setUser(account: GoogleSignInAccount?) {
        user = if (account != null) {
            User(
                id = account.id ?: "",
                email = account.email,
                displayName = account.displayName,
                favorites = "[]",
                strategyList = "[]"
            )
        } else {
            null
        }
    }

    fun getUser(): User? {
        val jsonFavoriteList = Gson().toJson(_favoriteList)
        val jsonStrategyList = strategyListToJson(_strategyList)
        return user?.let { User(user!!.id, user!!.email, user!!.displayName, jsonFavoriteList, jsonStrategyList) }
    }

    val favoriteList: List<Stock>
        get() = _favoriteList.toList()
    val strategyList: MutableList<Strategy>
        get() = _strategyList


    fun addFavorite(stock: Stock) {
        _favoriteList.add(stock)
    }

    fun addAllFavorites(stocks: List<Stock>) {
        _favoriteList.addAll(stocks)
    }


    fun addStrategy(strategy: Strategy){
        _strategyList.add(strategy)
    }

    fun addAllStrategy(strategyList: List<Strategy>){
        _strategyList.addAll(strategyList)
    }

    fun updateStrategy(strategy: Strategy, pos: Int){
        _strategyList[pos] = strategy
    }

//    fun addFavoriteName(stockName: String) {
//        _favoriteNameList.add(stockName)
//    }
//
//    fun addAllFavoritesName() {
//        _favoriteNameList.addAll(_favoriteList)
//    }
}
