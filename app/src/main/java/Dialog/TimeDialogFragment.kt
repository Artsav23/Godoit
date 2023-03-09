package Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.godoit.databinding.FragmentTimeDialogBinding
import java.util.Calendar

class TimeDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentTimeDialogBinding
    private var calendarData: String = addCalendarData()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentTimeDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        addCalendarData()
        sendResult()
        changeCalendarDay()
        changeTime()
        return builder.create()
    }

    private fun addCalendarData(): String {
        val day = addZero(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        val month = addZero(Calendar.MONTH + 1)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return "$day.$month.$year"
    }

    private fun addZero(num: Int): String {
        return if (num.toString().length == 1) "0$num" else num.toString()

    }

    private fun changeTime() {
        binding.editTextTime.setOnClickListener {
             TimePickerDialog(context, { _, hour, minute ->
                val time = "${addZero(hour)}:${addZero(minute)}"
                binding.editTextTime.setText(time)
            }, 24, 60, true).show()
        }
    }

    private fun changeCalendarDay() {
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            calendarData = "${addZero(day)}.${addZero(month + 1)}.$year"
        }
    }

    private fun sendResult() {
        binding.sendDate.setOnClickListener {
            if (checkDate()) {
                parentFragmentManager.setFragmentResult(TAG,
                    Bundle().apply {
                        putString("calendarData", calendarData)
                        putString("time", binding.editTextTime.text.toString())
                    })
                dismiss()
            }
        }
    }

    private fun checkDate(): Boolean {
        val day = calendarData[0].toString() + calendarData[1]
        val month = calendarData[3].toString() + calendarData[4]
        val  year = calendarData[6].toString() + calendarData[7] + calendarData[8].toString() + calendarData[9]
        return (Calendar.getInstance().get(Calendar.YEAR) == year.toInt() && Calendar.MONTH+1 < month.toInt()) ||
                (Calendar.getInstance().get(Calendar.YEAR) < year.toInt())
                || (Calendar.getInstance().get(Calendar.YEAR) == year.toInt()  && Calendar.MONTH+1 == month.toInt() &&
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) <= day.toInt())

    }

    companion object {
        val TAG = TimeDialogFragment::class.java.name
    }
}