(ns ebay.helpers.filesystem
  (:use [clojure.java.io]
        [clojure.string]
        [clojure.java.shell :only [sh]]))

(defn mkdirp [path]
  (let [dir (java.io.File. path)]
    (when-not (.exists dir)
      (.mkdirs dir))))

(defn delete-recursively [directory]
    (if (= (:exit (sh  "rm" "-r" directory)) 0) true false))

(defn directory-from-path [path]
  (str (join (drop-last (split path #"/"))) "/"))


