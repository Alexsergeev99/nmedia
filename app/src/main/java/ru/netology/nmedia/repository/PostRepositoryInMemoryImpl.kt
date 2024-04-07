package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    var nextId: Long = 0

    private var posts = listOf(
        Post(
        id = nextId++,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "1",
        likedByMe = false,
        likes = 0,
        shares = 0
    ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Делиться впечатлениями о любимых фильмах легко, а что если рассказать так, чтобы все заскучали \uD83D\uDE34\n",
            published = "2",
            likedByMe = false,
            likes = 0,
            shares = 0
        ),
        )
    private  val data = MutableLiveData(posts)

    override fun getAll(): List<Post> = TODO()

    override fun shareById(id: Long) {
        posts = posts.map{
            if(it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }
    override fun likeById(post: Post): Post {
//        posts = posts.map{
//            if(it.id != id) it else it.copy(likes = if (it.likedByMe) --it.likes else ++it.likes, likedByMe = !it.likedByMe)
//        }
//        data.value = posts
        TODO()
    }

    override fun removeById(id: Long) {
        posts = posts.filter{it.id != id}
        data.value = posts
    }

    override fun save(post: Post){
//        if (post.id == 0L) {
//            posts = listOf(
//                post.copy(
//                    id = nextId++,
//                    author = "Нетология. Университет интернет-профессий будущего",
//                    published = "now",
//                    likedByMe = false,
//                    likes = 0,
//                    shares = 0,
//                    video = null
//                )
//            ) + posts
//        } else {
//            posts = posts.map{
//                if(it.id != post.id) it else it.copy(content = post.content)
//            }
//        }
//        data.value = posts
//        return
        TODO()
    }

}