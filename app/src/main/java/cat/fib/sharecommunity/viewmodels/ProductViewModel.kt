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
    private val _products = MutableLiveData<Resource<ArrayList<Product>>>()
    private val _idproduct = MutableLiveData<Resource<String>>()
    val product: LiveData<Resource<Product>> = _product
    var products: LiveData<Resource<ArrayList<Product>>>? = _products
    var idproduct: LiveData<Resource<String>> = _idproduct

    fun createProduct(product: Product) {
        viewModelScope.launch {
            _product.value = FirebaseProductService.createProduct(product)
        }
    }

    fun getProduct(id: String, userEmail: String) {
        viewModelScope.launch {
            _product.value = FirebaseProductService.getProduct(id, userEmail)
        }
    }

    fun getAvailableProducts() {
        viewModelScope.launch {
            _products.value = FirebaseProductService.getAvailableProducts()
        }
    }

    fun getUserProducts(userEmail: String) {
        viewModelScope.launch {
            _products.value = FirebaseProductService.getUserProducts(userEmail)
        }
    }


    fun deleteProduct(id: String, userEmail: String) {
        viewModelScope.launch {
            _idproduct.value = FirebaseProductService.deleteProduct(id, userEmail)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            _product.value = FirebaseProductService.updateProduct(product)
        }
    }
}