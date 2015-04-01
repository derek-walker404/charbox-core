package co.charbox.core.data;

public interface PersistentModel<ModelT> {

	String get_id();
	
	ModelT set_id(String id);
}
