package com.example.mobile.domain

import java.sql.Timestamp
import java.time.Instant

class Dato(var codice: Int?, val valore: Double, val momento: Instant, val tipo: String, val dataSet: String) {}