package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Program
import cat.fib.sharecommunity.utils.Resource

interface ProgramRepository {
    fun getProgram(programId: Int): LiveData<Resource<Program>>

    fun getPrograms(): LiveData<Resource<List<Program>>>
}