package com.mavendra.golekkostv3.app

import com.mavendra.golekkostv3.model.*
import com.mavendra.golekkostv3.model.rajaongkir.ResponOngkir
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name:String,
        @Field("phone") phone:String,
        @Field("email") email:String,
        @Field("password") password:String,
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<ResponModel>

    @POST("checkout")
    fun checkout(
        @Body data:CheckOut,
    ): Call<ResponModel>

    @GET("kostkontrakan")
    fun getKostKontrakan(
    ): Call<ResponModel>

    @GET("kostkontrakan/slemanTahun/{lokasi}")
    fun getKostKontrakanFiltSlemanTahun(
    ): Call<ResponModel>

    @GET("kostkontrakan/slemanBulan/{lokasi}")
    fun getKostKontrakanFiltSlemanBulan(
    ): Call<ResponModel>


    @GET("kostkontrakan/bantulTahun/{lokasi}")
    fun getKostKontrakanFiltBantulTahun(
    ): Call<ResponModel>

    @GET("kostkontrakan/bantulBulan/{lokasi}")
    fun getKostKontrakanFiltBantulBulan(
    ): Call<ResponModel>

    @GET("kostkontrakan/gunungKidulTahun/{lokasi}")
    fun getKostKontrakanFiltGunungKidulTahun(
    ): Call<ResponModel>

    @GET("kostkontrakan/gunungKidulBulan/{lokasi}")
    fun getKostKontrakanFiltGunungKidulBulan(
    ): Call<ResponModel>

    @GET("kostkontrakan/kotaYogyakartaTahun/{lokasi}")
    fun getKostKontrakanFiltKotaYoogyakartaTahun(
    ): Call<ResponModel>

    @GET("kostkontrakan/kotaYogyakartaBulan/{lokasi}")
    fun getKostKontrakanFiltKotaYoogyakartaBulan(
    ): Call<ResponModel>

    @GET("jasaangkut")
    fun getJasaAngkut(
    ): Call<ResponModel>

    @GET("barang")
    fun getBarang(
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("barang/uploadbarang")
    fun uploadbarang(
        @Field("usergeneral_id") usergeneral_id: Int,
        @Field("name_pemilik") name_pemilik:String,
        @Field("notelfon") notelfon:String,
        @Field("name") name:String,
        @Field("harga") harga:String,
        @Field("lokasi") lokasi:String,
        @Field("deskripsi") deskripsi:String,
    ): Call<ResponModel>

    @Multipart
    @POST("barang/uploadbaranggambar/{id}")
    fun uploadBarangFoto(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Call<ResponModel>

    @GET("barang/history/{id}")
    fun getCreateBarang(
        @Path("id") id: Int
    ): Call<ResponModel>

    @GET("transaksipenjualan")
    fun getRiwayatPenjualan(
    ): Call<ResponModel>

    @POST("barang/terjual/{id}")
    fun terjualBarang(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("barang/proses/{id}")
    fun prosesBarang(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("barang/dikirim/{id}")
    fun dikirimBarang(
        @Path("id") id: Int
    ): Call<ResponModel>

    @GET("province")
    fun getProvinsi(
        @Header("key") key:String
    ): Call<ResponModel>

    @GET("city")
    fun getKota(
        @Header("key") key: String,
        @Query("province") id: String
    ): Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id: Int
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
        @Header("key") key: String,
        @Field("origin") origin:String,
        @Field("destination") destination:String,
        @Field("weight") weight:Int,
        @Field("courier") courier:String
    ): Call<ResponOngkir>

    @GET("checkout/user/{id}")
    fun getRiwayat(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("checkout/batal/{id}")
    fun batalCheckout(
        @Path("id") id: Int
    ): Call<ResponModel>

    @Multipart
    @POST("checkout/upload/{id}")
    fun uploadBuktiTransfer(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part
    ): Call<ResponModel>

    @POST("checkout/selesai/{id}")
    fun selesaiCheckout(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("pesanjasa")
    fun pesanjasa(
        @Body data:CheckoutPesanJasa,
    ): Call<ResponModel>

    @GET("pesanjasa/user/{id}")
    fun getPesanjasa(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("pesanjasa/selesai/{id}")
    fun selesaiPesanJasa(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("pesanjasa/batal/{id}")
    fun batalPesanJasa(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("pesankostkontrakan")
    fun pesankostkontrakan(
        @Body data:CheckoutPesanKostKontrakan
    ): Call<ResponModel>

    @GET("pesankostkontrakan/user/{id}")
    fun getPesanKostkontrakan(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("pesankostkontrakan/selesai/{id}")
    fun selesaiPesankostkontrakan(
        @Path("id") id: Int
    ): Call<ResponModel>

    @POST("pesankostkontrakan/batal/{id}")
    fun batalPesankostkontrakan(
        @Path("id") id: Int
    ): Call<ResponModel>
}