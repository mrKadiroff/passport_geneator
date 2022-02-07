package com.example.passport.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.passport.databinding.MalumotItemBinding
import com.example.passport.entity.Malumotlar
import java.util.*

class PassportAdapter (var list: List<Malumotlar>,var onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<PassportAdapter.Vh>() {

    inner class Vh(var malumotItemBinding: MalumotItemBinding) :
        RecyclerView.ViewHolder(malumotItemBinding.root) {

        fun onBind(malumotlar: Malumotlar,position: Int) {
          malumotItemBinding.name.text = malumotlar.name


            val min = 20
            val max = 80
            val random: Int = Random().nextInt(max - min + 1) + min

            malumotItemBinding.phone.text = "AB42271${random}"

            malumotItemBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(malumotlar)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MalumotItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onItemClick(malumotlar: Malumotlar)
    }
}