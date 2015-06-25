(ns ebay.test.services.sniper
  (:use midje.sweet)
  (:require [ebay.services.sniper]
             [ebay.models.item]
             [ebay.models.user]))

(def default-user (ebay.models.user/map->User {:user-id 8 :username "username" :password "password"}))
(def updated-user (ebay.models.user/map->User {:user-id 8 :username "username" :password "newpassord"}))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))
(def updated-item (ebay.models.item/map->Item {:item-id 10 :price 30}))

(facts "the sniper api"
  (facts "about users"
    (facts "it adds a user" 
      (ebay.services.sniper/add default-user) => true)
    (facts "it removes a user" 
      (ebay.services.sniper/remove default-user) => true)
    (facts "it edits a user" 
      (ebay.services.sniper/edit updated-user) => true))
  (facts "about items"
    (facts "it adds a item" 
      (ebay.services.sniper/add default-user default-item) => true)
    (facts "it edit the item given a username and item" 
      (ebay.services.sniper/edit default-user updated-item ) => true)
    (facts "it removes an item by username and item-id" 
      (ebay.services.sniper/remove default-user default-item) => true)))

