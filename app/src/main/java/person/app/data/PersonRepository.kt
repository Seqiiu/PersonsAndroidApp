package person.app.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import person.app.data.local.database.UserEntityDao
import person.app.data.local.database.UserEntity
import person.app.data.models.Address
import person.app.data.models.Company
import person.app.data.models.User
import javax.inject.Inject

interface PersonRepository {
    val users: Flow<List<User>>

    suspend fun add(person: User)
}

class DefaultPersonRepository @Inject constructor(
    private val userDao: UserEntityDao
) : PersonRepository {

    override val users: Flow<List<User>> =
        userDao.getAllUsers().map { items ->
            items.map {
                it.toUser()
            }
        }

    override suspend fun add(person: User) {
        val exists = userDao.exists(person.id)
        if (!exists) {
            userDao.insertUser(
                UserEntity(
                    id = person.id,
                    name = person.name,
                    username = person.username,
                    email = person.email,
                    street = person.address.street,
                    suite = person.address.suite,
                    city = person.address.city,
                    zipcode = person.address.zipcode,
                    phone = person.phone,
                    website = person.website,
                    companyName = person.company.name,
                    companyCatchPhrase = person.company.catchPhrase
                )
            )
        } else {
            println("User with id ${person.id} already exists.")
        }
    }

    private fun UserEntity.toUser(): User {
        return User(
            id = this.id,
            name = this.name,
            username = this.username,
            email = this.email,
            address = Address(
                street = this.street,
                suite = this.suite,
                city = this.city,
                zipcode = this.zipcode
            ),
            phone = this.phone,
            website = this.website,
            company = Company(
                name = this.companyName,
                catchPhrase = this.companyCatchPhrase
            )
        )
    }
}