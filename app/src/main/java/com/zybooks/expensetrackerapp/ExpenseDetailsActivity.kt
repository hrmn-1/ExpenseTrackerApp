package com.zybooks.expensetrackerapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val name = intent.getStringExtra("name")
        val amount = intent.getDoubleExtra("amount", 0.0)
        val date = intent.getStringExtra("date")

        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val amountTextView: TextView = findViewById(R.id.amountTextView)
        val dateTextView: TextView = findViewById(R.id.dateTextView)

        nameTextView.text = "Expense Name: $name"
        amountTextView.text = "Expense Amount: $%.2f".format(amount)
        dateTextView.text = "Date of expense: $date"
    }
}
