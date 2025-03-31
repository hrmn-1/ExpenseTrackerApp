package com.zybooks.expensetrackerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ExpenseDetailsFragment : Fragment() {

    override fun onCreateView(inflator: LayoutInflater, container:ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.fragment_expense_details, container, false )

        val expenseName = arguments?.getString("expenseName")
        val expenseAmount = arguments?.getString("expenseAmount")
        val expenseDate = arguments?.getString("expenseDate")

        view.findViewById<TextView>(R.id.nameTextView).text = "Expense Name: $expenseName"
        view.findViewById<TextView>(R.id.amountTextView).text = "Expense Amount: $expenseAmount"
        view.findViewById<TextView>(R.id.dateTextView).text = "Date of expense: $expenseDate"

        return view
    }
}