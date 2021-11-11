package cat.fib.sharecommunity.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import cat.fib.sharecommunity.BuildConfig
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.ExerciseViewModel
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.data.api.Configuration
import cat.fib.sharecommunity.data.models.Exercise
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Fragment ConsultarExercici
 *
 *  Fragment encarregat de consultar la informació completa d'un exercici
 *
 *  @constructor Crea el Fragment ConsultarExercici
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class ConsultarExerciciFragment : Fragment() {

    private val viewModel by viewModels<ExerciseViewModel>()    // ViewModel de l'exercici

    private var nomIdentificadorExercici: String? = null    // Nom identificador de l'exercici

    lateinit var imatgeExercici: ImageView                  // ImageView amb la imatge de previsualització de l'exercici
    lateinit var nomExercici: TextView                      // TextView amb el nom de l'exercici
    lateinit var videotutorialExercici: VideoView           // VideoView amb el videotutorial de l'exercici
    lateinit var contingutDescripcioExercici: TextView      // TextView amb la descripció de l'exercici
    lateinit var contingutMusculTreballExercici: TextView   // TextView amb el múscul de treball de l'exercici
    lateinit var imatgeMusculTreballExercici: ImageView     // ImageView amb la imatge del múscul de treball de l'exercici
    lateinit var contingutEdatExercici: TextView            // TextView amb l'edat de l'exercici
    lateinit var contingutDificultatExercici: TextView      // TextView amb la dificultat de l'exercici
    lateinit var contingutDuracioExercici: TextView         // TextView amb la duració de l'exercici
    lateinit var contingutCategoriaExercici: TextView       // TextView amb la categoria de l'exercici

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        nomIdentificadorExercici = activity?.intent?.getStringExtra(EXTRA_MESSAGE)
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
        val view: View = inflater.inflate(R.layout.fragment_consultar_exercici, container, false)

        imatgeExercici = view.findViewById(R.id.imatgeExercici)
        nomExercici = view.findViewById(R.id.nomExercici)
        videotutorialExercici = view.findViewById(R.id.videotutorialExercici)
        contingutDescripcioExercici = view.findViewById(R.id.contingutDescripcioExercici)
        contingutMusculTreballExercici = view.findViewById(R.id.contingutMusculTreballExercici)
        imatgeMusculTreballExercici = view.findViewById(R.id.imatgeMusculTreballExercici)
        contingutEdatExercici = view.findViewById(R.id.contingutEdatExercici)
        contingutDificultatExercici = view.findViewById(R.id.contingutDificultatExercici)
        contingutDuracioExercici = view.findViewById(R.id.contingutDuracioExercici)
        contingutCategoriaExercici = view.findViewById(R.id.contingutCategoriaExercici)

        // nomIdentificadorExercici = "3" // Eliminar aquesta línia de codi perquè s'està forçant el paràmetre que li ha d'arribar

        nomIdentificadorExercici?.let {
            viewModel.getExercise(it)
        }

        viewModel.exercise?.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS)
                setContent(it.data)
            else if (it.status == Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'un exercici
     *
     *  @param exerciseData
     *  @author Albert Miñana Montecino
     */
    private fun setContent(exerciseData: Exercise?){

        Picasso.get().load(Configuration.Companion.urlServer + exerciseData?.pre.toString()).into(imatgeExercici)

        nomExercici.text = exerciseData?.name.toString()

        val videoPath: String = Configuration.Companion.urlServer + exerciseData?.videotutorial.toString()
        val uri: Uri = Uri.parse(videoPath)
        videotutorialExercici.setVideoURI(uri)

        val mediaController = MediaController(activity)
        videotutorialExercici.setMediaController(mediaController)
        mediaController.setAnchorView(videotutorialExercici)
        videotutorialExercici.requestFocus()
        //videotutorialExercici.start()
        videotutorialExercici.seekTo(1)

        contingutDescripcioExercici.text = exerciseData?.description.toString()
        contingutMusculTreballExercici.text = muscleName(exerciseData?.muscle.toString())

        Picasso.get().load(Configuration.Companion.urlServer + exerciseData?.muscleimage.toString()).into(imatgeMusculTreballExercici)

        contingutEdatExercici.text = ageName(exerciseData?.age.toString())
        contingutDificultatExercici.text = difficultyName(exerciseData?.difficulty.toString())
        contingutDuracioExercici.text = exerciseData?.length.toString()
        contingutCategoriaExercici.text = categoriesName(exerciseData?.categories!!)
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

    private fun muscleName(muscle: String): String? {
        when (muscle) {
            "Bi" -> return "Bíceps"
            "Tr" -> return "Tríceps"
            "Fa" -> return "Forearm"
            "Ch" -> return "Chest"
            "Sh" -> return "Shoulder"
            "Do" -> return "Dorsal"
            "Gl" -> return "Glutis"
            "Fe" -> return "Femoral"
            "Qu" -> return "Quàdriceps"
            "Ca" -> return "Calves"
            "Co" -> return "Core"
            else -> return null
        }
    }

    /** Function setExampleContent
     *
     *  Funció encarregada d'establir un contingut d'exemple amb la informació completa d'un exercici
     *
     *  @author Albert Miñana Montecino
     */
    private fun setExampleContent(){
        imatgeExercici.setImageResource(R.drawable.exemple_exercici)
        nomExercici.text = "Genolls alts corrent en el seu lloc"

        val videoPath: String = "android.resource://" + BuildConfig.APPLICATION_ID + "/" + R.raw.high_knees_running_in_place
        val uri: Uri = Uri.parse(videoPath)
        videotutorialExercici.setVideoURI(uri)

        val mediaController = MediaController(activity)
        videotutorialExercici.setMediaController(mediaController)
        mediaController.setAnchorView(videotutorialExercici)
        videotutorialExercici.requestFocus()
        //videotutorialExercici.start()
        videotutorialExercici.seekTo(1)

        contingutDescripcioExercici.text = "Aquest exercici combina aixecaments de genolls amb el típic moviment de carrera. És un dels moviments més efectius per millorar la vostra forma de córrer, així com la vostra velocitat, flexibilitat i equilibri. Els genolls elevats també augmenten la freqüència cardíaca i crema calories, per la qual cosa és ideal per a aquells que busquen greix. És l’exercici perfecte per a un desafiant entrenament cardio que farà que el ritme cardíac continuï. Els principals músculs implicats són els quads, els vedells i els glutis. Aquest moviment també fa participar els abdominals i us pot ajudar a desfer-vos dels tiradors de l’amor."
        contingutMusculTreballExercici.text = "Quàdriceps"
        imatgeMusculTreballExercici.setImageResource(R.drawable.quadriceps)
        contingutEdatExercici.text = "Adult"
        contingutDificultatExercici.text = "Mitjana"
        contingutDuracioExercici.text = "30 segons"
        contingutCategoriaExercici.text = "Cardio"
    }

}