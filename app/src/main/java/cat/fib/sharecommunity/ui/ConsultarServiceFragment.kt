package cat.fib.sharecommunity.ui

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.FOCUSABLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.MainActivity
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ServiceViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_crear_service.*
import kotlinx.android.synthetic.main.fragment_consultar_service.*

import dagger.hilt.android.AndroidEntryPoint

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE_1 = "cat.fib.sharecommunity.MESSAGE1"
private const val EXTRA_MESSAGE_2 = "cat.fib.sharecommunity.MESSAGE2"

/** Fragment ConsultarService
 *
 *  Fragment encarregat de consultar la informació completa d'un servei
 *
 *  @constructor Crea el Fragment ConsultarService
 *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
 */
@AndroidEntryPoint
class ConsultarServiceFragment : Fragment() {

    private val viewModel by viewModels<ServiceViewModel>()    // ViewModel del servei

    private var idService: String? = null  // ID del servei
    private var userEmailService: String? = null  // userEmail del servei
    private var servei: Service? = null

    lateinit var imatgeService: ImageView              // ImageView amb la imatge del servei
    lateinit var nomService: TextView                  // TextView amb el nom del servei
    lateinit var contingutDescripcioService: TextView  // TextView amb la descripció del servei
    lateinit var contingutUbicacioService: TextView    // TextView amb la ubicació del servei
    lateinit var contingutEstatService: TextView       // TextView amb l'estat del servei
    lateinit var contingutTipusService: TextView       // TextView amb el tipus del servei
    lateinit var contingutDataIniService: TextView       // TextView amb la data d'inici del servei
    lateinit var contingutDataFiService: TextView       // TextView amb la data final del servei
    lateinit var contingutDataPublicacio: TextView      // TextView amb la data de publicació del servei
    lateinit var contingutEmailUsuari: TextView         // TextView amb l'email de l'usuari del servei
    lateinit var contingutTipusCanguratgeService: RadioButton
    lateinit var contingutTipusTransportService: RadioButton
    lateinit var contingutTipusClassesService: RadioButton
    lateinit var contingutTipusBancService: RadioButton
    lateinit var contingutTipusCompartirService: RadioButton
    lateinit var contingutTipusAltreService: RadioButton

