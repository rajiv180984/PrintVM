// Runs once when the Mongo container is first created (docker-entrypoint-initdb.d).
// Creates an application user with access to the authdb database only.
// Collections and indexes are created by the application (spring.data.mongodb.auto-index-creation)
// and sample users by DataSeeder.
db = db.getSiblingDB('authdb');

db.createUser({
  user: 'auth_app',
  pwd: 'auth_app_password',
  roles: [{ role: 'readWrite', db: 'authdb' }]
});
