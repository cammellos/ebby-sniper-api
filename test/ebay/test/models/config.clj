(ns ebay.test.models.config
  (:use midje.sweet)
  (:use ebay.models.config))

(facts "about config" 
  (facts "about default config" 
    (facts "the default root-path is /tmp/esniper"
        (:root-path (default-config)) => "/tmp/esniper/")
    (facts "the default auctions-path is /tmp/esniper/auctions/"
        (:auctions-path (default-config)) => "/tmp/esniper/auctions/")
    (facts "the default database-path is /tmp/esniper/database/"
        (:database-path (default-config)) => "/tmp/esniper/database/"))

  (facts "about build-config config" 
    (facts "when root path is overridden"
      (let [config (build-config {:root-path "/home/app/"})]
        (facts "it sets the root path"
          (:root-path config) => "/home/app/")
        (facts "it sets the auction path accordingly"
          (:auctions-path config) => "/home/app/auctions/")
        (facts "it sets the database-path accordingly"
          (:database-path config) => "/home/app/database/")))))
