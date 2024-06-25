package ru.netology.nmedia.repository

import java.time.Instant

interface DateTimeRepository {
    fun getCurrentDate(): Long
}