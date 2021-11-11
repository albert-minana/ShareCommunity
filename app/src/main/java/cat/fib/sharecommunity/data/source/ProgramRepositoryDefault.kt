package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.api.ProgramService
import cat.fib.sharecommunity.data.models.Program
import cat.fib.sharecommunity.data.source.local.ProgramDao
import cat.fib.sharecommunity.utils.AppExecutors
import cat.fib.sharecommunity.utils.Resource

/**
 * Default implementation of [ProgramRepository].
 */
class ProgramRepositoryDefault (
    private val programDao: ProgramDao,
    private val programService: ProgramService,
    private val appExecutors: AppExecutors,
) : ProgramRepository {
    override fun getProgram(programId: Int): LiveData<Resource<Program>> {
        return object : NetworkBoundResource<Program, Program>(appExecutors) {
            override fun saveCallResult(item: Program) {
                programDao.insertProgram(item)
            }

            override fun shouldFetch(data: Program?) = data == null

            override fun loadFromDb() = programDao.getProgramById(programId)

            override fun createCall() = programService.getProgram(programId)
        }.asLiveData()


    }

    override fun getPrograms(): LiveData<Resource<List<Program>>> {
        return object : NetworkDatabaseResource<List<Program>, List<Program>>(appExecutors) {
            override fun saveCallResult(items: List<Program>) {
                for (i in items) {
                    programDao.insertProgram(i)
                }
            }

            override fun loadFromDb() = programDao.getPrograms()

            override fun createCall() = programService.getPrograms()
        }.asLiveData()
    }

}