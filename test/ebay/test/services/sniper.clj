(ns ebay.test.services.sniper
  (:use midje.sweet)
  (:require [ebay.services.sniper]
             [ebay.models.item]
             [ebay.models.user]))

(def default-user (ebay.models.user/map->User {:user-id 8 :username "username" :password "password"}))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))
(def updated-item (ebay.models.item/map->Item {:item-id 10 :price 30}))

(facts "the sniper api"
  (facts "about users"
    (facts "it adds a user" 
      (ebay.services.sniper/add-user default-user) => true)
    (facts "it retrieves a user by username" 
      (ebay.services.sniper/get-user (:username default-user)) => default-user)
    (facts "it removes a user by username" 
      (ebay.services.sniper/remove-user (:username default-user)) => true))
  (facts "about items"
    (facts "it adds a item" 
      (ebay.services.sniper/add-item default-user default-item) => true)
    (facts "it retrieves the items given a username" 
      (ebay.services.sniper/get-items (:username default-user)) => [default-item])
    (facts "it edit the item given a username and item" 
      (ebay.services.sniper/edit-item (:username default-user) updated-item ) => [updated-item])
    (facts "it removes an item by username and item-id" 
      (ebay.services.sniper/remove-item (:username default-user) (:item-id default-item)) => true)))

