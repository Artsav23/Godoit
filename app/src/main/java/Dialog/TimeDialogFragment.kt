package Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.example.godoit.CreateTask
import com.example.godoit.databinding.FragmentTimeDialogBinding
import java.util.Calendar

class TimeDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentTimeDialogBinding
    private var calendarData: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentTimeDialogBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        sendResult()
        changeCalendarDay()
        changeTime()
        return builder.create()
    }

    private fun changeTime() {
        binding.editTextTime.setOnClickListener {
             TimePickerDialog(context, { timePicker, i, i2 ->
                val time = "$i:$i2"
                binding.editTextTime.setText(time)
            }, 24, 60, true).show()
        }
    }

    private fun changeCalendarDay() {
        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            calendarData = "$day.${month + 1}.$year"
        }
    }

    private fun sendResult() {
        binding.sendDate.setOnClickListener {
            val bundle = Bundle().apply {
                putString("calendarData", calendarData)
                putString("time", binding.editTextTime.text.toString())
            }
            dialog
            dialog?.dismiss()
        }
    }

    companion object {
        val TAG = TimeDialogFragment::class.java.name
    }
}