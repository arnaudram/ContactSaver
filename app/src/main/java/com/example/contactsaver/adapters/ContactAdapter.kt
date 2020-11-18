package com.example.contactsaver.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsaver.R
import com.example.contactsaver.models.Contact


class ContactAdapter(private val data:List<Contact>): RecyclerView.Adapter<ContactAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private  val itemName=itemView.findViewById<TextView>(R.id.tv_contact_name)
      private  val itemEmail=itemView.findViewById<TextView>(R.id.tv_contact_email)
        fun bind(item: Contact) {
            with(item){
                itemName.text=name
                itemEmail.text=email
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_contact,parent,false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item=data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
      return data.size
    }
}