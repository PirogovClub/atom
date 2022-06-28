package automation.library.dbUtils;

import automation.library.common.Property;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import automation.library.common.TestContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Momgo db util helper class. In the core framework there is another mongo db helper clas which has implemented static class
 * member. It was observed that that was giving issue during parallel execution like connection close when expected to be open
 * or not able to open new connection etc. Therefore this class is implemented to over come parallel test execution issue observed.
 */
public class MongoDBUtilsHelper {

	MongoClient mongoClient;
	MongoDatabase database;
	String dbNameAsString;

	public MongoDBUtilsHelper() {

	}

	public void createConn() {
		if (mongoClient == null) {

			String propsPath = Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties";

			String uri = Property.getProperty(propsPath, "MongoDBurl");
			dbNameAsString = Property.getProperty(propsPath, "MongoDBName");
			MongoClientURI connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase(dbNameAsString);
		}
	}

	/**
	 * To create the connection when uri and db name is passed as parameter
	 *
	 * @param uri connection url
	 * @param mongoDbName mongodb name
	 */
	public void createConn(String uri, String mongoDbName) {
		if (mongoClient == null) {
			dbNameAsString = mongoDbName;
			MongoClientURI connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase(dbNameAsString);
		}
	}

	/**
	 * Create the db connection reading the config details from environment config file using the db prefix
	 * /src/test/resources/config/environments/env.properties
	 * read value from - dbPrefix_MongoDBurl, dbPrefix_MongoDBName
	 */
	public void createConn(String dbPrefix) {
		if (mongoClient == null) {
			String propsPath = Constants.ENVIRONMENTPATH + Property.getVariable("cukes.env") + ".properties";

			String uri = Property.getProperty(propsPath, dbPrefix+"_MongoDBurl");
			dbNameAsString = Property.getProperty(propsPath, dbPrefix+"_MongoDBName");
			MongoClientURI connectionString = new MongoClientURI(uri);
			mongoClient = new MongoClient(connectionString);
			database = mongoClient.getDatabase(dbNameAsString);
		}
	}


	/**
	 * return the collection
	 * @param collectionName - name of the collection to be returned
	 * @return collection
	 */
	public MongoCollection<Document> getCollection(String collectionName) {
		return database.getCollection(collectionName);
	}

	/**
	 * close the connection
	 */
	public void closeConn() {
		mongoClient.close();
		mongoClient = null;
	}

	/**
	 * drop the input collection
	 * @param collections - collection to be dropped
	 */
	public void dropCollection(List<String> collections) {
		for (String col : collections) {
			MongoCollection<Document> collection = getCollection(col);
			collection.drop();
		}
	}

	/**
	 * insert one document
	 * Note : connection to db must be established
	 */
	public void insertOne(String col, Document doc) {
		database.getCollection(col).insertOne(doc);
	}

	/**
	 * insert one documen
  	 * verride method to existing, no need to open or close the db when using this method,
	 * pass the db prefix.
	 *
	 * @param dbPrefix if entry in property file : dbPrefix_MongoDBurl then prefix would be 'dbPrefix'
	 * @param col - collection name
	 * @param doc - document to be inserted
	 */
	public void insertOne(String dbPrefix, String col, Document doc) {
		createConn(dbPrefix);
		database.getCollection(col).insertOne(doc);
		closeConn();
	}

	/**
	 * insert many documents
	 * Note : connection to db must be established
	 * @param col collection name
	 * @param docs to be insereted
	 */
	public void insertMany(String col, List<? extends Document> docs) {
		database.getCollection(col).insertMany(docs);
	}

	/**
	 * fine one document
	 * Note : connection to db must be established
	 * @param col collecton Name
	 * @param query
	 * @return collection as json String
	 */
	public String findOne(String col, BasicDBObject query) {
		return database.getCollection(col).find(query).first().toJson();
	}

	/**
	 * override method to existing, no need to open/close the db when using this method, pass the db prefix.
	 * @param dbPrefix if entry in property file : dbPrefix_MongoDBurl then prefix would be 'dbPrefix'
	 * @param col      collection name
	 * @param query    the query filter
	 * @return string in json format
	 */
	public String findOne(String dbPrefix, String col, BasicDBObject query) {
		try {
			createConn(dbPrefix);
			return database.getCollection(col).find(query).first().toJson();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeConn();
		}
	}

	/**
	 * override method to existing, no need to open or close the db when using this method, pass the db prefix.
	 * @param dbPrefix if entry in property file : dbPrefix_MongoDBurl then prefix would be 'dbPrefix'
	 * @param col      collection name
	 * @param query    the query filter
	 * @return Mongo document in json format
	 */
	public Document findOneDocument(String dbPrefix, String col, BasicDBObject query) {
		try {
			createConn(dbPrefix);
			return database.getCollection(col).find(query).first();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeConn();
		}
	}

	/**
	 * return many document found as per filter
	 * @param col
	 * @param query
	 */
	public MongoIterable<Document> findMany(String col, BasicDBObject query) {
		return database.getCollection(col).find(query);
	}

