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
    private val _services = MutableLiveData<Resource<ArrayList<Service>>>()
    private val _idservei = MutableLiveData<Resource<String>>()
    val service: LiveData<Resource<Service>> = _service
    var services: LiveData<Resource<ArrayList<Service>>>? = _services
    var idservei: LiveData<Resource<String>> = _idservei

    fun createService(service: Service) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.createService(service)
        }
    }

    fun getService(id: String, userEmail: String) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.getService(id, userEmail)
        }
    }

    fun getAvailableServices() {
        viewModelScope.launch {
            _services.value = FirebaseServiceService.getAvailableServices()
        }
    }

    fun deleteService(id: String, userEmail: String) {
        viewModelScope.launch {
            _idservei.value = FirebaseServiceService.deleteService(id, userEmail)
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            _service.value = FirebaseServiceService.updateService(service)
        }
    }

}