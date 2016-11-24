(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [rum.core :as rum :refer [defc]])

  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

;; Let's Use Devcards
;; ==================
;; Here's an example of a devcard with Sablono:

(defcard first-card
  (sab/html [:div
             [:h1 "This is your first devcard!"]]))

;; Can you replicate that with rum?

(defcard rum-card
  (rum/defc label [text]
    [:div [:h1 "This is your first devcard in rum"]]))

;; That's a little dumb. You're gonna be doing what,
;; (defcard name
;;    (rum/defc name [args]
;;       body))

;; Then again, will that do name conflict? 

(defcard named-card
  (rum/defc named-card [text]
    [:div [:h1 "This might name conflict?"]]))

;; It does not name-conflict. Cool. Fix a devcards template with a different macro?

(comment
  (defmacro defc
    "Coupling the defcard macro with the rum defcomponent macro. Is that dumb?"
    [name args body]
    `(defcard ~name
       (rum/defc ~name [~args]))
    ))

;; Yeah idk from macros. Maybe later. For now, just type the name twice. Don't be a wus.

;; Future User Experience
;; ======================

;; Well you'll want to say howdy.

(rum/defc welcome [name]
  [:div [:h2 "good morning, " name]])

(defcard welcome-2-card
  (welcome "bob"))


;; Let's do tweets
;; ===============

(defc tweet [{:keys [user id]}]
  "A simple view of a tweet that you can click and read using usual browser, and that you can add tags to."
  [:div.tweet
   [:span.user user]
   [:span.tweet-link [:a {:href (str "https://twitter.com/" user "/status/" id)} "follow link"]]
   [:span.tag-field "first tag, second tag"]])

;; https://twitter.com/ekstasis/status/801004674035970048

(defcard tweet-card
  (tweet {:id "801004674035970048" :user "ekstasis"}))

(defcard chip
  (mdl/chip (mdl/chip-text "Basic Chip")))

(mdl/chip (mdl/chip-text "Basic Chip"))

(defcard asdf (mdl/chip {:mdl [:deletable]} (mdl/chip-text "Deletable Chip")
                        (mdl/chip-action :button {:type "button"} (mdl/icon "cancel"))))

(mdl/button-chip (mdl/chip-text "Button Chip"))



;; Main
;; ====
(defn main []
  ;; conditionally start the app based on whether the #main-app-area
  ;; node is on the page
  (if-let [node (.getElementById js/document "main-app-area")]
    (.render js/ReactDOM (sab/html [:div "This is working"]) node)))

(main)

;; remember to run lein figwheel and then browse to
;; http://localhost:3449/cards.html

