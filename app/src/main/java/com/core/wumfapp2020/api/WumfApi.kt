package com.app.api.api

import com.core.wumfapp2020.api.GetFriendsRequest
import com.core.wumfapp2020.api.GetFriendsResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WumfApi {

    @POST("/checkRegistration")
    fun checkReg(@Body data: CheckRegistrationRequest): Call<CheckRegistrationResponse>

    @POST("/registration")
    fun registration(@Body data: RegistrationRequest): Call<RegistrationResponse>

    @POST("/login")
    fun login(@Body data: LoginRequest): Call<LoginResponse>

    @GET("/getApps")
    fun getApps(): Call<GetAppsResponse>

    @POST("/getFriends")
    fun getFriends(@Body friendsRequest: GetFriendsRequest): Call<GetFriendsResponse>

    @POST("/addApp")
    fun addApp(@Body appRequest: AddAppRequest): Call<AddAppResponse>

    @POST("/removeApp")
    fun removeApp(@Body appRequest: RemoveAppRequest): Call<RemoveAppResponse>

    @POST("/getNotMyApps")
    fun getNotMyApps(@Body data: GetNotMyAppsRequest): Call<GetNotMyAppsResponse>

}