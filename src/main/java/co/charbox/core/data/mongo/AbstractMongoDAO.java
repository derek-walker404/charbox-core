package co.charbox.core.data.mongo;

import java.util.List;

import org.bson.types.ObjectId;

import co.charbox.core.data.GenericDAO;
import co.charbox.core.data.PersistentModel;
import co.charbox.core.data.SearchResults;
import co.charbox.core.utils.JsonUtils;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public abstract class AbstractMongoDAO<ModelT extends PersistentModel<ModelT>> implements GenericDAO<ModelT> {

	private final DBCollection collection;
	private final Class<ModelT> modelClass;
	
	public AbstractMongoDAO(DBCollection collection, Class<ModelT> modelClass) {
		this.collection = collection;
		this.modelClass = modelClass;
	}
	
	protected final DBCollection getCollection() {
		return collection;
	}
	
	protected boolean hasSort() {
		return false;
	}
	
	protected DBObject getSort() {
		return null;
	}
	
	public SearchResults<ModelT> find(int limit, int offset) {
		DBCursor result = getCollection().find().limit(limit).skip(offset);
		if (hasSort()) {
			result.sort(getSort());
		}
		List<ModelT> models = Lists.newArrayList();
		while (result.hasNext()) {
			ModelT temp = convert(result.next());
			if (temp != null) {
				models.add(temp);
			}
		}
		return new SearchResults<ModelT>()
				.setLimit(limit)
				.setOffset(offset)
				.setResults(models);
	}
	
	public long count() {
		return getCollection().count();
	}
	
	public ModelT find(String id) {
		try {
			return convert(collection.findOne(new ObjectId(id)));
		} catch (IllegalArgumentException e) {
			// do nothing when invalid id's are provided
			// TODO: need to log? probably not
		}
		return null;
	}
	
	public ModelT insert(ModelT model) {
		ObjectId expectedId = new ObjectId();
		DBObject inserObject = (DBObject)JSON.parse(convert(model));
		inserObject.put("_id", expectedId);
		getCollection().insert(inserObject);
		return find(expectedId.toString());
	}
	
	protected String convert(ModelT model) {
		return JsonUtils.toJson(model);
	}
	
	protected ModelT convert(DBObject obj) {
		if (obj == null) {
			return null;
		}
		ObjectId id = (ObjectId) obj.get("_id");
		obj.put("_id", id.toString());
		return JsonUtils.fromJson(JSON.serialize(obj), modelClass);
	}
	
	public ModelT update(ModelT model) {
		if (model == null) {
			return null;
		}
		DBObject updateObj = (DBObject)JSON.parse(convert(model));
		updateObj.removeField("_id");
		WriteResult wr = getCollection().update(getIdQuery(model.get_id()), updateObj);
		return wr.getN() == 1 ? model : null;
	}
	
	public boolean delete(String id) {
		if (id == null) {
			return false;
		}
		return getCollection().remove(getIdQuery(id)).getN() == 1;
	}
	
	protected DBObject getIdQuery(String id) {
		return new BasicDBObject("_id", new ObjectId(id));
	}
}
