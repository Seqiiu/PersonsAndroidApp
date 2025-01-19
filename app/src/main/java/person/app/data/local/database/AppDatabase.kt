package person.app.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class], version = 2, autoMigrations = [
    AutoMigration(from = 1, to = 2)
])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserEntityDao
}