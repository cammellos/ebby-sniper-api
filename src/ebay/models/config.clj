(ns ebay.models.config)

(defrecord Config [root-path auctions-path database-path])


(defn default-config []
  (map->Config {
     :root-path "/tmp/esniper/" 
     :auctions-path "/tmp/esniper/auctions/"
     :database-path "/tmp/esniper/database/"}))

(defn build-config [{:keys [root-path auctions-path database-path]}]
  (default-config))

