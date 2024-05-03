package ru.netology.nmedia.dao

import android.icu.text.UnicodeSet.CASE
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity
import androidx.room.OnConflictStrategy


@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

//    @Query("UPDATE PostEntity SET content = :text WHERE id = :id")
//    fun changeContentById(id: Long, text: String)
//    suspend fun save(post: PostEntity) = if(post.id == 0L) insert(post) else changeContentById(post.id, post.content)

    @Query( "UPDATE PostEntity SET likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END, likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END WHERE id = :id")
    suspend fun likeById(id: Long)

    @Query(" UPDATE PostEntity SET\n" +
            "shares = shares + 1\n" +
            "WHERE id = :id")
    suspend fun shareById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)
}