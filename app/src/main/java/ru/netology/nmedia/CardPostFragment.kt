package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.EditPostFragment.Companion.idArg
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.IntArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class CardPostFragment : Fragment() {
    companion object {
        var Bundle.textArg1: String? by StringArg
        var Bundle.idArg: Int? by IntArg
    }

    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCardPostBinding.inflate(
            inflater,
            container,
            false
        )
        binding.mainText.setText(arguments?.textArg1)


        fun Int.toShortString(): String = when (this) {
            in 0..<1_000 -> this.toString()
            in 1_000..<10_000 -> "${(this / 100) / 10.0}K"
            in 10_000..<1_000_000 -> "${this / 1000}K"
            in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
            in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
            else -> "MANY"
        }

        val postId: Long = (arguments?.idArg ?: -1).toLong()
        viewModel.data.observe(viewLifecycleOwner) { list ->
            list.find { it.id == postId }?.let { post ->
                binding.likes.text = post.likes.toShortString()

                PostViewHolder(binding.singlePost, object : onInteractionListener {

                    override fun onLike(post: Post) {
                        binding.likes.text = post.likes.toShortString()
                        binding.likes.setOnClickListener {
                            onLike(post)
                        }
                        viewModel.likeById(post.id)
                    }

                    override fun onShare(post: Post) {
                        viewModel.shareById(post.id)
                        binding.reposts.text = post.shares.toShortString()
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, post.content)
                        }
                        val chooser =
                            Intent.createChooser(intent, getString(R.string.chooser_share_post))
                        startActivity(chooser)
                    }

                    override fun onRemove(post: Post) {
                        viewModel.removeById(post.id)
                        findNavController().navigateUp()
                    }

                    override fun onClick(post: Post) {
//                        findNavController().navigate(R.id.action_feedFragment_to_cardPostFragment)
                    }

                    override fun onEdit(post: Post) {
                        viewModel.edit(post)
                        findNavController().navigate(
                            R.id.action_cardPostFragment_to_editPostFragment2,
                            Bundle().apply {
                                textArg = post.content
                            })
                    }
                }).bind(post)
            }
        }
//        activity?.onBackPressedDispatcher?.onBackPressed().apply {
//            findNavController().navigateUp()
//        }
        return binding.root
    }
}
