(ns ebay.test.helpers.config
  (:use midje.sweet
        ring.mock.request
        )
  (:require [ebay.helpers.config])
  (:import  ebay.models.item.Item))

(facts "the ebay config sniper api" 
  (with-state-changes [(after :facts (ebay.helpers.config/delete-recursively))]
    (facts "about save-item-to-file"

      (facts "it returns a filepath" 
        (let [user {:id 8} item (ebay.models.item/Item. 10 20) ] 
          (ebay.helpers.config/save-item-to-file user item) => "/tmp/esniper/8/10.txt")

      (facts "it persists the configuration file" 
        (let [user {:id 8} item (ebay.models.item/Item. 10 20)
              path (ebay.helpers.config/save-item-to-file user item) ] 
          (.exists (clojure.java.io/as-file path)) => true))
             
      (facts "creates a valid config file" 
        (let [user {:id 8} item (ebay.models.item/Item. 10 20)
              path (ebay.helpers.config/save-item-to-file user item) ] 
              (slurp path) => "10 20\n"))))

    (facts "about delete-item-from-file"

      (facts "it return true on success" 
        (let [user {:id 8} item (ebay.models.item/Item. 10 20)
              path (ebay.helpers.config/save-item-to-file user item) ] 
              (ebay.helpers.config/delete-item-from-file user item) => truthy
             ))

      (facts "it return false if file does not exists" 
        (let [user {:id 8} item (ebay.models.item/Item. 10  20)
              path "/tmp/esniper/8/10.txt" ] 
              (ebay.helpers.config/delete-item-from-file user item) => falsey
             ))

      (facts "it deletes the file" 
        (let [user {:id 8} item (ebay.models.item/Item. 10  20) 
              path (ebay.helpers.config/save-item-to-file user item)  
              success (ebay.helpers.config/delete-item-from-file user item)]
                (.exists (clojure.java.io/as-file path)) => falsey
             ))
           )
))
