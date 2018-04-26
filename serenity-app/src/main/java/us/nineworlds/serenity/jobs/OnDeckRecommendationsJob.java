package us.nineworlds.serenity.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.RetryConstraint;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import us.nineworlds.serenity.common.android.injection.ApplicationContext;
import us.nineworlds.serenity.common.android.injection.InjectingJob;
import us.nineworlds.serenity.common.media.model.IMediaContainer;
import us.nineworlds.serenity.common.rest.SerenityClient;
import us.nineworlds.serenity.core.menus.MenuItem;
import us.nineworlds.serenity.core.model.VideoContentInfo;
import us.nineworlds.serenity.core.model.impl.EpisodeMediaContainer;
import us.nineworlds.serenity.core.model.impl.MenuMediaContainer;
import us.nineworlds.serenity.core.model.impl.MovieMediaContainer;
import us.nineworlds.serenity.core.services.RecommendAsyncTask;

public class OnDeckRecommendationsJob extends InjectingJob {

  @Inject @ApplicationContext Context context;

  @Inject SerenityClient client;

  @Inject EventBus eventBus;

  @Inject JobManager jobManager;

  @Override public void onAdded() {

  }

  @Override public void onRun() throws Throwable {
    IMediaContainer mediaContainer = client.retrieveSectionsTv();
    onResponse(mediaContainer);
  }

  protected void onResponse(IMediaContainer mc) throws Throwable {
    List<MenuItem> menuItems = new MenuMediaContainer(mc).createMenuItems();
    if (menuItems.isEmpty()) {
      return;
    }

    for (MenuItem library : menuItems) {
      if ("movie".equals(library.getType())) {
        String section = library.getSection();

        IMediaContainer mediaContainer = client.retrieveSectionsTv(section, "onDeck");
        onMovieResponse(mediaContainer);
      }

      if ("show".equals(library.getType())) {
        String section = library.getSection();
        IMediaContainer mediaContainer = client.retrieveSectionsTv(section, "onDeck");
        onShowResponse(mediaContainer);
      }
    }
  }

  protected void onMovieResponse(IMediaContainer mediaContainer) {
    MovieMediaContainer movieContainer = new MovieMediaContainer(mediaContainer);
    List<VideoContentInfo> movies = movieContainer.createVideos();
    for (VideoContentInfo movie : movies) {
      new RecommendAsyncTask(movie, context).execute();
    }
  }

  protected void onShowResponse(IMediaContainer mediaContainer) {
    EpisodeMediaContainer episodeContainer = new EpisodeMediaContainer(mediaContainer);
    List<VideoContentInfo> episodes = episodeContainer.createVideos();
    for (VideoContentInfo episode : episodes) {
      new RecommendAsyncTask(episode, context).execute();
    }
  }

  @Override protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

  }

  @Override
  protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
    return null;
  }
}
