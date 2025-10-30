package com.example.api_client.network

import com.example.api_client.data.model.ClienteModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ClienteApiService {
    
    /**
     * GET /api/clientes/
     * Listar todos los clientes
     */
    @GET("api/clientes/")
    suspend fun getClientes(): Response<ClienteListResponse>
    
    /**
     * GET /api/clientes/{id}/
     * Obtener detalle de un cliente específico
     */
    @GET("api/clientes/{id}/")
    suspend fun getCliente(@Path("id") id: Int): Response<ClienteResponse>
    
    /**
     * POST /api/clientes/
     * Crear un nuevo cliente
     */
    @Headers("Content-Type: application/json")
    @POST("api/clientes/")
    suspend fun createCliente(@Body cliente: ClienteModel): Response<ClienteResponse>
    
    /**
     * PUT /api/clientes/{id}/
     * Actualizar completamente un cliente
     */
    @Headers("Content-Type: application/json")
    @PUT("api/clientes/{id}/")
    suspend fun updateCliente(@Path("id") id: Int, @Body cliente: ClienteModel): Response<ClienteResponse>
    
    /**
     * PATCH /api/clientes/{id}/
     * Actualizar parcialmente un cliente
     */
    @Headers("Content-Type: application/json")
    @PATCH("api/clientes/{id}/")
    suspend fun partialUpdateCliente(@Path("id") id: Int, @Body cliente: ClienteModel): Response<ClienteResponse>
    
    /**
     * DELETE /api/clientes/{id}/
     * Eliminar un cliente
     */
    @DELETE("api/clientes/{id}/")
    suspend fun deleteCliente(@Path("id") id: Int): Response<ClienteResponse>
    
    /**
     * GET /api/clientes/buscar_por_documento/?tipo=DNI
     * Buscar clientes por tipo de documento
     */
    @GET("api/clientes/buscar_por_documento/")
    suspend fun buscarPorDocumento(@Query("tipo") tipo: String): Response<ClienteListResponse>
}

/**
 * Clase de respuesta para un solo cliente
 */
data class ClienteResponse(
    val success: Boolean,
    val message: String? = null,
    val data: ClienteModel? = null,
    val errors: Map<String, List<String>>? = null
)

/**
 * Clase de respuesta para lista de clientes
 */
data class ClienteListResponse(
    val success: Boolean,
    val message: String? = null,
    val data: List<ClienteModel>? = null,
    val count: Int? = null,
    val errors: Map<String, List<String>>? = null
)