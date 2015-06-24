(ns ebay.test.helpers.esniper
  (:use [ midje.sweet]
        [clojure.java.shell :only [sh]]
        [clojure.java.io])
  (:require [ebay.helpers.esniper]
             [ebay.models.config]
             [ebay.models.item]
             [ebay.models.user]))

(def ^:private config (ebay.models.config/default-config))

(def ^:private base-dir (:auctions-path config))

(defn- delete-recursively []
  (if (.exists (as-file base-dir))
    (sh  "rm" "-r" base-dir)))


(def default-user (ebay.models.user/map->User {:username "username" :password "password"}))
(def default-item (ebay.models.item/map->Item {:item-id 10 :price 20}))

(def username-digest "14c4b06b824ec593239362517f538b29")

(facts "the ebay config sniper api" 
  (with-state-changes [(after :facts (delete-recursively))]
    (facts "about save"
      (facts "when passed a user"
        (facts "it returns a filepath" 
          (let [user default-user ] 
            (ebay.helpers.esniper/save user) => (str "/tmp/esniper/auctions/" username-digest "/config.txt"))
        (facts "it persists the user configuration file" 
          (let [user default-user
                path (ebay.helpers.esniper/save user) ] 
            (.exists (clojure.java.io/as-file path)) => true))
        (facts "creates a valid user config file" 
          (let [user default-user 
                path (ebay.helpers.esniper/save user) ] 
                (slurp path) => "username = username\npassword = password\nseconds = 10"))))
      (facts "when passed a user & an item"
        (facts "it returns a filepath" 
          (let [user default-user item default-item ] 
            (ebay.helpers.esniper/save user item) => (str "/tmp/esniper/auctions/" username-digest "/items/10.txt"))
        (facts "it persists the configuration file" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item) ] 
            (.exists (clojure.java.io/as-file path)) => true))
        (facts "creates a valid config file" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item) ] 
                (slurp path) => "10 20\n")))))
    (facts "about delete"
      (facts "when passed a user"
        (facts "it return true on success" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item) ] 
                (ebay.helpers.esniper/delete user) => truthy))
        (facts "it return false if file does not exists" 
          (let [user default-user item default-item 
                path (str "/tmp/esniper/auctions/" username-digest "/items/10.txt") ] 
                (ebay.helpers.esniper/delete (assoc user :username "blah") item) => falsey))
        (facts "it deletes the the user directory and all the files" 
          (let [user default-user item default-item 
                user-path (ebay.helpers.esniper/save user)  
                path (ebay.helpers.esniper/save user item)  
                success (ebay.helpers.esniper/delete user)]
                  (.exists (clojure.java.io/as-file user-path)) => falsey)))
      (facts "when passed a user & an item"
        (facts "it return true on success" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item) ] 
                (ebay.helpers.esniper/delete user item) => truthy))
        (facts "it return false if file does not exists" 
          (let [user default-user item default-item 
                path (str "/tmp/esniper/auctions/" username-digest "/items/10.txt") ] 
                (ebay.helpers.esniper/delete user item) => falsey))
        (facts "it deletes the file" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item)  
                success (ebay.helpers.esniper/delete user item)]
                  (.exists (clojure.java.io/as-file path)) => falsey))))))
