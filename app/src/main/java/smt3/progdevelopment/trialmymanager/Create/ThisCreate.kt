package smt3.progdevelopment.trialmymanager.Create

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.Adapter.DataScheduleAdapter
import smt3.progdevelopment.trialmymanager.Model.Schedule
import smt3.progdevelopment.trialmymanager.R
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.databinding.ActivityThisCreateBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences
import kotlin.collections.ArrayList

class ThisCreate : AppCompatActivity() {

    private lateinit var mLoading: ProgressDialog
    private lateinit var mThisCreateBinding: ActivityThisCreateBinding
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreference: mySharedPreference
    private lateinit var userId: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mThisCreateBinding = ActivityThisCreateBinding.inflate(layoutInflater)
        setContentView(mThisCreateBinding.root)
        // Initialize variable
        mLoading = ProgressDialog(this@ThisCreate)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")

        myPreference = mySharedPreference(this@ThisCreate)
        userId = myPreference.getValue(Constants.USER_ID)!!


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getDataSchedule()

        mThisCreateBinding.btnCreate.setOnClickListener {
            startActivity(
                Intent(this, CreateSchedule::class.java).putExtra(
                    Constants.USER_ID,
                    userId
                )
            )
        }
    }

    private fun getDataSchedule() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Schedule")

        mDatabase.child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val addSchedule = ArrayList<Schedule>()
                    for (item in snapshot.children) {
                        val listSchedule = item.getValue(Schedule::class.java)
                        addSchedule.add(listSchedule!!)
                    }


                    with(mThisCreateBinding.rvList) {
                        layoutManager = LinearLayoutManager(this@ThisCreate)
                        setHasFixedSize(true)
                        adapter = DataScheduleAdapter(this@ThisCreate, addSchedule)
                    }

                } else {
                    Toast.makeText(this@ThisCreate, "No schedule list", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ThisCreate, "{${error.message}}", Toast.LENGTH_SHORT).show()
            }

        })
    }
}