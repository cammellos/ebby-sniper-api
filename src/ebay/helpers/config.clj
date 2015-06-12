(ns ebay.helpers.config

  (:use clojure.java.io
        [clojure.java.shell :only [sh]]
))

(def base-dir "/tmp/esniper/")

(defn- mkdirp [path]
  (let [dir (java.io.File. path)]
    (if (.exists dir)
      true
      (.mkdirs dir))))


(defn- base-dir-for-user [{user-id :id :as user}]
  (str base-dir user-id "/"))

(defn- file-path [{user-id :id :as user} {item-price :price item-id :item-id :as item}]
  (let [ directory (base-dir-for-user user)
         path (str directory item-id ".txt")]
    {:directory directory :path path}))

(defn- generate-config-file [{:keys [item-id price]}]
  (str item-id " " price "\n"))

(defn delete-recursively []
  (if (.exists (as-file base-dir))
    (sh  "rm" "-r" base-dir)))

(defn save-item-to-file [user item]
  (let [ config-file (generate-config-file item)
        {:keys [directory path]} (file-path user item)]
    (do 
      (mkdirp directory)
      (with-open [wrtr (writer path)] (.write wrtr config-file))
      path)))

(defn delete-item-from-file [{user-id :id :as user} {item-price :price item-id :item-id :as item}]
  (delete-file (:path (file-path user item)) true))

