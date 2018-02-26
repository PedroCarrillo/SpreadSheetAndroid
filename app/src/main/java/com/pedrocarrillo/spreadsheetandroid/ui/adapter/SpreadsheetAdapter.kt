package com.pedrocarrillo.spreadsheetandroid.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pedrocarrillo.spreadsheetandroid.R
import com.pedrocarrillo.spreadsheetandroid.data.model.Person

/**
 * @author Pedro Carrillo
 */
class SpreadsheetAdapter(val items : MutableList<Person>) : RecyclerView.Adapter<SpreadsheetAdapter.RowViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RowViewHolder?, position: Int) {
        val currentPerson = items.get(position)
        holder?.setData(currentPerson)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RowViewHolder {
        return RowViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.activity_row, parent, false))
    }

    class RowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        private val tvUser : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_user) }
        private val tvMajor : TextView by lazy<TextView> { itemView.findViewById(R.id.tv_major) }

        fun setData(person : Person) {
            tvUser.text = person.name
            tvMajor.text = person.major
        }

    }
}
