package com.example.currencyconverter.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    private val _input = MutableLiveData("0")
    val input: LiveData<String> = _input

    private val _output = MutableLiveData("0")
    val output: LiveData<String> = _output

    var fromUnit = "Kilometers"
    var toUnit = "Miles"
    var selectedCategory = "Length"

    private val categories = mapOf(
        "Length" to listOf("Kilometers", "Miles", "Meters"),
        "Weight" to listOf("Kilograms", "Pounds", "Ounces"),
        "Currency" to listOf("USD", "EUR", "GBP")
    )
    fun getCategoriesByType(): Map<String, List<String>> {
        return categories
    }

    val _fromUnit = MutableLiveData(fromUnit)
    val fromUnitLiveData: LiveData<String> = _fromUnit

    val _toUnit = MutableLiveData(toUnit)
    val toUnitLiveData: LiveData<String> = _toUnit

    private val conversionRates = mapOf(
        "Kilometers-Miles" to 0.621371,
        "Miles-Kilometers" to 1.60934,
        "Kilometers-Meters" to 1000.0,
        "Meters-Kilometers" to 0.001,
        "Meters-Miles" to 0.000621371,
        "Miles-Meters" to 1609.344,
        "Kilograms-Pounds" to 2.20462,
        "Pounds-Kilograms" to 0.453592,
        "Kilograms-Ounces" to 35.274,
        "Ounces-Kilograms" to 0.0283495,
        "Pounds-Ounces" to 16.0,
        "Ounces-Pounds" to 0.0625,
        "USD-EUR" to 0.9531,
        "EUR-USD" to 1.0493,
        "USD-GBP" to 0.8049,
        "GBP-USD" to 1.2424,
        "GBP-EUR" to 1.1841,
        "EUR-GBP" to 0.8445,
        "Kilometers-Kilometers" to 1.0,
        "Miles-Miles" to 1.0,
        "Meters-Meters" to 1.0,
        "Kilograms-Kilograms" to 1.0,
        "Pounds-Pounds" to 1.0,
        "Ounces-Ounces" to 1.0,
        "USD-USD" to 1.0,
        "EUR-EUR" to 1.0,
        "GBP-GBP" to 1.0
    )

    fun onKeyPress(key: String) {
        when (key) {
            "C" -> _input.value = "0"
            "." -> if (!_input.value.orEmpty().contains(".")) _input.value += "."
            else -> {
                if (_input.value == "0") {
                    _input.value = key
                } else if (_input.value.orEmpty().length < 15) { // Limit input length
                    _input.value += key
                }
            }
        }
        convert()
    }

    fun swapUnits() {
        val temp = fromUnit
        fromUnit = toUnit
        toUnit = temp
        _fromUnit.value = fromUnit
        _toUnit.value = toUnit
        convert()
    }

    fun convert() {
        val value = _input.value?.toDoubleOrNull() ?: 0.0
        val rateKey = "$fromUnit-$toUnit"
        val rate = conversionRates[rateKey]

        _output.value = if (rate != null) {
            String.format("%.2f", value * rate)
        } else {
            "N/A"
        }
    }

    fun getCategories(): List<String> {
        return listOf("Kilometers", "Miles", "Meters", "Kilograms", "Pounds", "Ounces", "USD", "EUR", "GBP")
    }

}