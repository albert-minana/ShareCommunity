package cat.fib.sharecommunity.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import cat.fib.sharecommunity.viewmodels.PredefinedRoutineViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_consultar_coleccio.*


// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Classe FragmentConsultarColeccio
 *
 *  Classe on hi ha el fragment d'una col·lecció
 *
 *  @constructor Inicialitza els camps d'informació amb les dades de la col·lecció consultada.
 *  @author Daniel Cárdenas.
 */

@AndroidEntryPoint
class FragmentConsultarColeccio : Fragment(), RecyclerViewNomAdapter.OnItemClickListener {
    private val viewModelCollection by viewModels<CollectionViewModel>()

    private var identificadorCollection: String? = null      // Identificador de la col·lecció
    private var coleccio: Collection? = null                 // Model de la col·lecció

    lateinit var nom_coleccio: TextView
    lateinit var descripcio_coleccio: TextView
    lateinit var llistat_rutines_text: TextView
    lateinit var llistat_rutines: ArrayList<CardViewNom>
    lateinit var recyclerView: RecyclerView


    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        identificadorCollection = activity?.intent?.getStringExtra(EXTRA_MESSAGE)
    }

    /** Funció inicialitzadora
     *
     *  Funció que inicialitza les variables amb el valor corresponent segons la col·lecció consultada.
     *
     *  @param inflater, container, savedInstanceState
     *  @return View?
     *  @author Daniel Cárdenas.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_consultar_coleccio, container, false)

        nom_coleccio = v.findViewById(R.id.NomColleccioConsultada)
        descripcio_coleccio = v.findViewById(R.id.descripcióColleccioConsultada)
        llistat_rutines_text = v.findViewById(R.id.RutinesText)
        recyclerView = v.findViewById(R.id.recycler_view)

        llistat_rutines = ArrayList<CardViewNom>()

        // identificadorCollection = "3" // Eliminar aquesta línia de codi perquè s'està forçant el paràmetre que li ha d'arribar

        identificadorCollection?.let {
            viewModelCollection.getCollection(it)
        }

        viewModelCollection.collection?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                coleccio = it.data
                setUpData()
            }
            else if (it.status == Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return v
    }

    /** Function onItemClick
     *
     *  Funció encarregada de configurar el comportament del clic en un CardViewItem del RecyclerView
     *
     *  @param position
     *  @author Daniel Cárdenas
     */
    override fun onItemClick(position: Int) {
        if (position < coleccio!!.predef_routines.size){
            // El CardViewItem clicat és una rutina
            val identificadorRutina = coleccio!!.predef_routines[position].toString()
            val intent = Intent(activity, ConsultarRutinaPredefinidaActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, identificadorRutina)
            }
            startActivity(intent)
        }
    }


    /** Funció setUpData
     *
     *  Funció que omple els camps del fragment amb les dades de la col·lecció corresponent.
     *
     *  @param collectionData
     *  @author Daniel Cárdenas.
     */
    fun setUpData() {
        nom_coleccio.text = coleccio!!.name.toString()
        descripcio_coleccio.text = coleccio!!.description.toString()
        setPredefinedRoutinesContent(0)
    }


    /** Function setPredefinedRoutinesContent
     *
     *  Funció encarregada de generar la llista de CardViewItems amb la imatge i el nom de les rutines predefinides que formen la col·lecció
     *
     *  @param  position
     *  @author Daniel Cárdenas
     */
    private fun setPredefinedRoutinesContent(position: Int) {
        if (position < coleccio!!.predef_routines.size){
            val viewModelRutines by viewModels<PredefinedRoutineViewModel>()   // ViewModel de les rutines predefinides de la col·lecció
            val identificadorRutina = coleccio!!.predef_routines[position]
            viewModelRutines.getPredefinedRoutine(identificadorRutina)
            viewModelRutines.predefinedRoutine?.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS){
                    val item = CardViewNom(it.data!!.name + " (Rutina)")
                    llistat_rutines.plusAssign(item)
                    setPredefinedRoutinesContent(position+1)
                }
                else if (it.status == Status.ERROR)
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
            })
        }
        else setActivitiesContent()
    }

    /** Function setActivitiesContent
     *
     *  Funció encarregada d'omplir el RecyclerView amb la llista de CardViewItems que contenen la imatge i el nom de les rutines predefinides que formen la col·lecció
     *
     *  @author Daniel Cárdenas
     */
    private fun setActivitiesContent(){
        recyclerView.adapter = RecyclerViewNomAdapter(llistat_rutines, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }



    private fun setExampleContent(){
        nom_coleccio.text = "Col·lecció d'exemple"
        descripcio_coleccio.text = "Descripció de la col·lecció d'exemple"

        llistat_rutines = ArrayList<CardViewNom>()
        for (i in 0 until 500) {
            val imatge = when (i % 3) {
                0 -> "http://simpleicon.com/wp-content/uploads/android.png"
                1 -> "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/647px-Apple_logo_black.svg.png"
                else -> "https://icons-for-free.com/iconfiles/png/512/microsoft+windows+icon-1320186681671871370.png"
            }
            val item = CardViewNom( "Item $i")
            llistat_rutines.plusAssign(item)
        }
        recyclerView.adapter = RecyclerViewNomAdapter(llistat_rutines, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }
}