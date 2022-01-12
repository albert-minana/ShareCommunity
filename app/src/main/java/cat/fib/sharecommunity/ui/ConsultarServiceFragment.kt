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
import cat.fib.sharecommunity.dataclasses.Service
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.ServiceViewModel

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

        //imatgeService = view.findViewById(R.id.imatgeProducte)
        nomService = view.findViewById(R.id.nomService)
        contingutDescripcioService = view.findViewById(R.id.contingutDescripcioService)
        contingutUbicacioService = view.findViewById(R.id.contingutUbicacioService)
        contingutEstatService = view.findViewById(R.id.contingutEstatService)
        contingutTipusService = view.findViewById(R.id.contingutTipusService)
        contingutDataIniService = view.findViewById(R.id.contingutDataIniService)
        contingutDataFiService = view.findViewById(R.id.contingutDataFiService)
        contingutDataPublicacio = view.findViewById(R.id.contingutDataPublicacio)
        contingutEmailUsuari = view.findViewById(R.id.contingutEmailUsuari)

        idService?.let {
            viewModel.getService(userEmailService!!, it)
        }

        viewModel.service?.observe(viewLifecycleOwner, Observer {
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
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
     */
    fun setContent(productData: Service?){
        //Picasso.get().load(Configuration.Companion.urlServer + classData?.pre.toString()).into(imatgeProducte)
        nomService.text = productData?.name.toString()
        contingutDescripcioService.text = serviceData?.description.toString()
        contingutEstatService.text = serviceData?.state.toString()
        contingutTipusService.text = serviceData?.type.toString()
        contingutDataPublicacio.text = serviceData?.publishDate.toString()
        contingutEmailUsuari.text = serviceData?.userEmail.toString()
    }
/*
    private fun stateName(state: String): String? {
        when (state) {
            "D" -> return "Disponible"
            "R" -> return "Reservat"
            "F" -> return "Finalitzat"
            else -> return null
        }
    }*/

    private fun typeName(type: String): String? {
        when (type) {
            "G" -> return "Canguratge"
            "T" -> return "Transport"
            "C" -> return "Classes"
            "B" -> return "Banc de temps"
            "S" -> return "Compartir activitats"
            "A" -> return "Altres"
            else -> return null
        }
    }

    /** Function setContent
     *
     *  Funció encarregada d'establir un contingut d'exemple amb la informació completa d'un service
     *
     *  @author Daniel Cárdenas Rafael & Xavier Sancho-Tello i Bayarri
     */
    fun setContent(){
        imatgeService.setImageResource(R.drawable.fotopae)
        nomService.text = "Llibre matemàtiques"
        contingutDescripcioService.text = "LLibre mates de 4t d'ESO"
        contingutUbicacioService.text = "Barcelona"
        contingutEstatService.text = "Disponible"
        contingutTipusService.text = "Material escolar"
        contingutDataIniService.text = "13:00"
        contingutDataFiService.text = "15:00"
    }

}