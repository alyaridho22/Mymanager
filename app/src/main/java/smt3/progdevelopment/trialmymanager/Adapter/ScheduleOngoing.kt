package smt3.progdevelopment.trialmymanager.Adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.Create.EditSchedule
import smt3.progdevelopment.trialmymanager.Model.Schedule
import smt3.progdevelopment.trialmymanager.Preference.mySharedPreference
import smt3.progdevelopment.trialmymanager.R
import java.util.ArrayList

class ScheduleOngoing(var mContext: Context, var mData: ArrayList<Schedule>) :
    RecyclerView.Adapter<DataSchedule.ViewHolder>() {

    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var userId: String

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mTanggal = v.findViewById<TextView>(R.id.ogtanggal)
        var mJam = v.findViewById<TextView>(R.id.ogjam)
        var mNama = v.findViewById<TextView>(R.id.ognama)
        var mExpense = v.findViewById<TextView>(R.id.ogexpense)
        var mDetail = v.findViewById<TextView>(R.id.ogdetail)
        var btnDone = v.findViewById<Button>(R.id.done)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.schedule, parent, false)

        mLoading = ProgressDialog(mContext)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading...")
        mDatabase = FirebaseDatabase.getInstance().getReference("Scedule")
        myPreferences = mySharedPreference(mContext)
        userId = myPreferences.setValue("id")

        return ViewHolder(view)

    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTanggal.text = mData[position].date
        holder.mJam.text = mData[position].time
        holder.mNama.text = mData[position].activity
        holder.mExpense.text = mData[position].expense
        holder.mDetail.text = mData[position].detail
    }
}

