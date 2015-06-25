(ns ebay.helpers.esniper
  (:require [ebay.models.config]
            [ebay.models.user]
            [ebay.models.item]
            [digest])
  (:use [clojure.java.io]
        [clojure.java.shell :only [sh]]))

(def ^:private base-dir (:auctions-path (ebay.models.config/default-config)))


(defn- mkdirp [path]
  (let [dir (java.io.File. path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn- delete-recursively [directory]
    (if (= (:exit (sh  "rm" "-r" directory)) 0) true false))


(defn- base-dir-for-user [{username :username}]
  (str base-dir (digest/md5 username) "/"))

(defn- base-dir-for-user-items [user]
  (str (base-dir-for-user user) "items/"))

(defn- file-path 
  ([user]
    (let [directory (base-dir-for-user user)
          path (str directory "config.txt")]
      {:directory directory :path path}))
  ([user {item-id :item-id}]
    (let [directory (base-dir-for-user-items user)
          path (str directory item-id ".txt")]
      {:directory directory :path path})))

(defn- write-config-file [directory path config-file]
  (do 
    (mkdirp directory)
    (with-open [wrtr (writer path)] 
      (.write wrtr config-file)))
  path)

(defn- valid-user? [{username :username password :password}]
  (not (or (empty? password) (empty? username))))

(defn- user-exists? [user]
  (.exists (as-file (base-dir-for-user user))))

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
    (if (valid-user? user)
      (let [config-file (config-file-for user)
           {:keys [directory path]} (file-path user)]
        (write-config-file directory path config-file)) false))
  ([user item]
    (if (valid-user? user)
      (let [config-file (config-file-for item)
           {:keys [directory path]} (file-path user item)]
        (write-config-file directory path config-file)) false)))

(defn delete
  "Removes user or items config"
  ([user]
    (if (and (valid-user? user) (user-exists? user))
      (let [path (base-dir-for-user user)]
          (delete-recursively path)) false))
  ([user item]
    (if (and (valid-user? user) (user-exists? user))
      (let [path (:path (file-path user item))]
        (when (.exists (as-file path))
          (delete-file path true))) false)))

(defn edit
  "Edits the user or items config"
  ([user]
   (if (user-exists? user)
     (do
       (delete user)
       (save user)) false))
  ([user item]
    (if (user-exists? user)
      (do
        (delete user item)
        (save user item)) false)))
