(ns ebay.services.sniper
  (:require [ebay.helpers.esniper]))

(defn save
  ([user]
    (boolean (ebay.helpers.esniper/save user)))
  ([user item]
    (boolean (ebay.helpers.esniper/save user item))))

(defn delete
  ([user]
    (ebay.helpers.esniper/delete user))
  ([user item]
    (ebay.helpers.esniper/delete user item)))

(defn edit 
  ([user]
    (boolean (ebay.helpers.esniper/edit user)))
  ([user item]
    (boolean (ebay.helpers.esniper/edit user item))))

(defn exists?
  ([user]
    (boolean (ebay.helpers.esniper/exists? user)))
  ([user item]
    (boolean (ebay.helpers.esniper/exists? user item))))

(defn valid?
  ([user]
    (boolean (ebay.helpers.esniper/valid? user)))
  ([user item]
    (boolean (ebay.helpers.esniper/valid? user item))))

(defn invalid?
  ([user]
    (not (valid? user)))
  ([user item]
    (not (valid? user item))))

