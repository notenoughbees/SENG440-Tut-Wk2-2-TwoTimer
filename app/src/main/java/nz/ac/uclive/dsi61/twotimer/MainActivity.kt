package nz.ac.uclive.dsi61.twotimer

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

class MainActivity : Activity() {
    // instance variables
    private lateinit var time1: TextView // lateinit modifier lets us initialise these variables later
    private lateinit var time2: TextView
    private lateinit var picker1: Spinner
    private lateinit var picker2: Spinner

    private lateinit var handler: Handler
    private lateinit var updateTask: Runnable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        time1 = findViewById(R.id.time1)
        picker1 = findViewById(R.id.picker1)
        time2 = findViewById(R.id.time2)
        picker2 = findViewById(R.id.picker2)


        // task
        handler = Handler(Looper.getMainLooper())

        updateTask = Runnable {
            syncTimes()
            handler.postDelayed(updateTask, 1000)
        }


        // populate the spinners
        val timeZones = TimeZone.getAvailableIDs()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, timeZones)
        picker1.adapter = adapter
        picker2.adapter = adapter

        // spinner listener: two callback functions
        val spinnerListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                syncTimes()
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                syncTimes()
            }
        }

        picker1.onItemSelectedListener = spinnerListener
        picker2.onItemSelectedListener = spinnerListener

        // for testing, we can pre-select values for the spinners
        picker1.setSelection(timeZones.indexOf("Pacific/Auckland"))
        picker2.setSelection(timeZones.indexOf("America/Chicago"))
    }


    override fun onStart() {
        super.onStart()
        handler.post(updateTask)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateTask)
    }


    private fun syncTimes() {
        Log.d("FOO", "Updating!") // in Logcat tab

        val formatter = SimpleDateFormat("d MMMM HH:mm")
        val today = Calendar.getInstance()

        var timeZone = TimeZone.getTimeZone(picker1.selectedItem.toString())
        formatter .timeZone = timeZone
        time1.text = formatter.format(today.time)

        timeZone = TimeZone.getTimeZone(picker2.selectedItem.toString())
        formatter.timeZone = timeZone
        time2.text = formatter.format(today.time)
    }

}