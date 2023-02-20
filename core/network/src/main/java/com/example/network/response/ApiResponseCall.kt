package com.example.network.response

import android.util.Log
import com.example.network.response.ApiResponse.Failure.HttpFailure
import com.example.network.response.ApiResponse.Failure.NetworkFailure
import com.example.network.response.ApiResponse.Failure.UnknownFailure
import com.example.network.response.ApiResponse.Success
import com.example.utils.tag.tagObj
import java.io.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class ApiResponseCall(private val call: Call<ApiResponse<*>>) :
    Call<ApiResponse<*>> by call {

    override fun enqueue(callback: Callback<ApiResponse<*>>) =
        call.enqueue(object : Callback<ApiResponse<*>> {
            override fun onResponse(
                call: Call<ApiResponse<*>>,
                response: Response<ApiResponse<*>>
            ) {
                if (response.isSuccessful) {
                    callback.onResponse(
                        call,
                        Response.success(Success(data = checkNotNull(response.body())))
                    )
                } else {
                    Log.w(
                        API_RESPONSE_CALL_TAG,
                        "HttpFailure ${response.code()}, ${call.request().url}"
                    )
                    callback.onResponse(
                        call,
                        Response.success(
                            HttpFailure(
                                code = response.code(),
                                message = response.message()
                            )
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ApiResponse<*>>, error: Throwable) {
                when (error) {
                    is IOException -> {
                        Log.e(
                            API_RESPONSE_CALL_TAG,
                            "NetworkFailure ${call.request().url}, ${error.message}"
                        )
                        callback.onResponse(call, Response.success(NetworkFailure(error)))
                    }
                    else -> {
                        Log.e(
                            API_RESPONSE_CALL_TAG,
                            "UnknownFailure ${call.request().url}, ${error.message}"
                        )
                        callback.onResponse(call, Response.success(UnknownFailure(error)))
                    }
                }
            }
        })

    private companion object {
        val API_RESPONSE_CALL_TAG: String = ApiResponseCall.tagObj()
    }
}
