package com.zybooks.expensetrackerapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {


    class Expense(val name: String, val amount: Double)

    private lateinit var nameInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView

    private val expenseList = ArrayList<Expense>()
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameInput = findViewById(R.id.nameInput)
        amountInput = findViewById(R.id.amountInput)
        addButton = findViewById(R.id.addButton)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ExpenseAdapter(expenseList)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val amountText = amountInput.text.toString()

            if (name.isEmpty() || amountText.isEmpty()) {
                Toast.makeText(this, "Please enter name and amount", Toast.LENGTH_SHORT).show()
            } else {
                val amount = amountText.toDoubleOrNull()
                if (amount == null) {
                    Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show()
                } else {
                    val newExpense = Expense(name, amount)
                    expenseList.add(newExpense)
                    adapter.notifyItemInserted(expenseList.size - 1)

                    nameInput.text.clear()
                    amountInput.text.clear()
                    Toast.makeText(this, "Expense Added", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
