package ru.netology.nmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentRegBinding
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.viewmodel.UserViewModel
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class RegFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRegBinding.inflate(
            inflater,
            container,
            false
        )

        binding.enter.setOnClickListener {
            val login = binding.login.text.toString()
            val password = binding.passwordReg.text.toString()
            val scope = CoroutineScope(EmptyCoroutineContext)
            scope.launch {
                viewModel.uploadUser(login, password)
            }
            postViewModel.load()
            findNavController().navigateUp()
//                Toast.makeText(context, "Authorization failed", Toast.LENGTH_LONG).show()
//                findNavController().navigateUp()
        }
        return binding.root
    }
}