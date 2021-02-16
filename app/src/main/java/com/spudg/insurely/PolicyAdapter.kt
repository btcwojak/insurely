package com.spudg.insurely

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.policy_row.view.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class PolicyAdapter(private val context: Context, private val items: ArrayList<PolicyModel>) :
        RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainRowItem = view.policy_main_row_layout!!
        val tagView = view.policy_tag!!
        val priceView = view.policy_price!!
        val noteView = view.policy_note!!
        val dateView = view.policy_next_date!!
        val colourView = view.tag_colour_policy!!
        val remainingDaysView = view.policy_remaining_days!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.policy_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val priceFormatter: NumberFormat = DecimalFormat("#,##0.00")
        val daysFormatter: NumberFormat = DecimalFormat("#,##0")

        val policy = items[position]

        val sdfDayDate = SimpleDateFormat("EEEE d MMM yyyy", Locale.getDefault())
        val date = sdfDayDate.format(policy.nextDateMillis.toLong())

        holder.dateView.text = holder.dateView.context.getString(R.string.policy_expires_date, date.toString())

        val remainingDays = ceil((((Calendar.getInstance().timeInMillis.toString()
                .toLong() - policy.nextDateMillis.toLong()) / 86400000F) * -1F)).toInt()

        val remainingDaysFormatted = daysFormatter.format(remainingDays)

        val negativeRemainingDaysFormatted = daysFormatter.format(-remainingDays)

        if (remainingDays in 8..30) {
            holder.remainingDaysView.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_amber_background
            )
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.black))
            holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.days_until_renewal, remainingDaysFormatted.toString())

        } else if (remainingDays in 1..7) {
            holder.remainingDaysView.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_red_background
            )
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.black))
            if (remainingDays == 1) {
                holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.day_until_renewal, remainingDaysFormatted.toString())
            } else {
                holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.days_until_renewal, remainingDaysFormatted.toString())
            }
        } else if (remainingDays == 0) {
            holder.remainingDaysView.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_black_background
            )
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.white))
            holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.expires_today)
        } else if (remainingDays < 0) {
            holder.remainingDaysView.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_black_background
            )
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.white))
            if (remainingDays == -1) {
                holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.day_overdue, negativeRemainingDaysFormatted)
            } else {
                holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.days_overdue, negativeRemainingDaysFormatted)
            }
        } else {
            holder.remainingDaysView.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_green_background
            )
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.black))
            holder.remainingDaysView.text = holder.remainingDaysView.context.getString(R.string.days_until_renewal, remainingDaysFormatted.toString())
        }

        if (context is MainActivity) {
            holder.tagView.text = context.getPolicyTagTitle(policy.tag)
        }

        holder.priceView.text = priceFormatter.format((policy.price).toDouble()).toString()
        holder.noteView.text = policy.note

        if (context is MainActivity) {
            val colour = context.getPolicyTagColour(policy.tag)
            holder.colourView.setBackgroundColor(colour)
        }


        holder.mainRowItem.setOnClickListener {
            if (context is MainActivity) {
                context.updatePolicy(policy)
            }
        }

        holder.mainRowItem.setOnLongClickListener {
            if (context is MainActivity) {
                context.deletePolicy(policy)
            }
            true
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


}