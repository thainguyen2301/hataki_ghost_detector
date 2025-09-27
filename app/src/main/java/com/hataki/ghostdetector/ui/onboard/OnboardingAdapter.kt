package com.hataki.ghostdetector.ui.onboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hataki.ghostdetector.data.model.OnboardingItem
import com.hataki.ghostdetector.databinding.ItemOnboardingBinding

class OnboardingAdapter(private val items: List<OnboardingItem>) :
    RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OnboardingItem) {
            binding.titleText.text = item.title
            binding.descText.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
