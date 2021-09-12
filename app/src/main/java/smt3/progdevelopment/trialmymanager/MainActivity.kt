package smt3.progdevelopment.trialmymanager

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.Create.ThisCreate
import smt3.progdevelopment.trialmymanager.Ongoing.OngoingPage
import smt3.progdevelopment.trialmymanager.Profile.UserProfile
import smt3.progdevelopment.trialmymanager.Report.ReportPage
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var userId: String
    private lateinit var mMainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mMainBinding.root)

//        val namauser = findViewById<TextView>(R.id.datauser)
//        val keCreate = findViewById<ImageButton>(R.id.toplannerbt)
//        val keOngoing = findViewById<ImageButton>(R.id.toongoingbt)
//        val keReport = findViewById<ImageButton>(R.id.toreportbt)

        mLoading = ProgressDialog(this@MainActivity)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")

        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        myPreferences = mySharedPreference(this@MainActivity)
        userId = myPreferences.getValue(Constants.USER_ID)!!

//        Mengambil data dari shared preferences
        mMainBinding.tvUser.text = myPreferences.getValue(Constants.USER_NAME)
        //namauser.text = myPreferences.getValue("username")

        mMainBinding.btnCreate.setOnClickListener {
            val goCreate = Intent(
                this@MainActivity, ThisCreate::class.java
            )
            startActivity(goCreate)
            finish()
        }

        mMainBinding.btnOnGoing.setOnClickListener {
            val goOngoing = Intent(
                this@MainActivity, OngoingPage::class.java
            )
            startActivity(goOngoing)
//            finish()
        }

        mMainBinding.btnReport.setOnClickListener {
            val goReport = Intent(
                this@MainActivity, ReportPage::class.java
            )
            startActivity(goReport)
//            finish()
        }

        mMainBinding.tvUser.setOnClickListener {
            val goReport = Intent(
                this@MainActivity, UserProfile::class.java
            )
            startActivity(goReport)
//            finish()
        }
    }
}
