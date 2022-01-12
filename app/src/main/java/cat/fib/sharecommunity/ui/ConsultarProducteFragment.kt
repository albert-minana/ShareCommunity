package cat.fib.sharecommunity.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.dataclasses.Product
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    lateinit var imatgeProducte: ImageView              // ImageView amb la imatge del producte
    lateinit var nomProducte: TextView                  // TextView amb el nom del producte
    lateinit var contingutDescripcioProducte: TextView  // TextView amb la descripció del producte
    lateinit var contingutUbicacioProducte: TextView    // TextView amb la ubicació del producte
    lateinit var contingutEstatProducte: TextView       // TextView amb l'estat del producte
    lateinit var contingutTipusProducte: TextView       // TextView amb el tipus del producte
    lateinit var contingutDataPublicacio: TextView      // TextView amb la data de publicació del producte
    lateinit var contingutEmailUsuari: TextView         // TextView amb l'email de l'usuari del producte

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
        contingutDescripcioProducte = view.findViewById(R.id.contingutDescripcioProducte)
        contingutUbicacioProducte = view.findViewById(R.id.contingutUbicacioProducte)
        contingutEstatProducte = view.findViewById(R.id.contingutEstatProducte)
        contingutTipusProducte = view.findViewById(R.id.contingutTipusProducte)
        contingutDataPublicacio = view.findViewById(R.id.contingutDataPublicacio)
        contingutEmailUsuari = view.findViewById(R.id.contingutEmailUsuari)

        idProducte?.let {
            viewModel.getProduct(it, userEmailProducte!!)
        }

        viewModel.product?.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS)
                setContent(it.data)
            else if (it.status == Resource.Status.ERROR)
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        return view
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir el contingut amb la informació completa d'un producte
     *
     *  @param  classData
     *  @author Daniel Cárdenas Rafael
     */
    fun setContent(productData: Product?){
        //Picasso.get().load(Configuration.Companion.urlServer + classData?.pre.toString()).into(imatgeProducte)
        nomProducte.text = productData?.name.toString()
        contingutDescripcioProducte.text = productData?.description.toString()
        contingutUbicacioProducte.text = productData?.ubication.toString()
        contingutEstatProducte.text = productData?.state.toString()
        contingutTipusProducte.text = productData?.type.toString()
        contingutDataPublicacio.text = productData?.publishDate.toString()
        contingutEmailUsuari.text = productData?.userEmail.toString()
    }
/*
    private fun stateName(state: String): String? {
        when (state) {
            "D" -> return "Disponible"
            "R" -> return "Reservat"
            "F" -> return "Finalitzat"
            else -> return null
        }
    }

    private fun typeName(type: String): String? {
        when (type) {
            "R" -> return "Roba"
            "M" -> return "Material escolar"
            "J" -> return "Joguines"
            "A" -> return "Altres"
            else -> return null
        }
    } */

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
        contingutTipusProducte.text = "Material escolar"
    }

}