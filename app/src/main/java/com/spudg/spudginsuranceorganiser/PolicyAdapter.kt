package com.spudg.spudginsuranceorganiser

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

class PolicyAdapter(val context: Context, private val items: ArrayList<PolicyModel>) :
    RecyclerView.Adapter<PolicyAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val policyItem = view.policy_row_layout!!
        val mainRowItem = view.policy_main_row_layout!!
        val tagView = view.policy_tag!!
        val priceView = view.policy_price!!
        val noteView = view.policy_note!!
        val dateView = view.policy_next_date!!
        val remainingDaysView = view.policy_remaining_days!!
        val rowLine = view.row_line!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.policy_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val formatter: NumberFormat = DecimalFormat("#,##0.00")

        val policy = items[position]

        val sdf = SimpleDateFormat("EEEE d MMM yyyy")
        val date = sdf.format(policy.nextDateMillis.toLong())

        holder.dateView.text = "Policy expires on $date\n(${policy.frequency})"

        val remainingDays = (((Calendar.getInstance().timeInMillis.toString()
            .toLong() - policy.nextDateMillis.toLong()) / 86400000) * -1)

        if (remainingDays <= 30 && remainingDays > 7) {
            holder.policyItem.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_amber_background
                )
            )
            holder.rowLine.setBackgroundColor(getColor(context, R.color.lineForAmber))
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.black))
            holder.dateView.setTextColor(getColor(context, android.R.color.black))
            holder.noteView.setTextColor(getColor(context, android.R.color.black))
            holder.priceView.setTextColor(getColor(context, android.R.color.black))
            holder.tagView.setTextColor(getColor(context, android.R.color.black))
            holder.remainingDaysView.text = "$remainingDays days until renewal"
        } else if (remainingDays <= 7 && remainingDays > 0) {
            holder.policyItem.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_red_background
                )
            )
            holder.rowLine.setBackgroundColor(getColor(context, R.color.lineForRed))
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.black))
            holder.dateView.setTextColor(getColor(context, android.R.color.black))
            holder.noteView.setTextColor(getColor(context, android.R.color.black))
            holder.priceView.setTextColor(getColor(context, android.R.color.black))
            holder.tagView.setTextColor(getColor(context, android.R.color.black))
            if (remainingDays.toInt() == 1) {
                holder.remainingDaysView.text = "$remainingDays day until renewal"
            } else {
                holder.remainingDaysView.text = "$remainingDays days until renewal"
            }
        } else if (remainingDays.toInt() == 0) {
            holder.policyItem.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_black_background
                )
            )
            holder.rowLine.setBackgroundColor(getColor(context, R.color.lineForBlack))
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.white))
            holder.dateView.setTextColor(getColor(context, android.R.color.white))
            holder.noteView.setTextColor(getColor(context, android.R.color.white))
            holder.priceView.setTextColor(getColor(context, android.R.color.white))
            holder.tagView.setTextColor(getColor(context, android.R.color.white))
            holder.remainingDaysView.text = "Expires today"
        } else if (remainingDays < 0) {
            holder.policyItem.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_black_background
                )
            )
            holder.rowLine.setBackgroundColor(getColor(context, R.color.lineForBlack))
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.white))
            holder.dateView.setTextColor(getColor(context, android.R.color.white))
            holder.noteView.setTextColor(getColor(context, android.R.color.white))
            holder.priceView.setTextColor(getColor(context, android.R.color.white))
            holder.tagView.setTextColor(getColor(context, android.R.color.white))
            if (remainingDays.toInt() == -1) {
                holder.remainingDaysView.text = "${-remainingDays} day overdue"
            } else {
                holder.remainingDaysView.text = "${-remainingDays} days overdue"
            }
        } else {
            holder.policyItem.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.policy_expiry_green_background
                )
            )
            holder.rowLine.setBackgroundColor(getColor(context, R.color.lineForGreen))
            holder.remainingDaysView.setTextColor(getColor(context, android.R.color.black))
            holder.dateView.setTextColor(getColor(context, android.R.color.black))
            holder.noteView.setTextColor(getColor(context, android.R.color.black))
            holder.priceView.setTextColor(getColor(context, android.R.color.black))
            holder.tagView.setTextColor(getColor(context, android.R.color.black))
            holder.remainingDaysView.text = "$remainingDays days until renewal"
        }

        if (context is MainActivity) {
            holder.tagView.text = context.getPolicyTagTitle(policy.tag)
        }

        holder.priceView.text = formatter.format((policy.price).toDouble()).toString()
        holder.noteView.text = policy.note

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