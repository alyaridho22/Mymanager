package smt3.progdevelopment.trialmymanager.Authentication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.MainActivity
import smt3.progdevelopment.trialmymanager.Model.User
import smt3.progdevelopment.trialmymanager.R
import java.text.SimpleDateFormat
import java.util.*

private lateinit var mLoading: ProgressDialog
private lateinit var mDatabase: DatabaseReference
private lateinit var myPreferences: Preference

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val daftarNama = findViewById<EditText>(R.id.signup_name)
        val daftarEmail = findViewById<EditText>(R.id.signup_email)
        val daftarNomor = findViewById<EditText>(R.id.signup_pn)
        val daftarPw = findViewById<EditText>(R.id.signup_pw)
        val daftar = findViewById<ImageButton>(R.id.signupbt)
        val keLogin = findViewById<ImageButton>(R.id.tologinbt)

        mLoading = ProgressDialog( this@SignUp)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading...")

        mDatabase = FirebaseDatabase.getInstance().getReference( "User")
        myPreferences = Preference(this@SignUp)

        keLogin.setOnClickListener {

            val goSignIn = Intent( this@SignUp, SignIn::class.java)
            startActivity(goSignIn)
            finish()
        }
        daftar.setOnClickListener {

            if (validate()) {
                val masukNama = daftarNama.text.toString()
                val masukEmail = daftarEmail.text.toString()
                val masukNomor = daftarNomor.text.toString()
                val masukPassword = daftarPw.text.toString()

                signUp(masukNama, masukEmail, masukNomor, masukPassword)

            }
        }
    }

    val daftarNama = findViewById<EditText>(R.id.signup_name)
    val daftarEmail = findViewById<EditText>(R.id.signup_email)
    val daftarNomor = findViewById<EditText>(R.id.signup_pn)
    val daftarPw = findViewById<EditText>(R.id.signup_pw)
    val daftar = findViewById<ImageButton>(R.id.signupbt)
    val keLogin = findViewById<ImageButton>(R.id.tologinbt)
    private fun validate(): Boolean {
        //cek apa form sudah terisi atau belum
        if (daftarNama.text.isEmpty()){
            daftarNama.requestFocus()
            daftarNama.error = "Enter your First Name"
            return false
        }
        if (daftarEmail.text.isEmpty()){
            daftarEmail.requestFocus()
            daftarEmail.error = "Enter your Last Name"
            return false
        }
        if (daftarNomor.text.isEmpty()){
            daftarNomor.requestFocus()
            daftarNomor.error = "Enter your Email"
            return false
        }
        if (daftarPw.text.isEmpty()){
            daftarPw.requestFocus()
            daftarPw.error = "Enter your Password"
            return false
        }
        return false
    }
    private fun signUp(
        masukNama: String, masukEmail: String, masukNomor: String, masukPassword: String
    ){
        mLoading.show()

        val cekEmail = mDatabase.orderByChild("email").equalTo(masukEmail)

        cekEmail.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError){
                mLoading.dismiss()
                Toast.makeText(
                    this@SignUp,
                    "{$error.message}",
                Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(snapshot: DataSnapshot){
                if (snapshot.value == null){
                    //get date time now
                    val mCurrentTime =
                        SimpleDateFormat( "yyyyMMdd:HHmmss", Locale.getDefault())
                    .format(Date())

                    val user = User(
                        mCurrentTime, masukNama, masukEmail, masukNomor, masukPassword
                    )
                    mDatabase.child(mCurrentTime).setValue(user)

                    myPreferences.setValue("user", "signIn")

                    //menyimpan data user yang sudha masuk ke shared preferences
                    myPreferences.setValue("id", user.id)
                    myPreferences.setValue("username", user.username)
                    myPreferences.setValue("email", user.email)
                    myPreferences.setValue("phone", user.phone)
                    myPreferences.setValue("password", user.password)

                    //intent ke MainActivity
                    val goMain = Intent(this@SignUp, MainActivity::class.java)
                    startActivity(goMain)
                    finish()

                    mLoading.dismiss()
                }else {
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