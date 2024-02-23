package ru.netology.nmedia

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class NewPostResultContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent = Intent(context, NewPostFragment::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): String? = intent?.getStringExtra(Intent.EXTRA_TEXT)

}