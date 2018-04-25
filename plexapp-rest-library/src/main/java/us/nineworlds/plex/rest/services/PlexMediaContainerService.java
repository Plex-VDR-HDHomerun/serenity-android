package us.nineworlds.plex.rest.services;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import us.nineworlds.plex.rest.model.impl.MediaContainer;

public interface PlexMediaContainerService {

    @GET("/")
    Call<MediaContainer> retrieveRoot();

    @GET("library")
    Call<MediaContainer> retrieveLibrary();

    @GET("tv.plex.providers.epg.onconnect:23")
    Call<MediaContainer> retrieveLibraryTv();

    @GET("library/sections")
    Call<MediaContainer> retrieveSections();

    @GET("tv.plex.providers.epg.onconnect:23/sections")
    Call<MediaContainer> retrieveSectionsTv();

    @GET("library/sections/{key}")
    Call<MediaContainer> retrieveSections(@Path("key") String key);

    @GET("tv.plex.providers.epg.onconnect:23/sections/{key}")
    Call<MediaContainer> retrieveSectionsTv(@Path("key") String key);

    @GET("library/sections/{key}/{category}")
    Call<MediaContainer> retrieveSections(@Path("key") String key,
                                          @Path(value = "category", encoded = true) String category);

    @GET("tv.plex.providers.epg.onconnect:23/sections/{key}/{category}")
    Call<MediaContainer> retrieveSectionsTv(@Path("key") String key,
                                          @Path(value = "category", encoded = true) String category);

    @GET("library/sections/{key}/{category}/{secondaryCategory}")
    Call<MediaContainer> retrieveSections(@Path("key") String key,
                                          @Path(value = "category", encoded = true)  String category,
                                          @Path(value = "secondaryCategory", encoded = true) String secondaryCategory);

     @GET("tv.plex.providers.epg.onconnect:23/sections/{key}/{category}/{secondaryCategory}")
    Call<MediaContainer> retrieveSectionsTv(@Path("key") String key,
                                          @Path(value = "category", encoded = true)  String category,
                                          @Path(value = "secondaryCategory", encoded = true) String secondaryCategory);

    @GET("{urlPath}")
    Call<MediaContainer> retrieveItemByUrlPath(@Path(value = "urlPath", encoded = true) String key);


    @GET("library/sections/{key}/search?type=1")
    Call<MediaContainer> movieSearch(@Path("key") String key,
                                    @Query("query") String query);

    @GET("library/sections/{key}/search?type=2")
    Call<MediaContainer> tvShowsSearch(@Path("key") String key,
                              @Query("query") String query);

    @GET("library/sections/{key}/search?type=4")
    Call<MediaContainer> episodeSearch(@Path("key") String key,
                                       @Query("query") String query);

    @GET("tv.plex.providers.epg.onconnect:23/sections/{key}/search?type=1")
    Call<MediaContainer> movieSearchTv(@Path("key") String key,
                                    @Query("query") String query);

    @GET("tv.plex.providers.epg.onconnect:23/sections/{key}/search?type=2")
    Call<MediaContainer> tvShowsSearchTv(@Path("key") String key,
                              @Query("query") String query);

    @GET("tv.plex.providers.epg.onconnect:23/sections/{key}/search?type=4")
    Call<MediaContainer> episodeSearchTv(@Path("key") String key,
                                       @Query("query") String query);
}
