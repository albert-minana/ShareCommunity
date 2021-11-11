package cat.fib.sharecommunity.data.source

import androidx.lifecycle.LiveData
import cat.fib.sharecommunity.data.models.Collection
import cat.fib.sharecommunity.utils.Resource

interface CollectionRepository {

    fun getCollection(collectionName: String): LiveData<Resource<Collection>>

    fun getCollections(): LiveData<Resource<List<Collection>>>
}