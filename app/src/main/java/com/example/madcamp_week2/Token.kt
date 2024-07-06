import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.google.gson.annotations.SerializedName

interface Token {

    @FormUrlEncoded
    @POST("oauth2/v4/token")
    fun getAccessToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String
    ): Call<TokenResponse>

}

data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("token_type") val tokenType: String
)
