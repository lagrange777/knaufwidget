package com.vrt.knaufwidget.appwidgets

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vrt.knaufwidget.R
import com.vrt.knaufwidget.appwidgets.CalendarSettingsItem.Companion.ADD
import com.vrt.knaufwidget.appwidgets.CalendarSettingsItem.Companion.DISABLE
import com.vrt.knaufwidget.appwidgets.CalendarSettingsItem.Companion.ENABLE


sealed class CalendarSettingsItem {

    companion object {
        const val ADD: Int = 0
        const val ENABLE: Int = 1
        const val DISABLE: Int = 2
    }

    abstract val viewType: Int
    abstract val color: Int
    abstract var text: String

    data class Add(override val viewType: Int = ADD) : CalendarSettingsItem() {
        override var text: String = "Добавить Outlook"
        override val color: Int = R.color.light_blue_200
    }

    data class Enable(override val viewType: Int = ENABLE) : CalendarSettingsItem() {
        override var text: String = ""
        override val color: Int = R.color.light_blue_600
    }

    data class Disable(override val viewType: Int = DISABLE) : CalendarSettingsItem() {
        override var text: String = ""
        override val color: Int = androidx.appcompat.R.color.material_blue_grey_800
    }
}

typealias IVHBinder = (ViewGroup) -> CalendarSettingsAdapter.CalendarSettingsVH

class CalendarSettingsAdapter : RecyclerView.Adapter<CalendarSettingsAdapter.CalendarSettingsVH>() {


    var data: MutableList<CalendarSettingsItem> = mutableListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val vhMap: Map<Int, IVHBinder> = mapOf(
        ADD to ::createAddVH,
        ENABLE to ::createEnableVH,
        DISABLE to ::createDisableVH
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarSettingsVH {
        return vhMap[viewType]?.invoke(parent) ?: createAddVH(parent)
    }

    override fun onBindViewHolder(holder: CalendarSettingsVH, position: Int) {
        data.getOrNull(position)?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = data.count()


    private fun createEnableVH(parent: ViewGroup): EnableVH =
        EnableVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_calendar_enable, parent, false)
        )


    private fun createDisableVH(parent: ViewGroup): DisableVH =
        DisableVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_calendar_disable, parent, false)
        )

    private fun createAddVH(parent: ViewGroup): AddVH =
        AddVH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_calendar_add, parent, false)
        )


    sealed class CalendarSettingsVH(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: CalendarSettingsItem)
    }

    inner class EnableVH(view: View) : CalendarSettingsVH(view) {
        override fun bind(item: CalendarSettingsItem) {

        }
    }

    inner class DisableVH(view: View) : CalendarSettingsVH(view) {
        override fun bind(item: CalendarSettingsItem) {

        }
    }

    inner class AddVH(view: View) : CalendarSettingsVH(view) {
        override fun bind(item: CalendarSettingsItem) {

        }
    }
}