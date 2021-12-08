package cat.fib.sharecommunity.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.PreferencesActivity
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.data.models.User
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Fragment Sport
 *
 *  Fragment encarregat de consultar i modificar la informació d'esport del perfil d'usuari
 *
 *  @constructor Crea el Fragment SportFragment
 *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
 */
@AndroidEntryPoint
class SportFragment : Fragment(R.layout.fragment_sport) {

    private val viewModel by viewModels<UserViewModel>()

    private var identificadorUsuari: String? = null     // Identificador de l'usuari

    lateinit var level: TextView
    lateinit var interestCategories: TextView
    lateinit var interestObjectives: TextView
    lateinit var routinesDone: TextView
    lateinit var points: TextView
    lateinit var achievements: TextView
    lateinit var preferencesButton: Button

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        identificadorUsuari = prefs.getString("email", null)
    }

    /** Function onCreateView
     *
     *  Funció encarregada de configurar i mostrar el contingut del fragment
     *
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fragment_sport, container, false)
        level = v.findViewById(R.id.Nivell)
        interestCategories = v.findViewById(R.id.CategoriesInterès)
        interestObjectives = v.findViewById(R.id.Objectiu)
        routinesDone = v.findViewById(R.id.NumRutines)
        points = v.findViewById(R.id.Punts)
        achievements = v.findViewById(R.id.Assoliments)

        preferencesButton = v.findViewById(R.id.ActualitzarPreferencesPerfilButton)


        identificadorUsuari?.let {
            //viewModel.getUser(it.toInt())
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) setUpSport(it.data)
            else if (it.status == Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        setUpButtonPreferences()

        return v
    }

    /** Function setUpSport
     *
     *  Funció encarregada d'establir el contingut amb la informació d'esport del perfil d'usuari
     *
     *  @param userData
     *  @author  Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
     */
    fun setUpSport(userData: User?) {
        /*
        val levelUser = getLevel(userData?.level)
        level.text = levelUser
        val categories = getCategories(userData?.categories)
        interestCategories.text = categories.joinToString()
        val objectives = getObjectives(userData?.objectives)
        interestObjectives.text = objectives.joinToString()
        routinesDone.text = userData?.activitiesdone.toString()
        points.text = userData?.points.toString()
        achievements.text = userData?.achievements.toString()

         */
    }

    /** Function getLevel
     *
     *  Funció encarregada d'establir el contingut amb la informació el nivell del perfil d'usuari.
     *
     *  @param level
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    private fun getLevel(level: String?): String {
        return when (level) {
            "B" -> "Principiant"
            "I" -> "Intermedi"
            "A" -> "Avançat"
            else -> "Cap"
        }
    }

    /** Function getObjectives
     *
     *  Funció encarregada d'establir el contingut amb la informació dels objectius d'interès del perfil d'usuari.
     *
     *  @param objectives
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    private fun getObjectives(objectives: ArrayList<String>?): ArrayList<String> {
        var objectius: ArrayList<String> = ArrayList()
        if (objectives != null) {
            if (objectives.contains("S")) objectius.add("Salut")
            if (objectives.contains("Fr")) objectius.add("Força")
            if (objectives.contains("P")) objectius.add("Pèrdua de pes")
            if (objectives.contains("Fl")) objectius.add("Flexibilitat")
            if (objectives.contains("Rs")) objectius.add("Resistència")
            if (objectives.contains("Rc")) objectius.add("Recuperació")
            if (objectives.contains("A")) objectius.add("Agilitat")
        }
        return objectius
    }

    /** Function getCategories
     *
     *  Funció encarregada d'establir el contingut amb la informació de les categories d'interès del perfil d'usuari.
     *
     *  @param categories
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    private fun getCategories(categories: ArrayList<String>?): ArrayList<String> {
        var categoriesUser: ArrayList<String> = ArrayList()
        if (categories != null) {
            if (categories.contains("S")) categoriesUser.add("Força")
            if (categories.contains("C")) categoriesUser.add("Càrdio")
            if (categories.contains("Y")) categoriesUser.add("Ioga")
            if (categories.contains("E")) categoriesUser.add("Estiraments")
            if (categories.contains("R")) categoriesUser.add("Rehabilitació")
            if (categories.contains("P")) categoriesUser.add("Pilates")
        }
        return categoriesUser
    }


    /** Function setUpButtonPreferences
     *
     *  Funció que restableix les preferències d'esport del perfil d'usuari mitjançant el qüestionari de preferències.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun setUpButtonPreferences() {
        preferencesButton.setOnClickListener {
            val intent = Intent(activity, PreferencesActivity::class.java)
            startActivity(intent)
        }
    }

}