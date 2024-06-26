package ru.netology.nmedia.repository

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import javax.inject.Inject

class DateTimeRepositoryImpl @Inject constructor() : DateTimeRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCurrentDate(): Long = LocalDate.now().toString()
        .toLong() // Чтобы не ругалось, нужно desugaring https://developer.android.com/studio/write/java8-support#library-desugaring
}