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

(def my-creds (let [get-env (fn [name] (System/getenv name))
                    key (get-env "TWITTER_CONSUMER_KEY")
                    secret (get-env "TWITTER_CONSUMER_SECRET")
                    token (get-env "TWITTER_ACCESS_TOKEN")
                    secret-token (get-env "TWITTER_ACCESS_TOKEN_SECRET")]
                (make-oauth-creds key secret token secret-token)))

(defn tweet-by-id [id]
  (let [data (statuses-show {:id id :callback (println "hi")})]
    data))

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
