package co.charbox.core.data;


public interface GenericDAO<ModelT> {

	public SearchResults<ModelT> find(int limit, int offset);
	
	public long count();
	
	public ModelT find(String id);
	
	public ModelT insert(ModelT model);
	
	public ModelT update(ModelT model);
	
	public boolean delete(String id);
}