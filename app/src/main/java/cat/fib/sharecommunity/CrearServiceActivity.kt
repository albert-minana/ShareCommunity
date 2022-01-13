package cat.fib.sharecommunity

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ServiceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_crear_perfil.textInputLayoutNom
import kotlinx.android.synthetic.main.activity_crear_service.*
import java.text.SimpleDateFormat
import java.util.*

/** Classe CrearService
 *
 *  Classe on es crea un producte amb els camps principals necessaris.
 *
 *  @constructor Crea un producte amb tots els camps amb valor nul.
 *  @author Daniel Cárdenas & Xavier Sancho-Tello Bayarri.
 */
@AndroidEntryPoint
class CrearServiceActivity : AppCompatActivity() {
    //Our variables
    var Nom: EditText? = null
    var Descripcio: EditText? = null
    var Ubicacio: EditText? = null
    var Tipus_Canguratge: RadioButton? = null
    var Tipus_Transport: RadioButton? = null
    var Tipus_Classes: RadioButton? = null
    var Tipus_Banc: RadioButton? = null
    var Tipus_Compartir: RadioButton? = null
    var Tipus_Altre: RadioButton? = null
    var Data_Inici: EditText? = null
    var Data_Fi: EditText? = null

    private val viewModel by viewModels<ServiceViewModel>()


    /** Funció inicialitzadora
     *
     *  Funció que fa que es mostri la pantalla a l'usuari i que inicialitza totes les variables.
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas & Xavier Sancho-Tello Bayarri.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_service)

        Nom = findViewById(R.id.Nom)
        Descripcio = findViewById(R.id.Descripcio)
        Ubicacio = findViewById(R.id.Ubicacio)
        Tipus_Canguratge = findViewById(R.id.Tipus_Canguratge)
        Tipus_Transport = findViewById(R.id.Tipus_Transport)
        Tipus_Classes = findViewById(R.id.Tipus_Classes)
        Tipus_Banc = findViewById(R.id.Tipus_Banc)
        Tipus_Compartir = findViewById(R.id.Tipus_Compartir)
        Tipus_Altre = findViewById(R.id.Tipus_Altre)
        Data_Inici = findViewById(R.id.DataInici)
        Data_Fi = findViewById(R.id.DataFi)

        setupSendButton()
    }

    /** Function setupSendButton
     *
     *  Funció que comprova si els camps són correctes per crear un service.
     *
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
     */
    fun setupSendButton() {
        CrearServiceButton.setOnClickListener {
            val validateName = validateName()
            val validateDescription = validateDescription()
            val validateUbication = validateUbication()
            val validateType = validateType()
            val validateDataIni = validateDataIni()
            val validateDataFi = validateDataFi()
            if (validateName && validateDescription && validateUbication && validateType && validateDataIni && validateDataFi) {
                val name = Nom?.text.toString()
                val descripcio = Descripcio?.text.toString()
                val ubicacio = Ubicacio?.text.toString()
                var type: String
                var data_ini = Data_Inici?.text.toString()
                var data_fi = Data_Fi?.text.toString()
                when {
                    Tipus_Canguratge?.isChecked == true -> type = "Canguratge"
                    Tipus_Transport?.isChecked == true -> type = "Transport"
                    Tipus_Classes?.isChecked == true -> type = "Classes"
                    Tipus_Banc?.isChecked == true -> type = "Banc"
                    Tipus_Compartir?.isChecked == true -> type = "Compartir"
                    else -> type = "Altre"
                }
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val publishDate = sdf.format(Date())

                val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val userEmail = prefs.getString("email", null)
                val service = Service(name, descripcio, ubicacio, type, data_ini, data_fi, publishDate, userEmail!!)
                viewModel.createService(service)
                viewModel.service.observe(this, Observer {
                    if (it.status == Resource.Status.SUCCESS) {
                        showHome()
                    }
                    else if (it.status == Resource.Status.ERROR) {
                        Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
                    }


                })
            }
        }
    }

    /** Function validateName
     *
     *  Funció que comprova si el camp Nom és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateName(): Boolean {
        val name = Nom?.text.toString()
        if (name.isEmpty()) {
            textInputLayoutNom?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutNom?.setError(null)
            return true
        }
    }

    /** Function validateDescription
     *
     *  Funció que comprova si el camp Descripcio és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateDescription(): Boolean {
        val descripcio = Descripcio?.text.toString()
        if (descripcio.isEmpty()) {
            textInputLayoutDescripcio?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutDescripcio?.setError(null)
            return true
        }
    }

    /** Function validateUbication
     *
     *  Funció que comprova si el camp Ubicacio és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateUbication(): Boolean {
        val ubicacio = Ubicacio?.text.toString()
        if (ubicacio.isEmpty()) {
            textInputLayoutUbicacio?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutUbicacio?.setError(null)
            return true
        }
    }

    /** Function validateType
     *
     *  Funció que comprova si el camp Tipus és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello
     */
    private fun validateType(): Boolean {
        val TypeNotChecked = (Tipus_Canguratge?.isChecked == false && Tipus_Transport?.isChecked == false && Tipus_Classes?.isChecked == false &&  Tipus_Banc?.isChecked == false &&  Tipus_Compartir?.isChecked == false && Tipus_Altre?.isChecked == false)
        if (TypeNotChecked) {
            textInputLayoutType?.setError("El camp no està seleccionat")
            Tipus_Altre?.setError("El camp no està seleccionat")
            return false
        }
        else {
            textInputLayoutType?.setError(null)
            Tipus_Altre?.setError(null)
            return true
        }
    }

    /** Function validateDataIni
     *
     *  Funció que comprova si el camp Data Inici és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    private fun validateDataIni(): Boolean {
        val data_ini = Data_Inici?.text.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        if (data_ini.isEmpty()) {
            textInputLayoutDataInici?.setError("El camp no pot ser buit")
            return false
        }
        else {
            if(data_ini < currentDate) {
                textInputLayoutDataInici?.setError("La data no pot ser anterior a l'actual")
                return false
            }
            textInputLayoutDataInici?.setError(null)
            return true
        }
    }

    /** Function validateDataFi
     *
     *  Funció que comprova si el camp Data Fi és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    private fun validateDataFi(): Boolean {
        val data_fi = Data_Fi?.text.toString()
        val data_ini = Data_Inici?.text.toString()
        if (data_fi.isEmpty()) {
            textInputLayoutDataFi?.setError("El camp no pot ser buit")
            return false
        }
        else {
            if(data_fi < data_ini) {
                textInputLayoutDataInici?.setError("La data no pot ser anterior a la inicial")
                return false
            }
            textInputLayoutDataFi?.setError(null)
            return true
        }
    }

        private fun show(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    /** Function showHome
     *
     *  Funció encarregada de canviar a l'activitat principal.
     *
     *  @author Daniel Cárdenas Rafael
     */
    private fun showHome(){
        val homeIntent = Intent(this, MainActivity::class.java)
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(homeIntent)
    }

}