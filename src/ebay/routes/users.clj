(ns ebay.routes.users
  (:require 
    [ebay.models.user]
    [ebay.services.sniper]
    [compojure.core :refer [defroutes GET POST PUT DELETE]]
    [ring.util.http-response :refer [ok]]))


(defroutes users-routes
  (POST "/users" [username password] 
    (if (ebay.services.sniper/save (ebay.models.user/map->User {:username username :password password})) {:status 201 :body (str "/users/" username)} {:status 406}))
  (PUT "/users/:username" [username password] 
    (if (ebay.services.sniper/edit (ebay.models.user/map->User {:username username :password password})) {:status 200} {:status 406}))
  (DELETE "/users/:username" [username] 
    (if (ebay.services.sniper/delete (ebay.models.user/map->User {:username username})) {:status 200} {:status 404})))


