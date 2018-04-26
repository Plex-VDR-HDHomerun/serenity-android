package us.nineworlds.plex.rest.services;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlexTextService {

    @GET(":/scrobble?identifier=tv.plex.providers.epg.onconnect")
    Call<String> watched(@Query("key") String key);

    @GET(":/unscrobble?identifier=tv.plex.providers.epg.onconnect")
    Call<String> unwatched(@Query("key") String key);

    @GET(":/progress?identifier=tv.plex.providers.epg.onconnect")
    Call<String> progress(@Query("key") String key,
                          @Query("time") String offset);
}
