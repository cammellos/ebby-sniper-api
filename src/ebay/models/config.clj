(ns ebay.models.config)

(defrecord Config [root-path auctions-path database-path])

(defn build-config 
  ([] (build-config {}))
  ([{:keys [root-path auctions-path database-path] :or {root-path "/tmp/esniper/"}}]
    (map->Config {
      :root-path root-path
      :auctions-path (or auctions-path (str root-path "auctions/"))
      :database-path (or database-path (str root-path "database/"))})))

(defn default-config [] (build-config))
