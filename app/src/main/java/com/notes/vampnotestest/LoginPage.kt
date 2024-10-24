package com.notes.vampnotestest

import android.content.Intent
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

class LoginPage : AppCompatActivity() {

    private lateinit var signUpAccountTextView : TextView

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_page)

        signUpAccountTextView = findViewById(R.id.signup_account_textview_btn)
        progressBar = findViewById(R.id.progress_bar)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.edit_password)
        loginButton = findViewById(R.id.signin_button)
        auth = Firebase.auth

        loginButton.setOnClickListener{
            loginUser()
        }

        signUpAccountTextView.setOnClickListener{
            startActivity(Intent(this, signup::class.java))
        }

    }
    private fun loginUser()
    {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        //3rd step
        if (!validateData(email,password)){
            return
        }

        //4th step
        loginAccountInFirebase(email,password)
    }

    ///step2 create a validate fun
    private fun validateData(email:String,password: String): Boolean{
        //to check whether input data is correct or not
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.error = "Email is Inavalid"
            return false
        }
        if(password.length<6){
            passwordEditText.error = "Password length is invalid"
            return false
        }
        return true
    }

    //step5
    private fun loginAccountInFirebase(email: String, password: String){
        changeInProgress(true)
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                changeInProgress(false)
                if(task.isSuccessful){
                    //login successful
                    //Checking email verified or not
                    if (auth.currentUser?.isEmailVerified()==true){
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        //go to main activity
                    }
                }
                else{
                    //login failed
                    Toast.makeText(this,task.exception?.localizedMessage?:"Login failed",Toast.LENGTH_SHORT).show()
                }
            }

    }
    private fun changeInProgress(inProgress: Boolean){
        if (inProgress){
            progressBar.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        }
        else{
            progressBar.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
        }
    }
}