package person.app.data.local

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import person.app.data.DefaultPersonRepository
import person.app.data.PersonRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton // Dagger will make sure in this scenario that this is single instance
    @Binds // This will bind all places Injecting dogRepository interface to the Default implementation
    fun bindsPersonRepository(
        personRepository: DefaultPersonRepository
    ): PersonRepository
}