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

        val spinnerFrom = view.findViewById<Spinner>(com.example.currencyconverter.android.R.id.spinner_from)
        val spinnerTo = view.findViewById<Spinner>(com.example.currencyconverter.android.R.id.spinner_to)
        val tvInput = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_input)
        val tvOutput = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_output)
        val btnCopyInput = view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_copy_input)
        val btnCopyOutput = view.findViewById<Button>(com.example.currencyconverter.android.R.id.btn_copy_output)
        val tvSwap = view.findViewById<TextView>(com.example.currencyconverter.android.R.id.tv_swap)

        // Populate spinners with categories and units
        val categories = viewModel.getCategories()
        spinnerFrom.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        spinnerTo.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)

        spinnerFrom.setSelection(categories.indexOf(viewModel.fromUnit))
        spinnerTo.setSelection(categories.indexOf(viewModel.toUnit))

        spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.apply {
                    fromUnit = categories[position]
                    _fromUnit.value = fromUnit
                    convert()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.apply {
                    toUnit = categories[position]
                    _toUnit.value = toUnit
                    convert()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        viewModel.fromUnitLiveData.observe(viewLifecycleOwner) { fromUnit ->
            val position = categories.indexOf(fromUnit)
            if (position >= 0) {
                spinnerFrom.setSelection(position)
            } else {
                Toast.makeText(requireContext(), "Invalid From Unit", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.toUnitLiveData.observe(viewLifecycleOwner) { toUnit ->
            val position = categories.indexOf(toUnit)
            if (position >= 0) {
                spinnerTo.setSelection(position)
            } else {
                Toast.makeText(requireContext(), "Invalid To Unit", Toast.LENGTH_SHORT).show()
            }
        }
        // Observe data changes
        viewModel.input.observe(viewLifecycleOwner) { tvInput.text = it }
        viewModel.output.observe(viewLifecycleOwner) { tvOutput.text = it }

        // Observe unit changes
        viewModel.fromUnitLiveData.observe(viewLifecycleOwner) { fromUnit ->
            val position = categories.indexOf(fromUnit)
            if (position >= 0) spinnerFrom.setSelection(position)
        }

        viewModel.toUnitLiveData.observe(viewLifecycleOwner) { toUnit ->
            val position = categories.indexOf(toUnit)
            if (position >= 0) spinnerTo.setSelection(position)
        }

        // Handle swap button
        tvSwap.setOnClickListener { viewModel.swapUnits() }

        // Handle copy buttons
        btnCopyInput.setOnClickListener { copyToClipboard(tvInput.text.toString()) }
        btnCopyOutput.setOnClickListener { copyToClipboard(tvOutput.text.toString()) }

        return view
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

}