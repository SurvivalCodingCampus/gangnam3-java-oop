package com.survivalcoding.day15_network

import com.survivalcoding.day15_network.datasource.*
import com.survivalcoding.day15_network.model.Post
import com.survivalcoding.day15_network.model.Todo
import com.survivalcoding.day15_network.repository.*
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    // val client = HttpClient(CIO)
    // val response = client.get("https://jsonplaceholder.typicode.com/todos/1")
    // println(response.bodyAsText())

    val dataSource: TodoDataSource = TodoDataSourceImpl()

    // HTTP Method - GET
    // println(datasource.getTodos())
    // println(datasource.getTodo(id = 1))

    // HTTP Method - POST
    // println(datasource.postTodo(request = CreateTodoRequest(userId = 1, title = "네트워크 학습")))

    // HTTP Method - PUT
    // println(datasource.putTodo(id = 1, request = UpdateTodoRequest(userId = 1, title = "네트워크 학습", completed = true)))

    // HTTP Method - PATCH
    // println(datasource.patchTodo(id = 1, request = PatchTodoRequest(title = "Network Study")))

    // HTTP Method - DELETE
    // datasource.deleteTodo(id = 1)

    val repository: TodoRepository = TodoRepositoryImpl(dataSource)

    // HTTP Method - GET
    // println(repository.getTodos())
    println(repository.getTodo(id = 1))

    // HTTP Method - POST
    println(repository.addTodo(userId = 1, title = "네트워크 학습"))

    // HTTP Method - PUT
    println(repository.updateTodo(todo = Todo(id = 1, userId = 1, title = "네트워크 학습", completed = true)))

    // HTTP Method - PATCH
    println(repository.updateTitle(id = 1, title = "Network Study"))

    // HTTP Method - DELETE
    println(repository.removeTodo(id = 1))

    println("===== 연습문제 3 =====")

    val postDataSource: PostDataSource = RemoteDataSource()
    val postRepository: PostRepository = PostRepositoryImpl(postDataSource)

    // HTTP Method - GET
    // println(postRepository.getPosts())
    println(postRepository.getPost(id = 1))
    println(postRepository.getPostsByKeyword("lorem").size)

    // HTTP Method - POST
    println(postRepository.createPost(post = Post(userId = 1, title = "네트워크 학습", body = "어려움")))

    // HTTP Method - PUT
    println(postRepository.updatePost(post = Post(id = 1, userId = 1, title = "Network Study", body = "Difficult")))

    // HTTP Method - DELETE
    println(postRepository.deletePost(id = 1))

    println("===== 연습문제 4 =====")

    val imageDataSource: ImageDataSource = ImageDataSourceImpl()
    val imageRepository: ImageRepository = ImageRepositoryImpl(imageDataSource)

    val pokemonUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon"

    imageRepository.saveImage(
        url = "$pokemonUrl/1.png",
        path = "images/이상해씨.png",
    )
    imageRepository.saveImages(
        urls = listOf("$pokemonUrl/1.png", "$pokemonUrl/2.png", "$pokemonUrl/3.png"),
        directory = "images/pokemons"
    )

    val isNotExists = imageRepository.saveImageIfNotExists(
        url = "$pokemonUrl/1.png",
        path = "images/이상해씨.png"
    )

    println("이미지 유무: ${!isNotExists}")

    println("===== 과제 - 포켓몬 도감 정보 가져오기 =====")

    val pokeDataSource: PokeDataSource = PokeDataSourceImpl()
    val pokeRepository: PokeRepository = PokeRepositoryImpl(pokeDataSource)

    println(pokeRepository.getPokemon("pikachu"))
}