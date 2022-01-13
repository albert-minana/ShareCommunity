package cat.fib.sharecommunity.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import cat.fib.sharecommunity.CardViewItem
import cat.fib.sharecommunity.CardViewNom
import cat.fib.sharecommunity.ConsultarProducteActivity
//import cat.fib.sharecommunity.R
//import cat.fib.sharecommunity.RecyclerViewAdapter
import cat.fib.sharecommunity.*
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cercar_filtrar_producte.*

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE_1 = "cat.fib.sharecommunity.MESSAGE1"
private const val EXTRA_MESSAGE_2 = "cat.fib.sharecommunity.MESSAGE2"

/** Fragment CercarFiltrarService
 *
 *  Fragment encarregat de cercar un servei mitjançant un filtre
 *
 *  @constructor Crea el Fragment CercarFiltrarService
 *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
 */
@AndroidEntryPoint
class CercarFiltrarServiceFragment : Fragment(), RecyclerViewNomAdapter.OnItemClickListener {

    private val viewModel by viewModels<ServiceViewModel>()       // ViewModel dels serveis

    private var llistatServices: ArrayList<Service>? = null             // Llistat del model serveis

    lateinit var recyclerView: RecyclerView                     // RecyclerView de CardViewItems que contenen la imatge i el nom de tot el conjunt de productes
    lateinit var list: ArrayList<CardViewNom>                  // Llistat de CardViewItems que contenen la imatge i el nom de tot el conjunt de productes
                                                                //POTSER NO CALEN

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
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cercar_filtrar_service, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        list = ArrayList<CardViewNom>()

        viewModel.getAvailableServices()
        viewModel.services?.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                llistatServices = it.data
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
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    private fun setContent(){
        for (i in llistatServices!!) {
            //val imatge = i.photo!!
            val nom = i.name
            //val item = CardViewItem(imatge, nom)
            val item = CardViewNom(nom)
            list.plusAssign(item)
        }
        recyclerView.adapter = RecyclerViewNomAdapter(list, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    /** Function onItemClick
     *
     *  Funció encarregada de configurar el comportament del clic en un CardViewItem del RecyclerView
     *
     *  @param position
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    override fun onItemClick(position: Int) {

        val idService = llistatServices!![position].id
        val userEmailProduct = llistatServices!![position].userEmail
        val intent = Intent(activity, ConsultarServiceActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE_1, idService)
            putExtra(EXTRA_MESSAGE_2, userEmailProduct)
        }
        startActivity(intent)

    }
}