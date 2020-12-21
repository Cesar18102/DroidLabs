package nure.kyrychenko_oleh_volodymyrovych_5.Listeners;

import nure.kyrychenko_oleh_volodymyrovych_5.Model.AsyncNoteQueryResult;

public interface OnAsyncNoteQueryDoneListener {
    void handleNoteQueryFinish(AsyncNoteQueryResult queryResult);
    void handleNoteQueryStart();
}
