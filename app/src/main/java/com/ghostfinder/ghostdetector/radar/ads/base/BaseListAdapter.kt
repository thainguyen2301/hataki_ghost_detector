package com.ghostfinder.ghostdetector.radar.ads.base

import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseListAdapter<T, VB : ViewBinding, VH : BaseViewHolder<T, VB>>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(
    AsyncDifferConfig.Builder(diffCallback)
        .setBackgroundThreadExecutor { CoroutineScope(Dispatchers.Default).launch { it.run() } }
        .build()
) {
    override fun onBindViewHolder(holder: VH, position: Int) {
        currentList.getOrNull(position)?.let { item ->
            if (!holder.onBindData(item, position)) {
                holder.onBindData(item)
            }
        }
    }
}

