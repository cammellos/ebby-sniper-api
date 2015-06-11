(ns ebay.test.handler
  (:use midje.sweet
        ring.mock.request
        ebay.handler
        ebay.database.items
        ))


(facts "main route"
  (let [response (app (request :get "/"))]
    (:status response) => 200))

(facts "not-found route"
  (let [response (app (request :get "/invalid"))]
    (:status response) => 404))

(facts "the users api" 
  (facts "creating a user" 
    (facts "returns 201"
      (let [response (app (request :post "/users" {:username 124 :password 24}))]
        (:status response) => 201))
    (facts "returns the user"
      (let [response (app (request :post "/users" {:username 124 :password 24}))]
        (:body response) => {:username 124 :id 1}))))

(facts "the item api"
  (facts "creating an item"
    (facts "it returns 201" 
      (let [response (app (request :post "/users/1/items" {:item-id 124 :price 24}))]
        (:status response) => 201))
    (facts "it returns the item" 
      (let [response (app (request :post "/users/2/items" {:item-id 124 :price 24}))]
        (:body response) => {:item-id 124 :price 24}))))


