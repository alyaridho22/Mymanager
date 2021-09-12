package smt3.progdevelopment.trialmymanager.Authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.MainActivity
import smt3.progdevelopment.trialmymanager.Model.User
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.databinding.ActivitySignUpBinding
import java.text.SimpleDateFormat
import java.util.*


class SignUp : AppCompatActivity() {

    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var mSignUpBinding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(mSignUpBinding.root)

//        val daftarNama = findViewById<EditText>(R.id.signup_name)
//        val daftarEmail = findViewById<EditText>(R.id.signup_email)
//        val daftarNomor = findViewById<EditText>(R.id.signup_pn)
//        val daftarPw = findViewById<EditText>(R.id.signup_pw)
//        val daftar = findViewById<ImageButton>(R.id.signupbt)
//        val keLogin = findViewById<ImageButton>(R.id.tologinbt)

        mLoading = ProgressDialog(this@SignUp)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading...")

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        myPreferences = mySharedPreference(this@SignUp)

        mSignUpBinding.btnCreate.setOnClickListener {

//            startActivity(Intent(this@SignUp, SignIn::class.java))
//            finish()
            if (validate()) {
                val mName = mSignUpBinding.etName.text.toString()
                val mEmail = mSignUpBinding.etEmail.text.toString()
                val mPhone = mSignUpBinding.etPhone.text.toString()
                val mPassword = mSignUpBinding.etPassword.text.toString()

                signUp(mName, mEmail, mPhone, mPassword)
            }
        }
    }

    private fun validate(): Boolean {
        //cek apa form sudah terisi atau belum
        if (mSignUpBinding.etName.text.isEmpty()) {
            mSignUpBinding.etName.requestFocus()
            mSignUpBinding.etName.error = "Enter your name"
            return false
        }
        if (mSignUpBinding.etEmail.text.isEmpty()) {
            mSignUpBinding.etEmail.requestFocus()
            mSignUpBinding.etEmail.error = "Enter your email"
            return false
        }
        if (mSignUpBinding.etPhone.text.isEmpty()) {
            mSignUpBinding.etPhone.requestFocus()
            mSignUpBinding.etPhone.error = "Enter your phone number"
            return false
        }
        if (mSignUpBinding.etPassword.text.isEmpty()) {
            mSignUpBinding.etPassword.requestFocus()
            mSignUpBinding.etPassword.error = "Enter your Password"
            return false
        }
        return true
    }

    private fun signUp(
        mName: String, mEmail: String, mPhone: String, mPassword: String
    ) {
        mLoading.show()

        val emailCheck = mDatabase.orderByChild("email").equalTo(mEmail)

        emailCheck.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                mLoading.dismiss()
                Toast.makeText(
                    this@SignUp,
                    "{$error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == null) {
                    //get date time now
                    val mCurrentTime =
                        SimpleDateFormat("yyyyMMdd:HHmmss", Locale.getDefault())
                            .format(Date())

                    val user = User(
                        mCurrentTime, mName, mEmail, mPhone, mPassword
                    )
                    mDatabase.child(mCurrentTime).setValue(user)

                    Toast.makeText(
                        this@SignUp,
                        "Your account has been successfully registered",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this@SignUp, SignIn::class.java))
                    finish()

                    mLoading.dismiss()

//                    myPreferences.setValue(Constants.USER, Constants.LOGIN)
//
//                    //menyimpan data user yang sudha masuk ke shared preferences
//                    myPreferences.setValue(Constants.USER_ID, user.id)
//                    myPreferences.setValue(Constants.USER_NAME, user.username)
//                    myPreferences.setValue(Constants.USER_EMAIL, user.email)
//                    myPreferences.setValue(Constants.USER_PHONE, user.phone)
//                    myPreferences.setValue(Constants.USER_PASSWORD, user.password)
//
//                    //intent ke MainActivity
//                    val goMain = Intent(this@SignUp, MainActivity::class.java)
//                    startActivity(goMain)
//                    finish()
//
//                    mLoading.dismiss()
                } else {
                    mLoading.dismiss()
                    Toast.makeText(
                        this@SignUp,
                        "Email sudah digunakan",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
        })
    }
}