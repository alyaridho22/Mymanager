package smt3.progdevelopment.trialmymanager.Create

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.Model.Schedule
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.databinding.ActivityUpdateScheduleBinding
import java.text.SimpleDateFormat
import java.util.*

class UpdateScheduleActivity : AppCompatActivity() {

    private lateinit var mUpdateScheduleBinding: ActivityUpdateScheduleBinding
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var myPreference: mySharedPreference
    private lateinit var userId: String
    private lateinit var scheduleId: String
    private lateinit var mLoading: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mUpdateScheduleBinding = ActivityUpdateScheduleBinding.inflate(layoutInflater)
        setContentView(mUpdateScheduleBinding.root)

        mLoading = ProgressDialog(this)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")
        
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Schedule")
        myPreference = mySharedPreference(this)
        userId = myPreference.getValue(Constants.USER_ID)!!

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        if(intent != null){
            scheduleId = intent.getStringExtra("id")!!
        }

        setSchedule()

        mUpdateScheduleBinding.btnUpdate.setOnClickListener {
            if(validate()){
                val mDate = mUpdateScheduleBinding.tvValueDate.text.toString()
                val mActivity = mUpdateScheduleBinding.etActivity.text.toString()
                val mTime = mUpdateScheduleBinding.tvValueTime.text.toString()
                val mDetail = mUpdateScheduleBinding.etDetail.text.toString()
                val mExpense = mUpdateScheduleBinding.etExpense.text.toString()

                updateSchedule(mDate, mActivity, mTime, mDetail, mExpense)
            }
        }


        val myCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            chooseDate(myCalendar)
        }

        mUpdateScheduleBinding.btnChooseDate.setOnClickListener {
            DatePickerDialog(
                this,
                datePickerDialog,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        mUpdateScheduleBinding.btnChooseTime.setOnClickListener {
            val startHour = myCalendar.get(Calendar.HOUR_OF_DAY)
            val startMinute = myCalendar.get(Calendar.MINUTE)

            TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    mUpdateScheduleBinding.tvValueTime.text = "$hourOfDay.$minute"
                }, startHour, startMinute, false
            ).show()
        }
        
    }

    private fun setSchedule() {
        mLoading.show()
        mDatabaseReference.child(userId).child(scheduleId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mLoading.dismiss()
                val schedule = snapshot.getValue(Schedule::class.java)

                with(mUpdateScheduleBinding){
                    tvValueDate.text = schedule!!.date
                    tvValueTime.text = schedule.time
                    etActivity.setText(schedule.activity)
                    etDetail.setText(schedule.detail)
                    etExpense.setText(schedule.expense)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                mLoading.dismiss()
                Toast.makeText(
                    this@UpdateScheduleActivity,
                    error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun updateSchedule(mDate: String, mActivity: String, mTime: String, mDetail: String, mExpense: String) {

        mLoading.show()

        val schedule = Schedule(scheduleId, mDate, mActivity, mTime, mDetail, mExpense)
        mDatabaseReference.child(userId).child(scheduleId).setValue(schedule)

        Toast.makeText(this, "Successfully update a schedule", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, ThisCreate::class.java))
        finish()
        mLoading.dismiss()

    }

    private fun validate(): Boolean{
        if(mUpdateScheduleBinding.etActivity.text.toString() == ""){
            mUpdateScheduleBinding.etActivity.error = "Please fill in the activity column first"
            mUpdateScheduleBinding.etActivity.requestFocus()
            return false
        }
        else if(mUpdateScheduleBinding.etDetail.text.toString() == ""){
            mUpdateScheduleBinding.etDetail.error = "Please fill in the detail column first"
            mUpdateScheduleBinding.etDetail.requestFocus()
            return false
        }
        else if(mUpdateScheduleBinding.etExpense.text.toString() == ""){
            mUpdateScheduleBinding.etExpense.error = "Please fill in the expense column first"
            mUpdateScheduleBinding.etExpense.requestFocus()
            return false
        }

        return true
    }

    private fun chooseDate(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        mUpdateScheduleBinding.tvValueDate.text = sdf.format(myCalendar.time)
    }
}