package ru.netology.nmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.CardPostFragment.Companion.textArg1
import ru.netology.nmedia.EditPostFragment.Companion.idArg
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.viewmodel.PostViewModel

class FeedFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
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
                viewModel.likeById(post)
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
//            val newPost = adapter.currentList.size < posts.size
//            adapter.submitList(posts) //{
//                if (newPost) {
//                    binding.list.smoothScrollToPosition(0)
//                }
            //}
        //}

//        viewModel.edited.observe(viewLifecycleOwner) { post ->
//            if (post.id == 0L) {
//                return@observe
//            } else {
//                with(binding.content) {
//                    requestFocus()
//                    binding.currentMessage.text = post.content
//                    binding.group.visibility = View.VISIBLE
//                    setText(post.content)
//                }
//            }
//        }
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) {state->
            adapter.submitList(state.posts)
            binding.errorGroup.isVisible = state.error
            binding.emptyText.isVisible = state.empty
            binding.progress.isVisible = state.loading
        }

        binding.retry.setOnClickListener {
            viewModel.load()
        }

        binding.swipe.setOnRefreshListener {
            viewModel.load()
            binding.swipe.isRefreshing = false
        }

        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}





