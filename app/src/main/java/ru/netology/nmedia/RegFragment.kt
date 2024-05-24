package ru.netology.nmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentRegBinding
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.UserViewModel
import kotlin.coroutines.EmptyCoroutineContext

class RegFragment : Fragment() {
    private val viewModel: AuthViewModel by activityViewModels()
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
        val login = binding.login.toString()
        val password = binding.passwordReg.toString()
        val scope = CoroutineScope(EmptyCoroutineContext)

        val rightData = arguments?.textArg == getString(R.string.sign_in)

        binding.enter.setOnClickListener {
            if (rightData) {
                scope.launch {
                    viewModel.uploadUser(login, password)
                }
                findNavController().navigateUp()
            }
        }
        return binding.root
    }
}