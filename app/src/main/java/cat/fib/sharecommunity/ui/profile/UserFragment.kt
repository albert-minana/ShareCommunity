package cat.fib.sharecommunity.ui.profile

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.MainActivity
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.data.models.User
import cat.fib.sharecommunity.ui.dialog.DatePickerFragment
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputLayout.END_ICON_NONE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/** Fragment User
 *
 *  Fragment encarregat de consultar i modificar la informació d'usuari del perfil d'usuari
 *
 *  @constructor Crea el Fragment UserFragment
 *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael
 */
@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {

    private val viewModel by viewModels<UserViewModel>()

    private var identificadorUsuari: String? = null     // Identificador de l'usuari
    private var proveidor: String? = null               // Proveïdor de l'usuari
    private var user: User? = null                      // Model de l'usuari

    lateinit var nom: TextView
    lateinit var cognoms: TextView
    lateinit var correu: TextView
    lateinit var contrasenya: TextView
    lateinit var telefon: TextView
    lateinit var data: TextView
    lateinit var sexeDona: RadioButton
    lateinit var sexeHome: RadioButton
    lateinit var sexeAltre: RadioButton
    lateinit var botoActualitzarPerfil: Button
    lateinit var layaoutContrasenya: TextInputLayout


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
        proveidor = prefs.getString("provider", null)

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
        val v: View = inflater.inflate(R.layout.fragment_user, container, false)
        nom = v.findViewById(R.id.Nom)
        cognoms = v.findViewById(R.id.Cognoms)
        correu = v.findViewById(R.id.CorreuElectronic)
        contrasenya = v.findViewById(R.id.Contrasenya)
        telefon = v.findViewById(R.id.Telefon)
        layaoutContrasenya = v.findViewById(R.id.textInputLayoutContrasenya)
        if (proveidor == "Google" || proveidor == "Facebook") {
            correu.isEnabled = false
            contrasenya.isEnabled = false
            layaoutContrasenya.endIconMode = END_ICON_NONE
        }
        data = v.findViewById(R.id.DataNaixement)
        data.setOnClickListener {
            showDatePickerDialog()
        }
        sexeDona = v.findViewById(R.id.Sexe_Dona)
        sexeHome = v.findViewById(R.id.Sexe_Home)
        sexeAltre = v.findViewById(R.id.Sexe_Altre)

        botoActualitzarPerfil = v.findViewById(R.id.ActualitzarUsuariButton)

        identificadorUsuari?.let {
            viewModel.getUserByEmail(it)
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                user = it.data
                setUpUser(it.data)
            } else if (it.status == Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        setupUpdateProfileButton()

        return v
    }

    /** Function setUpUser
     *
     *  Funció encarregada d'establir el contingut amb la informació d'usuari del perfil d'usuari
     *
     *  @param userData
     *  @author  Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
     */
    fun setUpUser(userData: User?) {
        nom.text = userData?.firstname.toString()
        cognoms.text = userData?.lastname.toString()
        correu.text = userData?.email.toString()
        contrasenya.text = userData?.password.toString()
        telefon.text = userData?.phone.toString()
        if (userData?.birthday != null) data.text = LocalDate.parse(userData?.birthday.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString()
        when (userData?.gender) {
            "M" -> sexeHome.isChecked = true
            "W" -> sexeDona.isChecked = true
            else -> sexeAltre.isChecked = true
        }
    }

    /** Function showDatePickerDialog
     *
     *  Funció que selecciona la data marcada en el fragment del calendari i l'assigna a la data de naixement de l'usuari.
     *
     *  @author Daniel Cárdenas.
     */
    private fun showDatePickerDialog() {
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val dayStr = day.twoDigits()
            val monthStr = (month + 1).twoDigits() // +1 because January is zero
            val selectedDate = "$dayStr/$monthStr/$year"
            data?.setText(selectedDate)
        })
        newFragment.show(childFragmentManager, "datePicker")
    }

    /** Function twoDigits
     *
     *  Funció que afegeix el dígit '0' davant d'un nombre menor que 10.
     *
     *  @author Daniel Cárdenas.
     */
    fun Int.twoDigits() = if (this <= 9) "0$this" else this.toString()

    /** Function setupUpdateProfileButton
     *
     *  Funció que comprova si els camps són correctes per actualitzar el perfil d'usuari.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun setupUpdateProfileButton() {
        botoActualitzarPerfil.setOnClickListener {
            val validateName = validateName()
            val validateLastName = validateLastName()
            var validateEmail = true
            if (proveidor == "Local") validateEmail = validateEmail()
            var validatePassword = true
            if (proveidor == "Local") validatePassword = validatePassword()
            val validatePhone = validatePhone()
            val validateBirthday = validateBirthday()
            val validateGender = validateGender()
            if (validateName && validateLastName && validateEmail && validatePassword && validatePhone && validateBirthday && validateGender) {
                user!!.firstname = nom.text.toString()
                user!!.lastname = cognoms.text.toString()
                if (proveidor == "Local") {
                    user!!.email = correu.text.toString()
                    user!!.password = contrasenya.text.toString()
                }
                user!!.phone = telefon.text.toString()
                user!!.birthday = LocalDate.parse(data.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()
                user!!.gender = when {
                    sexeHome.isChecked -> "M"
                    sexeDona.isChecked -> "W"
                    else -> "X"
                }
                viewModel.updateUser(user!!)
                viewModel.user.observe(viewLifecycleOwner, Observer {
                    if (it.status == Status.SUCCESS) {
                        setUpUser(it.data)
                        val prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                        prefs.putString("name", it.data?.firstname.toString() + " " + it.data?.lastname.toString())
                        prefs.putString("email", it.data?.email.toString())
                        prefs.apply()
                        (activity as MainActivity).setNameAndEmail()
                    } else if (it.status == Status.ERROR) {
                        showErrorField(2)
                        /*
                        if (it.status.toString().contentEquals("username")){
                            showErrorField(1)
                        }
                        else if (it.status.toString().contentEquals("email")){
                            showErrorField(2)
                        }
                        else Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
                         */
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
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validateName(): Boolean {
        val name = nom.text.toString()
        if (name.isEmpty()) {
            textInputLayoutNom?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutNom?.setError(null)
            return true
        }
    }

    /** Function validateLastName
     *
     *  Funció que comprova si el camp Cognoms és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validateLastName(): Boolean {
        val lastName = cognoms.text.toString()
        if (lastName.isEmpty()) {
            textInputLayoutCognoms?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutCognoms?.setError(null)
            return true
        }
    }

    /** Function validatePhone
     *
     *  Funció que comprova si el camp Telèfon és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validatePhone(): Boolean {
        val phone = telefon.text.toString()
        if (phone.isEmpty()) {
            textInputLayoutTelefon?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutTelefon?.setError(null)
            return true
        }
    }

    /** Function validateEmail
     *
     *  Funció que comprova si el camp Correu Electrònic és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validateEmail(): Boolean {
        val email = correu.text.toString()
        if (email.isEmpty()) {
            textInputLayoutCorreuElectronic?.setError("El camp no pot ser buit")
            return false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputLayoutCorreuElectronic?.setError("Format incorrecte")
            return false
        }
        else {
            textInputLayoutCorreuElectronic?.setError(null)
            return true
        }
    }

    /** Function validatePassword
     *
     *  Funció que comprova si el camp Contrasenya és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validatePassword(): Boolean {
        val password = contrasenya.text.toString()
        if (!validPassword(password)) {
            textInputLayoutContrasenya?.setError("Mínim 8 caràcters, 1 majúscula, 1 minúscula, 1 número, 1 símbol")
            return false
        }
        else {
            textInputLayoutContrasenya?.setError(null)
            return true
        }
    }

    /** Function validateBirthday
     *
     *  Funció que comprova si el camp Data Naixement és correcte.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validateBirthday(): Boolean {
        val birthday = data.text.toString()
        if (birthday.isEmpty()) {
            textInputLayoutDataNaixement?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutDataNaixement?.setError(null)
            return true
        }
    }

    /** Function validateGender
     *
     *  Funció que comprova la selecció del camp Sexe.
     *
     *  @return Retorna cert si és correcte, fals en cas contrari.
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    private fun validateGender(): Boolean {
        val genderNotChecked = (sexeDona.isChecked == false && sexeHome.isChecked == false && sexeAltre.isChecked == false)
        if (genderNotChecked) {
            textInputLayoutSexe?.setError("El camp no està seleccionat")
            sexeAltre.setError("El camp no està seleccionat")
            return false
        }
        else {
            textInputLayoutSexe?.setError(null)
            sexeAltre.setError(null)
            return true
        }
    }


    /** Function validPassword
     *
     *  Funció que comprova si una contrasenya és vàlida o no, comprovant que tingui com a mínim 8 caràcters amb una lletra majúscula, una minúscula, un número i un símbol.
     *
     *  @param password
     *  @return Retorna el booleà valida si es compleixen les condicions
     *  @author Daniel Cárdenas.
     */
    fun validPassword(password: String): Boolean {
        var valida: Boolean = true
        var minuscula: Int = 0
        var mayuscula: Int = 0
        var numero: Int = 0
        var caracterespecial: Int = 0

        if (password.length < 8) return false
        var i: Int = 0
        while (i < password.length) {
            var c = password[i].toChar()
            if (c <= ' ' || c > '~') {
                valida = false
                break
            }
            if ((c > ' ' && c < '0') || (c >= ':' && c < 'A') || (c >= '[' && c < 'a') || (c >= '{' && c < 127.toChar())) {
                caracterespecial++
            }
            if (c >= '0' && c < ':') numero++
            if (c >= 'A' && c < '[') mayuscula++
            if (c >= 'a' && c < '{') minuscula++
            i++
        }
        valida = valida && caracterespecial > 0 && numero > 0 && minuscula > 0 && mayuscula > 0
        return valida
    }

    /** Function showErrorField
     *
     *  Funció encarregada de mostrar missatges d'error en els camps del perfil d'usuari
     *
     *  @param  fieldNumber Indica 1 l'error està relacionat amb el nom d'usuari ja existeix, indica 2 l'error està relacionat amb el correu electrònic, indica 3 si l'error està relacionat amb qualsevol dels dos
     *  @author Albert Miñana Montecino
     */
    private fun showErrorField(fieldNumber: Int) {
        var message: String = ""
        if (fieldNumber == 1){
            message = "El nom d'usuari introduït ja existeix. Si us plau, escolli'n un altre"
        }
        else if (fieldNumber == 2){
            message = "El correu electrònic introduït ja existeix. Si us plau, escolli'n un altre"
        }
        else if (fieldNumber == 3){
            message = "El nom d'usuari o el correu electrònic introduïts ja existeixen. Si us plau, escolli'n uns altres"
        }
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Advertència")
        builder.setMessage(message)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.create()
        dialog.show()
    }
}