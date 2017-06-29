package me.saket.dank.di;

import com.danikula.videocache.HttpProxyCacheServer;
import com.squareup.moshi.Moshi;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Singleton;

import dagger.Component;
import me.saket.dank.data.DankRedditClient;
import me.saket.dank.data.ErrorManager;
import me.saket.dank.data.InboxManager;
import me.saket.dank.data.SharedPrefsManager;
import me.saket.dank.data.SubmissionManager;
import me.saket.dank.data.SubredditSubscriptionManager;
import me.saket.dank.data.UserPrefsManager;
import me.saket.dank.data.VotingManager;
import me.saket.dank.notifs.MessagesNotificationManager;
import me.saket.dank.ui.submission.CommentsManager;
import me.saket.dank.ui.user.UserSession;
import me.saket.dank.utils.ImgurManager;
import me.saket.dank.utils.JacksonHelper;
import okhttp3.OkHttpClient;

@Component(modules = DankAppModule.class)
@Singleton
public interface DankAppComponent {
  DankRedditClient dankRedditClient();

  SharedPrefsManager sharedPrefs();

  OkHttpClient okHttpClient();

  HttpProxyCacheServer httpProxyCacheServer();

  DankApi api();

  UserPrefsManager userPrefs();

  ImgurManager imgur();

  SubredditSubscriptionManager subredditSubscriptionManager();

  JacksonHelper jacksonHelper();

  ErrorManager errorManager();

  InboxManager inboxManager();

  MessagesNotificationManager messagesNotificationManager();

  Moshi moshi();

  BriteDatabase briteDatabase();

  SubmissionManager submissionManager();

  VotingManager votingManager();

  UserSession userSession();

  CommentsManager commentsManager();
}
