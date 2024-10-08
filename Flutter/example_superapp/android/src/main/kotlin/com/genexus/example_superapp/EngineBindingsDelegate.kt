package com.genexus.example_superapp

interface EngineBindingsDelegate {
    fun onConfirm(args: String?)
    fun onCancel()
    suspend fun onGetInformation(args: String?): String?
}
