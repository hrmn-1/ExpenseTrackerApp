package com.zybooks.expensetrackerapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.createSavedStateHandle
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val expenseList: ArrayList<MainActivity.Expense>,

) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView = itemView.findViewById(R.id.expenseName)
        val amountText: TextView = itemView.findViewById(R.id.expenseAmount)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val detailsButton: Button = itemView.findViewById(R.id.detailsButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.expense_item, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.nameText.text = expense.name
        holder.amountText.text = "$%.2f".format(expense.amount)

        holder.detailsButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ExpenseDetailsActivity::class.java)
            intent.putExtra("name", expense.name)
            intent.putExtra("amount", expense.amount)
            intent.putExtra("date", expense.date)
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            expenseList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, expenseList.size)
            Toast.makeText(holder.itemView.context, "Expense Deleted", Toast.LENGTH_SHORT).show()


        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }
}
