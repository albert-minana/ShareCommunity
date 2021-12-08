package cat.fib.sharecommunity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_preferences.*
import cat.fib.sharecommunity.data.models.User
import dagger.hilt.android.AndroidEntryPoint

/** Classe Activty Qüestionari Inicial
 *
 *  Activity on es troba tota la lògica per poder fer el qüestionari inicial de l'usuari després de registrar-se.
 *
 *  @constructor Crea el qüestionari de l'usuari amb tots els camps sense seleccionar.
 *  @author Adrià Espinola.
 */
@AndroidEntryPoint
class PreferencesActivity : AppCompatActivity() {

    private val viewModel by viewModels<UserViewModel>()

    private var identificadorUsuari: String? = null   // Identificador de l'usuari

    private var userPreferences: User? = null

    /** Funció inicialitzadora
     *
     *  Funció que fa que es mostri la pantalla del qüestionari inicial.
     *
     *  @param savedInstanceState
     *  @author Adrià Espinola.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        identificadorUsuari = prefs.getString("userId", null)
        setContentView(R.layout.activity_preferences)
        identificadorUsuari?.let {
            //viewModel.getUser(it.toInt())
        }
        viewModel.user.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                setContent(it.data)
                userPreferences = it.data
            }
            else if (it.status == Status.ERROR) Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
        })
        setupSendButton()
    }

    /** Funció setupSendButton
     *
     *  Funció encarregada de configurar el botó d'enviar el qüestionari.
     *
     *  @author Adrià Espinola.
     */
    private fun setupSendButton() {
        buttonEnviar.setOnClickListener {
            send()
        }
    }

    /** Funció send
     *
     *  Funció que comprova que el qüestionari sigui omplert correctament.
     *
     *  @author Adrià Espinola.
     */
    private fun send() {
        val experiencia: String? = experience()
        val categories: MutableList<String> = chosenCategories()
        val countC = categories.size
        val objectius: MutableList<String> = chosenObjectives()
        val countO = objectius.size

        if ((experiencia != null) && (countC in 1..3) && (countO in 1..3)) {
            updateUser(userPreferences, objectius, categories, experiencia)
        }
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació de les preferències del perfil d'usuari.
     *
     *  @param user
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    private fun setContent(user: User?) {
        /*
        val level = user?.level
        val objectives = user?.objectives
        val categories = user?.categories


        when (level) {
            "B" -> radioButtonPrincipiant?.isChecked = true
            "I" -> radioButtonIntermedi.isChecked = true
            "A" -> radioButtonAvançat.isChecked = true
        }
        if (objectives != null) {
            if (objectives.contains("S")) checkBoxSalut.isChecked = true
            if (objectives.contains("Fr")) checkBoxForça2.isChecked = true
            if (objectives.contains("P")) checkBoxPerduaPes.isChecked = true
            if (objectives.contains("Fr")) checkBoxForça2.isChecked = true
            if (objectives.contains("Fl")) checkBoxFlexibilitat.isChecked = true
            if (objectives.contains("Rs")) checkBoxResistència.isChecked = true
            if (objectives.contains("Rc")) checkBoxRecuperació.isChecked = true
            if (objectives.contains("A")) checkBoxAgilitat.isChecked = true
        }
        if (categories != null) {
            if (categories.contains("S")) checkBoxForça.isChecked = true
            if (categories.contains("C")) checkBoxCardio.isChecked = true
            if (categories.contains("Y")) checkBoxIoga.isChecked = true
            if (categories.contains("E")) checkBoxEstiraments.isChecked = true
            if (categories.contains("R")) checkBoxRehabilitació.isChecked = true
            if (categories.contains("P")) checkBoxPilates.isChecked = true
        }

         */

    }

    /** Funció updateU
     *
     *  Funció que actualitza el objecte Usuari amb els objectius, categories i experiència seleccionades.
     *
     *  @author Adrià Espinola.
     */
    private fun updateUser(user: User?, objectius: MutableList<String>, categories: MutableList<String>, experiencia: String) {
     /*   if (user != null) {
            user.objectives = ArrayList(objectius)
            user.categories = ArrayList(categories)
            user.level = experiencia
            viewModel.updateUser(user)
            viewModel.user.observe(this, Observer {
                if (it.status == Status.SUCCESS) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                } else if (it.status == Status.ERROR) Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
            })
        }

      */
    }

    /** Function validateExperience
     *
     *  Funció que comprova si s'ha seleccionat un radioButton d'experiència.
     *
     *  @param view
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino.
     */
    public fun validateExperience(view: View) {
        experience()
    }

    /** Function validateObjectives
     *
     *  Funció que comprova si s'ha seleccionat entre 1 i 3 checkBoxes d'objectius.
     *
     *  @param view
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino.
     */
    public fun validateObjectives(view: View) {
        chosenObjectives()
    }

    /** Function validateCategories
     *
     *  Funció que comprova si s'ha seleccionat entre 1 i 3 checkBoxes de categories.
     *
     *  @param view
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino.
     */
    public fun validateCategories(view: View) {
        chosenCategories()
    }

    /** Funció experience
     *
     *  Funció que retorna el contingut del radio Button seleccionat.
     *
     *  @author Adrià Espinola.
     */
    private fun experience(): String? {
        var experience: String? = null
        when {
            radioButtonPrincipiant?.isChecked == true -> experience = "B"
            radioButtonIntermedi?.isChecked == true -> experience = "I"
            radioButtonAvançat?.isChecked == true -> experience = "A"
        }
        if (experience == null) {
            textViewExperiencia?.setError("Selecciona 1")
        }
        else textViewExperiencia?.setError(null)
        return experience
    }

    /** Funció chosenObjectives
     *
     *  Funció que retorna una llista amb els objectius seleccionats.
     *
     *  @author Adrià Espinola.
     */
    private fun chosenObjectives(): MutableList<String> {
        val objectius: MutableList<String> = ArrayList()
        if (checkBoxSalut.isChecked) objectius.add("S")
        if (checkBoxForça2.isChecked) objectius.add("Fr")
        if (checkBoxPerduaPes.isChecked) objectius.add("P")
        if (checkBoxFlexibilitat.isChecked) objectius.add("Fl")
        if (checkBoxResistència.isChecked) objectius.add("Rs")
        if (checkBoxRecuperació.isChecked) objectius.add("Rc")
        if (checkBoxAgilitat.isChecked) objectius.add("A")
        if (objectius.size == 0 || objectius.size > 3) {
            textViewObjectius?.setError("Selecciona entre 1 i 3")
        }
        else textViewObjectius?.setError(null)
        return objectius
    }

    /** Funció chosenCategories
     *
     *  Funció que retorna una llista amb les categories seleccionades.
     *
     *  @author Adrià Espinola.
     */
    private fun chosenCategories(): MutableList<String> {
        val categories: MutableList<String> = ArrayList()
        if (checkBoxForça.isChecked) categories.add("S")
        if (checkBoxCardio.isChecked) categories.add("C")
        if (checkBoxIoga.isChecked) categories.add("Y")
        if (checkBoxEstiraments.isChecked) categories.add("E")
        if (checkBoxRehabilitació.isChecked) categories.add("R")
        if (checkBoxPilates.isChecked) categories.add("P")
        if (categories.size == 0 || categories.size > 3) {
            textViewCategories?.setError("Selecciona entre 1 i 3")
        }
        else textViewCategories?.setError(null)
        return categories
    }
}