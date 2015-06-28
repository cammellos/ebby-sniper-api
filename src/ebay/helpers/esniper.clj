(ns ebay.helpers.esniper
  (:require [ebay.models.config]
            [ebay.models.user]
            [ebay.models.item]
            [digest])
  (:use 
        [ebay.helpers.filesystem]
        [clojure.java.io]))

(def ^:private base-dir (:auctions-path (ebay.models.config/default-config)))

(defn- base-dir-for 
  ([{username :username}]
    (str base-dir (digest/md5 username) "/"))
  ([user item]
    (str (base-dir-for user) "items/")))

(defn- file-path 
  ([user]
    (let [directory (base-dir-for user)
          path (str directory "config.txt")]
      {:directory directory :path path}))
  ([user {:keys [item-id] :as item}]
    (let [directory (base-dir-for user item)
          path (str directory item-id ".txt")]
      {:directory directory :path path})))

(defn- write-config-file [directory path config-file]
  (do 
    (mkdirp directory)
    (with-open [wrtr (writer path)] 
      (.write wrtr config-file)))
  path)

(defn exists? 
  ([user]
  (.exists (as-file (base-dir-for user))))
  ([user item]
  (and (exists? user) (.exists (as-file (:path (file-path user item)))))))

(defn- with-existing [elements function]
  (cond 
    (every? exists? elements) (function)
    :else false))

(defn valid? 
  ([{username :username password :password}]
    (and (not-empty password) (not-empty username)))
  ([user {item-id :item-id price :price}]
   (and (valid? user) (and (not-empty item-id) (and (number? price) (pos? price))))))

(defn- with-valid [elements function]
  (cond 
    (apply valid? elements) (function)
    :else false))


(defmulti  #^{:private true} config-file-for (fn [object] (class object)))

(defmethod config-file-for ebay.models.user.User
  [{:keys [username password]}]
  (str "username = " username "\n" "password = " password "\n" "seconds = 10" ))

(defmethod config-file-for ebay.models.item.Item
  [{:keys [item-id price]}]
  (str item-id " " price "\n"))


(defn save
  "Saves user or items to an esniper config file"
  ([user]
    (with-valid [user]
      #(let [config-file (config-file-for user)
           {:keys [directory path]} (file-path user)]
        (write-config-file directory path config-file))))
  ([user item]
    (with-valid [user item]
      #(let [config-file (config-file-for item)
           {:keys [directory path]} (file-path user item)]
        (write-config-file directory path config-file)))))


(defn delete
  "Removes user or items config"
  ([user]
    (with-existing [user] #(delete-recursively (base-dir-for user))))
  ([user item]
    (with-existing [user item] #(delete-file (as-file (:path (file-path user item))) true))))

(defn edit
  "Edits the user or items config"
  [& args]
   (with-existing args
     #(do (apply delete args) (apply save args))))

