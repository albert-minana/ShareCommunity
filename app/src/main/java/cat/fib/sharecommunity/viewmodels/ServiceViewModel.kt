package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.firebaseserviceobjects.FirebaseServiceService
import kotlinx.coroutines.launch

class ServiceViewModel : ViewModel() {
    private val _service = MutableLiveData<Resource<Service>>()
    private val _service = MutableLiveData<Resource<ArrayList<Service>>>()
    val service: LiveData<Resource<Service>> = _service
    var service: LiveData<Resource<ArrayList<Service>>>? = _service

    fun createService(service: Service) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.createService(service)
        }
    }

    fun getService(userEmail: String, id: String) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.getService(userEmail, id)
        }
    }

    fun getAvailableProducts() {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.getAvailableService()
        }
    }
}