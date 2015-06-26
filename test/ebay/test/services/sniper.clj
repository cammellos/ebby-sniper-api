(ns ebay.test.services.sniper
  (:use [midje.sweet]
        [clojure.java.shell :only [sh]]
        [clojure.java.io])
  (:require [ebay.services.sniper]
             [ebay.models.config]
             [ebay.models.item]
             [ebay.models.user]))

(def ^:private config (ebay.models.config/default-config))

(def ^:private base-dir (:auctions-path config))

(defn- delete-recursively []
  (if (.exists (as-file base-dir))
    (sh  "rm" "-r" base-dir)))


(def default-user (ebay.models.user/map->User {:username "username" :password "password"}))
(def updated-user (ebay.models.user/map->User {:username "username" :password "newpassord"}))
(def no-username-user (ebay.models.user/map->User {:password "newpassord"}))
(def no-password-user (ebay.models.user/map->User {:username "username"}))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))
(def updated-item (ebay.models.item/map->Item {:item-id 10 :price 30}))
(def non-existing-item (ebay.models.item/map->Item {:item-id 10 :price 30}))

(with-state-changes [(after :facts (delete-recursively))]
  (facts "the sniper api"
    (facts "about users"
      (facts "it adds a user" 
        (ebay.services.sniper/save default-user) => true)
      (facts "it deletes a user" 
        (do
          (ebay.services.sniper/save default-user)
          (ebay.services.sniper/delete default-user) => true))
      (facts "it returns true if the user exists"
         (do
          (ebay.services.sniper/save default-user)
          (ebay.services.sniper/exists? default-user) => true))
      (facts "it returns false if the user does not exists"
          (ebay.services.sniper/exists? default-user) => false)
      (facts "it returns false if the user does not have a password"
          (ebay.services.sniper/valid? no-password-user) => false)
      (facts "it returns false if the user does not have a username"
          (ebay.services.sniper/valid? no-username-user) => false)
      (facts "it returns true if the user is valid"
          (ebay.services.sniper/valid? default-user) => true)
      (facts "it edits a user" 
        (do
          (ebay.services.sniper/save default-user)
          (ebay.services.sniper/edit updated-user) => true)))
    (facts "about items"
      (facts "it adds a item" 
        (ebay.services.sniper/save default-user default-item) => true)
      (facts "it edit the item given a username and item" 
        (do
          (ebay.services.sniper/save default-user default-item)
          (ebay.services.sniper/edit default-user updated-item ) => true))
      (facts "it returns true if the user & items exists"
         (do
          (ebay.services.sniper/save default-user default-item)
          (ebay.services.sniper/exists? default-user default-item) => true))
      (facts "it returns false if the user does not exists"
          (ebay.services.sniper/exists? default-user default-item) => false)
      (facts "it returns false if the item does not exists"
          (ebay.services.sniper/save default-user)
          (ebay.services.sniper/exists? default-user non-existing-item) => false)
      (facts "it deletes an item by username and item-id" 
        (do
          (ebay.services.sniper/save default-user default-item)
          (ebay.services.sniper/delete default-user default-item) => true)))))

