package com.example.bankapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {

    var name: String? = ""
    var accountNumber: String? = ""
    lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.black)


        name = intent.getStringExtra("NAME").toString()
        accountNumber = intent.getStringExtra("ACCOUNT NUMBER").toString()

        findViewById<TextView>(R.id.username).text = "Hello, $name"

        val stringBuilder = StringBuilder()
        for (i in 0 until accountNumber!!.length) {
            stringBuilder.append(accountNumber!![i])
            if ((i + 1) % 4 == 0 && i < accountNumber!!.length - 1) {
                stringBuilder.append(" ") // Add a space after every four characters
            }
        }

        findViewById<TextView>(R.id.card_number).text = stringBuilder.toString()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<ImageView>(R.id.logOutBtn).setOnClickListener {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}
