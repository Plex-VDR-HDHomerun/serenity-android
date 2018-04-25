/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.plex.rest;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import us.nineworlds.plex.rest.config.IConfiguration;
import us.nineworlds.plex.rest.model.impl.MediaContainer;
import us.nineworlds.plex.rest.services.PlexMediaContainerService;
import us.nineworlds.plex.rest.services.PlexTextService;
import us.nineworlds.serenity.common.media.model.IMediaContainer;

/**
 * This class acts as a factory for retrieving items from Plex.
 *
 * This is a singleton so only one of these will ever exist currently.
 *
 * @author dcarver
 */
public class PlexappClient {

  private static PlexappClient instance = null;

  private ResourcePaths resourcePath = null;

  private PlexMediaContainerService mediaContainerclient;
  private PlexTextService textClient;

  private PlexappClient(IConfiguration config) {
    resourcePath = new ResourcePaths(config);

    if (resourcePath.getHost() != null && resourcePath.getHost().length() > 0) {
      init();
    }
  }

  public static PlexappClient getInstance(IConfiguration config) {
    if (instance == null) {
      instance = new PlexappClient(config);
    }
    return instance;
  }

  private void init() {
    Serializer serializer = new Persister();

    HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
    OkHttpClient.Builder okClient = new OkHttpClient.Builder();
    logger.setLevel(HttpLoggingInterceptor.Level.HEADERS);
    okClient.addInterceptor(logger);
    okClient.cache(null);

    Retrofit.Builder builder = new Retrofit.Builder();
    Retrofit mediaContainerAdapter = builder.baseUrl(resourcePath.getRoot())
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
        .client(okClient.build())
        .build();

    mediaContainerclient = mediaContainerAdapter.create(PlexMediaContainerService.class);

    Retrofit stringAdapter = new Retrofit.Builder().baseUrl(resourcePath.getRoot())
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okClient.build())
        .build();

    textClient = stringAdapter.create(PlexTextService.class);
  }

  private void reinitIfNecessary() {
    if (mediaContainerclient != null && textClient != null) {
      return;
    }
    init();
  }

  /**
   * Retrieve the root metadata from the Plex Media Server.
   *
   * @throws Exception
   */
  public IMediaContainer retrieveRootData() throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveRoot();
    MediaContainer mediaContainer = call.execute().body();
    return mediaContainer;
  }

  /**
   * This retrieves the available libraries.  This can include such
   * things as Movies, and TV shows.
   *
   * @return MediaContainer the media container for the library
   * @throws Exception
   */
  public MediaContainer retrieveLibrary() throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveLibrary();
    MediaContainer mediaContainer = call.execute().body();
    return mediaContainer;
  }

  /**
   * This retrieves the available libraries.  This can include such
   * things as Movies, and TV shows.
   *
   * @return MediaContainer the media container for the library
   * @throws Exception
   */
  public MediaContainer retrieveSectionsTv() throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveSectionsTv();
    return call.execute().body();
  }

  /**
   * This retrieves the available libraries.  This can include such
   * things as Movies, and TV shows.
   *
   * @param key the section key
   * @return MediaContainer the media container for the library
   * @throws Exception
   */
  public MediaContainer retrieveSectionsTv(String key) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveSectionsTv(key);
    return call.execute().body();
  }

  /**
   * For Movies this will return a MediaContainer with Videos.  For
   * TV Shows this will return a MediaContainer with Directories.
   *
   * @return MediaContainer
   * @throws Exception
   */
  public MediaContainer retrieveSectionsTv(String key, String category) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveSectionsTv(key, category);
    return call.execute().body();
  }

  public MediaContainer retrieveSectionsTv(String key, String category, String secondaryCategory) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveSectionsTv(key, category, secondaryCategory);
    return call.execute().body();
  }

  public MediaContainer retrieveSeasons(String key) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveItemByUrlPath(key.replaceFirst("/", ""));
    return call.execute().body();
  }

  public MediaContainer retrieveMusicMetaData(String key) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveItemByUrlPath(key);
    return call.execute().body();
  }

  public MediaContainer retrieveEpisodes(String key) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveItemByUrlPath(key);
    return call.execute().body();
  }

  public MediaContainer retrieveMovieMetaData(String key) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.retrieveItemByUrlPath(key);
    return call.execute().body();
  }

  public MediaContainer searchMovies(String key, String query) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.movieSearch(key, query);
    return call.execute().body();
  }

  public MediaContainer searchEpisodes(String key, String query) throws Exception {
    reinitIfNecessary();
    Call<MediaContainer> call = mediaContainerclient.episodeSearch(key, query);
    MediaContainer mediaContainer = call.execute().body();
    return mediaContainer;
  }

  public String baseURL() {
    return resourcePath.getRoot();
  }

  /**
   * Sets a video as watched. viewCount will be 1.
   */
  public boolean setWatched(String key) throws IOException {
    reinitIfNecessary();
    Call<String> call = textClient.watched(key);
    Response<String> response = call.execute();

    return requestSuccessful(response);
  }

  /**
   * Sets a video as unwatched. viewCount will not be present.
   */
  public boolean setUnWatched(String key) throws IOException {
    reinitIfNecessary();
    Call<String> call = textClient.unwatched(key);
    Response<String> response = call.execute();
    return requestSuccessful(response);
  }

  public boolean setProgress(String key, String offset) throws IOException {
    reinitIfNecessary();
    Call<String> call = textClient.progress(key, offset);
    Response<String> response = call.execute();
    return requestSuccessful(response);
  }

  protected boolean requestSuccessful(Response<String> response) {
    int responseCode = response.code();
    if (responseCode == 200) {
      return true;
    }
    return false;
  }

  public String getMediaTagURL(String resourceType, String resourceName, String identifier) {
    return resourcePath.getMediaTagURL(resourceType, resourceName, identifier);
  }

  public String getSeasonsURL(String key) {
    return resourcePath.getSeasonsURL(key);
  }

  public String getImageURL(String url, int width, int height) {
    return resourcePath.getImageURL(url, width, height);
  }
}
