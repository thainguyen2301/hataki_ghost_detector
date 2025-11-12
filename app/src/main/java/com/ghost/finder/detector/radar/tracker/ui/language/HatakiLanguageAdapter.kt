package com.ghost.finder.detector.radar.tracker.ui.language

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.voicechanger.effect.changevoice.ui.language.FlagHatakiAdapter
import java.util.concurrent.Executors

class LanguageHatakiAdapter : ListAdapter<LanguageHatakiItem, RecyclerView.ViewHolder>(
    AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor(Executors.newSingleThreadExecutor())
        .build()
) {

    private var clickListener: (LanguageHatakiItem) -> Unit = {}

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int,
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_WITH_CHILD -> {
                LanguageWithChildHolder(
                    HatakiItemLanguageParentWithChildBinding.inflate(layoutInflater, parent, false)
                )
            }
            TYPE_WITHOUT_CHILD -> {
                LanguageWithoutChildHolder(
                    HatakiItemLanguageParentWithoutChildBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> {
                ChildViewHolder(
                    HatakiItemLanguageChildBinding.inflate(layoutInflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < currentList.size && position >= 0) {
            val item = getItem(position)
            if (holder is LanguageWithChildHolder && item is LanguageHatakiItem.Parent)
                holder.bindData(item)
            else if (holder is LanguageWithoutChildHolder && item is LanguageHatakiItem.ParentWithoutChild) {
                holder.bindData(item)
            } else if (holder is ChildViewHolder && item is LanguageHatakiItem.Child) {
                holder.bindData(item)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is LanguageHatakiItem.Child -> TYPE_CHILD
            is LanguageHatakiItem.Parent -> TYPE_WITH_CHILD
            is LanguageHatakiItem.ParentWithoutChild -> TYPE_WITHOUT_CHILD
        }
    }

    private fun getFont(context: Context, @StringRes assetFile: Int): Typeface {
        return try {
            Typeface.createFromAsset(context.assets, context.getString(assetFile))
        } catch (e: Exception) {
            Typeface.DEFAULT
        }
    }

    fun setOnItemClick(itemClickListener: (LanguageHatakiItem) -> Unit) {
        this.clickListener = itemClickListener
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class LanguageWithChildHolder(val binding: HatakiItemLanguageParentWithChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var data: LanguageHatakiItem.Parent? = null
        private val adapter by lazy { FlagHatakiAdapter() }

        init {
            binding.rvFlag.adapter = adapter
            binding.touchView.setOnClickListener {
                data?.let { clickListener(it) }
            }
        }

        fun bindData(data: LanguageHatakiItem.Parent) {
            this.data = data
            binding.animationView.isVisible = data.isShowAnimation
            binding.ivLanguage.setImageResource(data.image)
            binding.tvLanguage.text = data.languageName
            adapter.submitList(data.flags)
            binding.paddingTop.isVisible = adapterPosition != 0
        }
    }

    inner class LanguageWithoutChildHolder(val binding: HatakiItemLanguageParentWithoutChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var data: LanguageHatakiItem? = null

        init {
            binding.containerView.setOnClickListener {
                data?.let { clickListener(it) }
            }
        }

        fun bindData(data: LanguageHatakiItem.ParentWithoutChild) {
            this.data = data
            binding.animationView.isVisible = data.isShowAnimation
            binding.ivLanguage.setImageResource(data.image)
            binding.tvLanguage.text = data.languageName
            binding.paddingTop.isVisible = adapterPosition != 0
            val context = binding.root.context

            if (data.isSelected) {
                binding.containerView.setBackgroundResource(R.drawable.bg_item_language_on)
                binding.tvLanguage.setTextColor(context.getColor(R.color.white))
            } else {
                binding.containerView.setBackgroundResource(R.drawable.bg_item_language_off)
                binding.tvLanguage.setTextColor(context.getColor(R.color.white))
            }
        }
    }

    inner class ChildViewHolder(
        private val binding: HatakiItemLanguageChildBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var data: LanguageHatakiItem? = null

        init {
            binding.regionContainer.setOnClickListener {
                data?.let { clickListener(it) }
            }
        }

        fun bindData(data: LanguageHatakiItem.Child) {
            this.data = data
            binding.tvLanguage.text = data.regionName
            binding.flag.setImageResource(data.image)
            val isLastItem = adapterPosition == currentList.size - 1
            val hideLine = isLastItem || currentList[adapterPosition + 1] !is LanguageHatakiItem.Child
            binding.verticalLine.isVisible = !hideLine
            val context = binding.root.context

            if (data.isSelected) {
                binding.regionContainer.setBackgroundResource(R.drawable.bg_item_language_on)
                binding.tvLanguage.setTextColor(context.getColor(R.color.white))
            } else {
                binding.regionContainer.setBackgroundResource(R.drawable.bg_item_language_off)
                binding.tvLanguage.setTextColor(context.getColor(R.color.white))
            }
        }
    }

    companion object {
        private const val TYPE_WITH_CHILD = 0
        private const val TYPE_WITHOUT_CHILD = 1
        private const val TYPE_CHILD = 2

        private val diffCallback = object : DiffUtil.ItemCallback<LanguageHatakiItem>() {
            override fun areItemsTheSame(oldItem: LanguageHatakiItem, newItem: LanguageHatakiItem): Boolean {
                return oldItem.getItemId() == newItem.getItemId()
            }

            override fun areContentsTheSame(
                oldItem: LanguageHatakiItem,
                newItem: LanguageHatakiItem,
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}
