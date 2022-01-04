package cat.fib.sharecommunity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.firebaseserviceobjects.FirebaseProductService
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _product = MutableLiveData<Resource<Product>>()
    val product: LiveData<Resource<Product>> = _product
    var productes: LiveData<Resource<List<Product>>>? = null

     fun getProduct(id: String) {
        viewModelScope.launch {
            _product.value = FirebaseProductService.getProductData(id)
        }
    }

    fun getProductes() {
        viewModelScope.launch {
            _product.value = FirebaseProductService.getProductes()
        }
    }

    fun createProduct(product: Product) {
        viewModelScope.launch {
            _product.value = FirebaseProductService.createProduct(product)
        }
    }
}