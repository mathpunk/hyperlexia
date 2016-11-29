(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [cljs.test :refer [testing is]]
            [cljs.spec :as s]
            [hyperlexia.item :as item]
            [rum.core :as rum :refer [defc]])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (.render js/ReactDOM (sab/html [:div "This is working"]) node)))

(main)
