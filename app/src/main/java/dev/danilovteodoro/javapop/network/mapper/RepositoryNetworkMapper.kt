package dev.danilovteodoro.javapop.network.mapper

import dev.danilovteodoro.javapop.model.Repository
import dev.danilovteodoro.javapop.network.RepositoryNetwork
import util.EntityMapper
import javax.inject.Inject

class RepositoryNetworkMapper @Inject constructor(
    val ownerNetworkMapper: OwnerNetworkMapper
) : EntityMapper<RepositoryNetwork,Repository> {

    override fun mapFromEntity(e: RepositoryNetwork): Repository {
        return Repository(
            id = e.id,
            name = e.name,
            description = e.description,
            owner = ownerNetworkMapper.mapFromEntity(e.owner),
            starCount = e.starCount,
            forksCount = e.forksCount
        )
    }

    override fun mapToEntity(d: Repository): RepositoryNetwork {
        return RepositoryNetwork(
            id = d.id,
            name = d.name,
            description = d.description,
            owner = ownerNetworkMapper.mapToEntity(d.owner),
            starCount = d.starCount,
            forksCount = d.forksCount
        )
    }

    fun mapFromEntityList(entities:List<RepositoryNetwork>) : List<Repository>{
        return entities.map { e -> mapFromEntity(e) }
    }
}