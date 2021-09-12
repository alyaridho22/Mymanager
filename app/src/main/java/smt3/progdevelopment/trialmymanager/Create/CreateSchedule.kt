package smt3.progdevelopment.trialmymanager.Create

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import smt3.progdevelopment.trialmymanager.Model.Schedule
import smt3.progdevelopment.trialmymanager.R
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference
import smt3.progdevelopment.trialmymanager.databinding.ActivityCreateScheduleBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateSchedule : AppCompatActivity() {

    private lateinit var mCreateBinding: ActivityCreateScheduleBinding
    private lateinit var myPreference: mySharedPreference
    private lateinit var userId: String
    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabaseReference: DatabaseReference

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCreateBinding = ActivityCreateScheduleBinding.inflate(layoutInflater)
        setContentView(mCreateBinding.root)

        mLoading = ProgressDialog(this)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading...")

        myPreference = mySharedPreference(this)
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Schedule")
        userId = myPreference.getValue(Constants.USER_ID)!!

        mCreateBinding.btnSave.setOnClickListener {
            if(validate()){
                val mDate = mCreateBinding.tvValueDate.text.toString()
                val mActivity = mCreateBinding.etActivity.text.toString()
                val mTime = mCreateBinding.tvValueTime.text.toString()
                val mDetail = mCreateBinding.etDetail.text.toString()
                val mExpense = mCreateBinding.etExpense.text.toString()

                createSchedule(mDate, mActivity, mTime, mDetail, mExpense)
            }
        }


        val myCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            chooseDate(myCalendar)
        }

        mCreateBinding.btnChooseDate.setOnClickListener {
            DatePickerDialog(
                this,
                datePickerDialog,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        mCreateBinding.btnChooseTime.setOnClickListener {
            val startHour = myCalendar.get(Calendar.HOUR_OF_DAY)
            val startMinute = myCalendar.get(Calendar.MINUTE)

            TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    mCreateBinding.tvValueTime.text = "$hourOfDay.$minute"
                }, startHour, startMinute, false
            ).show()
        }
    }

    private fun createSchedule(mDate: String, mActivity: String, mTime: String, mDetail: String, mExpense: String) {

        mLoading.show()
        val mCurrentTime = SimpleDateFormat("yyyyMMdd:HHmmss", Locale.getDefault()).format(Date())

        val schedule = Schedule(mCurrentTime, mDate, mActivity, mTime, mDetail, mExpense)
        mDatabaseReference.child(userId).child(mCurrentTime).setValue(schedule)

        Toast.makeText(this, "Successfully made a schedule", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, ThisCreate::class.java))
        finish()

    }

    private fun validate(): Boolean{
        if(mCreateBinding.etActivity.text.toString() == ""){
            mCreateBinding.etActivity.error = "Please fill in the activity column first"
            mCreateBinding.etActivity.requestFocus()
            return false
        }
        else if(mCreateBinding.etDetail.text.toString() == ""){
            mCreateBinding.etDetail.error = "Please fill in the detail column first"
            mCreateBinding.etDetail.requestFocus()
            return false
        }
        else if(mCreateBinding.etExpense.text.toString() == ""){
            mCreateBinding.etExpense.error = "Please fill in the expense column first"
            mCreateBinding.etExpense.requestFocus()
            return false
        }

        return true
    }

    private fun chooseDate(myCalendar: Calendar) {
        val myFormat = "dd-MM-yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        mCreateBinding.tvValueDate.text = sdf.format(myCalendar.time)
    }
}