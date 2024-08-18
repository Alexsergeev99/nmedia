package ru.netology.nmedia

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.netology.nmedia.EditPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentCardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.util.IntArg
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel

class CardPostFragment(private val repository: PostRepository
) : Fragment() {
    companion object {
        var Bundle.textArg1: String? by StringArg
        var Bundle.idArg: Int? by IntArg
    }

    private val viewModel: PostViewModel by activityViewModels()
    var auth: AppAuth = AppAuth.getInstance()
    val authViewModel: AuthViewModel by viewModels()

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
