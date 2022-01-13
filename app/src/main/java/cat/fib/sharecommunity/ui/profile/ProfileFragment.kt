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
import cat.fib.sharecommunity.dataclasses.Resource
import cat.fib.sharecommunity.viewmodels.UserProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

/** Fragment Profile
 *
 *  Fragment encarregat de consultar i modificar la informació completa del perfil d'usuari
 *
 *  @constructor Crea el Fragment ProfileFragment
 *  @author Albert Miñana Montecino, Adrià Espinola Garcia
 */
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<UserProfileViewModel>()

    private var emailUsuari: String? = null   // Identificador de l'usuari


    /** Function onCreate
     *
     *  Funció encarregada de crear el fragment
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        emailUsuari = prefs.getString("email", null)
    }

    /** Function onCreateView
     *
     *  Funció encarregada de configurar i mostrar el contingut del fragment
     *
     *  @param inflater
     *  @param container
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val userFragment = UserFragment()
        val interestFragment = InterestFragment()
        val historyFragment = HistoryFragment()

        val buttonUser: Button = view.findViewById(R.id.dadesUsuari)
        val buttonInterest: Button = view.findViewById(R.id.dadesIneteres)
        val buttonHistory: Button = view.findViewById(R.id.dadesHistoric)
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

        // Configurem el buttonInterest per mostrar el fragment interestFragment
        buttonInterest.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, interestFragment)
                commit()
            }
        }

        // Configurem el buttonHistory per mostrar el fragment historyFragment
        buttonHistory.setOnClickListener {
            childFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, historyFragment)
                commit()
            }
        }

        // Configurem el buttonDelete per eliminar un perfil d'usuari
        buttonDelete.setOnClickListener {
            confirmDeleteUserProfile()
        }
        return view
    }

    /** Function deleteUserProfile
     *
     *  Funció encarregada d'eliminar el perfil d'usuari
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
     */
    private fun deleteUserProfile() {
        emailUsuari?.let {
            viewModel.deleteUser(it)
        }
        viewModel.idUserProfile.observe(viewLifecycleOwner, Observer {
            if (it.status == Resource.Status.SUCCESS) {
                val prefs = requireActivity().getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
                prefs.clear()
                prefs.apply()
                val intent = Intent(activity, AuthenticationProviders::class.java)
                startActivity(intent)
            }
            else if (it.status == Resource.Status.ERROR) Toast.makeText(activity, "ERROR!", Toast.LENGTH_LONG).show()
        })
    }

    /** Function confirmDeleteUserProfile
     *
     *  Funció encarregada de confirmar la petició d'eliminació del perfil d'usuari
     *
     *  @author Albert Miñana Montecino, Adrià Espinola Garcia
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