    lateinit var botoCompartir: Button                  // Button per compartir service
    lateinit var buttons: LinearLayout                  // Buttons
    lateinit var botoEliminar: Button                   // Button per eliminar service
    lateinit var botoEditar: Button                     // Button per editar service
    lateinit var botoFinalitzar: Button                 // Button per finalitzar service

    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idService = activity?.intent?.getStringExtra(EXTRA_MESSAGE_1)
        userEmailService = activity?.intent?.getStringExtra(EXTRA_MESSAGE_2)
    }

    /** Function onCreateView
     *
     *  Funció encarregada de configurar i mostrar el contingut del fragment
     *
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_consultar_service, container, false)

        //imatgeService = view.findViewById(R.id.imatgeservice)
        nomService = view.findViewById(R.id.nomService)
        contingutDescripcioService = view.findViewById(R.id.TextInputEditTextDescripcioService)
        contingutUbicacioService = view.findViewById(R.id.TextInputEditTextUbicacioService)
        contingutEstatService = view.findViewById(R.id.TextInputEditTextEstatService)
        contingutTipusCanguratgeService = view.findViewById(R.id.Tipus_Canguratge)
        contingutTipusTransportService = view.findViewById(R.id.Tipus_Transport)
        contingutTipusClassesService = view.findViewById(R.id.Tipus_Classes)
        contingutTipusBancService = view.findViewById(R.id.Tipus_Banc)
        contingutTipusCompartirService = view.findViewById(R.id.Tipus_Compartir)
        contingutTipusAltreService = view.findViewById(R.id.Tipus_Altre)
        contingutDataIniService = view.findViewById(R.id.TextInputEditTextDataIniService)
        contingutDataFiService = view.findViewById(R.id.TextInputEditTextDataFiService)
        contingutDataPublicacio = view.findViewById(R.id.TextInputEditTextDataService)
        contingutEmailUsuari = view.findViewById(R.id.TextInputEditTextEmailService)

        idService?.let {
            viewModel.getService(userEmailService!!, it)
        }

        viewModel.service?.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                servei = it.data
                setContent(it.data)
                val prefs = this.requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val userEmail = prefs.getString("email", null)
                if (servei!!.userEmail == userEmail && servei!!.state == "Disponible") convertToEditarServiceActivity()
            }
            else if (it.status == Resource.Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    private fun convertToEditarServiceActivity() {
        contingutDescripcioService.isFocusableInTouchMode = true
        contingutDescripcioService.isCursorVisible = true
        contingutDescripcioService.isClickable = true

        contingutUbicacioService.isFocusableInTouchMode = true
        contingutUbicacioService.isCursorVisible = true
        contingutUbicacioService.isClickable = true

        contingutTipusCanguratgeService.isFocusableInTouchMode = true
        contingutTipusCanguratgeService.isCursorVisible = true
        contingutTipusCanguratgeService.isClickable = true

        contingutTipusTransportService.isFocusableInTouchMode = true
        contingutTipusTransportService.isCursorVisible = true
        contingutTipusTransportService.isClickable = true

        contingutTipusClassesService.isFocusableInTouchMode = true
        contingutTipusClassesService.isCursorVisible = true
        contingutTipusClassesService.isClickable = true

        contingutTipusBancService.isFocusableInTouchMode = true
        contingutTipusBancService.isCursorVisible = true
        contingutTipusBancService.isClickable = true

        contingutTipusCompartirService.isFocusableInTouchMode = true
        contingutTipusCompartirService.isCursorVisible = true
        contingutTipusCompartirService.isClickable = true

        contingutTipusAltreService.isFocusableInTouchMode = true
        contingutTipusAltreService.isCursorVisible = true
        contingutTipusAltreService.isClickable = true

        buttons.visibility = VISIBLE

        contingutEstatService.isEnabled = false
        contingutDataPublicacio.isEnabled = false
        contingutEmailUsuari.isEnabled = false

        setUpEditButtons()
    }

    fun setUpEditButtons() {
        botoEliminar.setOnClickListener {
            confirmDeleteService()
        }

        botoEditar.setOnClickListener {
            updateService()
        }

        botoFinalitzar.setOnClickListener {
            changeServiceState()
        }


    }

    /** Function deleteService
     *
     *  Funció encarregada d'eliminar el Service
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael
     */
    private fun deleteService() {
        viewModel.deleteService(servei!!.id, servei!!.userEmail)

        viewModel.idservei.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
            else if (it.status == Resource.Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })
    }

    /** Function updateService()
     *
     *  Funció que comprova si els camps són correctes per actualitzar el service.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun updateService() {
        val validateDescription = validateDescription()
        val validateUbication = validateUbication()
        val validateType = validateType()
        if (validateDescription && validateUbication && validateType) {
            val descripcio = contingutDescripcioService?.text.toString()
            val ubicacio = contingutUbicacioService?.text.toString()
            var type: String
            when {
                contingutTipusCanguratgeService?.isChecked == true -> type = "Canguratge"
                contingutTipusTransportService?.isChecked == true -> type = "Transport"
                contingutTipusClassesService?.isChecked == true -> type = "Classes"
                contingutTipusBancService?.isChecked == true -> type = "Banc"
                contingutTipusCompartirService?.isChecked == true -> type = "Compartir"
                else -> type = "Altre"
            }

            servei!!.description = descripcio
            servei!!.ubication = ubicacio
            servei!!.type = type

            viewModel.updateService(servei!!)
            viewModel.service.observe(viewLifecycleOwner, Observer {
                if (it.status == Resource.Status.SUCCESS) {
                    servei = it.data
                    setContent(servei)
                }
                else if (it.status == Resource.Status.ERROR) {
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    /** Function changeServiceState()
     *
     *  Funció que canvia l'estat del service a Finalitzat.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun changeServiceState() {
        servei!!.state = "Finalitzat"

        viewModel.updateService(servei!!)
        viewModel.service.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
            else if (it.status == Resource.Status.ERROR) {
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
            }
        })
    }

    /** Function validateDescription
     *
     *  Funció que comprova si el camp Descripcio és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateDescription(): Boolean {
        val descripcio = contingutDescripcioService?.text.toString()
        if (descripcio.isEmpty()) {
            textInputLayoutDescripcioService?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutDescripcioService?.setError(null)
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
        val ubicacio = contingutUbicacioService?.text.toString()
        if (ubicacio.isEmpty()) {
            textInputLayoutUbicacioService?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutUbicacioService?.setError(null)
            return true
        }
    }

    /** Function validateType
     *
     *  Funció que comprova si el camp Tipus és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Daniel Cárdenas Rafael
     */
    private fun validateType(): Boolean {
        val TypeNotChecked = (contingutTipusCanguratgeService?.isChecked == false && contingutTipusTransportService?.isChecked == false && contingutTipusClassesService?.isChecked == false && contingutTipusBancService?.isChecked == false && contingutTipusCompartirService?.isChecked == false && contingutTipusAltreService?.isChecked == false)
        if (TypeNotChecked) {
            textInputLayoutTipusService?.setError("El camp no està seleccionat")
            contingutTipusAltreService?.setError("El camp no està seleccionat")
            return false
        }
        else {
            textInputLayoutTipusService?.setError(null)
            contingutTipusAltreService?.setError(null)
            return true
        }
    }

    /** Function confirmDeleteService
     *
     *  Funció encarregada de confirmar la petició d'eliminació del service
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael
     */
    public fun confirmDeleteService() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Confirmació")
        builder.setMessage("Estàs segur que vols eliminar el teu service?")
        builder.setPositiveButton("Acceptar", DialogInterface.OnClickListener { dialog, which -> deleteService() })
        builder.setNegativeButton("Cancel·lar", null)
        val dialog: AlertDialog = builder.create()
        dialog.create()
        dialog.show()
    }


    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'un service
     *
     *  @param  classData
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello Bayarri
     */
    fun setContent(serviceData: Service?){
        nomService.text = serviceData?.name.toString()
        contingutDescripcioService.text = serviceData?.description.toString()
        contingutEstatService.text = serviceData?.state.toString()
        when (serviceData?.type) {
            "Canguratge" -> contingutTipusCanguratgeService.isChecked = true
            "Transport" -> contingutTipusTransportService.isChecked = true
            "Classes" -> contingutTipusClassesService.isChecked = true
            "Banc" -> contingutTipusBancService.isChecked = true
            "Compartir" -> contingutTipusCompartirService.isChecked = true
            else -> contingutTipusAltreService.isChecked = true
        }
        contingutDataPublicacio.text = serviceData?.publishDate.toString()
        contingutEmailUsuari.text = serviceData?.userEmail.toString()
    }



    /** Function setContent
     *
     *  Funció encarregada d'establir un contingut d'exemple amb la informació completa d'un service
     *
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
     */
    fun setContent(){
        //imatgeService.setImageResource(R.drawable.fotopae)
        nomService.text = "Llibre matemàtiques"
        contingutDescripcioService.text = "LLibre mates de 4t d'ESO"
        contingutUbicacioService.text = "Barcelona"
        contingutEstatService.text = "Disponible"
        contingutDataIniService.text = "13:00"
        contingutDataFiService.text = "15:00"
    }

    private fun setUpShareButton() {
        botoCompartir.setOnClickListener {
            val missatge =
                "L'aplicació ShareCommunity és un espai d'intercanvi de productes i serveis. Un membre de la comunitat ha compratit el servei " + servei!!.name + ", potser t'interessa!"
            val intent = Intent()
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, missatge)
            intent.action = Intent.ACTION_SEND
            val chooseIntent = Intent.createChooser(intent, "Compartir en xarxes socials")
            startActivity(chooseIntent)
        }
    }

}