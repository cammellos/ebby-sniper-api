(ns ebay.test.services.sniper
  (:use midje.sweet)
  (:require [ebay.services.sniper]
             [ebay.models.item]
             [ebay.models.user]))

(def default-user (ebay.models.user/map->User {:user-id 8 :username "username" :password "password"}))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))

(facts "the sniper api"
  (facts "about users"
    (facts "it adds a user" 
      (ebay.services.sniper/add-user default-user) => true)
    (facts "it retrieves a user by username" 
      (ebay.services.sniper/get-user (:username default-user)) => default-user)
    (facts "it removes a user by username" 
      (ebay.services.sniper/remove-user (:username default-user)) => default-user)))
    ;(facts "it edits a user by username" 
    ;  (let [edited-user (ebay.models.user/map->User {:username "username" :password "new-password"})] 
    ;      ((ebay.services.sniper/edit-user (:username default-user)) => true)
    ;      ((ebay.services.sniper/get-user (:username default-user)) => edited-user)))))

