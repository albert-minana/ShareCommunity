package cat.fib.sharecommunity.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

/** Classe DatePickerFragment
 *
 *  Classe que implementa el calendari per seleccionar la data de naixement de l'usuari
 *
 *  @constructor Crea un listener amb valor nul
 *  @author Daniel Cárdenas.
 */
class DatePickerFragment : DialogFragment() {

    private var listener: DatePickerDialog.OnDateSetListener? = null

    /** Funció inicialitzadora
     *
     *  Funció que inicialitza un calendari en un rang desde fa 110 anys fins fa 7 anys i retorna un dialog.
     *
     *  @param savedInstanceState
     *  @return Retorna un Dialog.
     *  @author Daniel Cárdenas.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(requireActivity(), listener, year, month, day)

        c.set(Calendar.DAY_OF_MONTH, day)
        datePickerDialog.datePicker.minDate = c.timeInMillis
        c.set(Calendar.YEAR, year + 1)
        datePickerDialog.datePicker.maxDate = c.timeInMillis
        return datePickerDialog
    }

    companion object {
        fun newInstance(listener: DatePickerDialog.OnDateSetListener): DatePickerFragment {
            val fragment = DatePickerFragment()
            fragment.listener = listener
            return fragment
        }
    }

}