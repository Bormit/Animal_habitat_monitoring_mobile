package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.mirea.animal_habitat_monitoring_mobile.R

class SpinnerAdapter(context: Context, private val data: Array<String>) :
    ArrayAdapter<String>(context, R.layout.spinner_item, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (position == 0) {
            // Возвращаем пустую View без содержимого для первого элемента
            return View(context)
        }

        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val animalItem = getItem(position)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.spinner_item, parent, false)

        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = animalItem
        return if (position == 0) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.lightGray))
            textView
        }
        else{
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))
            textView
        }
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): String? {
        return super.getItem(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
}
