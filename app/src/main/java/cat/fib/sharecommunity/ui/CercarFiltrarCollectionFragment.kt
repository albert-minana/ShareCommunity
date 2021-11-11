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
import cat.fib.sharecommunity.*
import cat.fib.sharecommunity.data.models.Collection
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.CollectionViewModel
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Fragment CercarFiltrarCollection
 *
 *  Fragment encarregat de cercar una col·lecció mitjançant un filtre
 *
 *  @constructor Crea el Fragment CercarFiltrarCollection
 *  @author Daniel Cárdenas
 */
@AndroidEntryPoint
class CercarFiltrarCollectionFragment : Fragment(), RecyclerViewNomAdapter.OnItemClickListener {

    private val viewModel by viewModels<CollectionViewModel>()    // ViewModel de les col·leccions

    private var llistatColeccions: List<Collection>? = null        // Llistat del model col·lecció

    lateinit var recyclerView: RecyclerView                         // RecyclerView de Strings que contenen el nom de tot el conjunt de col·leccions
    lateinit var list: ArrayList<CardViewNom>                       // Llistat de strings que contenen el nom de tot el conjunt de col·leccions

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas
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
     *  @author Daniel Cárdenas
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cercar_filtrar_coleccio, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
        list = ArrayList<CardViewNom>()

        viewModel.getCollections()
        viewModel.collections?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                llistatColeccions= it.data
                setContent()
            }
            else if (it.status == Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada de generar la llista de noms de tot el conjunt de col·leccions
     *
     *  @author Daniel Cárdenas
     */
    private fun setContent(){
        for (i in llistatColeccions!!) {
            val nom = i.name
            val item = CardViewNom(nom)
            list.plusAssign(item)
        }
        recyclerView.adapter = RecyclerViewNomAdapter(list, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    /** Function onItemClick
     *
     *  Funció encarregada de configurar el comportament del clic en un nom del RecyclerView
     *
     *  @param position
     *  @author Daniel Cárdenas
     */
    override fun onItemClick(position: Int) {
        val nomIdentificadorColeccio = llistatColeccions!![position].name
        val intent = Intent(activity, ConsultarColeccioActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, nomIdentificadorColeccio)
        }
        startActivity(intent)
    }

}