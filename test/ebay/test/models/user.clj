(ns ebay.test.models.user
  (:use midje.sweet)
  (:require [ebay.models.user]))

(def default-user (ebay.models.user/map->User {:username "username" :password "password" :user-id 8}))

(facts "about the user model"
  (let [user default-user]
    (facts "it stores the username"
      (:username user) => "username")
    (facts "it stores the user-id"
      (:user-id user) => 8)
    (facts "it stores the password"
      (:password user) => "password")))
