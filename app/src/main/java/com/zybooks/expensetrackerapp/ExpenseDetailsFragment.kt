
package com.zybooks.expensetrackerapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsFragment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_expense_details)

        val name = intent.getStringExtra("name")
        val amount = intent.getDoubleExtra("amount", 0.0)
        val date = intent.getStringExtra("date")

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val amountTextView =  findViewById<TextView>(R.id.amountTextView)
        val dateTextView =  findViewById<TextView>(R.id.dateTextView)

        nameTextView.text = "Expense Name: $name"
        amountTextView.text = "Expense Amount: $%.2f".format(amount)
        dateTextView.text = "Date of expense: $date"
    }
}
