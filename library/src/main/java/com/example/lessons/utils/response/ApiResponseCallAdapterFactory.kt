package com.example.lessons.utils.response

import com.example.lessons.contacts.domain.api.response.ApiResponse
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit

class ApiResponseCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }
        val innerType = getParameterUpperBound(0, returnType)
        if (getRawType(innerType) != ApiResponse::class.java) {
            return null
        }
        check(innerType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }
        val responseType = getParameterUpperBound(0, innerType)
        return ApiResponseCallAdapter(responseType)
    }
}