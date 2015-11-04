package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;

/**
 * This RemoteViewsService serves is similar to an adapter and is used for our list in the widget.
 */
public class ScoresWidgetViewsService extends RemoteViewsService {
    private static final String[] SCORES_COLUMNS = {
            DatabaseContract.ScoresTable.HOME_COL,
            DatabaseContract.ScoresTable.AWAY_COL,
            DatabaseContract.ScoresTable.HOME_GOALS_COL,
            DatabaseContract.ScoresTable.AWAY_GOALS_COL,
            DatabaseContract.ScoresTable.TIME_COL
    };

    // These indices must match the projection in SCORES_COLUMNS
    static final int INDEX_HOME_COL = 0;
    static final int INDEX_AWAY_COL = 1;
    static final int INDEX_HOME_GOALS_COL = 2;
    static final int INDEX_AWAY_GOALS_COL = 3;
    static final int INDEX_TIME_COL = 4;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                //Get date because we will only display scores for today's date
                Date todaysDateInMillis = new Date(System.currentTimeMillis());
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                String todaysDate = mformat.format(todaysDateInMillis);

                Uri scoreWithDateUri = DatabaseContract.ScoresTable.buildScoreWithDate();

                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(scoreWithDateUri,
                        SCORES_COLUMNS,
                        null,
                        new String[]{todaysDate},
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.scores_widget_list_item);

                String homeName = data.getString(INDEX_HOME_COL);
                String awayName = data.getString(INDEX_AWAY_COL);
                int homeGoals = data.getInt(INDEX_HOME_GOALS_COL);
                int awayGoals = data.getInt(INDEX_AWAY_GOALS_COL);
                String matchTime = data.getString(INDEX_TIME_COL);

                views.setTextViewText(R.id.home_name, homeName);
                views.setTextViewText(R.id.away_name, awayName);
                views.setTextViewText(R.id.time_textview, matchTime);
                views.setTextViewText(R.id.score_textview,
                        Utilities.getScores(homeGoals, awayGoals));

                //Dynamic content description for the list
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                    if (homeGoals < 0) {
                        views.setContentDescription(R.id.widget_list_item,
                                getString(R.string.content_description_widget_list_item_inactive,
                                        homeName,
                                        awayName,
                                        matchTime));
                    } else {
                        views.setContentDescription(R.id.widget_list_item,
                                getString(R.string.content_description_widget_list_item_active,
                                        homeName,
                                        awayName,
                                        homeGoals,
                                        awayGoals,
                                        matchTime));
                    }
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }


}
