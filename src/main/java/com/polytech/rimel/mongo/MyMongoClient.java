package com.polytech.rimel.mongo;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.polytech.rimel.model.CommitHistory;
import org.mongojack.DBCursor;
import org.mongojack.JacksonDBCollection;

public class MyMongoClient extends MongoClient{

    private static final int PORT = 27017;

    public MyMongoClient(String host){
        super(host, PORT);
    }


    public JacksonDBCollection<CommitHistory, String> getCollectionFromDatabase(String database, String collection){
        DB db = super.getDB(database);
        return JacksonDBCollection.wrap(db.getCollection(collection), CommitHistory.class,
                String.class);
    }


    public static void main( String args[] ) {
        MyMongoClient client = new MyMongoClient("novagen.fr");
        DBCursor<CommitHistory> cursor = client.getCollectionFromDatabase("rimel","repositories").find();

        System.out.println(cursor.count());

        for (CommitHistory iterable_element : cursor) {
            System.out.println(iterable_element.toString());
        }

    }
}
