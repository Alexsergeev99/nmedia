package ru.netology.nmedia

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.adapter.LoadingStateAdapter
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.adapter.onInteractionListener
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    val authViewModel: AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )

        val dialog = context?.let { AlertDialog.Builder(it) }
        dialog?.setTitle(R.string.enterToContinue)
            ?.setCancelable(true)
            ?.setPositiveButton(R.string.sign_in) { _, _ ->
                findNavController().navigate(R.id.action_feedFragment_to_regFragment)
            }
            ?.setNegativeButton(
                R.string.cancel
            ) { _, _ ->
                findNavController().navigateUp()
            }
        dialog?.create()

        val adapter = PostsAdapter(object : onInteractionListener {
            override fun onLike(post: Post) {
                if (authViewModel.authorized) {
                    viewModel.likeById(post.id)
                } else {
                    dialog?.show()
                }
            }

            override fun onShare(post: Post) {
                viewModel.shareById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                val chooser = Intent.createChooser(intent, getString(R.string.chooser_share_post))
                startActivity(chooser)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onClick(post: Post) {
//                findNavController().navigate(R.id.action_feedFragment_to_cardPostFragment,
//                    Bundle().apply {
//                        textArg1 = post.content
//                        idArg = post.id.toInt()
//                    })
            }

            override fun onClickPhoto(post: Post) {
                findNavController().navigate(R.id.action_feedFragment_to_photoFragment,
                    Bundle().apply {
                        textArg = post.attachment?.url
                    }
                )
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment2,
                    Bundle().apply {
                        textArg = post.content
                    })
//                val intent = Intent().apply {
//                    action = Intent.ACTION_SEND
//                    type = "text/plain"
//                    putExtra(Intent.EXTRA_TEXT, post.content)
//                }
//                editPostLauncher.launch(intent.getStringExtra(Intent.EXTRA_TEXT))
            }
        }
        )
        //}

        lifecycleScope.launchWhenCreated {
            binding.newPosts.isVisible = true
            binding.newPosts.setOnClickListener {
                binding.newPosts.isVisible = false
                binding.list.smoothScrollToPosition(0)
                viewModel.showNewPosts()
            }
        }

//        viewModel.newerCount.observe(viewLifecycleOwner) {
//            binding.newPosts.isVisible = true
//            binding.newPosts.setOnClickListener {
//                binding.newPosts.isVisible = false
//                binding.list.smoothScrollToPosition(0)
//                viewModel.showNewPosts()
//            }
//        }

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoadingStateAdapter {
                adapter.retry()
            },
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

//        lifecycleScope.launchWhenCreated {
//            viewModel.data.collectLatest {
//                adapter.submitData(it)
//            }
//        }

//        viewModel.data.observe(viewLifecycleOwner) { state ->
//            val isNewPost = state.posts.size > adapter.currentList.size
//            adapter.submitData(state.posts) {
//                if (isNewPost) {
//                    binding.list.smoothScrollToPosition(0)
//                }
//                binding.emptyText.isVisible = state.empty
//            }
//        }
        viewModel.dataState.observe(viewLifecycleOwner) { state ->
//            binding.errorGroup.isVisible = state.error
            binding.progress.isVisible = state.loading
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_text, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry) { viewModel.load() }
                    .setAnchorView(binding.add)
                    .show()
            }
        }

        binding.retry.setOnClickListener {
            viewModel.load()
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swipe.isRefreshing = it.refresh is LoadState.Loading
//                        || it.append is LoadState.Loading || it.prepend is LoadState.Loading
            }
        }
        binding.swipe.setOnRefreshListener {
            viewModel.load()
            binding.swipe.isRefreshing = false
            binding.newPosts.isVisible = false
            adapter.refresh()
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            adapter.refresh()
        }

        binding.add.setOnClickListener {
            if (authViewModel.authorized) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
            } else {
                dialog?.show()
            }
        }

        return binding.root
    }
}





