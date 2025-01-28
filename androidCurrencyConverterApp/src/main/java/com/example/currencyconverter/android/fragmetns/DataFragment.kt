package com.example.currencyconverter.android.fragmetns
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.android.ConverterViewModel
import com.example.currencyconverter.R

class DataFragment : Fragment() {

    private lateinit var viewModel: ConverterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(com.example.currencyconverter.android.R.layout.fragment_data, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(ConverterViewModel::class.java)

        val tvLength = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_length)
        val tvWeight = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_weight)
        val tvCurrency = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_currency)
        val spinnerFrom = view.findViewById<Spinner>(com.example.currencyconverter.android.R.id.spinner_from)
        val spinnerTo = view.findViewById<Spinner>(com.example.currencyconverter.android.R.id.spinner_to)
        val tvInput = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_input)
        val tvOutput = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_output)
        val btnCopyInput = view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_copy_input)
        val btnCopyOutput = view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_copy_output)
        val tvSwap = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_swap)

        // Categories and units
        val categories = viewModel.getCategoriesByType()

        // Update spinners based on selected category
        fun updateUnitSpinners(category: String) {
            val units = categories[category] ?: emptyList()
            spinnerFrom.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)
            spinnerTo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)

            spinnerFrom.setSelection(units.indexOf(viewModel.fromUnit))
            spinnerTo.setSelection(units.indexOf(viewModel.toUnit))
        }

        // Update text styles for the selected category
        fun highlightSelectedCategory(selectedView: TextView) {
            listOf(tvLength, tvWeight, tvCurrency).forEach { textView ->
                textView.setBackgroundResource(android.R.color.transparent)
                textView.setTextColor(resources.getColor(android.R.color.black))
            }
            selectedView.setTextColor(resources.getColor(android.R.color.holo_green_dark))
        }

        // Handle category selection
        tvLength.setOnClickListener {
            viewModel.selectedCategory = "Length"
            highlightSelectedCategory(tvLength)
            updateUnitSpinners("Length")
        }

        tvWeight.setOnClickListener {
            viewModel.selectedCategory = "Weight"
            highlightSelectedCategory(tvWeight)
            updateUnitSpinners("Weight")
        }

        tvCurrency.setOnClickListener {
            viewModel.selectedCategory = "Currency"
            highlightSelectedCategory(tvCurrency)
            updateUnitSpinners("Currency")
        }

        // Set initial category
        highlightSelectedCategory(tvLength)
        updateUnitSpinners("Length")

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val units = categories[viewModel.selectedCategory] ?: emptyList()
                viewModel.fromUnit = units[position]
                viewModel.convert()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val units = categories[viewModel.selectedCategory] ?: emptyList()
                viewModel.toUnit = units[position]
                viewModel.convert()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Handle swap button
        tvSwap.setOnClickListener {
            viewModel.swapUnits()
            val units = categories[viewModel.selectedCategory] ?: emptyList()
            spinnerFrom.setSelection(units.indexOf(viewModel.fromUnit))
            spinnerTo.setSelection(units.indexOf(viewModel.toUnit))

        }

        // Handle copy buttons
        btnCopyInput.setOnClickListener { copyToClipboard(tvInput.text.toString()) }
        btnCopyOutput.setOnClickListener { copyToClipboard(tvOutput.text.toString()) }

        // Observe input/output
        viewModel.input.observe(viewLifecycleOwner) { tvInput.text = it }
        viewModel.output.observe(viewLifecycleOwner) { tvOutput.text = it }

        return view
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}