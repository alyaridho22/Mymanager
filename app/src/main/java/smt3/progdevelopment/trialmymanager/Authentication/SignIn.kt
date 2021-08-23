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
import smt3.progdevelopment.trialmymanager.Preference.mySharedPreference
import smt3.progdevelopment.trialmymanager.R

class SignIn : AppCompatActivity() {

    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val masukEmail = findViewById<EditText>(R.id.insertEmail)
        val masukPw = findViewById<EditText>(R.id.insertPassword)
        val masuk = findViewById<ImageButton>(R.id.loginbutton)
        val keSignup = findViewById<TextView>(R.id.toSignUp)

        mLoading = ProgressDialog(this@SignIn)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading...")

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        myPreferences = mySharedPreference(this@SignIn)



        if (myPreferences.getValue("user").equals("signIn")){
            val goMain = Intent(
                this@SignIn, MainActivity::class.java
            )
            startActivity(goMain)
            finish()
            return
        }

        keSignup.setOnClickListener{
            val goSignIn= Intent(
                this@SignIn, SignUp::class.java
            )
            startActivity(goSignIn)
            finish()
        }
        masuk.setOnClickListener{
            if (validate()) {
            val mEmail = masukEmail.text.toString()
            val mPassword = masukPw.text.toString()

            signUp(mEmail, mPassword)

        }
    }
}
    val masukEmail = findViewById<EditText>(R.id.insertEmail)
    val masukPw = findViewById<EditText>(R.id.insertPassword)

private fun  validate(): Boolean{

    if (masukEmail.text.isEmpty()){
    }
    if (masukPw.text.isEmpty()) {
        masukPw.requestFocus()
        masukPw.error = "Enter your password"
        return false
    }
    return false
}

private fun signUp(mEmail: String, mPassword: String){
    mLoading.show()

    val cekEmail = mDatabase.orderByChild( "email").equalTo(mEmail)

    cekEmail.addListenerForSingleValueEvent(object : ValueEventListener){
        override fun onCancelled(error: DatabaseError) {
            mLoading.dismiss()
            Toast.makeText(
                this@SignIn,
             "${error.message}",
            Toast.LENGTH_SHORT
            ).show()
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.value != null){
                val user: User? = null

                for (item in snapshot.children){
                    user = item.getValue(User::class.java)
                }
                if (user!!.password == mPassword){
                    myPreferences.setValue("user", "signin")

                    myPreferences.setValue("id", user.id)
                    myPreferences.setValue("username", user.username)
                    myPreferences.setValue("phone", user.phone)
                    myPreferences.setValue("email", user.email)
                    myPreferences.setValue("password", user.password)

                    val goMain = Intent( this@SignIn, MainActivity::class.java
                    )
                    startActivity(goMain)
                    finish()


                    mLoading.dismiss()
                }else{
                    mLoading.dismiss()
                    Toast.makeText(
                        this@SignIn,
                    "Password salah",
                    Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                mLoading.dismiss()
                Toast.makeText(
                     this@SignIn,
                "email belum terdaftar",
                Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}}