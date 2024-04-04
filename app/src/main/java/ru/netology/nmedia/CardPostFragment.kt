package ru.netology.nmedia

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
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

        val postId: Long = (arguments?.idArg ?: -1).toLong()
//        viewModel.data.observe(viewLifecycleOwner) { list ->
//            list.find { it.id == postId }?.let { post ->
////                binding.likes.text = post.likes.toShortString()
//
//                PostViewHolder(binding.singlePost, object : onInteractionListener {
//
//                    override fun onLike(post: Post) {
//                        viewModel.likeById(post.id)
//                    }
//
//                    override fun onShare(post: Post) {
//                        viewModel.shareById(post.id)
//                        val intent = Intent().apply {
//                            action = Intent.ACTION_SEND
//                            type = "text/plain"
//                            putExtra(Intent.EXTRA_TEXT, post.content)
//                        }
//                        val chooser =
//                            Intent.createChooser(intent, getString(R.string.chooser_share_post))
//                        startActivity(chooser)
//                    }
//
//                    override fun onRemove(post: Post) {
//                        viewModel.removeById(post.id)
//                        findNavController().navigateUp()
//                    }
//
//                    override fun onClick(post: Post) {
////                        findNavController().navigate(R.id.action_feedFragment_to_cardPostFragment)
//                    }
//
//                    override fun onEdit(post: Post) {
//                        viewModel.edit(post)
//                        findNavController().navigate(
//                            R.id.action_cardPostFragment_to_editPostFragment2,
//                            Bundle().apply {
//                                textArg = post.content
//                            })
//                    }
//                }).bind(post)
//            }
//        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireActivity()).apply {
                            findNavController().navigateUp()
                    }.create().show()
                }
            }
        )
        return binding.root
    }
}
