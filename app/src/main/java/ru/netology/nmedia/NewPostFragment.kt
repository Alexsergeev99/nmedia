package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.cast.framework.media.ImagePicker
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let(binding.edit::setText)

        binding.edit.requestFocus()

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.save_post, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.save_post -> {
                        viewModel.changeContent(binding.edit.text.toString())
                        viewModel.save()
                        AndroidUtils.hideKeyboard(requireView())
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner)

//        binding.pickPhoto.setOnClickListener {
//            ImagePicker.with(this)
//                .crop()
//                .compress(2048)
//                .provider(ImageProvider.GALLERY)
//                .galleryMimeTypes(
//                    arrayOf(
//                        "image/png",
//                        "image/jpeg",
//                    )
//                )
//                .createIntent(pickPhotoLauncher::launch)
//        }
//
//        binding.takePhoto.setOnClickListener {
//            ImagePicker.with(this)
//                .crop()
//                .compress(2048)
//                .provider(ImageProvider.CAMERA)
//                .createIntent(pickPhotoLauncher::launch)
//        }


        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.errorGroup.isVisible = state.error
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_text, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry) { viewModel.load() }
                    .show()
            }
        }

        binding.retry.setOnClickListener {
            viewModel.load()
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            viewModel.load()
            findNavController().navigateUp()
        }

        return binding.root
    }
}