(ns ebay.services.sniper
  (:require [ebay.helpers.esniper]))

(defmacro build-function [action]
  `(defn ~action
    ([user#]
      (boolean ((intern 'ebay.helpers.esniper '~action) user#)))
    ([user# item#]
      (boolean ((intern 'ebay.helpers.esniper '~action) user# item#)))))

(build-function save)
(build-function delete)
(build-function edit)
(build-function exists?)
(build-function valid?)

(defn invalid?
  ([user]
    (not (valid? user)))
  ([user item]
    (not (valid? user item))))

