(ns ebay.routes.items
  (:require [ebay.layout :as layout]
            [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn index [])

(defn create [])

(defroutes items-routes
  (GET "/" [] (index))
  (POST "/" [] (create)))

