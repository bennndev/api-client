package com.example.api_client.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ClienteModel(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("nombre")
    var nombre: String,
    @SerializedName("apellido")
    var apellido: String,
    @SerializedName("telefono")
    var telefono: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("documento")
    var documento: String
)
