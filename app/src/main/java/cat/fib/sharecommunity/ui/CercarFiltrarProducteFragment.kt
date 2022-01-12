package cat.fib.sharecommunity.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.fib.sharecommunity.CardViewItem
import cat.fib.sharecommunity.ConsultarProducteActivity
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.RecyclerViewAdapter
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE_1 = "cat.fib.sharecommunity.MESSAGE1"
private const val EXTRA_MESSAGE_2 = "cat.fib.sharecommunity.MESSAGE2"

/** Fragment CercarFiltrarProducte
 *
 *  Fragment encarregat de cercar un producte mitjançant un filtre
 *
 *  @constructor Crea el Fragment CercarFiltrarProducte
 *  @author Daniel Cárdenas Rafael
 */
@AndroidEntryPoint
class CercarFiltrarProducteFragment : Fragment(), RecyclerViewAdapter.OnItemClickListener {

    private val viewModel by viewModels<ProductViewModel>()       // ViewModel dels productes

    private var llistatProductes: ArrayList<Product>? = null             // Llistat del model producte

    lateinit var recyclerView: RecyclerView                     // RecyclerView de CardViewItems que contenen la imatge i el nom de tot el conjunt de productes
    lateinit var list: ArrayList<CardViewItem>                  // Llistat de CardViewItems que contenen la imatge i el nom de tot el conjunt de productes

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /** Function onCreateView
     *
     *  Funció encarregada de configurar i mostrar el contingut del fragment
     *
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cercar_filtrar_producte, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        list = ArrayList<CardViewItem>()

        viewModel.getAvailableProducts()
        viewModel.products?.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                llistatProductes = it.data
                System.out.println(it.data)
                setContent()
            }
            else if (it.status == Resource.Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada de generar la llista de CardViewItems amb la imatge i el nom de tot el conjunt de productes
     *
     *  @author Daniel Cárdenas Rafael
     */
    private fun setContent(){
        for (i in llistatProductes!!) {
            val imatge = i.photo!!
            val nom = i.name
            val item = CardViewItem(imatge, nom)
            list.plusAssign(item)
        }
        recyclerView.adapter = RecyclerViewAdapter(list, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    /** Function onItemClick
     *
     *  Funció encarregada de configurar el comportament del clic en un CardViewItem del RecyclerView
     *
     *  @param position
     *  @author Daniel Cárdenas Rafael
     */
    override fun onItemClick(position: Int) {
        val idProduct = llistatProductes!![position].id
        val userEmailProduct = llistatProductes!![position].userEmail
        val intent = Intent(activity, ConsultarProducteActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE_1, idProduct)
            putExtra(EXTRA_MESSAGE_2, userEmailProduct)
        }
        startActivity(intent)

    }
}