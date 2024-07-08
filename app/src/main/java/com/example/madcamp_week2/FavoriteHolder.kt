object FavoriteHolder {
    private val _favoriteList: MutableSet<String> = mutableSetOf()

    val favoriteList: List<String>
        get() = _favoriteList.toList()

    fun addFavorite(stockId: String) {
        _favoriteList.add(stockId)
    }
}
