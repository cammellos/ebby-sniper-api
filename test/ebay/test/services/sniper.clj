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
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))
(def updated-item (ebay.models.item/map->Item {:item-id 10 :price 30}))

(with-state-changes [(after :facts (delete-recursively))]
  (facts "the sniper api"
    (facts "about users"
      (facts "it adds a user" 
        (ebay.services.sniper/save default-user) => true)
      (facts "it deletes a user" 
        (do
          (ebay.services.sniper/save default-user)
          (ebay.services.sniper/delete default-user) => true))
      (facts "it edits a user" 
        (do
          (ebay.services.sniper/save default-user)
          (ebay.services.sniper/edit updated-user) => true)))
    (facts "about items"
      (facts "it adds a item" 
        (ebay.services.sniper/add default-user default-item) => true)
      (facts "it edit the item given a username and item" 
        (do
          (ebay.services.sniper/add default-user default-item)
          (ebay.services.sniper/edit default-user updated-item ) => true))
      (facts "it deletes an item by username and item-id" 
        (do
          (ebay.services.sniper/add default-user default-item)
          (ebay.services.sniper/delete default-user default-item) => true)))))

