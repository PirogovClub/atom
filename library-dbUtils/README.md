## DbUtils Library

### 1. Purpose and Contents
The 'dbUtils' library contains a set of java helper classes that support database operation and can be reused by projects helper classes:

| Class         | Purpose                                                      |
| --------------| ------------------------------------------------------------ |
| Constant      | POJO to define the constant for DB util helper e.g. basepath, environment config file|
| DBUtilsHelper | Connect and run DML or DDL SQL statements against a relational database. |
| MongoDBHelper | Connect and run queries or updates against a MongoDB |
| ElasticHelper | Connect and run the queries or update for elastic search |

### 2. Usage
command line paramiter:
atom.db.utils.property.file

#### 2.1 Database Helper 

Connection details can be added to the [environment properties](#environment-properties) file and will be picked up and used by the framework. The examples below show a Sybase and Oracle connection setting.

```
#Sybase
DBurl=jdbc:jtds:sybase://localhost:5030;autoCommit=false
DBdriver=net.sourceforge.jtds.jdbc.Driver	
DBusr=devenv
DBpwd=devenv
```

```
#Oracle - When SID is provided 
DBurl=jdbc:oracle:thin:@localhost:1521:SID
DBdriver=oracle.jdbc.OracleDriver	
DBusr=devenv
DBpwd=devenv

#Oracle - When Service Name is provided 
DBurl=jdbc:oracle:thin:@//localhost:1521/serviceName
DBdriver=oracle.jdbc.OracleDriver	
DBusr=devenv
DBpwd=devenv
```

Note: If there are more than one database to be connected then dbPrefic can be used to have multiple db connections
```
#Oracle with Db prefix - 'TestDB'						
TestDB_DBurl=jdbc:oracle:thin:@localhost:1521:SID
TestDB_DBdriver=oracle.jdbc.OracleDriver	
TestDB_DBusr=devenv
TestDB_DBpwd=devenv

Note : When using above config then use the respective helper method (using dbPrefix) to perform the db operation
```
<br>

The *DBUtilsHelper* class includes the methods listed below.  

```
/** connects via JDBC using the url/usr/pwd and driver specified in the test project config */
public static void createConn()

/** connects via JDBC using the url/usr/pwd and driver supplied in input params */
public static void createConn(String url, String driver, String usr, String pwd)

/** connects via JDBC using the dbPrefix and specified in the test project config */
public static void createConn(String dbPrefix)

/** closes the JDBC connection */
public static void closeConn()

/** runs SQL query statement returning single database row with columns as an object array */
public static Object[] getDBArray(String sql, Object...params)

/** runs SQL query statement returning multiple rows with columns as an object array */
public static List<Object[]> getDBArrayList(String sql, Object...params)

/** runs SQL query statement returning single row with columns as an object map */
public static Map<String, Object> getDBMap(String sql, Object...params)

/** runs SQL query statement returning multiple rows with columns as an object map */
public static List<Map<String, Object>> getDBMapList(String sql, Object...params)

/** runs SQL query statement returning single row as any POJO java class*/
public static <T> Object getDBBean(String sql, Class<T> clazz, Object...params)

/** runs SQL query statement returning multiple rows as list of any POJO java class*/
public static <T> List<T> getDBBeanList(String sql, Class<T> clazz, Object...params)

/** runs SQL insert/update/delete statement returning number of affected rows*/
public static int update(String sql, Object...params)

/** method to execute query for the given Db from environment config file*/
public static Map<String, Object> getDBMap2(String dbPrefix, String sql, Object... params)

/** method to execute query for the given Db from environment config file*/
public static List<Map<String, Object>> getDBMapList2(String dbPrefix, String sql, Object... params) throws

/** method to execute query for the given Db from environment config file*/
public static int update2(String dbPrefix, String sql, Object... params) throws SQLException, IOException {

/** Run multiple database insert/update/delete queries*/
public static int[] bulkUpdate(String sql, Object[]... params) throws SQLException, IOException {

/** Run database insert/update/delete query&/
public static int multiUpdate(String[] sqls, Object... params) throws SQLException, IOException {

```

There is a single update method and 3 groups of read methods.   The update method returns the number of affected rows.  The read methods return:

1. array - either single array object for 1 row, or list of array objects for multiple rows
2. map - either single map object for 1 row, or list of map objects for multiple rows
3. pojo - either single pojo object for 1 row, or list of pojo objects for multiple rows

The read methods all operate the same way and will execute the same SQL.  The choice of which to use in your test code is based on which object type you would prefer for the returned result set.  Generally speaking either the map or pojo approaches will make the test code most readable since the keys of the map or the instance variables of the pojo match the database column heading names.

All of the update/read query types accept an optional list of parameters for substitution into the SQL statements (replaces any '?' in the SQL) .  

<br>

The following code snippets show some example usage.

*Read with single row returned into Object array of columns:*

```java
String sql = "select * from myTable where myCol = 'foo'";
Obj[] rs = getDBArray(sql);

System.out.println(rs[0].toString());
System.out.println(rs[1].toString());
......
```

*Read with multiple rows returned into Map, where column headings are the key values:*

```java
String sql = "select col1, col2, col3 from myTable where someColumn >= 1";
List<Map<String, Object> rows = getDBMapList(sql);

for (Map<String, Object> map : rows){
    System.out.println(map.get("col1"));
    System.out.println(map.get("col2"));
    System.out.println(map.get("col3"));
}
```

*Read with multiple rows returned into list of POJO's and with substitution variables:*

```java
class myPOJO{
	public String col1;
	public String col2;
}
......
Object[] params = {1, "foo"};

String sql = "select col1, col2 from myTable where someColumn >= ? and anotherColumn = ?";
List<myPOJO> rows = getDBBeanList(sql, myPOJO.class, params);

for (myPOJO pojo : rows) {
	System.out.println(pojo.col1);
	System.out.println(pojo.col2);
}

```

*Update:*

```java
String sql = "update myTable set myCol = 1";
int rows = update(sql);

System.out.println("number of rows updated = " + rows);
```

*Update with substitution variables:*

```java
Object[] params = {"foo"};
String sql = "delete from myTable where some_id = ? ";
int rows = update(sql, params);

System.out.println("number of rows deleted = " + rows);
```
<br>

#### 2.2 Mongo Database Helper 
Connection details can be added to the [environment properties](#environment-properties) file and will be picked up and used by the framework. The examples below show a Sybase and Oracle connection setting.

```
DBurl=mongodb://username:password@localhost:port/defaultDatabase?authSource=authDb&ssl=false
DBName=defaultDatabase
```

The MongoDBHelper class includes the methods listed below.

```
/** connects to MongoDB using the url/usr/pwd and database name specified in the test project config */
public static void createConn()

/** Create the db connection when uri and db name is provided*/
public static void createConn(String uri, String mongoDbName)

/** Create the db connection dbPrefix and specified in the test project config */
public static void createConn(String dbPrefix) 

/** returns a collection from the database */ 
public static MongoCollection<Document> getCollection(String col)

/** closes the client connection */
public static void closeConn()

/** iterates through a list of collections and drops them from the database */
public static void dropCollection(List<String> collections)

/** inserts a single document into the collection */
public static void insertOne(String col, Document doc)

/** Override method to existing using dbPrefix for db connection. insert single document into the collection*/
public static void insertOne(String dbPrefix, String col, Document doc) {

/** inserts multiple documents into the collection */
public static void insertMany(String col, List<? extends Document> docs)

/** runs the query and returns only one document as a string */
public static String findOne(String col, BasicDBObject query)

/** override method to existing using dbPrefix for db connection. Runs the query and returns only one document as a string*/
public static String findOne(String dbPrefix, String col, BasicDBObject query) 
	
/**runs the query and returns only one document */
public static Document findOneDocument(String dbPrefix, String col, BasicDBObject query) {

/** runs the query and returns a list of documents that match the conditions */
public static MongoIterable<Document> findMany(String col, BasicDBObject query)

/** returns a list of all the documents from a database collection */
public static MongoIterable<Document> getDoc(String col)

/** runs the query and updates a single document from the collection */
public static void updateOne(String col, BasicDBObject query, BasicDBObject updateObj)

/** override method to existing, no need to have separate open/close connection method while using this method*/
public static void updateOne(String dbPrefix, String col, BasicDBObject query, BasicDBObject updateObj)

/** runs the query and updates multiple documents from the collection */
public static void updateMany(String col, BasicDBObject query, BasicDBObject updateObj)

/** runs the query and deletes a single document from the collection */
public static void deleteOne(String col, BasicDBObject query)

/** runs the query and deletes multiple documents from the collection */
public static void deleteMany(String col, BasicDBObject query)

/** creates an Object to pass on as query (filter) in the mongo documnet methods. Equivalent to and condition in SQL*/
public BasicDBObject andQuery(JSONObject jsonObject)

/** creates an Object to pass on to Bson update in the mongo document update method*/
public BasicDBObject setQuery(JSONObject jsonObject){

/** return the BasicDBObject created from input JSONObject*/
public BasicDBObject setDataHelper(JSONObject jsonObject) {

/** return the list of BasicDBObject created from JSONObject*/
public List<BasicDBObject> queryHelper(JSONObject jsonObject)
```

The following code snippets show some example usage.

*Java method to find one document using a JSONObject and verifying if expected value matches with the actual value in the document for a specified field:*

```java
public void findOneDoc(String field, int value) {
    JSONObject jsonObj = new JSONObject();
	jsonObj.put("foo","bar");
	
	Document doc = MongoDBHelper.findOne("collection_name", andQuery(jsonObj));	
	Assert.assertEquals(value, doc.get(field));
}
```

#### 2.3 Elastic Search Helper 
The ElasticHelper class includes the methods listed below.

```
/** Closes the connection if one exist*/
public void close() 

/** This gets a search results for the search*/
public SearchResponse search(SearchRequest searchRequest) throws IOException 

/** Gets a single document */
public GetResponse getDocument(String index, String type, String id) throws IOException 

/** Updates a document using a JSON input*/
public UpdateResponse updateDocument(String index, String type, String id, String json) throws IOException 

/** Creates a document in the index using the generated id*/
public IndexResponse insertDocumenet(String index, String type, String json) throws IOException 

/** Creates a document in the index and setting the id*/
public IndexResponse insertDocumenet(String index, String type, String id, String json) throws IOException 

/** Deletes a document using the id*/
public DeleteResponse deleteDocument(String index, String type, String id) throws IOException 

/**Creates a new index with no settings*/
public CreateIndexResponse createIndex(String index) throws IOException 

/** Creates a new index passing in the index request*/
public CreateIndexResponse createIndex(CreateIndexRequest request) throws IOException 

/**Delete index using request*/
public DeleteIndexResponse deleteIndex(DeleteIndexRequest  request) throws IOException 

/**Delete index using request*/
public DeleteIndexResponse deleteIndex(String index) throws IOException 

/**Returns the clinet so it can be used in more complex ways, such as scroll search*/
public RestHighLevelClient getClient() 
```