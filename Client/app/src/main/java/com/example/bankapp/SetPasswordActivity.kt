package com.example.bankapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Random

class SetPasswordActivity : AppCompatActivity() {

    private lateinit var otp1 : EditText
    private lateinit var otp2 : EditText
    private lateinit var otp3 : EditText
    private lateinit var otp4 : EditText
    private lateinit var otp5 : EditText
    private lateinit var otp6 : EditText
    private lateinit var rotp1 : EditText
    private lateinit var rotp2 : EditText
    private lateinit var rotp3 : EditText
    private lateinit var rotp4 : EditText
    private lateinit var rotp5 : EditText
    private lateinit var rotp6 : EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth : FirebaseAuth
    var email: String? = ""
    var name: String? = ""
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_password)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")

        init()
        progressBar.visibility = View.GONE
        addTextChangeListener()

        val accountNumber = generateUniqueAccountNumber()
        if (intent.hasExtra("Name")) {
            this.name = intent.getStringExtra("Name").toString()
        }
        if (intent.hasExtra("Email")) {
            this.email = intent.getStringExtra("Email").toString()
        }

        findViewById<Button>(R.id.login_button).setOnClickListener {
            val typedOTP =
                (otp1.text.toString() + otp2.text.toString() + otp3.text.toString()
                        + otp4.text.toString() + otp5.text.toString() + otp6.text.toString())
            val typedROTP =
                (rotp1.text.toString() + rotp2.text.toString() + rotp3.text.toString()
                        + rotp4.text.toString() + rotp5.text.toString() + rotp6.text.toString())

            if (typedOTP.isNotEmpty() && typedROTP.isNotEmpty()) {
                if (typedOTP.length == 6 && typedROTP.length == 6 && typedOTP == typedROTP) {
                    progressBar.visibility = View.VISIBLE
                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                    if (currentUserUid != null) {
                        val userReference = database.child(currentUserUid)
                        val userMap = mapOf(
                            "account_number" to accountNumber,
                            "name" to name,
                            "email" to email,
                            "mpin" to typedROTP
                        )
                        userReference.setValue(userMap).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent  = Intent(this@SetPasswordActivity, MainActivity::class.java)
                                intent.putExtra("NAME",name)
                                intent.putExtra("ACCOUNT NUMBER", accountNumber)
                                startActivity(intent)
                                finish()
                            } else {
                                // Handle the error
                                Toast.makeText(this, "Failed to save user data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "MPIN in both the boxes should be same", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please Enter MPIN in both boxes", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun init(){
        otp1 = findViewById(R.id.OTP1)
        otp2 = findViewById(R.id.OTP2)
        otp3 = findViewById(R.id.OTP3)
        otp4 = findViewById(R.id.OTP4)
        otp5 = findViewById(R.id.OTP5)
        otp6 = findViewById(R.id.OTP6)
        rotp1 = findViewById(R.id.ROTP1)
        rotp2 = findViewById(R.id.ROTP2)
        rotp3 = findViewById(R.id.ROTP3)
        rotp4 = findViewById(R.id.ROTP4)
        rotp5 = findViewById(R.id.ROTP5)
        rotp6 = findViewById(R.id.ROTP6)
        progressBar = findViewById(R.id.phoneProgressBar)
    }

    private fun addTextChangeListener() {
        otp1.addTextChangedListener(EditTextWatcher(otp1))
        otp2.addTextChangedListener(EditTextWatcher(otp2))
        otp3.addTextChangedListener(EditTextWatcher(otp3))
        otp4.addTextChangedListener(EditTextWatcher(otp4))
        otp5.addTextChangedListener(EditTextWatcher(otp5))
        otp6.addTextChangedListener(EditTextWatcher(otp6))

        rotp1.addTextChangedListener(EditTextWatcher(rotp1))
        rotp2.addTextChangedListener(EditTextWatcher(rotp2))
        rotp3.addTextChangedListener(EditTextWatcher(rotp3))
        rotp4.addTextChangedListener(EditTextWatcher(rotp4))
        rotp5.addTextChangedListener(EditTextWatcher(rotp5))
        rotp6.addTextChangedListener(EditTextWatcher(rotp6))
    }

    inner class EditTextWatcher(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

        override fun afterTextChanged(p0: Editable?) {

            val text = p0.toString()
            when (view.id) {
                R.id.OTP1 -> if (text.length == 1) otp2.requestFocus()
                R.id.OTP2 -> if (text.length == 1) otp3.requestFocus() else if (text.isEmpty()) otp1.requestFocus()
                R.id.OTP3 -> if (text.length == 1) otp4.requestFocus() else if (text.isEmpty()) otp2.requestFocus()
                R.id.OTP4 -> if (text.length == 1) otp5.requestFocus() else if (text.isEmpty()) otp3.requestFocus()
                R.id.OTP5 -> if (text.length == 1) otp6.requestFocus() else if (text.isEmpty()) otp4.requestFocus()
                R.id.OTP6 -> if (text.isEmpty()) otp5.requestFocus()

                R.id.ROTP1 -> if (text.length == 1) rotp2.requestFocus()
                R.id.ROTP2 -> if (text.length == 1) rotp3.requestFocus() else if (text.isEmpty()) rotp1.requestFocus()
                R.id.ROTP3 -> if (text.length == 1) rotp4.requestFocus() else if (text.isEmpty()) rotp2.requestFocus()
                R.id.ROTP4 -> if (text.length == 1) rotp5.requestFocus() else if (text.isEmpty()) rotp3.requestFocus()
                R.id.ROTP5 -> if (text.length == 1) rotp6.requestFocus() else if (text.isEmpty()) rotp4.requestFocus()
                R.id.ROTP6 -> if (text.isEmpty()) rotp5.requestFocus()

            }
        }
    }

    // Generate a unique 12-digit account number
    private fun generateUniqueAccountNumber(): String {
        val random = Random()
        val min = 1_000_000_000_00L // Minimum 12-digit number
        val max = 9_999_999_999_99L // Maximum 12-digit number
        var accountNumber: Long

        // Keep generating until a unique account number is found
        do {
            accountNumber = min + (random.nextDouble() * (max - min + 1)).toLong()
        } while (!isAccountNumberUnique(accountNumber))

        findViewById<TextView>(R.id.accountNumberText).text = accountNumber.toString()
        return (accountNumber.toString())
    }

    // Check if the generated account number is unique in Firebase
    private fun isAccountNumberUnique(accountNumber: Long): Boolean {
        // Use a ValueEventListener to check if the account number already exists
        var isUnique = true
        val query = database.orderByChild("account_number").equalTo(accountNumber.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Account number already exists, generate a new one
                    isUnique = false
                    generateUniqueAccountNumber()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(this@SetPasswordActivity, "Database error occurred", Toast.LENGTH_SHORT).show()
            }
        })

        return isUnique
    }

}