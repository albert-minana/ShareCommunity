package cat.fib.sharecommunity.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.R
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Fragment ConsultarClasse
 *
 *  Fragment encarregat de consultar la informació completa d'una classe
 *
 *  @constructor Crea el Fragment ConsultarClasse
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class ConsultarClasseFragment : Fragment() {
/*
    private val viewModel by viewModels<ClassViewModel>()    // ViewModel de la classe

    private var nomIdentificadorClasse: String? = null  // Nom identificador de la classe

    lateinit var imatgeClasse: ImageView                // ImageView amb la imatge de previsualització de la classe
    lateinit var nomClasse: TextView                    // TextView amb el nom de la classe
    lateinit var contingutEntrenadorClasse: TextView    // TextView amb l'entrenador de la classe
    lateinit var contingutDescripcioClasse: TextView    // TextView amb la descripció de la classe
    lateinit var contingutAreaTreballClasse: TextView   // TextView amb l'àrea de treball de la classe
    lateinit var contingutEdatClasse: TextView          // TextView amb l'edat de la classe
    lateinit var contingutDificultatClasse: TextView    // TextView amb la dificultat de la classe
    lateinit var contingutDuracioClasse: TextView       // TextView amb la duració de la classe
    lateinit var contingutCategoriaClasse: TextView     // TextView amb la categoria de la classe

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nomIdentificadorClasse = activity?.intent?.getStringExtra(EXTRA_MESSAGE)
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
        val view: View = inflater.inflate(R.layout.fragment_consultar_classe, container, false)

        imatgeClasse = view.findViewById(R.id.imatgeClasse)
        nomClasse = view.findViewById(R.id.nomClasse)
        contingutEntrenadorClasse = view.findViewById(R.id.contingutEntrenadorClasse)
        contingutDescripcioClasse = view.findViewById(R.id.contingutDescripcioClasse)
        contingutAreaTreballClasse = view.findViewById(R.id.contingutAreaTreballClasse)
        contingutEdatClasse = view.findViewById(R.id.contingutEdatClasse)
        contingutDificultatClasse = view.findViewById(R.id.contingutDificultatClasse)
        contingutDuracioClasse = view.findViewById(R.id.contingutDuracioClasse)
        contingutCategoriaClasse = view.findViewById(R.id.contingutCategoriaClasse)

        //nomIdentificadorClasse = "3" // Eliminar aquesta línia de codi perquè s'està forçant el paràmetre que li ha d'arribar

        nomIdentificadorClasse?.let {
            viewModel.getClass(it)
        }

        viewModel.classe?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS)
                setContent(it.data)
            else if (it.status == Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'una classe
     *
     *  @param  classData
     *  @author Albert Miñana Montecino
     */
    fun setContent(classData: Class?){
        Picasso.get().load(Configuration.Companion.urlServer + classData?.pre.toString()).into(imatgeClasse)
        nomClasse.text = classData?.name.toString()
        contingutEntrenadorClasse.text = classData?.trainer.toString()
        contingutDescripcioClasse.text = classData?.description.toString()
        contingutAreaTreballClasse.text = workareaName(classData?.workarea.toString())
        contingutEdatClasse.text = ageName(classData?.age.toString())
        contingutDificultatClasse.text = difficultyName(classData?.difficulty.toString())
        contingutDuracioClasse.text = classData?.length.toString()
        contingutCategoriaClasse.text = categoriesName(classData?.categories!!)
    }

    private fun workareaName(workarea: String): String? {
        when (workarea) {
            "UB" -> return "Tren superior"
            "LB" -> return "Tren inferior"
            "FB" -> return "Cos sencer"
            "C" -> return "Core"
            else -> return null
        }
    }

    private fun categoriesName(categories: ArrayList<String>): String? {
        var categoriesString: ArrayList<String> = ArrayList()
        if (categories.contains("S")) categoriesString.add("Força")
        if (categories.contains("C")) categoriesString.add("Càrdio")
        if (categories.contains("Y")) categoriesString.add("Ioga")
        if (categories.contains("E")) categoriesString.add("Estiraments")
        if (categories.contains("R")) categoriesString.add("Rehabilitació")
        if (categories.contains("P")) categoriesString.add("Pilates")
        return categoriesString.joinToString()
    }

    private fun difficultyName(difficulty: String): String? {
        when (difficulty) {
            "E" -> return "Fàcil"
            "M" -> return "Mitjana"
            "H" -> return "Difícil"
            else -> return null
        }
    }

    private fun ageName(age: String): String? {
        when (age) {
            "K" -> return "Nen"
            "T" -> return "Gent jove"
            "A" -> return "Adult"
            "E" -> return "Gent gran"
            else -> return null
        }
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir un contingut d'exemple amb la informació completa d'una classe
     *
     *  @author Albert Miñana Montecino
     */
    fun setContent(){
        imatgeClasse.setImageResource(R.drawable.fitness_home)
        nomClasse.text = "Classe d'estiraments"
        contingutEntrenadorClasse.text = "Albert"
        contingutDescripcioClasse.text = "Es realitzen una gran varietat d'estiraments, pensats pel pre i post entrenament."
        contingutAreaTreballClasse.text = "Cos sencer"
        contingutEdatClasse.text = "Adult"
        contingutDificultatClasse.text = "Fàcil"
        contingutDuracioClasse.text = "15 minuts"
        contingutCategoriaClasse.text = "Estiraments"
    }
*/
}