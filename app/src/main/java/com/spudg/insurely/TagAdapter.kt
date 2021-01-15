package com.spudg.insurely

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tag_row.view.*

class TagAdapter(private val context: Context, private val items: ArrayList<TagModel>) :
    RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagItem = view.tag_row_layout!!
        val nameView = view.name_tag!!
        val colourView = view.colour_tag!!
        val updateView = view.update_tag!!
        val deleteView = view.delete_tag!!
        val defaultView = view.default_tag!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.tag_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tag = items[position]
        holder.nameView.text = tag.name
        holder.colourView.setBackgroundColor(tag.colour.toInt())

        if (tag.name == "Vehicle Insurance" || tag.name == "Home Contents Insurance" || tag.name == "Home Building Insurance" || tag.name == "Life Insurance" || tag.name == "Other Insurance") {
            holder.updateView.visibility = View.GONE
            holder.deleteView.visibility = View.GONE
            holder.defaultView.visibility = View.VISIBLE
        } else {
            holder.updateView.visibility = View.VISIBLE
            holder.deleteView.visibility = View.VISIBLE
            holder.defaultView.visibility = View.GONE

            holder.updateView.setOnClickListener {
                if (context is TagActivity) {
                    context.updateTag(tag)
                }
            }

            holder.deleteView.setOnClickListener {
                if (context is TagActivity) {
                    context.deleteTag(tag)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}