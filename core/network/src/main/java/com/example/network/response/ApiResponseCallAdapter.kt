package com.example.network.response

import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter

internal class ApiResponseCallAdapter(private val responseType: Type) :
    CallAdapter<ApiResponse<*>, Call<ApiResponse<*>>> {

    override fun adapt(call: Call<ApiResponse<*>>): Call<ApiResponse<*>> {
        return ApiResponseCall(call)
    }

    override fun responseType(): Type = responseType

}