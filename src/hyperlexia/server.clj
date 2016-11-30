(ns hyperlexia.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defroutes handler
  (GET "/tweet/:id" [id] (str "tweet no. " id))
  (route/not-found "<h1>Page not found</h1>"))