	/**
	 * return all the document for a collection provided
	 */
	public  MongoIterable<Document> getDoc(String col) {
		return database.getCollection(col).find();
	}

	/**
	 * update one document as per query filter
	 */
	public void updateOne(String col, BasicDBObject query, BasicDBObject updateObj) {
		database.getCollection(col).updateOne(query, updateObj);
	}

	/**
	 * override method to existing, no need to open/close the db when using this method, pass the db prefix.
	 * @param dbPrefix db prefix as per config file
	 * @param col collection name
	 * @param query the query filter
	 * @param updateObj a document describing the update, which may not be null. The update to apply must include only update operators.
	 */
	public void updateOne(String dbPrefix,String col, BasicDBObject query, BasicDBObject updateObj) {
		createConn(dbPrefix);
		database.getCollection(col).updateOne(query, updateObj);
		closeConn();
	}

	/**
	 * update many document as per query filter
	 */
	public void updateMany(String col, BasicDBObject query, BasicDBObject
			updateObj) {
		database.getCollection(col).updateMany(query, updateObj);
	}

	/**
	 * delete one document as per query filter
	 */
	public void deleteOne(String col, BasicDBObject query) {
		database.getCollection(col).deleteOne(query);
	}

	/**
	 * delete many document as per query filter
	 */
	public void deleteMany(String col, BasicDBObject query) {
		database.getCollection(col).deleteMany(query);
	}
	
    /**
     * Helper method to stor files that exceed the BSON document size limit of 16MB using GridFS
     * @param dbPrefix if entry in property file : Mongo_DBurl then prefix would be 'Mongo'
     * @param bucketName bucket where chucks and file collection will be created
     * @param ResType which type of file pdf - presentation, umage - image (metadata)
     * @param resName filename to be stored in mongodb
     * @param streamToUploadFrom InputStrem to be updloaded
     * @return ObjectId of document created
     */
    public ObjectId uploadToGridFS(String dbPrefix, String bucketName, String ResType, String resName, InputStream streamToUploadFrom) {
        ObjectId _id;
        try {
            createConn(dbPrefix);
            GridFSBucket gridFSBucket = GridFSBuckets.create(database, bucketName);

            GridFSUploadOptions options = new GridFSUploadOptions()
                    .chunkSizeBytes(358400)
                    .metadata(new Document("type", ResType));
            _id = gridFSBucket.uploadFromStream(resName, streamToUploadFrom, options);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
        return _id;
    }

    /**
     * Helper method to retrieve files using GridFS
     * @param dbPrefix if entry in property file : Mongo_DBurl then prefix would be 'Mongo'
     * @param bucketName bucket where chucks and file collection will be created
     * @param _id ObjectID of the documnet to be retrieved
     * @return byte[] stream of the file
     */
    public byte[] downloadGfromGridFS(String dbPrefix, String bucketName, ObjectId _id) {

        byte[] bytesToWriteTo;

        try {
            createConn(dbPrefix);
            GridFSBucket gridFSBucket = GridFSBuckets.create(database, bucketName);

            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(_id);
            int fileLength = (int) downloadStream.getGridFSFile().getLength();
            bytesToWriteTo = new byte[fileLength];
            downloadStream.read(bytesToWriteTo);
            downloadStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConn();
        }
        return bytesToWriteTo;

    }
	

	/**
	 * creates an Object to pass on as query(filter) in the mongo document methods. Equivalent to and condition in SQL
	 * @param jsonObj jsonObject input json Object
	 * @return and query filter
	 */
	public BasicDBObject andQuery(JSONObject jsonObj) {
		if (jsonObj != null) {
			BasicDBObject andQuery = new BasicDBObject();
			andQuery.put("$and", queryHelper(jsonObj));
			return andQuery;
		} else {
			return null;
		}
	}

	/**
	 * creates object to pass on Bson update in the mongo document update methods.
	 * BasicDBObject obj = new BasicDBObject();
	 * obj.put("key","value");
	 * BasicDBObject setData = new BasicDBObject();
	 * setData.put("$set",obj);
	 *
	 * @param jsonObj
	 * @return setData
	 */
	public BasicDBObject setData(JSONObject jsonObj) {
		if (jsonObj != null) {
			BasicDBObject setData = new BasicDBObject();
			setData.put("$set", setDataHelper(jsonObj));
			return setData;
		} else {
			return null;
		}
	}

	/**
	 * return the BasicDBObject created from input JSONObject
	 */
	public BasicDBObject setDataHelper(JSONObject jsonObj) {
		BasicDBObject obj = new BasicDBObject();
		Iterator<?> keys = jsonObj.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			obj.put(key, jsonObj.get(key));
		}
		return obj;
	}

	/**
	 * return the list of BasicDBObject created from JSONObject
	 */
	public List<BasicDBObject> queryHelper(JSONObject jsonObj) {
		List<BasicDBObject> obj = new ArrayList<>();
		Iterator<?> keys = jsonObj.keys();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			obj.add(new BasicDBObject(key, jsonObj.get(key)));
		}
		return obj;
	}
}
