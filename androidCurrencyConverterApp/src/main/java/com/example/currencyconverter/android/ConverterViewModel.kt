package com.example.currencyconverter.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    private val _input = MutableLiveData("0")
    val input: LiveData<String> = _input

    private val _output = MutableLiveData("0")
    val output: LiveData<String> = _output

    private var fromUnit = "Kilometers"
    private var toUnit = "Miles"

    private val conversionRates = mapOf(
        "Kilometers-Miles" to 0.621371,
        "Miles-Kilometers" to 1.60934,
        "Kilometers-Meters" to 1000.0,
        "Meters-Kilometers" to 0.001,
        "Kilograms-Pounds" to 2.20462,
        "Pounds-Kilograms" to 0.453592,
        "USD-EUR" to 0.85,
        "EUR-USD" to 1.18,
        "USD-GBP" to 0.75,
        "GBP-USD" to 1.33
    )

    fun onKeyPress(key: String) {
        when (key) {
            "C" -> _input.value = "0"
            "." -> if (!_input.value!!.contains(".")) _input.value += "."
            else -> {
                if (_input.value == "0") _input.value = key
                else _input.value += key
            }
        }
        convert()
    }

    fun swapUnits() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        convert()
    }

    fun convert() {
        val value = _input.value?.toDoubleOrNull() ?: 0.0
        val rate = conversionRates["$fromUnit-$toUnit"] ?: 1.0
        _output.value = String.format("%.2f", value * rate)
    }

    fun getCategories(): List<String> {
        return listOf("Kilometers", "Miles", "Meters", "Kilograms", "Pounds", "USD", "EUR", "GBP")
    }
}