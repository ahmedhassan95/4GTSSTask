package hk.com.a4gtsstask;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ahmed on 29 July 2017.
 */

public interface ApiInterface {

    @GET("todos")
    Call<List<Note>> getAllNotes();

    @POST("todos")
    Call<Note> addNote(@Field("title") String title, @Field("order") int order,@Field("completed") boolean completed);

    @PATCH("todos/{id}")
    Call<Note> updateNote (@Path("id") int id , @Field("title") String title);

    @DELETE("todos/{id}")
    Call<Note> deleteNote (@Path("id") int id);
}
