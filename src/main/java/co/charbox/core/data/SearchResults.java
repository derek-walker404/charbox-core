package co.charbox.core.data;

import java.util.List;

public class SearchResults<ModelT> {

	private int limit;
	private int offset;
	private List<ModelT> results;
	
	public int getLimit() {
		return limit;
	}
	public SearchResults<ModelT> setLimit(int limit) {
		this.limit = limit;
		return this;
	}
	public int getOffset() {
		return offset;
	}
	public SearchResults<ModelT> setOffset(int offset) {
		this.offset = offset;
		return this;
	}
	public List<ModelT> getResults() {
		return results;
	}
	public SearchResults<ModelT> setResults(List<ModelT> results) {
		this.results = results;
		return this;
	}
}
