package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cat.fib.sharecommunity.data.models.Collection
import cat.fib.sharecommunity.data.source.CollectionRepository
import cat.fib.sharecommunity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    var collection : LiveData<Resource<Collection>>?= null
    var collections: LiveData<Resource<List<Collection>>>? = null

    fun getCollection(collectionName: String) {
        collection = collectionRepository.getCollection(collectionName)
    }

    fun getCollections() {
        collections = collectionRepository.getCollections()
    }
}