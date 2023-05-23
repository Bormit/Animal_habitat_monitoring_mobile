package ru.mirea.animal_habitat_monitoring_mobile.view.fragments

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import ru.mirea.animal_habitat_monitoring_mobile.R

class SpinnerAdapter(context: Context, data: Array<String>) :
    ArrayAdapter<String>(context, R.layout.spinner_item, data) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val textView = view.findViewById<TextView>(android.R.id.text1)

        if (position == 0) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.gray))
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))
        }

        return view
    }

    override fun isEnabled(position: Int): Boolean {
        // Запретить выбор подсказки
        return position != 0
    }

    override fun getCount(): Int {
        // Учитываем подсказку в общем количестве элементов
        return super.getCount() + 1
    }

    override fun getItem(position: Int): String? {
        // Возвращаем null для позиции подсказки
        if (position == 0) return null
        return super.getItem(position - 1)
    }

    override fun getItemId(position: Int): Long {
        // Используйте позицию подсказки в качестве идентификатора
        if (position == 0) return -1
        return super.getItemId(position - 1)
    }
}
