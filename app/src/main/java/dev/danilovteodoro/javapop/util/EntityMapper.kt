package util

interface EntityMapper<Entity,DomainModel> {

    fun mapFromEntity(e:Entity):DomainModel
    fun mapToEntity(d:DomainModel):Entity
}