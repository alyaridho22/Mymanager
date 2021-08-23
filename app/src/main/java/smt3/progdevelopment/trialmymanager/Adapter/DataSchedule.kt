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

class DataSchedule(var mContext: Context, var mData: ArrayList<Schedule>) :
    RecyclerView.Adapter<DataSchedule.ViewHolder>() {

        private lateinit var mLoading: ProgressDialog
        private lateinit var mDatabase : DatabaseReference
        private lateinit var myPreferences: mySharedPreference
        private lateinit var userId: String

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var mTanggal = v.findViewById<TextView>(R.id.sctanggal)
            var mJam = v.findViewById<TextView>(R.id.scjam)
            var mNama = v.findViewById<TextView>(R.id.scnama)
            var btnEdit = v.findViewById<Button>(R.id.edit)
            var btnDelete = v.findViewById<Button>(R.id.delete)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
            .inflate(R.layout.schedule, parent,false)

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

        holder.btnEdit.setOnClickListener {
            val goUpdate = Intent(mContext,EditSchedule::class.java)
                .putExtra("scheduleid", mData[position].id)
            (mContext as Activity).startActivity(goUpdate)
            (mContext as Activity).finish()
        }

        holder.btnDelete.setOnClickListener {
            mLoading.show()
            val delete = mDatabase.child(userId)
                .orderByChild("id").equalTo(mData[position].id)
            delete.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    mLoading.dismiss()
                    Toast.makeText(
                        mContext, "${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    mLoading.dismiss()
                    for (item in snapshot.children) {
                        item.ref.removeValue()
                    }

                    mData.removeAt(position)
                    notifyItemRemoved(position)
                    Toast.makeText(
                        mContext, "Deleted", Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}