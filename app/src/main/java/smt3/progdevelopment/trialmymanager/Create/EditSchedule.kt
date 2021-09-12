package smt3.progdevelopment.trialmymanager.Create

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.R

class EditSchedule : AppCompatActivity() {
    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var userId: String
    private lateinit var ScheduleId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_schedule)
        mLoading = ProgressDialog(this@EditSchedule)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")

        mDatabase = FirebaseDatabase.getInstance().getReference("Schedule")
        myPreferences = mySharedPreference(this@EditSchedule)
        userId = myPreferences.getValue("id")!!

//        if (intent != null) {
//            ScheduleId = intent.getStringExtra("Schedule")
//        }


    }
}