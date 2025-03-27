db = db.getSiblingDB('DB_CONFIG');

if (!db.getUser('root')) {
  db.createUser({
    user: "root",
    pwd: "root",
    roles: [{ role: "root", db: "admin" }]
  });
}

/**
 * 
 *  This is the most important file.
 * 
 * Created by Varnit Mittal and Aditya Priyadarshi on 21/03/2025 at 3:14 AM.
 * 
 * This is used to dockerize MongoDb.
 */
