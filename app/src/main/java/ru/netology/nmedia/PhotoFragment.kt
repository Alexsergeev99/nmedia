package ru.netology.nmedia

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaType
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import ru.netology.nmedia.databinding.FragmentPhotoBinding
import ru.netology.nmedia.viewmodel.PostViewModel


class PhotoFragment : Fragment() {

    companion object {
        const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPhotoBinding.inflate(
            inflater,
            container,
            false
        )
        val url = arguments?.textArg ?: ""

        Glide.with(binding.attachment)
            .load("${BASE_URL}/media/$url")
            .placeholder(R.drawable.image_loading)
            .error(R.drawable.image_error)
            .timeout(30000)
            .into(binding.attachment)

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
