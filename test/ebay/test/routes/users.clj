(ns ebay.test.routes.users
  (:use midje.sweet
        ring.mock.request
        ebay.test.support.helpers
        ebay.handler))


(with-state-changes [(after :facts (delete-recursively))]
  (facts "the users api" 

    (facts "POST /users" 
      (facts "when sending an invalid user it returns 406"
        (:status (app (request :post "/users" {:password "password"}))) => 406
        (:status (app (request :post "/users" {:username "username"}))) => 406
        (:status (app (request :post "/users" {:password "password" :username ""}))) => 406
        (:status (app (request :post "/users" {:username "username" :password ""}))) => 406)
      (facts "when sending a new user" 
        (let [response (app (request :post "/users" {:username "username" :password "password"}))]
          "it returns 201"
            (:status response) => 201
          "it returns the user path"
            (:body response) => "/users/username"))
      (facts "when sending an existing user" 
        (do 
          (app (request :post "/users" {:username "username" :password "password"}))
          (let [response (app (request :post "/users" {:username "username" :password "password"}))]
            "it returns 200"
              (:status response) => 200))))

    (facts "PUT /users/{id}" 
      (facts "when sending an invalid user" 
        (do (app (request :post "/users" {:username "username" :password "password"}))
          "it returns 406"
            (:status (app (request :put "/users/username" {:password ""}))) => 406)
        (do (app (request :post "/users" {:username "username" :password "password"}))
            (:status (app (request :put "/users/username" {}))) => 406))
      (facts "when sending an existing user" 
        (do (app (request :post "/users" {:username "username" :password "password"}))
          (let [response (app (request :put "/users/username" {:password "newpassword"}))]
            "it returns 200"
              (:status response) => 200
            "it updates the user")))
      (facts "when sending a non existing user" 
        (let [response (app (request :put "/users/nonexistingusername" {:password "newpassword"}))]
          "it returns 404"
            (:status response) => 404)))
    (facts "DELETE /users/{id}" 
      (facts "when sending an existing user" 
       (do (app (request :post "/users" {:username "username" :password "password"}))
        (let [response (app (request :delete "/users/username"))]
          "it returns 200"
            (:status response) => 200
          "it deletes the user")))
        (facts "when sending a non existing user" 
          (let [response (app (request :delete "/users/nonexistingusername"))]
            "it returns 404"
              (:status response) => 404)))))
