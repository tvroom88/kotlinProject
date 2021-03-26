package com.kotlinapplications.Bmi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlinapplications.R
import kotlinx.android.synthetic.main.activity_bmi.*
import org.jetbrains.anko.startActivity

class BmiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        loadData()

        resultButton.setOnClickListener {

            saveData(
                heigthEditText.text.toString().toInt(),
                weightEditText.text.toString().toInt()
            )


//            val intent = Intent(this, ResultActivity::class.java)
//            intent.putExtra("weight", weightEditText.text.toString())
//            intent.putExtra("height", heigthEditText.text.toString())
//            startActivity(intent)
            startActivity<ResultActivity>(
                "weight" to weightEditText.text.toString(),
                "height" to heigthEditText.text.toString()
            )
        }
    }


    private fun saveData(height: Int, weight: Int) {
        val pref = getSharedPreferences(
            "abc", Context.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putInt("KEY_HEIGHT", height)
        editor.putInt("KEY_WEIGHT", weight)
            .apply()
    }

    private fun loadData() {
        val pref = getSharedPreferences(
            "abc", Context.MODE_PRIVATE
        )
        val height = pref.getInt("KEY_HEIGHT", 0)
        val weight = pref.getInt("KEY_WEIGHT", 0)

        if (height != 0 && weight != 0) {
            heigthEditText.setText(height.toString())
            weightEditText.setText(weight.toString())
        }
    }
}
