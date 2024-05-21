package ru.netology.nmedia

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.NewPostFragment.Companion.textArg
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.MenuProvider
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.ActivityIntentHandlerBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

class IntentHandlerActivity : AppCompatActivity(R.layout.activity_intent_handler) {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationsPermission()
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

        var currentMenuProvider: MenuProvider? = null
        val viewModel by viewModels<AuthViewModel>()
            viewModel.auth.observe(this) {
                val authorized = viewModel.authorized

                currentMenuProvider?.let {
                    removeMenuProvider(it)
                }

                addMenuProvider(object : MenuProvider {
                    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                        menuInflater.inflate(R.menu.auth_menu,menu)

                        menu.setGroupVisible(R.id.auth, authorized)
                        menu.setGroupVisible(R.id.unauth, !authorized)

                    }

                    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                        when(menuItem.itemId) {
                            R.id.sign_in,
                                R.id.sign_up -> {
                                AppAuth.getInstance().setAuth(5, "x-token")
                                true
                            }
                            R.id.logout -> {
                                AppAuth.getInstance().clearAuth()
                                true
                            } else -> {
                                false
                            }
                        }
                }.also {
                    currentMenuProvider = it
                })
            }

    }
    private fun requestNotificationsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return
        }
        val permission = Manifest.permission.POST_NOTIFICATIONS

        if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            return
        }
        requestPermissions(arrayOf(permission), 1)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(::println)
    }
}