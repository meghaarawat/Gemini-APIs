package com.kitlabs.aiapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitlabs.aiapp.databinding.ItemAnswerBinding
import com.kitlabs.aiapp.databinding.ItemQuestionBinding
import com.kitlabs.aiapp.model.QNModel
import com.kitlabs.aiapp.others.Cons
import com.kitlabs.aiapp.others.MyUtils

class QNAdapter(private val list: List<QNModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_QUES = 0
    private val VIEW_TYPE_ANS = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_QUES -> {
                QuesViewHolder(ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            VIEW_TYPE_ANS -> {
                AnsViewHolder(ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]

        when (holder) {
            is QuesViewHolder -> {
                holder.binding.tvQues.text = item.data
                if(item.bitmapList != null && item.bitmapList.size != 0) {
                    MyUtils.viewVisible(holder.binding.rvImages)
                    holder.binding.rvImages.apply {
                        layoutManager = LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
                        adapter = ImageAdapter(item.bitmapList)
                    }
                }
                else {
                    MyUtils.viewGone(holder.binding.rvImages)
                }
            }
            is AnsViewHolder -> {
                holder.binding.apply {
                    if(position == (list.size - 1)) {
                        tvAns.text = item.data
                        tvAns.avoidTextOverflowAtEdge(false)
                        tvAns.animateText(item.data)
                    }
                    else {
                        tvAns.text = item.data
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position].type) {
            Cons.QUES -> VIEW_TYPE_QUES
            else -> VIEW_TYPE_ANS
        }
    }

    inner class AnsViewHolder(val binding: ItemAnswerBinding): RecyclerView.ViewHolder(binding.root)

    inner class QuesViewHolder(val binding: ItemQuestionBinding): RecyclerView.ViewHolder(binding.root)
}
