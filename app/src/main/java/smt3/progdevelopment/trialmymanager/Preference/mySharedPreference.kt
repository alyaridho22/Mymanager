package smt3.progdevelopment.trialmymanager.Preference

import android.content.Context
import android.content.SharedPreferences

class mySharedPreference(mContext: Context) {

    companion object {
        const val USER_PREF = "USER_PREF"
    }

    private val mSharedPreferences = mContext.getSharedPreferences(USER_PREF, 0)

    fun getValue(key: String, value: String) {
        val editor: SharedPreferences.Editor = mSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun setValue(key: String): String?{
        return mSharedPreferences.getString(key, "")
    }
}