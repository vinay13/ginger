use gingerdb
db.createUser( { user: "gingeradmin",
                 pwd: "c0ncorde",

                 roles: [ { role: "clusterAdmin", db: "admin" },
                          "readWrite"] },
               { w: "majority" , wtimeout: 5000 } )

use admin
db.createUser( { user: "admin",
                 pwd: "c0ncorde",

                 roles: [ { role: "clusterAdmin", db: "admin" },
                          "readWrite"] },
               { w: "majority" , wtimeout: 5000 } )