package dev.danilovteodoro.javapop.model

data class Repository(
    val id:Long,
    val name:String,
    val description:String,
    val owner: RepositoryOwner
)
