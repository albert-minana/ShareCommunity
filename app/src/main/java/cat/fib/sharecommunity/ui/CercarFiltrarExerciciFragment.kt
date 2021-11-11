package cat.fib.sharecommunity.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.fib.sharecommunity.*
import cat.fib.sharecommunity.data.models.Exercise
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.ExerciseViewModel
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Fragment CercarFiltrarExercici
 *
 *  Fragment encarregat de cercar un exercici mitjançant un filtre
 *
 *  @constructor Crea el Fragment CercarFiltrarExercici
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class CercarFiltrarExerciciFragment : Fragment(), RecyclerViewAdapter.OnItemClickListener {

    private val viewModel by viewModels<ExerciseViewModel>()    // ViewModel dels exercicis

    private var llistatExercicis: List<Exercise>? = null        // Llistat del model exercici

    lateinit var recyclerView: RecyclerView                     // RecyclerView de CardViewItems que contenen la imatge i el nom de tot el conjunt d'exercicis
    lateinit var list: ArrayList<CardViewItem>                  // Llistat de CardViewItems que contenen la imatge i el nom de tot el conjunt d'exercicis

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
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
     *  @author Albert Miñana Montecino
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_cercar_filtrar_exercici, container, false)

        //llistatExercicis = ArrayList<Exercise>()

        recyclerView = view.findViewById(R.id.recycler_view)
        list = ArrayList<CardViewItem>()

        viewModel.getExercises()
        viewModel.exercises?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                llistatExercicis = it.data
                setContent()
            }
            else if (it.status == Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada de generar la llista de CardViewItems amb la imatge i el nom de tot el conjunt d'exercicis
     *
     *  @author Albert Miñana Montecino
     */
    private fun setContent(){
        for (i in llistatExercicis!!) {
            val imatge = cat.fib.sharecommunity.data.api.Configuration.urlServer + i.pre
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
     *  @author Albert Miñana Montecino
     */
    override fun onItemClick(position: Int) {
        val nomIdentificadorExercici = llistatExercicis!![position].name
        val intent = Intent(activity, ConsultarExerciciActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, nomIdentificadorExercici)
        }
        startActivity(intent)
    }

}