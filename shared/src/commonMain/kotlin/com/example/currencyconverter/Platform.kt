package com.example.currencyconverter

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform