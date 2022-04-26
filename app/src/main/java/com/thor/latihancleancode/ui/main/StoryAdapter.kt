package com.thor.latihancleancode.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thor.latihancleancode.R
import com.thor.latihancleancode.databinding.ItemEmptyBinding
import com.thor.latihancleancode.databinding.ItemStoryBinding
import com.thor.latihancleancode.repository.story.Story


class StoryAdapter(private val itemClickListener: (Story) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list: MutableList<Story> = mutableListOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Story>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> ItemViewHolder(
                ItemStoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> EmptyViewHolder(
                ItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                holder.bindItem(list[position])
                holder.binding.root.setOnClickListener {
                    itemClickListener.invoke(list[position])
                }
            }
            is EmptyViewHolder -> {
            }
        }
    }

    override fun getItemViewType(position: Int) = if (list.isEmpty()) 0 else 1

    override fun getItemCount() = if (list.isEmpty()) 1 else list.size

    class ItemViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(item: Story) {
            binding.info.text = item.createdAt
            binding.name.text = item.name

            Glide.with(binding.root.context)
                .load(item.photoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(binding.avatar)

            Glide.with(binding.root.context)
                .load(item.photoUrl)
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(binding.imageView)
        }
    }

    class EmptyViewHolder(binding: ItemEmptyBinding) : RecyclerView.ViewHolder(binding.root)
}