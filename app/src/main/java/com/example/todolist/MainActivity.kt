package com.example.todolist

import java.text.SimpleDateFormat
import java.util.*

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.todolist.constant.constant
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>
//    private lateinit var launcherForChange: ActivityResultLauncher<Intent>
    private lateinit var todoListAdapter: TodoListAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        todoListAdapter = TodoListAdapter(this)
        binding.todoListView.adapter = todoListAdapter



        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val headerDataText = result.data?.getStringExtra(constant.ADD_TODO_HEADER_COD).toString()
                val bodyDataText = result.data?.getStringExtra(constant.ADD_TODO_BODY_COD).toString()
                val previewDataText = bodyDataText?.substring(0, minOf(bodyDataText.length, 15)).toString()
                val flagActionChange = result.data?.getBooleanExtra(constant.FLAG_ACTION_CHANGE, false)
                val positionChange = result.data?.getIntExtra(constant.CHANGE_POSITION_TODO, -1) ?: -1


                Log.d("MyLog", "Получил данные - $headerDataText, $previewDataText, $bodyDataText")

                val currentDateTime = Date()
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(currentDateTime)
                val formattedDateTime = formatter.format(currentDateTime)


                val todoItems = TodoItem(headerDataText, previewDataText, bodyDataText, formattedDateTime)

                if (flagActionChange == false) {
                    todoListAdapter.addItem(todoItems)
                }
                else if (flagActionChange == true) {
                    todoListAdapter.updateItem(positionChange, todoItems)
                }
            }
        }

//        launcherForChange = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK) {
//
//            }
//        }


        todoListAdapter.setOnItemClickListener { todoItem ->
            val intent = Intent(this, AddTodoActivity::class.java)

            //получение айди заметки
            val position = todoListAdapter.getPosition(todoItem)
            Log.d("MyLog", "${position::class.java}")

            intent.putExtra(constant.ADD_TODO_HEADER_COD, todoItem.header)
            intent.putExtra(constant.ADD_TODO_BODY_COD, todoItem.text)

            intent.putExtra(constant.FLAG_ACTION_CHANGE, true)
            intent.putExtra(constant.CHANGE_POSITION_TODO, position)
            intent.putExtra(constant.DATE_STRING, todoItem.dateChange)

            launcher.launch(intent)

//            launcherForChange.launch(intent) // Отправка данных и открытие активити для изменения
        }



        binding.addButton.setOnClickListener {
            launcher.launch(Intent(this, AddTodoActivity::class.java))
        }



    }
}