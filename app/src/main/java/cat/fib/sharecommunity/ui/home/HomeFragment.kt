package cat.fib.sharecommunity.ui.home

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cat.fib.sharecommunity.R
import cat.fib.sharecommunity.ui.CercarFiltrarProducteFragment
import cat.fib.sharecommunity.ui.CercarFiltrarServiceFragment
import cat.fib.sharecommunity.ui.profile.ProfileFragment

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var botoProductes: Button
    lateinit var botoServeis: Button
    lateinit var botoMeuEspai: Button
    lateinit var boto: ClipData.Item

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        botoProductes = root.findViewById(R.id.button11)
        botoServeis = root.findViewById(R.id.button12)
        botoMeuEspai = root.findViewById(R.id.button13)

        setUpProductesButton()
        setUpServeisButton()
        setUpMeuEspaiButton()


        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    private fun setUpProductesButton() {
        botoProductes.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, CercarFiltrarProducteFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setUpServeisButton() {
        botoServeis.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, CercarFiltrarServiceFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun setUpMeuEspaiButton() {
        botoMeuEspai.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.nav_host_fragment, ProfileFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

}