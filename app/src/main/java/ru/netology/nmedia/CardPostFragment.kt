package ru.netology.nmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.netology.nmedia.EditPostFragment.Companion.idArg
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.IntArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class CardPostFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
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

        fun Int.toShortString(): String = when (this) {
            in 0..<1_000 -> this.toString()
            in 1_000..<10_000 -> "${(this / 100) / 10.0}K"
            in 10_000..<1_000_000 -> "${this / 1000}K"
            in 1_000_000..<10_000_000 -> "${(this / 100_000) / 10.0}M"
            in 10_000_000..<1_000_000_000 -> "${this / 1_000_000}M"
            else -> "MANY"
        }

        val postId: Long = (arguments?.idArg ?: -1).toLong()
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            with(binding) {
                author.text = post.author
                data.text = post.published
                mainText.text = post.content
                likes.isChecked = post.likedByMe
                reposts.text = post.shares.toShortString()
                likes.text = post.likes.toShortString()

            }

        }
        return binding.root
    }
}