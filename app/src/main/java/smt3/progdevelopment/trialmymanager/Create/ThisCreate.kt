package smt3.progdevelopment.trialmymanager.Create

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import smt3.progdevelopment.trialmymanager.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences

class ThisCreate : AppCompatActivity() {

    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: Preferences
    private lateinit var userId: String


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_this_create)
    // Initialize variable
    mLoading = ProgressDialog(this@ThisCreate)
    mLoading.setCancelable(false)
    mLoading.setMessage("Loading ...")

    mDatabase = FirebaseDatabase.getInstance().getReference("Schedule")
    myPreferences = Preferences(this@ThisCreate)

    // mengambil data yang di kirim melalui intent
    if (intent != null){
        userId = intent.getStringExtra("userId")!!
    }

    btnTambah.setOnClickListener {
        if (validate()){
            val mNama = etNama.text.toString()
            val mJumlah = etJumlah.text.toString().toInt()

            create(mNama, mJumlah)
        }
    }

}

private fun validate(): Boolean {
    // Cek apakah form sudah terisi atau belum
    if (etNama.text.isEmpty()){
        etNama.requestFocus()
        etNama.error = "Masukkan nama koleksi"
        return false
    }
    if (etJumlah.text.isEmpty()){
        etJumlah.requestFocus()
        etJumlah.error = "Masukkan jumlah koleksi"
        return false
    }
    return true
}
private fun create(mNama: String, mJumlah: Int) {
    mLoading.show()

    // Get Date Time Now
    val  mCurrentTime = SimpleDateFormat("yyyyMMdd:HHmmss", Locale.getDefault())
        .format(Date())

    // Mengisi variabel pada model Koleksi
    val  koleksi = Koleksi(mCurrentTime, mNama, mJumlah)
    mDatabase.child(userId).child(mCurrentTime).setValue(koleksi)

    // Intent ke Main Activity
    val goMain = Intent(
        this@CreateActivity, MainActivity::class.java
    )
    startActivity(goMain)
    finish()

    // Menghilangkan Loading
    mLoading.dismiss()

}
}