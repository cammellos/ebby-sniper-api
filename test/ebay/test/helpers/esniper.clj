(ns ebay.test.helpers.esniper
  (:use [ midje.sweet]
        [clojure.java.shell :only [sh]]
        [clojure.java.io]
        [ebay.test.support.helpers])
  (:require  [ebay.helpers.esniper]
             [ebay.models.item]
             [ebay.models.user]))



(def default-user (ebay.models.user/map->User {:username "username" :password "password"}))
(def updated-user (ebay.models.user/map->User {:username "username" :password "newpassword"}))
(def non-existing-user (ebay.models.user/map->User {:username "non-existing" :password "newpassword"}))
(def invalid-user (ebay.models.user/map->User {:username "" :password "newpassword"}))
(def invalid-password-user (ebay.models.user/map->User {:username "username" :password ""}))
(def default-item (ebay.models.item/map->Item {:item-id "10" :price 20}))

(def username-digest "14c4b06b824ec593239362517f538b29")

(with-state-changes [(after :facts (delete-recursively))]
  (facts "the ebay config sniper api" 
    "about save"
      "when passed a user"
        "it returns a filepath" 
          (ebay.helpers.esniper/save default-user) => (str "/tmp/esniper/auctions/" username-digest "/config.txt")
        "it persists the user configuration file" 
          (.exists (clojure.java.io/as-file (ebay.helpers.esniper/save default-user))) => true
        "it creates a valid user config file" 
          (slurp (ebay.helpers.esniper/save default-user)) => "username = username\npassword = password\nseconds = 10"
      "when passed a user with an invalid password"
        "it returns false" 
          (ebay.helpers.esniper/edit invalid-password-user) => false
      "when passed an invalid user"
        "it return false" 
          (ebay.helpers.esniper/edit invalid-user) => false
      "when passed a user & an item"
        "it returns a filepath" 
          (ebay.helpers.esniper/save default-user default-item) => (str "/tmp/esniper/auctions/" username-digest "/items/10.txt")
        "it persists the configuration file" 
          (.exists (clojure.java.io/as-file (ebay.helpers.esniper/save default-user default-item))) => true
        "it creates a valid config file" 
          (slurp (ebay.helpers.esniper/save default-user default-item)) => "10 20\n"
    "about edit"
      "when passed a user"
        "it edits config file" 
          (let [path (ebay.helpers.esniper/save default-user)]
            (slurp (ebay.helpers.esniper/edit updated-user)) => "username = username\npassword = newpassword\nseconds = 10"
      (facts "when passed a user with an invalid password"
        (facts "it return false" 
          (ebay.helpers.esniper/edit invalid-password-user) => false))
      (facts "when passed an invalid user"
        (facts "it return false" 
          (ebay.helpers.esniper/edit invalid-user) => false))
      (facts "when passed a non existing user"
        (facts "it return false" 
          (ebay.helpers.esniper/edit updated-user) => false)))
    (facts "about delete"
      (facts "when passed a user"
        (facts "it return true on success" 
          (do 
            (ebay.helpers.esniper/save default-user default-item)
            (ebay.helpers.esniper/delete default-user) => true))
        (facts "it return false if file does not exists" 
          (ebay.helpers.esniper/delete non-existing-user) => false)
        (facts "it deletes the the user directory and all the files" 
          (let [user default-user item default-item 
                user-path (ebay.helpers.esniper/save user)  
                path (ebay.helpers.esniper/save user item)  
                success (ebay.helpers.esniper/delete user)]
                  (.exists (clojure.java.io/as-file user-path)) => false)))
      (facts "when passed an invalid user"
        (facts "it return false" 
          (ebay.helpers.esniper/delete invalid-user) => false))
      (facts "when passed a user & an item"
        (facts "it return true on success" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item) ] 
                (ebay.helpers.esniper/delete user item) => true))
        (facts "it return false if file does not exists" 
          (let [user default-user item default-item 
                path (str "/tmp/esniper/auctions/" username-digest "/items/10.txt") ] 
                (ebay.helpers.esniper/delete user item) => false))
        (facts "it deletes the file" 
          (let [user default-user item default-item 
                path (ebay.helpers.esniper/save user item)  
                success (ebay.helpers.esniper/delete user item)]
                  (.exists (clojure.java.io/as-file path)) => false))))))
