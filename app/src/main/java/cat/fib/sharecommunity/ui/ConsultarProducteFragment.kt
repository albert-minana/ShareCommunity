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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.MainActivity
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ProductViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_crear_producte.*
import kotlinx.android.synthetic.main.activity_crear_producte.Tipus_Material
import kotlinx.android.synthetic.main.activity_crear_producte.Tipus_Roba
import kotlinx.android.synthetic.main.activity_crear_producte.textInputLayoutDescripcio
import kotlinx.android.synthetic.main.fragment_consultar_producte.*

// Paràmetres d'inicialització del Fragment
private const val EXTRA_MESSAGE_1 = "cat.fib.sharecommunity.MESSAGE1"
private const val EXTRA_MESSAGE_2 = "cat.fib.sharecommunity.MESSAGE2"

/** Fragment ConsultarProducte
 *
 *  Fragment encarregat de consultar la informació completa d'un producte
 *
 *  @constructor Crea el Fragment ConsultarProducte
 *  @author Daniel Cárdenas Rafael
 */
@AndroidEntryPoint
class ConsultarProducteFragment : Fragment() {

    private val viewModel by viewModels<ProductViewModel>()    // ViewModel del producte

    private var idProducte: String? = null              // ID del producte
    private var userEmailProducte: String? = null       // userEmail del producte
    private var producte: Product? = null               // Model del producte

    lateinit var imatgeProducte: ImageView              // ImageView amb la imatge del producte
    lateinit var nomProducte: TextView                  // TextView amb el nom del producte
    lateinit var contingutDescripcioProducte: TextView  // TextView amb la descripció del producte
    lateinit var contingutUbicacioProducte: TextView    // TextView amb la ubicació del producte
    lateinit var contingutEstatProducte: TextView       // TextView amb l'estat del producte
    lateinit var contingutDataPublicacio: TextView      // TextView amb la data de publicació del producte
    lateinit var contingutEmailUsuari: TextView         // TextView amb l'email de l'usuari del producte
    lateinit var tipusRoba: RadioButton
    lateinit var tipusMaterial: RadioButton
    lateinit var tipusJoguina: RadioButton
    lateinit var tipusAltre: RadioButton

