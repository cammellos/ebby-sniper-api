(ns ebay.test.helpers.config
  (:use midje.sweet
        ring.mock.request
        )
  (:require [ebay.helpers.config]
             [ebay.models.item]
             [ebay.models.user]
             ))

(def default-user (ebay.models.user/map->User {:user-id 8 }))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))

(facts "the ebay config sniper api" 
  ( facts "about the config object"
    (let [config-object (ebay.helpers.config/default-config)]
      (facts "about the root path" 
        (facts "it is /tmp/esniper/ by default"
          (:root-path config-object) => "/tmp/esniper/"))))

  (with-state-changes [(after :facts (ebay.helpers.config/delete-recursively))]
    (facts "about save-item-to-file"
      (facts "it returns a filepath" 
        (let [user default-user item default-item ] 
          (ebay.helpers.config/save-item-to-file user item) => "/tmp/esniper/8/10.txt")
      (facts "it persists the configuration file" 
        (let [user default-user item default-item 
              path (ebay.helpers.config/save-item-to-file user item) ] 
          (.exists (clojure.java.io/as-file path)) => true))
      (facts "creates a valid config file" 
        (let [user default-user item default-item 
              path (ebay.helpers.config/save-item-to-file user item) ] 
              (slurp path) => "10 20\n"))))

    (facts "about delete-item-from-file"
      (facts "it return true on success" 
        (let [user default-user item default-item 
              path (ebay.helpers.config/save-item-to-file user item) ] 
              (ebay.helpers.config/delete-item-from-file user item) => truthy))
      (facts "it return false if file does not exists" 
        (let [user default-user item default-item 
              path "/tmp/esniper/8/10.txt" ] 
              (ebay.helpers.config/delete-item-from-file user item) => falsey))
      (facts "it deletes the file" 
        (let [user default-user item default-item 
              path (ebay.helpers.config/save-item-to-file user item)  
              success (ebay.helpers.config/delete-item-from-file user item)]
                (.exists (clojure.java.io/as-file path)) => falsey)))))
