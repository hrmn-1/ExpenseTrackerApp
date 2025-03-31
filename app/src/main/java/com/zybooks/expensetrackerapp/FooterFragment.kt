package com.zybooks.expensetrackerapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FooterFragment : Fragment() {

    private lateinit var totalExpenseText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_footer, container, false)
        totalExpenseText = view.findViewById(R.id.totalAmountTextView)
        return view
    }

    fun updateTotalAmount(total: Double) {
        totalExpenseText.text = "Total Expense: $$total"
    }
}