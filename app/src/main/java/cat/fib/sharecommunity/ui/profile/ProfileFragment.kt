package cat.fib.sharecommunity.ui.profile

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import cat.fib.sharecommunity.AuthenticationProviders
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.utils.Status
import cat.fib.sharecommunity.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Fragment Profile
 *
 *  Fragment encarregat de consultar i modificar la informació completa del perfil d'usuari
 *
 *  @constructor Crea el Fragment ProfileFragment
 *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<UserViewModel>()

    private var identificadorUsuari: String? = null   // Identificador de l'usuari


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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val userFragment = UserFragment()
        val physicalFragment = PhysicalFragment()
        val sportFragment = SportFragment()

        val buttonUser: Button = view.findViewById(R.id.dadesUsuari)
        val buttonPhysical: Button = view.findViewById(R.id.dadesFisic)
        val buttonSport: Button = view.findViewById(R.id.dadesEsport)
        val buttonDelete: Button = view.findViewById(R.id.eliminarPerfil)

        // Establim el userFragment per defecte
        childFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, userFragment)
            commit()
        }

        // Configurem el buttonUser per mostrar el fragment userFragment
        buttonUser.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, userFragment)
                commit()
            }
        }

        // Configurem el buttonPhysical per mostrar el fragment physicalFragment
        buttonPhysical.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, physicalFragment)
                commit()
            }
        }

        // Configurem el buttonSport per mostrar el fragment sportFragment
        buttonSport.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, sportFragment)
                commit()
            }
        }

        // Configurem el buttonDelete per eliminar un perfil d'usuari
        buttonDelete.setOnClickListener {
            println("Intent eliminar")
            confirmDeleteUserProfile()
        }

        return view
    }

    /** Function deleteUserProfile
     *
     *  Funció encarregada d'eliminar el perfil d'usuari
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael, Oriol Prat Marín
     */
    private fun deleteUserProfile() {
        identificadorUsuari?.let {
            viewModel.deleteUser(it.toInt())
        }

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                val prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                val intent = Intent(activity, AuthenticationProviders::class.java)
                startActivity(intent)
            }
            else if (it.status == Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })
    }

    /** Function confirmDeleteUserProfile
     *
     *  Funció encarregada de confirmar la petició d'eliminació del perfil d'usuari
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia, Daniel Cárdenas Rafael
     */
    public fun confirmDeleteUserProfile() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Confirmació")
        builder.setMessage("Estàs segur que vols eliminar el teu perfil?")
        builder.setPositiveButton("Acceptar", DialogInterface.OnClickListener { dialog, which -> deleteUserProfile() })
        builder.setNegativeButton("Cancel·lar", null)
        val dialog: AlertDialog = builder.create()
        dialog.create()
        dialog.show()
    }

}