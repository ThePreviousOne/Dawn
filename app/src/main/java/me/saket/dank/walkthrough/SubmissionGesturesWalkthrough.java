package me.saket.dank.walkthrough;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2prateek.rx.preferences2.Preference;
import com.google.auto.value.AutoValue;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import me.saket.dank.R;
import me.saket.dank.ui.subreddit.SubmissionSwipeActionsProvider;
import me.saket.dank.ui.subreddit.SubredditSubmissionsAdapter;
import me.saket.dank.ui.subreddit.uimodels.SubredditScreenUiModel;
import me.saket.dank.utils.Optional;
import me.saket.dank.widgets.swipe.SwipeAction;
import me.saket.dank.widgets.swipe.SwipeActionIconView;
import me.saket.dank.widgets.swipe.SwipeActions;
import me.saket.dank.widgets.swipe.SwipeTriggerRippleDrawable;
import me.saket.dank.widgets.swipe.SwipeableLayout;
import me.saket.dank.widgets.swipe.SwipeableLayout.SwipeActionIconProvider;
import me.saket.dank.widgets.swipe.ViewHolderWithSwipeActions;

public class SubmissionGesturesWalkthrough {

  private final Preference<Boolean> hasUserLearnedPref;

  @Inject
  public SubmissionGesturesWalkthrough(@Named("user_learned_submission_gestures") Preference<Boolean> hasUserLearnedPref) {
    this.hasUserLearnedPref = hasUserLearnedPref;
  }

  public Observable<Optional<UiModel>> walkthroughRows() {
    return hasUserLearnedPref.asObservable()
        .flatMap(hasLearned -> hasLearned
            ? Observable.just(Optional.<UiModel>empty())
            : Observable.just(Optional.of(UiModel.create())));
  }

  @AutoValue
  public abstract static class UiModel implements SubredditScreenUiModel.SubmissionRowUiModel {

    @Override
    public Type type() {
      return Type.GESTURES_WALKTHROUGH;
    }

    @Override
    public long adapterId() {
      return SubredditSubmissionsAdapter.ADAPTER_ID_GESTURES_WALKTHROUGH;
    }

    public static UiModel create() {
      return new AutoValue_SubmissionGesturesWalkthrough_UiModel();
    }
  }

  public static class ViewHolder extends RecyclerView.ViewHolder implements ViewHolderWithSwipeActions {
    private final TextView titleView;
    private final TextView messageView;

    protected ViewHolder(View itemView) {
      super(itemView);
      titleView = itemView.findViewById(R.id.submissiongestureswalkthrough_item_title);
      messageView = itemView.findViewById(R.id.submissiongestureswalkthrough_item_message);

      titleView.setText(R.string.subreddit_gestureswalkthrough_title);
      messageView.setText(R.string.subreddit_gestureswalkthrough_message);
    }

    public static ViewHolder create(LayoutInflater inflater, ViewGroup parent) {
      View itemView = inflater.inflate(R.layout.list_item_submission_gestures_walkthrough, parent, false);
      return new ViewHolder(itemView);
    }

    @Override
    public SwipeableLayout getSwipeableLayout() {
      return ((SwipeableLayout) itemView);
    }
  }

  public static class Adapter implements SubredditScreenUiModel.SubmissionRowUiChildAdapter<UiModel, ViewHolder> {

    private final WalkthroughSwipeActionsProvider swipeActionsProvider;

    @Inject
    public Adapter(WalkthroughSwipeActionsProvider swipeActionsProvider) {
      this.swipeActionsProvider = swipeActionsProvider;
    }

    @Override
    public ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent) {
      ViewHolder holder = ViewHolder.create(inflater, parent);
      SwipeableLayout swipeableLayout = holder.getSwipeableLayout();
      swipeableLayout.setSwipeActions(swipeActionsProvider.actions());
      swipeableLayout.setSwipeActionIconProvider(swipeActionsProvider);
      swipeableLayout.setOnPerformSwipeActionListener(action ->
          swipeActionsProvider.perform(action, holder, swipeableLayout)
      );
      return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, UiModel uiModel) {
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, UiModel uiModel, List<Object> payloads) {

    }
  }

  public static class WalkthroughSwipeActionsProvider implements SwipeActionIconProvider {
    private final SubmissionSwipeActionsProvider submissionSwipeActionsProvider;
    private final Preference<Boolean> hasUserLearnedPref;
    private int discoveryCount = 0;

    @Inject
    public WalkthroughSwipeActionsProvider(
        SubmissionSwipeActionsProvider submissionSwipeActionsProvider,
        @Named("user_learned_submission_gestures") Preference<Boolean> hasUserLearnedPref)
    {
      this.submissionSwipeActionsProvider = submissionSwipeActionsProvider;
      this.hasUserLearnedPref = hasUserLearnedPref;
    }

    public SwipeActions actions() {
      return submissionSwipeActionsProvider.actionsWithSave();
    }

    @Override
    public void showSwipeActionIcon(SwipeActionIconView imageView, @Nullable SwipeAction oldAction, SwipeAction newAction) {
      submissionSwipeActionsProvider.showSwipeActionIcon(imageView, oldAction, newAction);
    }

    public void perform(SwipeAction action, ViewHolder holder, SwipeableLayout swipeableLayout) {
      swipeableLayout.playRippleAnimation(action, SwipeTriggerRippleDrawable.RippleType.REGISTER);

      ++discoveryCount;

      Context context = swipeableLayout.getContext();
      holder.titleView.setText(String.format("'%s'", context.getString(action.labelRes())));
      holder.titleView.setTextColor(ContextCompat.getColor(context, action.backgroundColorRes()));

      switch (discoveryCount) {
        case 1:
          holder.messageView.setText(R.string.subreddit_gestureswalkthrough_message_after_first_swipe_action);
          break;

        default:
        case 2:
          holder.messageView.setText(R.string.subreddit_gestureswalkthrough_message_after_second_swipe_action);
          holder.itemView.setOnClickListener(o -> hasUserLearnedPref.set(true));
          break;
      }
    }
  }
}