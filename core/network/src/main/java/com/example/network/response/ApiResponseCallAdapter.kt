package com.example.network.response

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

internal class ApiResponseCallAdapter(private val responseType: Type) :
    CallAdapter<ApiResponse<*>, Call<ApiResponse<*>>> {

    override fun adapt(call: Call<ApiResponse<*>>): Call<ApiResponse<*>> {
        return ApiResponseCall(call)
    }

    override fun responseType(): Type = responseType
}
