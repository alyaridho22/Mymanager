package smt3.progdevelopment.trialmymanager.Create

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.MainActivity
import smt3.progdevelopment.trialmymanager.Model.Schedule
import smt3.progdevelopment.trialmymanager.R

class EditSchedule : AppCompatActivity() {
    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: Preference
    private lateinit var userId: String
    private lateinit var koleksiId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_schedule)
        mLoading = ProgressDialog(this@EditSchedule)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")

        mDatabase = FirebaseDatabase.getInstance().getReference("Koleksi")
        myPreferences = Preference(this@EditSchedule)
        userId = myPreferences.getValue("id")!!

        if (intent != null){
            koleksiId = intent.getStringExtra("koleksiId")
        }

        setValue()

        btnUpdate.setOnClickListener {
            if (validate()){
                val mNama = etNama.text.toString()
                val mJumlah = etJumlah.text.toString().toInt()
                create(mNama, mJumlah)
            }
        }

    }


    private fun setValue() {
        mLoading.show()
        mDatabase.child(userId).child(koleksiId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mLoading.dismiss()
                    Toast.makeText(
                        this@EditSchedule,
                        "${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    mLoading.dismiss()
                    val koleksi = snapshot.getValue(Schedule::class.java)
                    etNama.setText(koleki!!.nama)
                    etJumlah.setText(koleksi.jumlah.toString())
                }
            })
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
        val koleksi = Schedule(koleksiId, mNama, mJumlah)
        mDatabase.child(userId).child(koleksiId).setValue(koleksi)
        val goMain = Intent(this@EditSchedule, MainActivity::class.java)
        startActivity(goMain)
        finish()
        mLoading.dismiss()
    }
}