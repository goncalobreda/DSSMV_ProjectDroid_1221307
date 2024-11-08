package com.example.libraryapp.api;

import com.example.libraryapp.models.*;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface LibraryApi {
    @GET("/v1/library")
    Call<List<com.example.libraryapp.models.Library>> getLibraries();

    @GET("/v1/library/{libraryId}/book")
    Call<List<com.example.libraryapp.models.BookWrapper>> getBooksByLibrary(@Path("libraryId") String libraryId);

    @POST("/v1/library")
    Call<com.example.libraryapp.models.Library> createLibrary(@Body com.example.libraryapp.models.Library library);

    @POST("/v1/library/{libraryId}/book/{isbn}")
    Call<Void> createBook(@Path("libraryId") String libraryId, @Path("isbn") String isbn, @Body com.example.libraryapp.models.BookRequest bookRequest);

    @PUT("/v1/library/{id}")
    Call<Void> updateLibrary(@Path("id") String libraryId, @Body com.example.libraryapp.models.Library library);

    @DELETE("/v1/library/{id}")
    Call<Void> deleteLibrary(@Path("id") String libraryId);

    @GET("/v1/user/checked-out")
    Call<List<com.example.libraryapp.models.CheckedOutBook>> getCheckedOutBooks(@Query("userId") String userId);

    @POST("/v1/library/{libraryId}/book/{bookId}/checkin")
    Call<Void> checkInBook(@Path("libraryId") String libraryId, @Path("bookId") String bookId, @Query("userId") String userId);
}
