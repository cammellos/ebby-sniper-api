(ns ebay.test.helpers.esniper
  (:use midje.sweet
        ring.mock.request
        )
  (:require [ebay.helpers.esniper]
             [ebay.models.item]
             [ebay.models.user]))

(def default-user (ebay.models.user/map->User {:username "username" :password "password"}))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))
(def username-digest "14c4b06b824ec593239362517f538b29")

(facts "the ebay config sniper api" 
  (with-state-changes [(after :facts (ebay.helpers.esniper/delete-recursively))]
    (facts "about save-item-to-file"
      (facts "it returns a filepath" 
        (let [user default-user item default-item ] 
          (ebay.helpers.esniper/save-item-to-file user item) => (str "/tmp/esniper/auctions/" username-digest "/items/10.txt"))
      (facts "it persists the configuration file" 
        (let [user default-user item default-item 
              path (ebay.helpers.esniper/save-item-to-file user item) ] 
          (.exists (clojure.java.io/as-file path)) => true))
      (facts "creates a valid config file" 
        (let [user default-user item default-item 
              path (ebay.helpers.esniper/save-item-to-file user item) ] 
              (slurp path) => "10 20\n"))))
    (facts "about delete-item-from-file"
      (facts "it return true on success" 
        (let [user default-user item default-item 
              path (ebay.helpers.esniper/save-item-to-file user item) ] 
              (ebay.helpers.esniper/delete-item-from-file user item) => truthy))
      (facts "it return false if file does not exists" 
        (let [user default-user item default-item 
              path (str "/tmp/esniper/auctions/" username-digest "/items/10.txt") ] 
              (ebay.helpers.esniper/delete-item-from-file user item) => falsey))
      (facts "it deletes the file" 
        (let [user default-user item default-item 
              path (ebay.helpers.esniper/save-item-to-file user item)  
              success (ebay.helpers.esniper/delete-item-from-file user item)]
                (.exists (clojure.java.io/as-file path)) => falsey)))))
