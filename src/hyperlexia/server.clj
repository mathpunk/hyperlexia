(ns hyperlexia.server
  (:require [compojure.core :refer :all]
            [compojure.route :as route]))

(defroutes handler
  (GET "/hello" [] "<h1>Hello World</h1>")
  (route/not-found "<h1>Page not found</h1>"))
