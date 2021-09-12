package smt3.progdevelopment.trialmymanager.Authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.MainActivity
import smt3.progdevelopment.trialmymanager.Model.User
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.R
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.databinding.ActivitySignInBinding

class SignIn : AppCompatActivity() {

    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var mSignInBinding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSignInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(mSignInBinding.root)


        mLoading = ProgressDialog(this@SignIn)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading...")

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        myPreferences = mySharedPreference(this@SignIn)



        if (myPreferences.getValue("user").equals("signIn")) {
            val goMain = Intent(
                this@SignIn, MainActivity::class.java
            )
            startActivity(goMain)
            finish()
            return
        }

        mSignInBinding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }



        mSignInBinding.btnSignIn.setOnClickListener {
            if (validate()) {
                val mEmail = mSignInBinding.etEmail.text.toString()
                val mPassword = mSignInBinding.etPassword.text.toString()

                signUp(mEmail, mPassword)
            }
        }

    }

    private fun validate(): Boolean {

        if (mSignInBinding.etEmail.text.isEmpty()) {
            mSignInBinding.etEmail.requestFocus()
            mSignInBinding.etPassword.error = "Enter your email"

            return false
        }
        if (mSignInBinding.etPassword.text.isEmpty()) {
            mSignInBinding.etPassword.requestFocus()
            mSignInBinding.etPassword.error = "Enter your password"
            return false
        }
        return true
    }

    private fun signUp(mEmail: String, mPassword: String) {
//        mLoading.show()

        val emailCheck = mDatabase.orderByChild("email").equalTo(mEmail)

        emailCheck.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null){
                    var user: User? = null

                    for (item in snapshot.children){
                        user = item.getValue(User::class.java)
                    }

                    if (user!!.password == mPassword){

                        myPreferences.setValue(Constants.USER, Constants.LOGIN)

                        myPreferences.setValue(Constants.USER_ID, user.id)
                        myPreferences.setValue(Constants.USER_NAME, user.username)
                        myPreferences.setValue(Constants.USER_EMAIL, user.email)
                        myPreferences.setValue(Constants.USER_PHONE, user.phone)
                        myPreferences.setValue(Constants.USER_PASSWORD, user.password)

                        startActivity(Intent(this@SignIn, MainActivity::class.java))
                        finish()

                        mLoading.dismiss()
                    }
                    else{
                        Toast.makeText(this@SignIn, "Email or password are wrong", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@SignIn, "Email not registered", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                mLoading.dismiss()
                Toast.makeText(
                    this@SignIn,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}


