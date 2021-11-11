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
import cat.fib.sharecommunity.data.models.Program
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.PredefinedRoutineViewModel
import cat.fib.sharecommunity.viewmodels.ProgramViewModel
import dagger.hilt.android.AndroidEntryPoint


// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Classe FragmentConsultarPrograma
 *
 *  Classe on hi ha el fragment d'un programa
 *
 *  @constructor Inicialitza els camps d'informació amb les dades del programa consultat.
 *  @author Daniel Cárdenas.
 */

@AndroidEntryPoint
class FragmentConsultarPrograma : Fragment(), RecyclerViewNomAdapter.OnItemClickListener {
    private val viewModelPrograma by viewModels<ProgramViewModel>()

    private var identificadorPrograma: String? = null      // Identificador del programa
    private var programa: Program? = null                  // Model del programa

    lateinit var nom_programa: TextView
    lateinit var descripcio: TextView
    lateinit var nivell_text: TextView
    lateinit var nivell_asignat: TextView
    lateinit var setmanes_text: TextView
    lateinit var setmanes_asignades: TextView
    lateinit var llistat_rutines_text: TextView

    lateinit var recyclerView: RecyclerView
    lateinit var llistat_rutines: ArrayList<CardViewNom>

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        identificadorPrograma = activity?.intent?.getStringExtra(EXTRA_MESSAGE)
    }

    /** Funció onCreateView
     *
     *  Funció encarregada de configurar i mostrar el contingut del fragment
     *
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @return View?
     *  @author Daniel Cárdenas.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_consultar_programa, container, false)

        nom_programa = v.findViewById(R.id.NomProgramaConsultat)
        descripcio = v.findViewById(R.id.descripcióProgramaConsultat)
        nivell_text = v.findViewById(R.id.NivellProgramaText)
        nivell_asignat = v.findViewById(R.id.nivellProgramaConsultat)
        setmanes_text = v.findViewById(R.id.SetmanesProgramaText)
        setmanes_asignades = v.findViewById(R.id.setmanesProgramaConsultat)
        llistat_rutines_text = v.findViewById(R.id.RutinesText)

        recyclerView = v.findViewById(R.id.recycler_view)

        llistat_rutines = ArrayList<CardViewNom>()
        //identificadorPrograma = "3" // Eliminar aquesta línia de codi perquè s'està forçant el paràmetre que li ha d'arribar

        identificadorPrograma?.let {
            viewModelPrograma.getProgram(it.toInt())
        }


        viewModelPrograma.program?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                programa = it.data
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
        if (position < programa!!.predef_routines.size){
            // El CardViewItem clicat és una rutina
            val identificadorRutina = programa!!.predef_routines[position].toString()
            val intent = Intent(activity, ConsultarRutinaPredefinidaActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, identificadorRutina)
            }
            startActivity(intent)
        }
    }

    /** Funció setUpData
     *
     *  Funció que omple els camps del fragment amb les dades del programa corresponent.
     *
     *  @author Daniel Cárdenas.
     */
    private fun setUpData() {
        nom_programa.text = programa!!.name.toString()
        descripcio.text = programa!!.description.toString()
        nivell_asignat.text = levelName(programa!!.difficulty)
        println("difficulty: " + programa!!.difficulty)
        setmanes_asignades.text = programa!!.weeks.toString()
        setPredefinedRoutinesContent(0)
    }

    private fun levelName(level: String): String? {
        when (level) {
            "B" -> return "Principiant"
            "I" -> return "Intermedi"
            "A" -> return "Avançat"
            else -> return null
        }
    }

    /** Function setPredefinedRoutinesContent
     *
     *  Funció encarregada de generar la llista de CardViewItems amb la imatge i el nom de les rutines predefinides que formen el programa
     *
     *  @param  position
     *  @author Daniel Cárdenas
     */
    private fun setPredefinedRoutinesContent(position: Int) {
        if (position < programa!!.predef_routines.size){
            val viewModelRutines by viewModels<PredefinedRoutineViewModel>()   // ViewModel de les rutines predefinides del programa
            val identificadorRutina = programa!!.predef_routines[position]
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
     *  Funció encarregada d'omplir el RecyclerView amb la llista de CardViewItems que contenen la imatge i el nom de les rutines predefinides que formen el programa
     *
     *  @author Daniel Cárdenas
     */
    private fun setActivitiesContent(){
        recyclerView.adapter = RecyclerViewNomAdapter(llistat_rutines, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }


    private fun setExampleContent(){
        nom_programa.text = "Programa d'exemple"
        descripcio.text = "Descripció del programa d'exemple"
        nivell_asignat.text = "B"
        setmanes_asignades.text = "6"

        llistat_rutines = ArrayList<CardViewNom>()
        for (i in 0 until 500) {
            val imatge = when (i % 3) {
                0 -> "http://simpleicon.com/wp-content/uploads/android.png"
                1 -> "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/647px-Apple_logo_black.svg.png"
                else -> "https://icons-for-free.com/iconfiles/png/512/microsoft+windows+icon-1320186681671871370.png"
            }
            val item = CardViewNom("Item $i")
            llistat_rutines.plusAssign(item)
        }
        recyclerView.adapter = RecyclerViewNomAdapter(llistat_rutines, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }
}