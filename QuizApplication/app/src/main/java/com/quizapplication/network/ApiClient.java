// network/ApiClient.java
package com.quizapplication.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://lama3-quiz-api.onrender.com/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)   // Wait up to 1 minute to connect
                    .readTimeout(180, TimeUnit.SECONDS)     // Wait up to 2 minutes for the response
                    .writeTimeout(180, TimeUnit.SECONDS)    // Wait up to 2 minutes for request body
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
