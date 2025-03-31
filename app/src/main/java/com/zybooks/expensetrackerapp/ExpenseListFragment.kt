package com.zybooks.expensetrackerapp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class ExpenseListFragment : Fragment() {
    private val expenseList = mutableListOf<Expense>()
    private lateinit var adapter: ExpenseAdapter
    private var footerFragment: FooterFragment? = null
    private val expensesFileName = "expenses.json"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val expenseNameInput = view.findViewById<EditText>(R.id.nameInput)
        val expenseAmountInput = view.findViewById<EditText>(R.id.amountInput)
        val addExpenseButton = view.findViewById<Button>(R.id.addButton)
        val financialTipsButton = view.findViewById<Button>(R.id.tipsButton)
        val expenseRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        adapter = ExpenseAdapter(expenseList, { position -> showExpenseDetails(position) }, { position -> deleteExpense(position) })
        expenseRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        expenseRecyclerView.adapter = adapter

        loadExpensesFromStorage()
        updateTotalAmount()

        footerFragment = childFragmentManager.findFragmentById(R.id.footerFragment) as? FooterFragment

        addExpenseButton.setOnClickListener {
            val name = expenseNameInput.text.toString().trim()
            val amount = expenseAmountInput.text.toString().trim().toDoubleOrNull()
            val date = getCurrentDate()// Method to fetch current date

            if (name.isEmpty() || amount == null) {
                Toast.makeText(requireContext(), "Please enter valid Name and Amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newExpense = Expense(name, amount, date)
            addExpenseToList(newExpense)

            // Clear input fields after adding
            expenseNameInput.text.clear()
            expenseAmountInput.text.clear()
        }

        financialTipsButton.setOnClickListener {
            openFinancialTipsWebsite()
        }
    }

    private fun showExpenseDetails(position: Int) {
        val expense = expenseList[position]
        val bundle = Bundle().apply {
            putString("expenseName", expense.name)
            putString("expenseAmount", expense.amount.toString())
            putString("expenseDate", expense.date)
        }
        findNavController().navigate(R.id.expenseDetailsFragment, bundle)
    }

    private fun deleteExpense(position: Int) {
        val removedExpense = expenseList[position]
        Toast.makeText(requireContext(), "Deleted: ${removedExpense.name}", Toast.LENGTH_SHORT).show()
        expenseList.removeAt(position)
        adapter.notifyDataSetChanged()
        saveExpensesToStorage()
        updateTotalAmount()
    }

    private fun addExpenseToList(expense: Expense) {
        expenseList.add(expense)
        adapter.notifyDataSetChanged()
        saveExpensesToStorage()
        updateTotalAmount()
    }

    private fun saveExpensesToStorage() {
        val jsonArray = JSONArray()
        expenseList.forEach { expense ->
            val obj = JSONObject()
            obj.put("name", expense.name)
            obj.put("amount", expense.amount)
            obj.put("date", expense.date)
            jsonArray.put(obj)
        }
        val jsonString = jsonArray.toString()

        try {
            requireContext().openFileOutput(expensesFileName, Context.MODE_PRIVATE).use {
                it.write(jsonString.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadExpensesFromStorage() {
        val file = File(requireContext().filesDir, expensesFileName)
        if (!file.exists()) return
        expenseList.clear()

        try {
            val inputStream = FileInputStream(file)
            val reader = InputStreamReader(inputStream)
            val content = reader.readText()
            reader.close()

            val jsonArray = JSONArray(content)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val name = obj.getString("name")
                val amount = obj.getDouble("amount")
                val date = obj.getString("date")
                expenseList.add(Expense(name, amount, date))
            }
            adapter.notifyDataSetChanged()
            updateTotalAmount()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateTotalAmount() {
        val total = expenseList.sumOf { it.amount }
        footerFragment?.updateTotalAmount(total)
    }

    private fun getCurrentDate(): String {
        return "2025-03-30"
    }



    private fun openFinancialTipsWebsite() {
        val browserIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.financialtips.com"))
        startActivity(browserIntent)
    }
}
