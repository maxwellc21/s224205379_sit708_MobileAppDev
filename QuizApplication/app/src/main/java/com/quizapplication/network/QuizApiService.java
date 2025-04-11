// network/QuizApiService.java
package com.quizapplication.network;

import com.quizapplication.models.Question;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface QuizApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/generate-quiz")
    Call<List<Question>> getQuiz(@Body Map<String, String> body);
}
