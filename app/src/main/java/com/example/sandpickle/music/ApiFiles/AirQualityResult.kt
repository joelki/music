package com.example.sandpickle.music.ApiFiles

class AirQualityResult(private var measurements: ArrayList<Measurement>) {

    fun getMeasurements(): ArrayList<Measurement> {
        return this.measurements
    }

    fun setMeasurements(value: ArrayList<Measurement>) {
        this.measurements = value
    }

}