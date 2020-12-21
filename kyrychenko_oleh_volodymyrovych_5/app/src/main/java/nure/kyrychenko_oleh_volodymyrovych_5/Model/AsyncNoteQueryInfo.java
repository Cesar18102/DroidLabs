package nure.kyrychenko_oleh_volodymyrovych_5.Model;

public class AsyncNoteQueryInfo {
    private AsyncQueryType queryType;
    private FilterSettings filters;
    private String getId;

    public AsyncQueryType getQueryType() {
        return queryType;
    }
    public void setQueryType(AsyncQueryType queryType) {
        this.queryType = queryType;
    }


    public FilterSettings getFilters() {
        return filters;
    }
    public void setFilters(FilterSettings filters) {
        this.filters = filters;
    }

    public String getGetId() {
        return getId;
    }
    public void setGetId(String getId) {
        this.getId = getId;
    }
}
