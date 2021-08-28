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



}
}