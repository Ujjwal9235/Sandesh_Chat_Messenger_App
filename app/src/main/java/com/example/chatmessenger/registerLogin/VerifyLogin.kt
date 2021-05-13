package com.example.chatmessenger.registerLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatmessenger.Messages.LatestMessagesActivity
import com.example.chatmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_verify_login.*

class VerifyLogin: AppCompatActivity() {

    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_verify_login.setOnClickListener {
            performLogin()
        }

        back_to_register_text_verify_view.setOnClickListener{
            finish()
        }
    }

    fun verifyemail() {
        val firebaseUser:FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val vemail:Boolean? = firebaseUser?.isEmailVerified
        startActivity(Intent(this,LatestMessagesActivity::class.java))
        if(vemail!!) {
            finish()
            startActivity(Intent(this, LatestMessagesActivity::class.java))
        }
        else{
                Toast.makeText(this,"please verify your email",Toast.LENGTH_SHORT).show()
                firebaseAuth?.signOut()
            }
    }

    private fun performLogin() {
        val email = email_edittext_verify_login.text.toString()
        val password = password_edittext_verify_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful)
                    {
                        return@addOnCompleteListener
                    }


                    verifyemail()
                    Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

//                    val intent = Intent(this, LatestMessagesActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

}