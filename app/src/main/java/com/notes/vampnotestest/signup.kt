package com.notes.vampnotestest

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class signup : AppCompatActivity() {

    private lateinit var loginaccountTextview: TextView
    private lateinit var createAccountBtn: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        loginaccountTextview = findViewById(R.id.login_account_textview_btn)
        createAccountBtn = findViewById(R.id.signup_button)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.edit_password)
        confirmPasswordEditText = findViewById(R.id.edit_confirm_password)
        progressBar = findViewById(R.id.progress_bar)
        auth = Firebase.auth

        createAccountBtn.setOnClickListener {
            createAccount()
        }

        loginaccountTextview.setOnClickListener {
            finish()
        }


    }

    private fun createAccount() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if(!validateData(email,password,confirmPassword)){
            return
        }

        // if data is valid
        createAccountInFirebase(email,password)
    }

    private fun validateData(email: String, password: String, confirmPassword: String): Boolean {
        // to check whether the input data is correct or not

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Invalid email format"
            return false
        }
        if (password.length < 6) {
            passwordEditText.error = "Password length is invalid"
            return false
        }
        if (password != confirmPassword) {
            confirmPasswordEditText.error = "Passwords do not match"
            return false
        }
        return true

    }

    private fun createAccountInFirebase(email: String, password: String){

        changeInProgress(true)

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task -> //task is a variable which will tell us whether account is created or not
                //step 8th to add change in progress only after account is created
                changeInProgress(false)
                if (task.isSuccessful){
                    //Account creation Successful
                    Toast.makeText(this, "Account Created Successfully, check email to verify", Toast.LENGTH_SHORT).show()
                    //Once account is created firebase will return a user object which will have a method to send email verification
                    auth.currentUser?.sendEmailVerification() //this will send email verification to the user
                    // now email is sent to the user to verify the email so we will sign out the user so that after
                    //verification user can login again
                    auth.signOut()
                }
                else{
                    // Account creation failure
                    Toast.makeText(this, task.exception?.localizedMessage ?: "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun changeInProgress(inProgress: Boolean){
        if (inProgress)
        {
            progressBar.visibility = View.VISIBLE
            createAccountBtn.visibility = View.GONE
        }

        else{
            progressBar.visibility = View.GONE
            createAccountBtn.visibility = View.VISIBLE
        }
    }
}
