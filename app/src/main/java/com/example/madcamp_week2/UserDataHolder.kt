import com.google.android.gms.auth.api.signin.GoogleSignInAccount

object UserDataHolder {
    private var user: User? = null

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
}
