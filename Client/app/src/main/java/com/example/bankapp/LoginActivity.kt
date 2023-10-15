package com.example.bankapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var otp1 : EditText
    private lateinit var otp2 : EditText
    private lateinit var otp3 : EditText
    private lateinit var otp4 : EditText
    private lateinit var otp5 : EditText
    private lateinit var otp6 : EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        database = FirebaseDatabase.getInstance().getReference("Users")


        findViewById<TextView>(R.id.createAccount).setOnClickListener {
            startActivity(Intent(this,CreateAccountActivity::class.java))
        }
        progressBar.visibility = View.GONE
        addTextChangeListener()

        findViewById<Button>(R.id.login_button).setOnClickListener {
            //collect otp from all the edit texts
            val accountNumberText = findViewById<EditText?>(R.id.account_number_text).text.toString()
            val typedOTP =
                (otp1.text.toString() + otp2.text.toString() + otp3.text.toString()
                        + otp4.text.toString() + otp5.text.toString() + otp6.text.toString())

            if(accountNumberText.isNotEmpty()) {
                if (typedOTP.isNotEmpty()) {
                    findMpinByAccountNumber(accountNumberText, typedOTP)
                } else {
                    Toast.makeText(this, "Please Enter MPIN", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Please Enter Account Number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTextChangeListener() {
        otp1.addTextChangedListener(EditTextWatcher(otp1))
        otp2.addTextChangedListener(EditTextWatcher(otp2))
        otp3.addTextChangedListener(EditTextWatcher(otp3))
        otp4.addTextChangedListener(EditTextWatcher(otp4))
        otp5.addTextChangedListener(EditTextWatcher(otp5))
        otp6.addTextChangedListener(EditTextWatcher(otp6))
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
        progressBar = findViewById(R.id.phoneProgressBar)
    }

    // Inside your LoginActivity class
    // Inside your LoginActivity class
    private fun findMpinByAccountNumber(accountNumber: String, typedOTP: String) {

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val userAccountNumber = userSnapshot.child("account_number").getValue(String::class.java)

                    if (userAccountNumber == accountNumber) {
                        val mpin = userSnapshot.child("mpin").getValue(String::class.java)
                        if (typedOTP.length == 6 && typedOTP == mpin) {
                            progressBar.visibility = View.VISIBLE
                            val intent  = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("NAME",userSnapshot.child("name").getValue(String::class.java))
                            intent.putExtra("ACCOUNT NUMBER", userAccountNumber)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "Please Enter Correct MPIN", Toast.LENGTH_SHORT).show()
                        }
                        return // Exit the loop after finding a match
                    }
                }

                // Handle the case where no matching account number was found
                Toast.makeText(this@LoginActivity, "Account number not found", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                Toast.makeText(this@LoginActivity, "Database error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }




}