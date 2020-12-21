package nure.kyrychenko_oleh_volodymyrovych_5.DataSource;

import android.os.AsyncTask;

import nure.kyrychenko_oleh_volodymyrovych_5.Listeners.OnAsyncNoteQueryDoneListener;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.AsyncNoteQueryInfo;
import nure.kyrychenko_oleh_volodymyrovych_5.Model.AsyncNoteQueryResult;

public class AsyncNoteRepoTask extends AsyncTask<AsyncNoteQueryInfo, Float, AsyncNoteQueryResult> {
    private ISyncNoteDataSource dataSource;
    private OnAsyncNoteQueryDoneListener listener;

    public AsyncNoteRepoTask(ISyncNoteDataSource dataSource, OnAsyncNoteQueryDoneListener listener) {
        this.dataSource = dataSource;
        this.listener = listener;
    }

    @Override
    protected AsyncNoteQueryResult doInBackground(AsyncNoteQueryInfo... infos) {
        AsyncNoteQueryInfo info = infos[0];
        AsyncNoteQueryResult queryResult = new AsyncNoteQueryResult(info);

        switch (info.getQueryType()) {
            case GET:
                queryResult.setGetAllResult(
                        dataSource.getAllNotes(
                                info.getFilters()
                        )
                );
                break;
            case GET_BY_ID:
                queryResult.setGetByIdResult(
                        dataSource.getById(
                                info.getGetId()
                        )
                );
                break;
        }

        return queryResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.handleNoteQueryStart();
    }

    @Override
    protected void onPostExecute(AsyncNoteQueryResult queryResult) {
        super.onPostExecute(queryResult);
        listener.handleNoteQueryFinish(queryResult);
    }
}
