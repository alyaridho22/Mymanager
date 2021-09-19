package smt3.progdevelopment.trialmymanager.Adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.database.*
import smt3.progdevelopment.trialmymanager.Create.UpdateScheduleActivity
import smt3.progdevelopment.trialmymanager.Model.Schedule
import smt3.progdevelopment.trialmymanager.R
import smt3.progdevelopment.trialmymanager.Utils.Constants
import smt3.progdevelopment.trialmymanager.Utils.mySharedPreference


class DataScheduleAdapter(var mContext: Context, var mData: ArrayList<Schedule>): RecyclerView.Adapter<DataScheduleAdapter.ViewHolder>(){

    private lateinit var mLoading: ProgressDialog
    private lateinit var mDatabase: DatabaseReference
    private lateinit var myPreferences: mySharedPreference
    private lateinit var userId: String

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var mDate = v.findViewById<MaterialTextView>(R.id.tv_date)
        var mTime = v.findViewById<MaterialTextView>(R.id.tv_time)
        var mEventName = v.findViewById<MaterialTextView>(R.id.tv_event_name)
        var btnUpdate = v.findViewById<MaterialButton>(R.id.btn_edit)
        var btnDelete = v.findViewById<MaterialButton>(R.id.btn_delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.schedule, parent, false)

        //Initialize variable
        mLoading = ProgressDialog(mContext)
        mLoading.setCancelable(false)
        mLoading.setMessage("Loading ...")
        mDatabase = FirebaseDatabase.getInstance().getReference("Schedule")
        myPreferences = mySharedPreference(mContext)
        userId = myPreferences.getValue(Constants.USER_ID)!!

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mDate.text = mData[position].date
        holder.mTime.text = mData[position].time
        holder.mEventName.text = mData[position].activity

        holder.btnUpdate.setOnClickListener {
            (mContext as Activity).startActivity(Intent(mContext, UpdateScheduleActivity::class.java).apply {
                putExtra("id", mData[position].id)
            })

//            (mContext as Activity).finish()
        }

        holder.btnDelete.setOnClickListener {
            mLoading.show()
            val delete = mDatabase.child(userId)
                .orderByChild("id")
                .equalTo(mData[position].id)

            delete.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mLoading.dismiss()
                    Toast.makeText(
                        mContext,
                        error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    mLoading.dismiss()
                    for (item in snapshot.children) {
                        item.ref.removeValue()
                    }
                    //Refresh data
                    mData.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(
                        mContext,
                        "A schedule has been successfully removed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}


//class DataSchedule(var mContext: Context, var mData: ArrayList<Schedule>) :
//    RecyclerView.Adapter<DataSchedule.ViewHolder>() {
//
//        private lateinit var mLoading: ProgressDialog
//        private lateinit var mDatabase : DatabaseReference
//        private lateinit var myPreferences: mySharedPreference
//        private lateinit var userId: String
//
//        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//            var mTanggal = v.findViewById<TextView>(R.id.sctanggal)
//            var mJam = v.findViewById<TextView>(R.id.scjam)
//            var mNama = v.findViewById<TextView>(R.id.scnama)
//            var btnEdit = v.findViewById<Button>(R.id.edit)
//            var btnDelete = v.findViewById<Button>(R.id.delete)
//        }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(mContext)
//            .inflate(R.layout.schedule, parent,false)
//
//        mLoading = ProgressDialog(mContext)
//        mLoading.setCancelable(false)
//        mLoading.setMessage("Loading...")
//        mDatabase = FirebaseDatabase.getInstance().getReference("Scedule")
//        myPreferences = mySharedPreference(mContext)
//        userId = myPreferences.setValue("id")!!
//
//        return ViewHolder(view)
//
//    }
//
//    override fun getItemCount(): Int = mData.size
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.mTanggal.text = mData[position].date
//        holder.mJam.text = mData[position].time
//        holder.mNama.text = mData[position].activity
//
//        holder.btnEdit.setOnClickListener {
//            val goUpdate = Intent(mContext,EditSchedule::class.java)
//                .putExtra("scheduleid", mData[position].id)
//            (mContext as Activity).startActivity(goUpdate)
//            (mContext as Activity).finish()
//        }
//
//        holder.btnDelete.setOnClickListener {
//            mLoading.show()
//            val delete = mDatabase.child(userId)
//                .orderByChild("id").equalTo(mData[position].id)
//            delete.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) {
//                    mLoading.dismiss()
//                    Toast.makeText(
//                        mContext, "${error.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    mLoading.dismiss()
//                    for (item in snapshot.children) {
//                        item.ref.removeValue()
//                    }
//
//                    mData.removeAt(position)
//                    notifyItemRemoved(position)
//                    Toast.makeText(
//                        mContext, "Deleted", Toast.LENGTH_SHORT
//                    ).show()
//                }
//            })
//        }
//    }
//}