    lateinit var botoCompartir: Button                  // Button per compartir producte
    lateinit var buttons: LinearLayout                  // Buttons
    lateinit var botoEliminar: Button                   // Button per eliminar producte
    lateinit var botoEditar: Button                     // Button per editar producte
    lateinit var botoFinalitzar: Button                 // Button per finalitzar producte


    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idProducte = activity?.intent?.getStringExtra(EXTRA_MESSAGE_1)
        userEmailProducte = activity?.intent?.getStringExtra(EXTRA_MESSAGE_2)
    }

    /** Function onCreateView
     *
     *  Funció encarregada de configurar i mostrar el contingut del fragment
     *
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @author Daniel Cárdenas Rafael
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_consultar_producte, container, false)

        imatgeProducte = view.findViewById(R.id.imatgeProducte)
        nomProducte = view.findViewById(R.id.nomProducte)
        contingutDescripcioProducte = view.findViewById(R.id.TextInputEditTextDescripcioProducte)
        contingutUbicacioProducte = view.findViewById(R.id.TextInputEditTextUbicacioProducte)
        contingutEstatProducte = view.findViewById(R.id.TextInputEditTextEstatProducte)
        tipusRoba = view.findViewById(R.id.Tipus_Roba)
        tipusMaterial = view.findViewById(R.id.Tipus_Material)
        tipusJoguina = view.findViewById(R.id.Tipus_Joguina)
        tipusAltre = view.findViewById(R.id.Tipus_Altre)
        contingutDataPublicacio = view.findViewById(R.id.TextInputEditTextDataProducte)
        contingutEmailUsuari = view.findViewById(R.id.TextInputEditTextEmailProducte)

        botoCompartir = view.findViewById(R.id.botoCompartir)
        buttons = view.findViewById(R.id.LinearLayoutButtons)
        botoEliminar = view.findViewById(R.id.eliminarProducte)
        botoEditar = view.findViewById(R.id.editarProducte)
        botoFinalitzar = view.findViewById(R.id.finalitzarProducte)

        idProducte?.let {
            viewModel.getProduct(it, userEmailProducte!!)
        }

        viewModel.product?.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                producte = it.data
                setContent(it.data)
                val prefs = this.requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
                val userEmail = prefs.getString("email", null)
                if (producte!!.userEmail == userEmail && producte!!.state == "Disponible") convertToEditarProducteActivity()
            }
            else if (it.status == Resource.Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        setUpShareButton()



        return view
    }

    private fun convertToEditarProducteActivity() {
        contingutDescripcioProducte.isFocusableInTouchMode = true
        contingutDescripcioProducte.isCursorVisible = true
        contingutDescripcioProducte.isClickable = true

        contingutUbicacioProducte.isFocusableInTouchMode = true
        contingutUbicacioProducte.isCursorVisible = true
        contingutUbicacioProducte.isClickable = true

        tipusRoba.isFocusableInTouchMode = true
        tipusRoba.isCursorVisible = true
        tipusRoba.isClickable = true

        tipusMaterial.isFocusableInTouchMode = true
        tipusMaterial.isCursorVisible = true
        tipusMaterial.isClickable = true

        tipusJoguina.isFocusableInTouchMode = true
        tipusJoguina.isCursorVisible = true
        tipusJoguina.isClickable = true

        tipusAltre.isFocusableInTouchMode = true
        tipusAltre.isCursorVisible = true
        tipusAltre.isClickable = true

        buttons.visibility = VISIBLE

        contingutEstatProducte.isEnabled = false
        contingutDataPublicacio.isEnabled = false
        contingutEmailUsuari.isEnabled = false

        setUpEditButtons()
    }

    fun setUpEditButtons() {
        botoEliminar.setOnClickListener {
            confirmDeleteProduct()
        }

        botoEditar.setOnClickListener {
            updateProduct()
        }

        botoFinalitzar.setOnClickListener {
            changeProductState()
        }


    }

    /** Function deleteProduct
     *
     *  Funció encarregada d'eliminar el producte
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael
     */
    private fun deleteProduct() {
        viewModel.deleteProduct(producte!!.id, producte!!.userEmail)

        viewModel.idproduct.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }
            else if (it.status == Resource.Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })
    }

    /** Function updateProduct()
     *
     *  Funció que comprova si els camps són correctes per actualitzar el producte.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun updateProduct() {
        val validateDescription = validateDescription()
        val validateUbication = validateUbication()
        val validateType = validateType()
        if (validateDescription && validateUbication && validateType) {
            val descripcio = contingutDescripcioProducte?.text.toString()
            val ubicacio = contingutUbicacioProducte?.text.toString()
            var type: String
            when {
                tipusRoba?.isChecked == true -> type = "Roba"
                tipusMaterial?.isChecked == true -> type = "Material"
                tipusJoguina?.isChecked == true -> type = "Joguina"
                else -> type = "Altre"
            }

            producte!!.description = descripcio
            producte!!.ubication = ubicacio
            producte!!.type = type

            viewModel.updateProduct(producte!!)
            viewModel.product.observe(viewLifecycleOwner, Observer {
                if (it.status == Resource.Status.SUCCESS) {
                    producte = it.data
                    setContent(producte)
                }
                else if (it.status == Resource.Status.ERROR) {
                    Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    /** Function changeProductState()
     *
     *  Funció que canvia l'estat del producte a Finalitzat.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun changeProductState() {
        producte!!.state = "Finalitzat"

        viewModel.updateProduct(producte!!)
        viewModel.product.observe(viewLifecycleOwner, Observer {
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
        val descripcio = contingutDescripcioProducte?.text.toString()
        if (descripcio.isEmpty()) {
            textInputLayoutDescripcioProducte?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutDescripcioProducte?.setError(null)
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
        val ubicacio = contingutUbicacioProducte?.text.toString()
        if (ubicacio.isEmpty()) {
            textInputLayoutUbicacioProducte?.setError("El camp no pot ser buit")
            return false
        }
        else {
            textInputLayoutUbicacioProducte?.setError(null)
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
        val TypeNotChecked = (tipusRoba?.isChecked == false && tipusMaterial?.isChecked == false && tipusJoguina?.isChecked == false && tipusAltre?.isChecked == false)
        if (TypeNotChecked) {
            textInputLayoutTipusProducte?.setError("El camp no està seleccionat")
            tipusAltre?.setError("El camp no està seleccionat")
            return false
        }
        else {
            textInputLayoutTipusProducte?.setError(null)
            tipusAltre?.setError(null)
            return true
        }
    }

    /** Function confirmDeleteProduct
     *
     *  Funció encarregada de confirmar la petició d'eliminació del producte
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael
     */
    public fun confirmDeleteProduct() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Confirmació")
        builder.setMessage("Estàs segur que vols eliminar el teu producte?")
        builder.setPositiveButton("Acceptar", DialogInterface.OnClickListener { dialog, which -> deleteProduct() })
        builder.setNegativeButton("Cancel·lar", null)
        val dialog: AlertDialog = builder.create()
        dialog.create()
        dialog.show()
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'un producte
     *
     *  @param  classData
     *  @author Daniel Cárdenas Rafael
     */
    fun setContent(productData: Product?){
        if (productData?.photo != null) Picasso.get().load(productData?.photo).into(imatgeProducte)
        nomProducte.text = productData?.name.toString()
        contingutDescripcioProducte.text = productData?.description.toString()
        contingutUbicacioProducte.text = productData?.ubication.toString()
        contingutEstatProducte.text = productData?.state.toString()
        when (productData?.type) {
            "Roba" -> tipusRoba.isChecked = true
            "Material" -> tipusMaterial.isChecked = true
            "Joguina" -> tipusJoguina.isChecked = true
            else -> tipusAltre.isChecked = true
        }
        contingutDataPublicacio.text = productData?.publishDate.toString()
        contingutEmailUsuari.text = productData?.userEmail.toString()
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir un contingut d'exemple amb la informació completa d'un producte
     *
     *  @author Daniel Cárdenas Rafael
     */
    fun setContent(){
        imatgeProducte.setImageResource(R.drawable.fotopae)
        nomProducte.text = "Llibre matemàtiques"
        contingutDescripcioProducte.text = "LLibre mates de 4t d'ESO"
        contingutUbicacioProducte.text = "Barcelona"
        contingutEstatProducte.text = "Disponible"
    }

    private fun setUpShareButton(){
        botoCompartir.setOnClickListener {
            val missatge = "L'aplicació ShareCommunity és un espai d'intercanvi de productes i serveis. Un membre de la comunitat ha compratit el producte " + producte!!.name+", potser t'interessa!"
            val intent = Intent()
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, missatge)
            intent.action = Intent.ACTION_SEND
            val chooseIntent = Intent.createChooser(intent, "Compartir en xarxes socials")
            startActivity(chooseIntent)
        }
    }

}