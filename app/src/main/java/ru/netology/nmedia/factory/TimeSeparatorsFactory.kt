package ru.netology.nmedia.factory

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Separator
import ru.netology.nmedia.repository.DateTimeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

class TimeSeparatorsFactory @Inject constructor(dateTimeRepository: DateTimeRepository) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun create(previous: Post?, next: Post?): Separator? {
        return when {
            previous == null && next?.published == next?.published?.let {
                DateUtils.isToday(it.toLong()).toString()
            } ->
                Separator(id = Random.nextLong(), title = "Сегодня")

            previous?.published == previous?.published?.let {
                DateUtils.isToday(it.toLong()).toString()
            } ||
                    previous == null &&
                    next?.published == next?.published?.let {
                DateUtils.isToday(it.toLong() + DateUtils.DAY_IN_MILLIS).toString()
            } ->
                Separator(id = Random.nextLong(), title = "Вчера")

            previous?.published == previous?.published?.let {
                DateUtils.isToday(it.toLong() + DateUtils.DAY_IN_MILLIS).toString()
            } ||
                    previous == null &&
                    next?.published == next?.published?.let {
                DateUtils.isToday(it.toLong() + DateUtils.DAY_IN_MILLIS + DateUtils.DAY_IN_MILLIS)
                    .toString()
            } ->
                Separator(id = Random.nextLong(), title = "На прошлой неделе")

            else -> null
        }
    }
}