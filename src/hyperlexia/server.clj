(ns hyperlexia.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route])
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)))

(defn tweet-by-id [id]
  (str "tweet no. " id))

(defn some-faves [n]
  (str n " tweets"))

(defn user-by-name [name]
  (str "user " name))

(defroutes handler
  (GET "/twitter/:id" [id] (tweet-by-id id))
  (GET "/twitter/favorites/:n" [n] (some-faves n))
  (GET "/twitter/faves/:n" [n] (some-faves n))
  (GET "/twitter/likes/:n" [n] (some-faves n))
  (GET "/twitter/user/:name" [name] (user-by-name name))
  (route/not-found "<h1>Page not found</h1>"))
