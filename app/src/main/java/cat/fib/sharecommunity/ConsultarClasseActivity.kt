package cat.fib.sharecommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.fib.sharecommunity.ui.ConsultarClasseFragment
import dagger.hilt.android.AndroidEntryPoint

/** Activity ConsultarClasse
 *
 *  Activitat encarregada de consultar la informació completa d'una classe
 *
 *  @constructor Crea l'Activity ConsultarClasse
 *  @author Albert Miñana Montecino
 */
@AndroidEntryPoint
class ConsultarClasseActivity : AppCompatActivity() {

    /** Function onCreate
     *
     *  Funció encarregada de crear, configurar i mostrar el contingut de l'activitat mitjançant el Fragment ConsultarClasse
     *
     *  @param savedInstanceState
     *  @author Albert Miñana Montecino
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_classe)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = ConsultarClasseFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}