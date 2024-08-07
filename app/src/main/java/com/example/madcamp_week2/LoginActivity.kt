package com.example.madcamp_week2

import com.example.madcamp_week2.Class.User
import UserDataHolder
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_week2.Class.Stock
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInButton: Button

    private val RC_SIGN_IN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInButton = findViewById<Button>(R.id.sign_in_button)
        signInButton.setOnClickListener {
            signIn()
        }

        // GoogleSignInOptions 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // GoogleSignInClient 초기화
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // 구글 로그인 성공 처리
            Log.d("Login", "구글 로그인 성공: ${account?.displayName}")

            // 사용자 정보 구글 서버에서 받아서 서버에 전송
            UserDataHolder.setUser(account)
            val user = UserDataHolder.getUser()
            if (user != null) {
                // 사용자가 이미 서버에 등록되어 있는지 확인 후 처리
                val loadPriceThread = Thread {
                    checkUserOnServer(user)
                }
                loadPriceThread.start()
                Thread.sleep(500)
                loadPriceThread.join()
            } else {
                Log.e("handleSignInResult", "com.example.madcamp_week2.Class.User data is null")
            }

            // MainActivity로 이동
            val intent = Intent(this, MainMenu::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // 500ms 후에 실행
            CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                startActivity(intent)
                finish() // LoginActivity 종료
            }

        } catch (e: ApiException) {
            // 구글 로그인 실패 처리
            Log.w("GoogleSignIn", "fail to login: ${e.message}")
            Toast.makeText(this, "구글 로그인 실패: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUserOnServer(user: User) {
        // 서버에서 사용자 정보 확인
        ApiClient.apiService.getUserById(user.id).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    // 이미 등록된 사용자인 경우
                    val serverUser = response.body()
                    serverUser?.favorites?.let { favoritesJson ->
                        val gson = Gson()
                        val type = object : TypeToken<List<Stock>>() {}.type
                        val favoritesList: List<Stock> = gson.fromJson(favoritesJson, type) ?: emptyList()
                        UserDataHolder.addAllFavorites(favoritesList)
                        Log.d("Login", "Favorite list: ${UserDataHolder.favoriteList}")
                    }
                    serverUser?.strategyList?.let { strategyJson ->
                        val strategyList = jsonToStrategyList(strategyJson)
                        UserDataHolder.addAllStrategy(strategyList)
                        Log.d("Login", "Strategy list: ${UserDataHolder.strategyList}")
                    }
                    Log.d("checkUserOnServer", "com.example.madcamp_week2.Class.User already exists on server: ${response.body()}")
                } else {
                    // 등록되지 않은 사용자인 경우
                    Log.d("checkUserOnServer", "com.example.madcamp_week2.Class.User not found on server, sending user data...")
                    sendUserDataToServer(user)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // 서버 요청 실패 처리
                Log.e("checkUserOnServer", "Failed to check user on server: ${t.message}")
            }
        })
    }

    private fun sendUserDataToServer(user: User) {
        Log.d("sendUserDataToServer", "Sending user data to server: $user")

        val call = ApiClient.apiService.createUser(user)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d("sendUserDataToServer", "서버에 사용자 정보 저장 성공: ${response.body()}")
                } else {
                    Log.d("sendUserDataToServer", "fail to save: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("sendUserDataToServer", "fail to request: ${t.message}")
            }
        })
    }
}
