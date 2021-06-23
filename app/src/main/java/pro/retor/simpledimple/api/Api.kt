package pro.retor.simpledimple.api

import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pro.retor.simpledimple.items.PersonItem
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface Api {
    @GET
    fun loadPersons(@Url url: String = "https://tools.mircod.one/simpledimple/"): Single<List<PersonEntity>>

    companion object {
        val API: Api by lazy {

            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()

            val REST_ADAPTER = Retrofit.Builder()
                .baseUrl("http://some.some")
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .setLenient()
                            .create()
                    )
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client).build()
            REST_ADAPTER.create(Api::class.java)
        }

        fun mapPerson(person: PersonEntity): PersonItem {
            return PersonItem(person.name ?: "", person.city ?: "", person.detail ?: "", person.image_url ?: "")
        }
    }
}