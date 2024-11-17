package com.num.ichat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.num.ichat.R
import com.num.ichat.databinding.SendItemLayoutBinding
import com.num.ichat.model.MessageModel

class MessageAdapter(var context: Context,var list:ArrayList<MessageModel>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var ITEM_SENT = 1
    var ITEM_RECEIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_SENT)
            SentViewHolder(LayoutInflater.from(context).inflate(R.layout.send_item_layout, parent, false)
            )
        else RecieverViewHolder(
            LayoutInflater.from(context).inflate(R.layout.reciever_layout_item, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        return if (FirebaseAuth.getInstance().uid == list[position].senderId) ITEM_SENT else ITEM_RECEIVE
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]
        if (holder.itemViewType == ITEM_SENT){
            val viewHolder = holder as SentViewHolder
            viewHolder.binding.userMsg.text = message.message
        }else{
            val viewHolder = holder as RecieverViewHolder
            viewHolder.binding.userMsg.text = message.message
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SentViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding = SendItemLayoutBinding.bind(view)
    }

    inner class RecieverViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding = SendItemLayoutBinding.bind(view)
    }
}