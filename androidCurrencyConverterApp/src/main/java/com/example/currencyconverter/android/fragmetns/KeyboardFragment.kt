package com.example.currencyconverter.android.fragmetns
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.android.ConverterViewModel
import com.example.currencyconverter.R

class KeyboardFragment : Fragment() {

    private lateinit var viewModel: ConverterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(com.example.currencyconverter.android.R.layout.fragment_keyboard, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ConverterViewModel::class.java)

        val buttons = listOf(
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_1),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_2),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_3),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_4),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_5),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_6),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_7),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_8),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_9),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_0),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_clear),
            view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_dot)
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                viewModel.onKeyPress(button.text.toString())
            }
        }

        return view
    }
}