package com.example.madcamp_week2

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.BlockLayout.addTradePlanLayout
import com.example.madcamp_week2.BlockLayout.boolBlockFunctionList
import com.example.madcamp_week2.BlockLayout.showRadioDialog
import com.example.madcamp_week2.CodeBlock.ActionBlock
import com.example.madcamp_week2.databinding.BlockActionBinding



class ActionBlockAdapter(
    private val actionBlockList: MutableList<ActionBlock>,
    private val context: Context
) : RecyclerView.Adapter<ActionBlockAdapter.MyViewHolder>() {

    interface OnItemButtonClickListener {
        fun onItemButtonClick(position: Int)
    }

    class MyViewHolder(val binding: BlockActionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BlockActionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.blockActionCondition.setOnClickListener {
            showRadioDialog(context, "choose condition for trade", boolBlockFunctionList.keys.toList()) { i ->
                actionBlockList[position].conditionBlock =
                    boolBlockFunctionList[i]?.let { it1 -> it1(holder.binding.blockActionCondition, context) }
            }
        }
        holder.binding.blockActionTradePlan.setOnClickListener {
            actionBlockList[position].tradePlanBlock = addTradePlanLayout(holder.binding.blockActionTradePlan, context)
            }

    }

    override fun getItemCount(): Int = actionBlockList.size

    fun addAction(action: ActionBlock) {
        actionBlockList.add(action)
        notifyItemInserted(actionBlockList.size - 1)
    }
}

