package com.voicechanger.effect.changevoice.ui.language
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ghostfinder.ghostdetector.radar.ads.base.BaseListAdapter
import com.ghostfinder.ghostdetector.radar.ads.base.BaseViewHolder
import com.ghostfinder.ghostdetector.radar.databinding.HatakiItemFlagBinding


class FlagHatakiAdapter: BaseListAdapter<Int, HatakiItemFlagBinding, FlagHatakiAdapter.FlagViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = HatakiItemFlagBinding.inflate(layoutInflater, parent, false)
        return FlagViewHolder(binding)
    }

    class FlagViewHolder(binding: HatakiItemFlagBinding) : BaseViewHolder<Int, HatakiItemFlagBinding>(binding) {

        override fun onBindData(data: Int) {
            super.onBindData(data)
            binding.root.setImageResource(data)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }
        }
    }
}