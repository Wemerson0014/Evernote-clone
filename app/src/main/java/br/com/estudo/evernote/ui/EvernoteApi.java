package br.com.estudo.evernote.ui;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EvernoteApi {

    @GET("/")
    Call<List<Note>> listNotes();

    @POST("/create")
    Call<Note> createNote(@Body Note note);
}
