import com.example.madcamp_week2.Class.User
import com.example.madcamp_week2.MyLanguage.Strategy
import com.example.madcamp_week2.strategyListToJson
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.Gson

object UserDataHolder {
    private var user: User? = null
    private val _favoriteList: MutableSet<String> = mutableSetOf()
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

    fun clearUser() {
        user = null
    }

    val favoriteList: List<String>
        get() = _favoriteList.toList()
    val strategyList: MutableList<Strategy>
        get() = _strategyList

//    val favoriteNameList: List<String>
//        get() = _favoriteNameList.toList()

    fun addFavorite(stockId: String) {
        _favoriteList.add(stockId)
    }

    fun addAllFavorites(stockIds: List<String>) {
        _favoriteList.addAll(stockIds)
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
