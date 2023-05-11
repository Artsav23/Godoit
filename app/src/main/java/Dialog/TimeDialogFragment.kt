package Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.godoit.databinding.FragmentTimeDialogBinding
import java.text.SimpleDateFormat
import java.util.*

class TimeDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentTimeDialogBinding
    private lateinit var alarmData: Calendar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentTimeDialogBinding.inflate(LayoutInflater.from(context))
        alarmData = Calendar.getInstance().apply {
            set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
            set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

        }

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        sendResult()
        changeCalendarDay()
        changeTime()
        return builder.create()
    }

    private fun changeTime() {
        binding.editTextTime.setOnClickListener {
             TimePickerDialog(context, { _, hour, minute ->
                 alarmData.set(Calendar.HOUR_OF_DAY, hour)
                 alarmData.set(Calendar.MINUTE, minute)
                binding.editTextTime.setText(SimpleDateFormat("HH:mm").format(alarmData.timeInMillis))
            }, 24, 60, true).show()
        }
    }

    private fun changeCalendarDay() {
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            alarmData.set(Calendar.YEAR, year)
            alarmData.set(Calendar.MONTH, month)
            alarmData.set(Calendar.DAY_OF_MONTH, day)
        }
    }

    private fun sendResult() {
        binding.sendDate.setOnClickListener {
            if (checkDate() || checkTime()) {
                parentFragmentManager.setFragmentResult(TAG,
                    Bundle().apply {
                        putInt("year", alarmData.get(Calendar.YEAR))
                        putInt("month", alarmData.get(Calendar.MONTH))
                        putInt("day", alarmData.get(Calendar.DAY_OF_MONTH))
                        putInt("hour", alarmData.get(Calendar.HOUR_OF_DAY))
                        putInt("minute", alarmData.get(Calendar.MINUTE))
                    })
                Log.d("my_log", alarmData.time.toString())
                dismiss()
            }
            else {
                Toast.makeText(context, "День либо время указаны некорректно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkTime(): Boolean {
        val minute = alarmData.get(Calendar.MINUTE)
        val hour = alarmData.get(Calendar.HOUR_OF_DAY)
        val day = alarmData.get(Calendar.DAY_OF_MONTH)
        val month = alarmData.get(Calendar.MONTH)
        val  year = alarmData.get(Calendar.YEAR)
        return (day == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && month == Calendar.getInstance().get(Calendar.MONTH) &&
                Calendar.getInstance().get(Calendar.YEAR) == year) &&
                ((hour == Calendar.getInstance().get(Calendar.HOUR_OF_DAY) &&
                        minute > Calendar.getInstance().get(Calendar.MINUTE)) || (hour > Calendar.getInstance().get(Calendar.HOUR_OF_DAY)))
    }

    private fun checkDate(): Boolean {
        val day = alarmData.get(Calendar.DAY_OF_MONTH)
        val month = alarmData.get(Calendar.MONTH)
        val  year = alarmData.get(Calendar.YEAR)
        return (Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) < month) ||
                (Calendar.getInstance().get(Calendar.YEAR) < year) ||
                (Calendar.getInstance().get(Calendar.YEAR) == year && Calendar.getInstance().get(Calendar.MONTH) == month &&
                        day > Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
    }

    companion object {
        val TAG = TimeDialogFragment::class.java.name
    }
}