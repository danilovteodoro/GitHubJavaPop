package dev.danilovteodoro.javapop.network



data class RepositoryNetwork(
    val id:Long,
    val name:String,
    val description:String,
    val owner:OwnerNetwork,
    val starCount:Long,
    val forksCount:Long
)
