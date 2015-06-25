(ns ebay.test.support.helpers
  (:use [ midje.sweet]
        [clojure.java.shell :only [sh]]
        [clojure.java.io])
  (:require [ebay.helpers.esniper]
             [ebay.models.config]
             [ebay.models.item]
             [ebay.models.user]))


(def ^:private config (ebay.models.config/default-config))

(def ^:private base-dir (:auctions-path config))

(defn delete-recursively []
  (if (.exists (as-file base-dir))
    (sh  "rm" "-r" base-dir)))

