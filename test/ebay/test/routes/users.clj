(ns ebay.test.routes.users
  (:use midje.sweet
        ring.mock.request
        ebay.handler))


(facts "the users api" 
  "POST /users" 
    "when sending an invalid user" 
      "it returns 406"
        (:status (app (request :post "/users" {:password "password"}))) => 406
        (:status (app (request :post "/users" {:username "username"}))) => 406
        (:status (app (request :post "/users" {:password "password" :username ""}))) => 406
        (:status (app (request :post "/users" {:username "username" :password ""}))) => 406
    "when sending a new user" 
      (let [response (app (request :post "/users" {:username "username" :password "password"}))]
        "it returns 201"
          (:status response) => 201
        "it creates the user"
        "it returns the user path"
          (:body response) => "/users/username")
    "when sending an existing user" 
      (do 
        (app (request :post "/users" {:username "username" :password "password"}))
        (let [response (app (request :post "/users" {:username "username" :password "password"}))]
          "it returns 200"
            (:status response) => 200))
  "PUT /users/{id}" 
    (do (app (request :post "/users" {:username "username" :password "password"}))
      "when sending an invalid user" 
        "it returns 406"
          (:status (app (request :put "/users/username" {:password ""}))) => 406
          (:status (app (request :put "/users/username" {}))) => 406
      "when sending an existing user" 
        (let [response (app (request :put "/users/username" {:password "newpassword"}))]
          "it returns 200"
            (:status response) => 200
          "it updates the user"))
      "when sending a non existing user" 
        (let [response (app (request :put "/users/nonexistingusername" {:password "newpassword"}))]
          "it returns 404"
            (:status response) => 404)
  "DELETE /users/{id}" 
    (do (app (request :post "/users" {:username "username" :password "password"}))
      "when sending an existing user" 
        (let [response (app (request :delete "/users/username"))]
          "it returns 200"
            (:status response) => 200
          "it deletes the user"))
      "when sending a non existing user" 
        (let [response (app (request :delete "/users/nonexistingusername"))]
          "it returns 404"
            (:status response) => 404))





