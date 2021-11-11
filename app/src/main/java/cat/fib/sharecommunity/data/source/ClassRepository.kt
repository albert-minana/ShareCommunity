package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Class
import cat.fib.sharecommunity.utils.Resource

interface ClassRepository {
    fun getClass(className: String): LiveData<Resource<Class>>

    fun getClasses(): LiveData<Resource<List<Class>>>
}