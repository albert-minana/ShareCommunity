package cat.fib.sharecommunity.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.RealitzarRutinaPredefinidaActivity
import cat.fib.sharecommunity.data.api.Configuration
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.ClassViewModel
import cat.fib.sharecommunity.viewmodels.ExerciseViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

/** Fragment RealitzarRutinaPredefinida
 *
 *  Fragment encarregat de mostrar la informació necessària per realitzar una activitat (exercici o clase) d'una rutina d'entrenament predefinida
 *
 *  @constructor Crea el Fragment RealitzarRutinaPredefinida
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class RealitzarRutinaPredefinidaFragment : Fragment() {

    private var nomIdentificadorActivitat: String? = null   // Nom identificador de l'activitat
    private var activitatEsExercici: Boolean? = null        // Indica cert si l'activitat és un exercici, fals si l'activitat és una classe

    lateinit var imatgeActivitat: ImageView                 // ImageView amb la imatge de previsualització de l'activitat
    lateinit var nomActivitat: TextView                     // TextView amb el nom de l'activitat
    lateinit var videoActivitat: VideoView                  // VideoView amb el video de l'activitat

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var realitzarRutinaPredefinidaActivity: RealitzarRutinaPredefinidaActivity = activity as RealitzarRutinaPredefinidaActivity
        nomIdentificadorActivitat = realitzarRutinaPredefinidaActivity.getCurrentActivityName()
        activitatEsExercici = realitzarRutinaPredefinidaActivity.currentActivityIsExercise()
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
        val view: View = inflater.inflate(R.layout.fragment_realitzar_rutina_predefinida, container, false)

        imatgeActivitat = view.findViewById(R.id.imageActivity)
        nomActivitat = view.findViewById(R.id.nameActivity)
        videoActivitat = view.findViewById(R.id.videoActivity)

        if (activitatEsExercici!!) {
            val viewModelExercici by viewModels<ExerciseViewModel>()    // ViewModel de l'exercici

            viewModelExercici.getExercise(nomIdentificadorActivitat!!)

            viewModelExercici.exercise?.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS){
                    setContent(it.data!!.pre, it.data!!.name, it.data!!.videoexercise)
                }
                else if (it.status == Status.ERROR) {
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
                }
            })
        }
        else{
            val viewModelClasse by viewModels<ClassViewModel>()    // ViewModel de la classe

            viewModelClasse.getClass(nomIdentificadorActivitat!!)

            viewModelClasse.classe?.observe(viewLifecycleOwner, Observer {
                if (it.status == Status.SUCCESS){
                    setContent(it.data!!.pre, it.data!!.name, it.data!!.videoclass)
                }
                else if (it.status == Status.ERROR) {
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
                }
            })
        }

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'una activitat (exercici o classe) de la rutina d'entrenament predefinida
     *
     *  @param image
     *  @param name
     *  @param video
     *  @author Albert Miñana Montecino
     */
    private fun setContent(image: String, name: String, video: String){
        println(image)
        println(name)
        println(video)

        Picasso.get().load(Configuration.Companion.urlServer + image).into(imatgeActivitat)

        nomActivitat.text = name

        val videoPath: String = Configuration.Companion.urlServer + video
        val uri: Uri = Uri.parse(videoPath)
        videoActivitat.setVideoURI(uri)

        val mediaController = MediaController(activity)
        videoActivitat.setMediaController(mediaController)
        mediaController.setAnchorView(videoActivitat)
        videoActivitat.requestFocus()
        //videotutorialExercici.start()
        videoActivitat.seekTo(1)
    }
}