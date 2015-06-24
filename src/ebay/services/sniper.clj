(ns ebay.services.sniper
  (:require [ebay.helpers.esniper]))

(defn add-user [user]
  (boolean (ebay.helpers.esniper/save user)))
(defn remove-user [user])
(defn get-user [user])
(defn edit-user [user])

(defn add-item [user item])
(defn remove-item [user item])
(defn get-items [user])
(defn edit-item [user item])
