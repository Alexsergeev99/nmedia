package ru.netology.nmedia

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.IntArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class EditPostFragment : Fragment() {

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
        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )
        binding.edit.requestFocus()
        binding.edit.setText(arguments?.textArg)
        val postId: Long = (arguments?.idArg ?: -1).toLong()
//        viewModel.data.observe(viewLifecycleOwner) {posts->
//            val post = posts.find { it.id == postId } ?: return@observe
//            with(binding) {
//                edit.setText(post.content)
//                edit.id = post.id.toInt()
//            }

        //}
//        binding.edit.setText()

        binding.ok.setOnClickListener {
            viewModel.changeContent(binding.edit.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
        }

//        binding.ok.setOnClickListener {
//            val intent = Intent()
//            if (binding.edit.text.isNullOrBlank()) {
//                setResult(Activity.RESULT_CANCELED, intent)
//            } else {
//                val content = binding.edit.text.toString()
//                intent.putExtra(Intent.EXTRA_TEXT, content)
//                setResult(Activity.RESULT_OK, intent)
//            }
//        }
//       arguments?.textArg?.let(binding.edit::setText)


        return binding.root
    }
}