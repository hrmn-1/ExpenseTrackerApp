package com.zybooks.expensetrackerapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListFragment : Fragment() {




    private lateinit var nameInput: EditText
    private lateinit var amountInput: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tipsButton: Button
    private lateinit var footerFragment: FooterFragment

    private val expenseList = ArrayList<Expense>()
    private lateinit var adapter: ExpenseAdapter
    private val fileName = "expenses.json"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        nameInput = view.findViewById(R.id.nameInput)
        amountInput = view.findViewById(R.id.amountInput)
        addButton = view.findViewById(R.id.addButton)
        recyclerView = view.findViewById(R.id.recyclerView)
        tipsButton = view.findViewById(R.id.tipsButton)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ExpenseAdapter(expenseList) { expense ->

            Toast.makeText(requireContext(), "Details: ${expense.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter
        loadExpenses()

        // Open the financial tips website when clicked
        tipsButton.setOnClickListener {
            val url = "https://www.financialtips.com"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            startActivity(intent)
        }

        // Initialize footer fragment
        footerFragment = childFragmentManager.findFragmentById(R.id.footerFragment) as? FooterFragment
            ?: FooterFragment()

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.footerContainer, footerFragment)
        transaction.commit()

        // Add expense when the button is clicked
        addButton.setOnClickListener {
            addExpense()
        }
    }

    private fun addExpense() {
        val name = nameInput.text.toString()
        val amountText = amountInput.text.toString()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (name.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter name and amount", Toast.LENGTH_SHORT).show()
        } else {
            val amount = amountText.toDoubleOrNull()
            if (amount == null) {
                Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show()
            } else {
                val newExpense = Expense(name, amount, currentDate)
                expenseList.add(newExpense)
                adapter.notifyItemInserted(expenseList.size - 1)

                // Save the updated expenses to file
                saveExpenses()

                // Update the total amount on the footer fragment
                val totalAmount = expenseList.sumOf { it.amount }
                footerFragment.updateTotalAmount(totalAmount)

                // Clear the input fields
                nameInput.text.clear()
                amountInput.text.clear()
                Toast.makeText(requireContext(), "Expense Added", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveExpenses() {
        val jsonArray = JSONArray()
        for (expense in expenseList) {
            val jsonObject = JSONObject().apply {
                put("name", expense.name)
                put("amount", expense.amount)
                put("date", expense.date)
            }
            jsonArray.put(jsonObject)
        }

        try {
            requireContext().openFileOutput(fileName, android.content.Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(jsonArray.toString().toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadExpenses() {
        try {
            requireContext().openFileInput(fileName).use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val jsonString = reader.readText()
                val jsonArray = JSONArray(jsonString)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    expenseList.add(
                        Expense(
                            jsonObject.getString("name"),
                            jsonObject.getDouble("amount"),
                            jsonObject.getString("date")
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
        } catch (e: FileNotFoundException) {
            // File does not exist yet
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Lifecycle Methods
    override fun onStart() {
        super.onStart()
        Log.d("FragmentLifecycle", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("FragmentLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("FragmentLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("FragmentLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FragmentLifecycle", "onDestroy called")
    }
}