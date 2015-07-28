(ns ebay.test.routes.items
  (:use midje.sweet
        ring.mock.request
        ebay.test.support.helpers
        ebay.handler))


(with-state-changes [(after :facts (delete-recursively))]
  (facts "the items api" 
    (facts "POST /users/{id}/items" 
      (facts "when sending an invalid item it returns 406"
        (do 
          (app (request :post "/users" {:username "username" :password "password"}))
          (:status (app (request :post "/users/username/items" {:item-id 823 }))) => 406
          (:status (app (request :post "/users/username/items" {:price 20}))) => 406
          (:status (app (request :post "/users/username/items" {:item-id 8232 :price "notanumber"}))) => 406
          (:status (app (request :post "/users/username/items" {}))) => 406))
      (facts "when sending a new item" 
        (do 
          (app (request :post "/users" {:username "username" :password "password"}))
          (let [response (app (request :post "/users/username/items" {:item-id 2323  :price 20}))]
            "it returns 201"
              (:status response) => 201
            "it returns the item path"
              (:body response) => "/users/username/2323")))
      (facts "when sending an existing item" 
        (do 
          (app (request :post "/users" {:username "username" :password "password"}))
          (app (request :post "/users/username/items" {:item-id 2323 :price 20}))
          (let [response (app (request :post "/users/username/items" {:item-id 2323 :price 20}))]
            "it returns 200"
              (:status response) => 200))))
    (facts "PUT /users/{id}/items/{id}" 
      (facts "when sending an invalid item" 
        (do (app (request :post "/users" {:username "username" :password "password"}))
          (app (request :post "/users/username/items" {:item-id 2323 :price 20}))
          "it returns 406"
            (:status (app (request :post "/users/username/items/2323" {:price "not number"}))) => 406) 
        (do (app (request :post "/users" {:username "username" :password "password"}))
          (app (request :post "/users/username/items" {:item-id 2323 :price 20}))
            (:status (app (request :post "/users/username/items/2323" {:price -1}))) => 406)))))
      ;(facts "when sending an existing user" 
      ;  (do (app (request :post "/users" {:username "username" :password "password"}))
      ;    (let [response (app (request :put "/users/username" {:password "newpassword"}))]
      ;      "it returns 200"
      ;        (:status response) => 200
      ;      "it updates the user")))
      ;(facts "when sending a non existing user" 
      ;  (let [response (app (request :put "/users/nonexistingusername" {:password "newpassword"}))]
      ;    "it returns 404"
      ;      (:status response) => 404)))
    ;(facts "DELETE /users/{id}" 
    ;  (facts "when sending an existing user" 
    ;   (do (app (request :post "/users" {:username "username" :password "password"}))
    ;    (let [response (app (request :delete "/users/username"))]
    ;      "it returns 200"
    ;        (:status response) => 200
    ;      "it deletes the user")))
    ;    (facts "when sending a non existing user" 
    ;      (let [response (app (request :delete "/users/nonexistingusername"))]
    ;        "it returns 404"
    ;          (:status response) => 404)))))
