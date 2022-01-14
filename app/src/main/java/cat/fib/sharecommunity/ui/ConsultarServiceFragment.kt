package cat.fib.sharecommunity.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.MainActivity
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.ui.dialog.DatePickerFragment
import cat.fib.sharecommunity.viewmodels.ServiceViewModel
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_crear_service.*
import kotlinx.android.synthetic.main.fragment_consultar_service.*


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
    private var tipus: String? = null

    lateinit var imatgeService: ImageView              // ImageView amb la imatge del servei
    lateinit var nomService: TextView                  // TextView amb el nom del servei
    lateinit var contingutDescripcioService: TextView  // TextView amb la descripció del servei
    lateinit var contingutUbicacioService: TextView    // TextView amb la ubicació del servei
    lateinit var contingutEstatService: TextView       // TextView amb l'estat del servei
    lateinit var contingutTipusService: TextView       // TextView amb el tipus del servei
    lateinit var contingutDataPublicacio: TextView      // TextView amb la data de publicació del servei
    lateinit var contingutEmailUsuari: TextView         // TextView amb l'email de l'usuari del servei
    lateinit var contingutDataInici: TextView
    lateinit var contingutDataFi: TextView

    lateinit var botoCompartir: Button                  // Button per compartir service
    lateinit var buttons: LinearLayout                  // Buttons
    lateinit var botoEliminar: Button                   // Button per eliminar service
    lateinit var botoEditar: Button                     // Button per editar service
    lateinit var botoFinalitzar: Button                 // Button per finalitzar service
    var mMultiLineRadioGroup: MultiLineRadioGroup? = null


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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_consultar_service, container, false)

        nomService = view.findViewById(R.id.nomService)
        contingutDescripcioService = view.findViewById(R.id.TextInputEditTextDescripcioService)
        contingutUbicacioService = view.findViewById(R.id.TextInputEditTextUbicacioService)
        contingutEstatService = view.findViewById(R.id.TextInputEditTextEstatService)
        contingutDataPublicacio = view.findViewById(R.id.TextInputEditTextDataService)
        contingutEmailUsuari = view.findViewById(R.id.TextInputEditTextEmailService)
        contingutDataInici = view.findViewById(R.id.TextInputEditTextDataIniService)
        contingutDataFi = view.findViewById(R.id.TextInputEditTextDataFiService)

        botoCompartir = view.findViewById(R.id.botoCompartirService)
        buttons = view.findViewById(R.id.LinearLayoutServiceButtons)
        botoEliminar = view.findViewById(R.id.eliminarService)
        botoEditar = view.findViewById(R.id.editarService)
        botoFinalitzar = view.findViewById(R.id.finalitzarService)
        mMultiLineRadioGroup = view.findViewById(R.id.main_activity_multi_line_radio_group_edit)
        mMultiLineRadioGroup?.setOnCheckedChangeListener(MultiLineRadioGroup.OnCheckedChangeListener { group, button ->
            tipus = button.text.toString()
        })

        idService?.let {
            viewModel.getService(it, userEmailService!!)
        }

        viewModel.service?.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                servei = it.data
                tipus = it.data!!.type
                setContent(it.data)
                val prefs = this.requireActivity().getSharedPreferences(
                    getString(R.string.prefs_file),
                    Context.MODE_PRIVATE
                )
                val userEmail = prefs.getString("email", null)
                if (servei!!.userEmail == userEmail && servei!!.state == "Disponible") convertToEditarServiceActivity()
            } else if (it.status == Resource.Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        setUpShareButton()

        return view
    }

    private fun convertToEditarServiceActivity() {
        contingutDescripcioService.isFocusableInTouchMode = true
        contingutDescripcioService.isCursorVisible = true
        contingutDescripcioService.isClickable = true

        contingutUbicacioService.isFocusableInTouchMode = true
        contingutUbicacioService.isCursorVisible = true
        contingutUbicacioService.isClickable = true

        mMultiLineRadioGroup?.isFocusableInTouchMode = true
        mMultiLineRadioGroup?.isClickable = true

        contingutDataInici?.setOnClickListener {
            showStartDatePickerDialog()
        }
        contingutDataFi?.setOnClickListener {
            showEndDatePickerDialog()
        }

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
            } else if (it.status == Resource.Status.ERROR) Toast.makeText(
                activity,
                "ERROR!",
                Toast.LENGTH_LONG
            ).show()
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


            servei!!.description = descripcio
            servei!!.ubication = ubicacio
            servei!!.type = tipus!!

            viewModel.updateService(servei!!)
            viewModel.service.observe(viewLifecycleOwner, Observer {
                if (it.status == Resource.Status.SUCCESS) {
                    servei = it.data
                    setContent(servei)
                } else if (it.status == Resource.Status.ERROR) {
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
            } else if (it.status == Resource.Status.ERROR) {
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
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello
     */
    private fun validateType(): Boolean {
        val TypeNotChecked = (tipus == null)
        if (TypeNotChecked) {
            textInputLayoutTipusService?.setError("El camp no està seleccionat")
            return false
        }
        else {
            textInputLayoutTipusService?.setError(null)
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
        builder.setPositiveButton(
            "Acceptar",
            DialogInterface.OnClickListener { dialog, which -> deleteService() })
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
        contingutUbicacioService.text = serviceData?.ubication.toString()
        contingutEstatService.text = serviceData?.state.toString()
        contingutDataInici.text = serviceData?.data_ini.toString()
        contingutDataFi.text = serviceData?.data_fi.toString()
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
    }

    private fun setUpShareButton() {
        botoCompartir.setOnClickListener {
            val missatge =
                "L'aplicació ShareCommunity és un espai d'intercanvi de productes i serveis. Un membre de la comunitat ha compartit el servei " + servei!!.name + ", potser t'interessa!"
            val intent = Intent()
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, missatge)
            intent.action = Intent.ACTION_SEND
            val chooseIntent = Intent.createChooser(intent, "Compartir en xarxes socials")
            startActivity(chooseIntent)
        }
    }

    /** Funció showStartDatePickerDialog
     *
     *  Funció que selecciona la data marcada en el fragment del calendari i l'assigna a la data de d'inici del servei.
     *
     *  @author Daniel Cárdenas.
     */
    private fun showStartDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day.twoDigits()
            val monthStr = (month + 1).twoDigits() // +1 because January is zero
            val selectedDate = "$dayStr/$monthStr/$year"
            contingutDataInici?.setText(selectedDate)
        })
        newFragment.show(childFragmentManager, "datePicker")
    }


    /** Funció showEndDatePickerDialog
     *
     *  Funció que selecciona la data marcada en el fragment del calendari i l'assigna a la data final del servei.
     *
     *  @author Daniel Cárdenas.
     */
    private fun showEndDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day.twoDigits()
            val monthStr = (month + 1).twoDigits() // +1 because January is zero
            val selectedDate = "$dayStr/$monthStr/$year"
            contingutDataFi?.setText(selectedDate)
        })

        newFragment.show(childFragmentManager, "datePicker")
    }

    /** Funció twoDigits
     *
     *  Funció que afegeix el dígit '0' davant d'un nombre menor que 10.
     *
     *  @author Daniel Cárdenas.
     */
    fun Int.twoDigits() = if (this <= 9) "0$this" else this.toString()

}