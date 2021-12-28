package cat.fib.sharecommunity.ui.profile

import android.content.Context
import android.graphics.Color
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
import cat.fib.sharecommunity.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * A simple [Fragment] subclass.
 * Use the [PhysicalFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class PhysicalFragment : Fragment(R.layout.fragment_physical) {
/*
    private val viewModel by viewModels<UserViewModel>()
    private val viewModelHealth by viewModels<HealthDataViewModel>()

    private var identificadorUsuari: String? = null     // Identificador de l'usuari
    private var user: User? = null                      // Model de l'usuari
*/
    lateinit var weight: TextView
    lateinit var height: TextView
    lateinit var imc: TextView
    lateinit var igc: TextView
    lateinit var botoActualitzarPerfil: Button
/*
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
        identificadorUsuari = prefs.getString("userId", null)
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
        val v: View = inflater.inflate(R.layout.fragment_physical, container, false)
        weight = v.findViewById(R.id.Pes)
        height = v.findViewById(R.id.Alçada)
        imc = v.findViewById(R.id.IMC)
        igc = v.findViewById(R.id.IGC)
        createExampleGraph(v)

        botoActualitzarPerfil = v.findViewById(R.id.ActualitzarPreferencesPerfilButton)

        identificadorUsuari?.let {
            //viewModel.getUser(it.toInt())
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                user = it.data
                setUpPhysical(it.data)
            } else if (it.status == Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })

        setupUpdateProfileButton()

        return v
    }

    /** Function setUpPhysical
     *
     *  Funció encarregada d'establir el contingut amb la informació de físic del perfil d'usuari
     *
     *  @param userData
     *  @author  Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
     */
    private fun setUpPhysical(userData: User?) {
        /*
        weight.text = userData?.weight.toString()
        height.text = userData?.height.toString()
        imc.text = userData?.imc.toString()
        igc.text = userData?.igc.toString()

         */
    }

    private fun setUpPhysical2(userData: HealthData?) {
        weight.text = userData?.weight.toString()
        height.text = userData?.height.toString()
        imc.text = userData?.imc.toString()
        igc.text = userData?.igc.toString()
    }

    /** Function setupUpdateProfileButton
     *
     *  Funció que comprova si els camps són correctes per actualitzar el perfil d'usuari.
     *
     *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
     */
    fun setupUpdateProfileButton() {
        botoActualitzarPerfil.setOnClickListener {
            val validateWeight = validateWeight()
            val validateHeight = validateHeight()
            if (validateWeight && validateHeight) {
                /*
                user!!.weight = weight.text.toString().toFloat()
                user!!.height = height.text.toString().toFloat()
                val user_weight = weight.text.toString().toFloat()
                val user_height = height.text.toString().toFloat()
                val user_id = identificadorUsuari?.toInt()
                user?.weight = user_weight
                user?.height = user_height
                viewModel.updateUser(user!!)
                viewModel.user.observe(viewLifecycleOwner, Observer {
                    if (it.status == Status.SUCCESS) {
                        setUpPhysical(it.data) //No cal
                        updateUser(user_id!!, user_weight, user_height)
                    } else if (it.status == Status.ERROR) {
                        Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
                    }
                })

                 */

            }
        }
    }

    private fun updateUser(user_id: Int, user_weight: Float, user_height: Float) {
        val healthData = HealthData(user_id, user_weight, user_height)
        viewModelHealth.createHealthData(healthData!!)
        viewModelHealth.healthData.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                setUpPhysical2(it.data)
            } else if (it.status == Status.ERROR) {
                Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
            }
        })
   }
*/
   /** Function validateWeight
    *
    *  Funció que comprova si el camp Weight és correcte.
    *
    *  @return Retorna cert si és correcte, fals en cas contrari.
    *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
    */
   private fun validateWeight(): Boolean {
       val pes = weight.text.toString()
       if (pes.isEmpty()) {
           textInputLayoutNom?.setError("El camp no pot ser buit")
           return false
       }
       else {
           textInputLayoutNom?.setError(null)
           return true
       }
   }

   /** Function validateHeight
    *
    *  Funció que comprova si el camp Height és correcte.
    *
    *  @return Retorna cert si és correcte, fals en cas contrari.
    *  @author Adrià Espinola Garcia, Albert Miñana Montecino, Daniel Cárdenas Rafael
    */
   private fun validateHeight(): Boolean {
       val altura = height.text.toString()
       if (altura.isEmpty()) {
           textInputLayoutNom?.setError("El camp no pot ser buit")
           return false
       }
       else {
           textInputLayoutNom?.setError(null)
           return true
       }
   }

   fun createExampleGraph(view: View) {
       val graph: GraphView = view.findViewById(R.id.graph)
       val series1: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>( arrayOf(
           DataPoint(0.0, -2.0),
           DataPoint(1.0, 5.0),
           DataPoint(2.0, 3.0),
           DataPoint(3.0, 2.0),
           DataPoint(4.0, 6.0)
       ))
       series1.color = Color.DKGRAY
       graph.addSeries(series1)

       val series2: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>( arrayOf(
           DataPoint(0.0, 3.0),
           DataPoint(1.0, 3.0),
           DataPoint(2.0, 6.0),
           DataPoint(3.0, 2.0),
           DataPoint(4.0, 5.0)
       ))
       series2.color = Color.LTGRAY
       graph.addSeries(series2)

       series1.title = "Pes"
       series2.title = "Alçada"
       graph.legendRenderer.isVisible = true
       graph.legendRenderer.align = LegendRenderer.LegendAlign.BOTTOM
   }

}