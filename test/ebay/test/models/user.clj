(ns ebay.test.models.user
  (:use midje.sweet)
  (:import ebay.models.user.User)
  (:require [ebay.models.user]))

(facts "about the user model"
  (let [user (User. "username" 8 "password")]
    (facts "it stores the username"
      (:username user) => "username")
    (facts "it stores the user-id"
      (:user-id user) => 8)
    (facts "it stores the password"
      (:password user) => "password")))
