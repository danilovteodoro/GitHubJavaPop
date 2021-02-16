package dev.danilovteodoro.javapop.network.mapper

import dev.danilovteodoro.javapop.model.RepositoryOwner
import dev.danilovteodoro.javapop.network.OwnerNetwork
import util.EntityMapper
import javax.inject.Inject

class OwnerNetworkMapper @Inject constructor() : EntityMapper<OwnerNetwork,RepositoryOwner>{

    override fun mapFromEntity(e: OwnerNetwork): RepositoryOwner {
        return RepositoryOwner(
            id = e.id,
            login = e.login,
            avatarUrl = e.avatarUrl
        )
    }

    override fun mapToEntity(d: RepositoryOwner): OwnerNetwork {
        return OwnerNetwork(
            id = d.id,
            login = d.login,
            avatarUrl = d.avatarUrl
        )
    }
}