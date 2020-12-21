package nure.kyrychenko_oleh_volodymyrovych_5.Model;

public class AsyncNoteQueryResult {
    private AsyncQueryType queryType;
    private INote[] getAllResult;
    private INote getByIdResult;

    public AsyncNoteQueryResult(AsyncNoteQueryInfo info) {
        queryType = info.getQueryType();
    }

    public AsyncQueryType getQueryType() {
        return queryType;
    }

    public INote[] getGetAllResult() {
        return getAllResult;
    }
    public void setGetAllResult(INote[] getAllResult) {
        this.getAllResult = getAllResult;
    }

    public INote getGetByIdResult() {
        return getByIdResult;
    }
    public void setGetByIdResult(INote getByIdResult) {
        this.getByIdResult = getByIdResult;
    }
}
