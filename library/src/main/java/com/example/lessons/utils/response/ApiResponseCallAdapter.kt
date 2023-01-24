package com.example.lessons.utils.response

import com.example.lessons.contacts.domain.api.response.ApiResponse
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