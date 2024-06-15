package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.ItemLoadingBinding

class LoadingStateAdapter(
    private val retryButtonListener: () -> Unit
) : LoadStateAdapter<PostLoadingViewHolder>() {
    override fun onBindViewHolder(holder: PostLoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PostLoadingViewHolder = PostLoadingViewHolder(
        ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retryButtonListener
    )
}

class PostLoadingViewHolder(
    private val itemLoadingBinding: ItemLoadingBinding,
    private val retryButtonListener: () -> Unit
) : RecyclerView.ViewHolder(itemLoadingBinding.root) {

    fun bind(loadState: LoadState) {
        itemLoadingBinding.apply {
            loadingBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState is LoadState.Error
            retryButton.setOnClickListener {
                retryButtonListener()
            }
        }
    }

}