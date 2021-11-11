package cat.fib.sharecommunity.di

import cat.fib.sharecommunity.data.api.*
import cat.fib.sharecommunity.data.source.*
import cat.fib.sharecommunity.data.source.CollectionRepository
import cat.fib.sharecommunity.data.source.CollectionRepositoryDefault
import cat.fib.sharecommunity.data.api.CollectionService
import cat.fib.sharecommunity.data.source.local.FitHausDatabase
import cat.fib.sharecommunity.utils.AppExecutors
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * The binding for ExerciseRepository has the default Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object ExerciseRepositoryModule {
    @Singleton
    @Provides
    fun provideExerciseRepository(
        exerciseService: ExerciseService,
        database: FitHausDatabase,
        appExecutors: AppExecutors
    ): ExerciseRepository {
        return ExerciseRepositoryDefault(
            database.exerciseDao(), exerciseService, appExecutors
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {
    @Singleton
    @Provides
    fun provideUserRepository(
        userService: UserService,
        database: FitHausDatabase,
        appExecutors: AppExecutors
    ): UserRepository {
        return UserRepositoryDefault(
            database.userDao(), userService, appExecutors
        )
    }
}

/**
 * The binding for ClassRepository has the default Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object ClassRepositoryModule {
    @Singleton
    @Provides
    fun provideClassRepository(
            classService: ClassService,
            database: FitHausDatabase,
            appExecutors: AppExecutors
    ): ClassRepository {
        return ClassRepositoryDefault(
                database.classDao(), classService, appExecutors
        )
    }
}


/**
 * The binding for PredefinedRoutineRepository has the default Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object PredefinedRoutineRepositoryModule {
    @Singleton
    @Provides
    fun providePredefinedRoutineRepository(
        predefinedRoutineService: PredefinedRoutineService,
        database: FitHausDatabase,
        appExecutors: AppExecutors
    ): PredefinedRoutineRepository {
        return PredefinedRoutineRepositoryDefault(
            database.predefinedRoutineDao(), predefinedRoutineService, appExecutors
        )
    }
}

/**
 * The binding for HealthDataRepository has the default Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object HealthDataRepositoryModule {
    @Singleton
    @Provides
    fun provideHealthDataRepository(
        healthDataService: HealthDataService,
        database: FitHausDatabase,
        appExecutors: AppExecutors
    ): HealthDataRepository {
        return HealthDataRepositoryDefault(
            database.healthDataDao(), healthDataService, appExecutors
        )
    }
}

/**
 * The binding for ProgramRepository has the default Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object ProgramRepositoryModule {
    @Singleton
    @Provides
    fun provideProgramRepository(
            programService: ProgramService,
            database: FitHausDatabase,
            appExecutors: AppExecutors
    ): ProgramRepository {
        return ProgramRepositoryDefault(
            database.programDao(), programService, appExecutors
        )
    }
}

/**
 * The binding for Collection has the default Repository.
 */
@Module
@InstallIn(SingletonComponent::class)
object CollectionRepositoryModule {
    @Singleton
    @Provides
    fun provideCollectionRepository(
            collectionService: CollectionService,
            database: FitHausDatabase,
            appExecutors: AppExecutors
    ): CollectionRepository {
        return CollectionRepositoryDefault(
            database.collectionDao(), collectionService, appExecutors
        )
    }
}