(ns ebay.test.helpers.config
  (:use midje.sweet
        ring.mock.request)
  (:require [ebay.helpers.config]))

(facts "the ebay config sniper api" 
  (facts "creates a valid config file" 
    (let [item {:item-id 124 :price 20} config-file (ebay.helpers.config/generate-config-file item)]  config-file => "124 20\n"))

  (facts "about save-item-to-file"
    (facts "it returns a filepath" 
      (let [user {:id 8} item {:item-id 10 :price 20} ] 
        (ebay.helpers.config/save-item-to-file user item) => "/tmp/8/10.txt")
    (facts "it persists the configuration file" 
      (let [user {:id 8} item {:item-id 10 :price 20} path (ebay.helpers.config/save-item-to-file user item) ] 
        (slurp path) => "10 20\n"))
      )))
