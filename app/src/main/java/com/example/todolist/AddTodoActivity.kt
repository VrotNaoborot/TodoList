package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import com.example.todolist.constant.constant
import com.example.todolist.databinding.ActivityAddTodoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTodoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTodoBinding
    var actionChange: Boolean = false
    var positionForChange = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val header_ = intent.getStringExtra(constant.ADD_TODO_HEADER_COD)
        val body_ = intent.getStringExtra(constant.ADD_TODO_BODY_COD)
        val date_ = intent.getStringExtra(constant.DATE_STRING)

        actionChange = intent.getBooleanExtra(constant.FLAG_ACTION_CHANGE, false)
        positionForChange = intent.getIntExtra(constant.CHANGE_POSITION_TODO, -1)

        Log.d("MyLog", "Получил данные $header_ $body_")
        Log.d("MyLog", "Action - $actionChange")
        Log.d("MyLog", "date - $date_")

        binding.tvDate.text = Editable.Factory.getInstance().newEditable(date_.toString())

        // если активити открывается в качестве просмотра имеющей закладки, то
        if (actionChange == true) {
            binding.edHeader.text = Editable.Factory.getInstance().newEditable(header_.toString())
            binding.edBody.text = Editable.Factory.getInstance().newEditable(body_.toString())
        }
        else {
            val currentDateTime = Date()
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(currentDateTime)
            val formattedDateTime = formatter.format(currentDateTime)

            binding.tvDate.text = formattedDateTime
        }



    }

    fun setOnClickAdd(view: View) {
        val headersText = binding.edHeader.text.toString()
        val bodysText = binding.edBody.text.toString()

        Log.d("MyLog", "данные - $headersText, $bodysText")

        val i = Intent()
        i.putExtra(constant.ADD_TODO_HEADER_COD, headersText)
        i.putExtra(constant.ADD_TODO_BODY_COD, bodysText)

        if (actionChange == false) {
            i.putExtra(constant.DATE_STRING, binding.tvDate.text.toString())
            setResult(RESULT_OK, i)
            finish()
        }
        else {
            i.putExtra(constant.FLAG_ACTION_CHANGE, true)
            i.putExtra(constant.CHANGE_POSITION_TODO, positionForChange)
            setResult(RESULT_OK, i)
            finish()
        }
    }

    fun setOnClickBack(view: View) {
        finish()
    }
}