package cat.fib.sharecommunity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.data.models.PredefinedRoutine
import cat.fib.sharecommunity.ui.RealitzarRutinaPredefinidaFragment
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.PredefinedRoutineViewModel
import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització de l'Activity
private const val EXTRA_MESSAGE = "cat.fib.sharecommunity.MESSAGE"

/** Activity RealitzarRutinaPredefinida
 *
 *  Activitat encarregada de realitzar les activitats (exercicis i/o classes) que formen una rutina d'entrenament predefinida
 *
 *  @constructor Crea l'Activity RealitzarRutinaPredefinida
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class RealitzarRutinaPredefinidaActivity : AppCompatActivity() {

    private val viewModel by viewModels<PredefinedRoutineViewModel>()   // ViewModel de la rutina predefinida

    private var identificadorRutinaPredefinida: String? = null      // Identificador de la rutina predefinida
    private var rutinaPredefinida: PredefinedRoutine? = null        // Model de la rutina predefinida

    lateinit var botoAnterior: Button                               // Button per navegar a l'anterior activitat (exercici o classe) de la rutina predefinida
    lateinit var botoCancelar: Button                               // Button per cancel·lar la realització de les activitats (exercicis i/o classes) de la rutina predefinida
    lateinit var botoSeguent: Button                                // Button per navegar a la següent activitat (exercici o classe) de la rutina predefinida

    private var posicio: Int? = null                                // Posició de l'activitat actual (exercici o classe) que està sent visitada

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant els Buttons Anterior, Cancel·lar i Següent i el Fragment RealitzarRutinaPredefinida
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realitzar_rutina_predefinida)

        botoAnterior = findViewById(R.id.buttonPrevious)
        botoCancelar = findViewById(R.id.buttonCancel)
        botoSeguent = findViewById(R.id.buttonNext)

        posicio = 0

        identificadorRutinaPredefinida = intent.getStringExtra(EXTRA_MESSAGE)

        //identificadorRutinaPredefinida = "3"

        identificadorRutinaPredefinida?.let {
            viewModel.getPredefinedRoutine(it.toInt())
        }

        viewModel.predefinedRoutine?.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                rutinaPredefinida = it.data
                setUpButtons()
                val limit = rutinaPredefinida!!.exercises.size + rutinaPredefinida!!.classes.size
                if (posicio!! < limit) {
                    updateContent()
                }
            }
            else if (it.status == Status.ERROR)
                Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
        })

    }

    private fun setUpButtons(){
        botoAnterior.setOnClickListener {
            if (posicio!!-1 >= 0) {
                posicio = posicio!! - 1
                updateContent()
            }
        }
        botoCancelar.setOnClickListener {
            showHome()
        }
        botoSeguent.setOnClickListener {
            val limit = rutinaPredefinida!!.exercises.size + rutinaPredefinida!!.classes.size
            if (posicio!!+1 < limit) {
                posicio = posicio!! + 1
                updateContent()
            }
            else if (posicio!!+1 == limit){
                showCongratulations()
            }
        }
    }

    /** Function showHome
     *
     *  Funció encarregada de canviar a l'activitat principal.
     *
     *  @author Albert Miñana Montecino
     */
    private fun showHome() {
        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(homeIntent)
    }

    /** Function showCongratulations
     *
     *  Funció encarregada de mostrar un missatge d'enhorabona al realitzar totes les activitats (exercicis i/o classes) que formen la rutina d'entrenament predefinida
     *
     *  @author Albert Miñana Montecino
     */
    private fun showCongratulations() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enhorabona")
        builder.setMessage("Has completat la rutina d'entrenament predefinida amb èxit")
        builder.setPositiveButton("Acceptar", DialogInterface.OnClickListener { dialog, which -> showHome() })
        val dialog: AlertDialog = builder.create()
        dialog.create()
        dialog.show()
    }

    /** Function updateContent
     *
     *  Funció encarregada d'actualitzar el contingut del Fragment RealitzarRutinPredefinida amb la informació de l'activitat seleccionada (exercici o classe) de rutina d'entrenament predefinida
     *
     *  @author Albert Miñana Montecino
     */
    private fun updateContent(){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragmentContainer = findViewById<FrameLayout>(R.id.fragment_container)
        fragmentContainer.removeAllViews()

        val fragment = RealitzarRutinaPredefinidaFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }

    public fun getCurrentActivityName(): String {
        if (posicio!! < rutinaPredefinida!!.exercises.size){
            return rutinaPredefinida!!.exercises[posicio!!]
        }
        else {
            val posicioAbsoluta = posicio!! - rutinaPredefinida!!.exercises.size
            return rutinaPredefinida!!.classes[posicioAbsoluta]
        }
    }

    public fun currentActivityIsExercise(): Boolean {
        return (posicio!! < rutinaPredefinida!!.exercises.size)
    }
}