package ru.netology.nmedia.factory

import android.os.Build
import androidx.annotation.RequiresApi
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Separator
import ru.netology.nmedia.repository.DateTimeRepository
import java.time.LocalDate
import javax.inject.Inject
import kotlin.random.Random

class TimeSeparatorsFactory @Inject constructor(dateTimeRepository: DateTimeRepository) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun create(previous: Post?, next: Post?): Separator? {
        return when {
            previous == null && next?.published == LocalDate.now().toString() ->
                Separator(id = Random.nextLong(), title = "Сегодня")

            previous?.published == LocalDate.now().toString() ||
                    previous == null && next?.published == LocalDate.now().toString() ->
                Separator(id = Random.nextLong(), title = "Вчера")

            else -> Separator(id = Random.nextLong(), title = "На прошлой неделе")
        }
    }
}