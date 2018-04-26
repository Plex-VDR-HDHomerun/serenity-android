package us.nineworlds.plex.rest.services;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import us.nineworlds.plex.rest.model.impl.MediaContainer;

public interface PlexMediaContainerService {

    @GET("/tv.plex.providers.epg.onconnect")
    Call<MediaContainer> retrieveRoot();

    @GET("tv.plex.providers.epg.onconnect{key}")
    Call<MediaContainer> retrieveLibraryTv(@Path("key") String key);

    @GET("/{key}/sections")
    Call<MediaContainer> retrieveSectionsTv();

    @GET("/sections/{key}")
    Call<MediaContainer> retrieveSectionsTv(@Path("key") String key);

    @GET("/{key}/sections/{key}/{category}")
    Call<MediaContainer> retrieveSectionsTv(@Path("key") String key,
                                            @Path(value = "category", encoded = true) String category);

     @GET("/{key}/sections/{key}/{category}/{secondaryCategory}")
    Call<MediaContainer> retrieveSectionsTv(@Path("key") String key,
                                            @Path(value = "category", encoded = true) String category,
                                            @Path(value = "secondaryCategory", encoded = true) String secondaryCategory);

    @GET("{urlPath}")
    Call<MediaContainer> retrieveItemByUrlPath(@Path(value = "urlPath", encoded = true) String key);


    @GET("/{key}/sections/{key}/search?type=1")
    Call<MediaContainer> movieSearchTv(@Path("key") String key,
                                    @Query("query") String query);

    @GET("/{key}/sections/{key}/search?type=2")
    Call<MediaContainer> tvShowsSearchTv(@Path("key") String key,
                                         @Query("query") String query);

    @GET("/{key}/sections/{key}/search?type=4")
    Call<MediaContainer> episodeSearchTv(@Path("key") String key,
                                       @Query("query") String query);
}
