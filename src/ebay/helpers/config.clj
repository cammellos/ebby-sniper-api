(ns ebay.helpers.config
  (:use clojure.java.io))

(defn mkdirp [path]
  (let [dir (java.io.File. path)]
    (if (.exists dir)
      true
      (.mkdirs dir))))

(defn generate-config-file [{:keys [item-id price]}]
  (str item-id " " price "\n"))

(defn save-item-to-file [{user-id :id :as user} {item-price :price item-id :item-id :as item}]
  (let [directory (str "/tmp/" user-id "/") 
        filename (str directory item-id ".txt") 
        config-file (generate-config-file item)]
    (do 
      (mkdirp directory)
      (with-open [wrtr (writer filename)] (.write wrtr config-file))
      filename)))
