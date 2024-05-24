package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.CardPostFragment.Companion.textArg1
import ru.netology.nmedia.EditPostFragment.Companion.idArg
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.UserViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()
    val authViewModel: AuthViewModel by viewModels()

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

        val adapter = PostsAdapter(object : onInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
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
                findNavController().navigate(R.id.action_feedFragment_to_cardPostFragment,
                    Bundle().apply {
                        textArg1 = post.content
                        idArg = post.id.toInt()
                    })
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
        var currentMenuProvider: MenuProvider? = null

        authViewModel.auth.observe(viewLifecycleOwner) {
            val authorized = authViewModel.authorized

//            currentMenuProvider?.let {
//                requireActivity().removeMenuProvider(it)
//            }
            currentMenuProvider?.let(requireActivity()::removeMenuProvider)

            requireActivity().addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.auth_menu, menu)

                    menu.setGroupVisible(R.id.auth, authorized)
                    menu.setGroupVisible(R.id.unauth, !authorized)

                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when(menuItem.itemId) {
                        R.id.sign_in -> {
                            findNavController().navigate(R.id.action_feedFragment_to_regFragment,
                                Bundle().apply {
                                    textArg = getString(R.string.sign_in)
                                })
                            true
                        }
                        R.id.sign_up -> {
                            AppAuth.getInstance().setAuth(5, "x-token")
                            true
                        }
                        R.id.logout -> {
                            AppAuth.getInstance().clearAuth()
                            true
                        } else -> {
                        false
                    }
                    }
            }.apply {
                currentMenuProvider = this
            }, viewLifecycleOwner)
        }

        viewModel.newerCount.observe(viewLifecycleOwner) {
            binding.newPosts.isVisible = true
            binding.newPosts.setOnClickListener {
                binding.newPosts.isVisible = false
                binding.list.smoothScrollToPosition(0)
                viewModel.showNewPosts()
            }
        }

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { state ->
            val isNewPost = state.posts.size > adapter.currentList.size
            adapter.submitList(state.posts) {
                if (isNewPost) {
                    binding.list.smoothScrollToPosition(0)
                }
                binding.emptyText.isVisible = state.empty
            }
        }
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

        binding.swipe.setOnRefreshListener {
            viewModel.load()
            binding.swipe.isRefreshing = false
            binding.newPosts.isVisible = false
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}





