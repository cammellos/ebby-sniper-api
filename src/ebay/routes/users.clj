(ns ebay.routes.users
  (:require 
    [ebay.models.user]
    [ebay.services.sniper]
    [compojure.core :refer [defroutes GET POST PUT DELETE]]
    [ring.util.http-response :refer [ok]]))


(defroutes users-routes
  (POST "/users" [username password] 
    (let [user (ebay.models.user/map->User {:username username :password password})]
      (cond 
        (ebay.services.sniper/invalid? user) {:status 406}
        (ebay.services.sniper/exists? user) {:status 200 :body (str "/users/" username)}
        (ebay.services.sniper/save user) {:status 201 :body (str "/users/" username)}
        :else {:status 500})))
  (PUT "/users/:username" [username password] 
    (let [user (ebay.models.user/map->User {:username username :password password})]
      (cond
        (not (ebay.services.sniper/exists? user)) {:status 404}
        (ebay.services.sniper/edit user) {:status 200}
        :else {:status 406})))
  (DELETE "/users/:username" [username] 
    (let [user (ebay.models.user/map->User {:username username})]
      (cond
        (not (ebay.services.sniper/exists? user)) {:status 404}
        (ebay.services.sniper/delete user) {:status 200}))))




