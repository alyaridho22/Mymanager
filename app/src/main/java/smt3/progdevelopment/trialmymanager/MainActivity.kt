package smt3.progdevelopment.trialmymanager

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import smt3.progdevelopment.trialmymanager.Preference.mySharedPreference
import smt3.progdevelopment.trialmymanager.Create.ThisCreate
import smt3.progdevelopment.trialmymanager.Ongoing.OngoingPage
import smt3.progdevelopment.trialmymanager.Profile.UserProfile
import smt3.progdevelopment.trialmymanager.Report.ReportPage

class MainActivity : AppCompatActivity() {
    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val namauser = findViewById<TextView>(R.id.datauser)
        val keCreate = findViewById<ImageButton>(R.id.toplannerbt)
        val keOngoing = findViewById<ImageButton>(R.id.toongoingbt)
        val keReport = findViewById<ImageButton>(R.id.toreportbt)

        mLoading = ProgressDialog(this@MainActivity)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        myPreferences = mySharedPreference(this@MainActivity)
        userId = myPreferences.getValue("id")!!

//        Mengambil data dari shared preferences
        namauser.text = myPreferences.getValue("username")

        keCreate.setOnClickListener {
            val goCreate = Intent(
                this@MainActivity, ThisCreate::class.java
            )
            startActivity(goCreate)
            finish()

        }

        keOngoing.setOnClickListener {
            val goOngoing = Intent(
                this@MainActivity, OngoingPage::class.java
            )
            startActivity(goOngoing)
            finish()
        }

        keReport.setOnClickListener {
            val goReport = Intent(
                this@MainActivity, ReportPage::class.java
            )
            startActivity(goReport)
            finish()
        }

        namauser.setOnClickListener {
            val goReport = Intent(
                this@MainActivity, UserProfile::class.java
            )
            startActivity(goReport)
            finish()
        }


            }
        }
