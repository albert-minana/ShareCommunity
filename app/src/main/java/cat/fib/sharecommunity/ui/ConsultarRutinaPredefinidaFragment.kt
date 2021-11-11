package cat.fib.sharecommunity.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.fib.sharecommunity.*
import cat.fib.sharecommunity.data.api.Configuration
import cat.fib.sharecommunity.data.models.PredefinedRoutine
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.ClassViewModel
import cat.fib.sharecommunity.viewmodels.ExerciseViewModel
import cat.fib.sharecommunity.viewmodels.PredefinedRoutineViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Fragment ConsultarRutinaPredefinida
 *
 *  Fragment encarregat de consultar la informació completa d'una rutina predefinida
 *
 *  @constructor Crea el Fragment ConsultarRutinaPredefinida
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class ConsultarRutinaPredefinidaFragment : Fragment(), RecyclerViewAdapter.OnItemClickListener {

    private val viewModelRutinaPredefinida by viewModels<PredefinedRoutineViewModel>()      // ViewModel de la rutina predefinida

    private var identificadorRutinaPredefinida: String? = null      // Identificador de la rutina predefinida
    private var rutinaPredefinida: PredefinedRoutine? = null        // Model de la rutina predefinida

    lateinit var imatgeRutinaPredefinida: ImageView                 // ImageView amb la imatge de previsualització de la rutina predefinida
    lateinit var nomRutinaPredefinida: TextView                     // TextView amb el nom de la rutina predefinida
    lateinit var contingutDescripcioRutinaPredefinida: TextView     // TextView amb la descripció de la rutina predefinida
    lateinit var contingutCategoriaRutinaPredefinida: TextView      // TextView amb la categoria de la rutina predefinida
    lateinit var contingutTempsRutinaPredefinida: TextView          // TextView amb el temps de la rutina predefinida
    lateinit var contingutEdatRutinaPredefinida: TextView           // TextView amb l'edat de la rutina predefinida
    lateinit var contingutNivellRutinaPredefinida: TextView         // TextView amb el nivell de la rutina predefinida
    lateinit var contingutEquipamentRutinaPredefinida: TextView     // TextView amb l'equipament de la rutina predefinida
    lateinit var contingutObjectiuRutinaPredefinida: TextView       // TextView amb l'objectiu de la rutina predefinida
    lateinit var contingutImpacteRutinaPredefinida: TextView        // TextView amb l'impacte de la rutina predefinida

    lateinit var recyclerView: RecyclerView                         // RecyclerView de CardViewItems que contenen la imatge i el nom de les activitats (exercicis i classes) que formen la rutina predefinida
    lateinit var list: ArrayList<CardViewItem>                     // Llistat de CardViewItems que contenen la imatge i el nom de les activitats (exercicis i classes) que formen la rutina predefinida

    lateinit var botoCompartir: Button                              // Button per compartir la rutina predefinida
    lateinit var botoRealitzar: Button                              // Button per realitzar la rutina predefinida


    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        identificadorRutinaPredefinida = activity?.intent?.getStringExtra(EXTRA_MESSAGE)
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
        val view: View = inflater.inflate(R.layout.fragment_consultar_rutina_predefinida, container, false)

        imatgeRutinaPredefinida = view.findViewById(R.id.imatgeRutinaPredefinida)
        nomRutinaPredefinida = view.findViewById(R.id.nomRutinaPredefinida)
        contingutDescripcioRutinaPredefinida = view.findViewById(R.id.contingutDescripcioRutinaPredefinida)
        contingutCategoriaRutinaPredefinida = view.findViewById(R.id.contingutCategoriaRutinaPredefinida)
        contingutTempsRutinaPredefinida = view.findViewById(R.id.contingutTempsRutinaPredefinida)
        contingutEdatRutinaPredefinida = view.findViewById(R.id.contingutEdatRutinaPredefinida)
        contingutNivellRutinaPredefinida = view.findViewById(R.id.contingutNivellRutinaPredefinida)
        contingutEquipamentRutinaPredefinida = view.findViewById(R.id.contingutEquipamentRutinaPredefinida)
        contingutObjectiuRutinaPredefinida = view.findViewById(R.id.contingutObjectiuRutinaPredefinida)
        contingutImpacteRutinaPredefinida = view.findViewById(R.id.contingutImpacteRutinaPredefinida)

        recyclerView = view.findViewById(R.id.recycler_view)

        botoCompartir = view.findViewById(R.id.botoCompartir)

        botoRealitzar = view.findViewById(R.id.botoRealitzar)

        list = ArrayList<CardViewItem>()

        //identificadorRutinaPredefinida = "1" // Eliminar aquesta línia de codi perquè s'està forçant el paràmetre que li ha d'arribar

        identificadorRutinaPredefinida?.let {
            viewModelRutinaPredefinida.getPredefinedRoutine(it.toInt())
        }

        viewModelRutinaPredefinida.predefinedRoutine?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                rutinaPredefinida = it.data
                setPredefinedRoutineContent()
            }
            else if (it.status == Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        //setExampleContent()

        setUpShareButton()

        setUpPlayButton()

        return view
    }

    /** Function onItemClick
     *
     *  Funció encarregada de configurar el comportament del clic en un CardViewItem del RecyclerView
     *
     *  @param position
     *  @author Albert Miñana Montecino
     */
    override fun onItemClick(position: Int) {
        if (position < rutinaPredefinida!!.exercises.size){
            // El CardViewItem clicat és un exercici
            val identificadorExercici = rutinaPredefinida!!.exercises[position]
            val intent = Intent(activity, ConsultarExerciciActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, identificadorExercici)
            }
            startActivity(intent)
        }
        else if (rutinaPredefinida!!.exercises.size <= position && position < (rutinaPredefinida!!.exercises.size + rutinaPredefinida!!.classes.size)){
            // El CardViewItem clicat és una classe
            val absolutePosition = position - rutinaPredefinida!!.exercises.size
            val identificadorClasse = rutinaPredefinida!!.classes[absolutePosition]
            val intent = Intent(activity, ConsultarClasseActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, identificadorClasse)
            }
            startActivity(intent)
        }

        //Toast.makeText(context, "Item $position clicked", Toast.LENGTH_SHORT).show()
        //val clickedItem = list[position]
        //clickedItem.text = "Item $position modified"
        //recyclerView.adapter?.notifyItemChanged(position)
    }

    /** Function setPredefinedRoutineContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'una rutina predefinida
     *
     *  @author Albert Miñana Montecino
     */
    private fun setPredefinedRoutineContent(){
        Picasso.get().load(rutinaPredefinida!!.image).into(imatgeRutinaPredefinida)
        nomRutinaPredefinida.text = rutinaPredefinida!!.name
        contingutDescripcioRutinaPredefinida.text = rutinaPredefinida!!.description
        contingutCategoriaRutinaPredefinida.text = categoriesName(rutinaPredefinida!!.categories)
        contingutTempsRutinaPredefinida.text = timeName(rutinaPredefinida!!.time)
        contingutEdatRutinaPredefinida.text = ageName(rutinaPredefinida!!.age)
        contingutNivellRutinaPredefinida.text = levelName(rutinaPredefinida!!.level)
        contingutEquipamentRutinaPredefinida.text = equipmentName(rutinaPredefinida!!.equipment)
        contingutObjectiuRutinaPredefinida.text = objectiveName(rutinaPredefinida!!.objective)
        contingutImpacteRutinaPredefinida.text = impactName(rutinaPredefinida!!.impact)

        setExercisesContent(0)
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

    private fun timeName(time: String): String? {
        when (time) {
            "P" -> return "Personalitzable"
            "R" -> return "Ronda circuits"
            "F" -> return "Fixat"
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

    private fun levelName(level: String): String? {
        when (level) {
            "P" -> return "Principiant"
            "I" -> return "Intermedi"
            "A" -> return "Avançat"
            else -> return null
        }
    }

    private fun equipmentName(equipment: String): String? {
        when (equipment) {
            "W" -> return "Sense"
            "HM" -> return "Material de casa"
            "K" -> return "Kettlebell"
            "B" -> return "Bodyweight"
            "D" -> return "Dumbbell"
            "RB" -> return "Resistance band"
            "RL" -> return "Resistance loop"
            "FR" -> return "Foam roller"
            else -> return null
        }
    }

    private fun objectiveName(objective: String): String? {
        when (objective) {
            "S" -> return "Salut"
            "Fr" -> return "Força"
            "P" -> return "Pèrdua de pes"
            "Fl" -> return "Flexibilitat"
            "Rs" -> return "Resistència"
            "Rc" -> return "Recuperació"
            "A" -> return "Agilitat"
            else -> return null
        }
    }

    private fun impactName(impact: String): String? {
        when (impact) {
            "L" -> return "Baix"
            "M" -> return "Moderat"
            "H" -> return "Alt"
            else -> return null
        }
    }

    /** Function setExercisesContent
     *
     *  Funció encarregada de generar la llista de CardViewItems amb la imatge i el nom dels exercicis que formen la rutina predefinida
     *
     *  @param  position
     *  @author Albert Miñana Montecino
     */
    private fun setExercisesContent(position: Int) {
        if (position < rutinaPredefinida!!.exercises.size){
            val viewModelExercicis by viewModels<ExerciseViewModel>()   // ViewModel dels exercicis de la rutina predefinida
            val identificadorExercici = rutinaPredefinida!!.exercises[position]
            viewModelExercicis.getExercise(identificadorExercici.toString())
            viewModelExercicis.exercise?.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS){
                    val item = CardViewItem(Configuration.Companion.urlServer + it.data!!.pre, it.data!!.name + " (Exercici)")
                    list.plusAssign(item)
                    setExercisesContent(position+1)
                }
                else if (it.status == Status.ERROR)
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
            })
        }
        else setClassesContent(0)
    }

    /** Function setClassesContent
     *
     *  Funció encarregada de generar la llista de CardViewItems amb la imatge i el nom de les classes que formen la rutina predefinida
     *
     *  @param  position
     *  @author Albert Miñana Montecino
     */
    private fun setClassesContent(position: Int) {
        if (position < rutinaPredefinida!!.classes.size){
            val viewModelClasses by viewModels<ClassViewModel>()    // ViewModel de les classes de la rutina predefinida
            val identificadorClasse = rutinaPredefinida!!.classes[position]
            viewModelClasses.getClass(identificadorClasse.toString())
            viewModelClasses.classe?.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS){
                    val item = CardViewItem(Configuration.Companion.urlServer + it.data!!.pre, it.data!!.name + " (Classe)")
                    list.plusAssign(item)
                    setClassesContent(position+1)
                }
                else if (it.status == Status.ERROR)
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
            })
        }
        else setActivitiesContent()
    }

    /** Function setActivitiesContent
     *
     *  Funció encarregada d'omplir el RecyclerView amb la llista de CardViewItems que contenen la imatge i el nom de les activitats (exercicis i classes) que formen la rutina predefinida
     *
     *  @author Albert Miñana Montecino
     */
    private fun setActivitiesContent(){
        recyclerView.adapter = RecyclerViewAdapter(list, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    /** Function setExampleContent
     *
     *  Funció encarregada d'establir un contingut d'exemple amb la informació completa d'una rutina predefinida
     *
     *  @author Albert Miñana Montecino
     */
    private fun setExampleContent(){
        Picasso.get().load("https://mens.laradiomovil.com/wp-content/uploads/2019/02/img_msanoja_20160504-144748_imagenes_lv_getty_gettyimages-546127037_2-k7r-U401526493710pEB-992x558@LaVanguardia-Web.jpg").into(imatgeRutinaPredefinida)
        nomRutinaPredefinida.text = "Rutina predefinida d'exemple"
        contingutDescripcioRutinaPredefinida.text = "Descripció de la rutina predefinida d'exemple"
        contingutCategoriaRutinaPredefinida.text = "Força, Càrdio, Estiraments"
        contingutTempsRutinaPredefinida.text = "Fixat"
        contingutEdatRutinaPredefinida.text = "Adult"
        contingutNivellRutinaPredefinida.text = "Intermedi"
        contingutEquipamentRutinaPredefinida.text = "Material de casa"
        contingutObjectiuRutinaPredefinida.text = "Salut"
        contingutImpacteRutinaPredefinida.text = "Moderat"

        list = ArrayList<CardViewItem>()
        for (i in 0 until 500) {
            val imatge = when (i % 3) {
                0 -> "http://simpleicon.com/wp-content/uploads/android.png"
                1 -> "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/647px-Apple_logo_black.svg.png"
                else -> "https://icons-for-free.com/iconfiles/png/512/microsoft+windows+icon-1320186681671871370.png"
            }
            val item = CardViewItem(imatge, "Item $i")
            list.plusAssign(item)
        }
        recyclerView.adapter = RecyclerViewAdapter(list, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
    }

    private fun setUpShareButton(){
        botoCompartir.setOnClickListener {
            val missatge = "L'aplicació FitHaus m'ajuda a estar en forma! Vols practicar l'activitat física tu també? Uneix-te i mira't la rutina d'entrenament predefinida "+rutinaPredefinida!!.name+", potser t'interessa!"
            val intent = Intent()
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, missatge)
            intent.action = Intent.ACTION_SEND
            val chooseIntent = Intent.createChooser(intent, "Compartir en xarxes socials")
            startActivity(chooseIntent)
        }
    }

    private fun setUpPlayButton() {
        botoRealitzar.setOnClickListener {
            val intent = Intent(activity, RealitzarRutinaPredefinidaActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, identificadorRutinaPredefinida)
            }
            startActivity(intent)
        }
    }


}
