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
    val service: LiveData<Resource<Service>> = _service
    var services: LiveData<Resource<List<Service>>>? = null

    fun getService(id: String) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.getServiceData(id)
        }
    }

    fun getServices() {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.getServices()
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.createServices(service)
        }
    }
}