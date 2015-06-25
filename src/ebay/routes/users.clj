(ns ebay.routes.users
  (:require 
    [compojure.core :refer [defroutes GET POST PUT DELETE]]
    [ring.util.http-response :refer [ok]]))

(defn index [])

(defn create [])

(defroutes users-routes
  (GET "/" [] (index))
  (POST "/" [] (create)))

