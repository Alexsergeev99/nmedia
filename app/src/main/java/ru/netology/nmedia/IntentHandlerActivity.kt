package ru.netology.nmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding

class IntentHandlerActivity : AppCompatActivity(R.layout.activity_intent_handler) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }
            else {
                intent.removeExtra(Intent.EXTRA_TEXT)
                findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container)
                    .navigate(R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = text
                        })
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}