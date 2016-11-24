(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [cljs.pprint :refer [ pprint ]]
            [rum.core :as rum :refer [defc]])

  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

;; Devcard Example
;; ===============
(defc label [text]
  [:div [:h1 "This example card has the text, \"" text "\""]])

(defcard label-card
  (label "Hello, Whirled"))

;; Components
;; ==========

;; MDL
;; ---
(defcard
  "## Material Design Lite
  I like things to look non-terrible, even though the structure is really what matters here. I don't mind that look being someone else's, because looks can be tweaked later. I'd like to get Material Design working.

  The chips from MDL look like a pretty standard tag. Let's try them.")

;; spec tag is-a set of strings

(defc tag-chip [name]
  (mdl/chip (mdl/chip-text name)))

(defcard tag-chip-card
  (tag-chip "research topic"))

(defcard
  "Alas... the component appears in the card, but is none of the material design styling, despite me including it into the index page in ROOT/resources.")

(defc deletable-tag-chip [name]
  (mdl/chip {:mdl [:deletable]} (mdl/chip-text name)
            (mdl/chip-action :button {:type "button"} (mdl/icon "cancel"))))

(defcard deletable-tag-chip-card
  (deletable-tag-chip "ill-tagged"))

(defcard "Frag. Well, just sketch in pure structure for now and sort out the style later. ")

;; Tweets
;; ------
(defcard "## Tweets")

(defn destructure-tweet [href]
  (let [regex #"https://twitter.com/(\w+)/status/([0-9]+).*"
        matches (first (re-seq regex href))]
    (println matches)
    {:type :tweet
     :id (nth matches 2)
     :user (nth matches 1)}))

(defcard destructure-card
  (destructure-tweet "https://twitter.com/ekstasis/status/801004674035970048"))

;; (defc tweet [{:keys [user id]}]
;;   "A simple view of a tweet that you can click and read using usual browser, and that you can add tags to."
;;   [:div.tweet
;;    [:span.user user]
;;    [:span.tweet-link [:a {:href (str "https://twitter.com/" user "/status/" id)} "follow link"]]
;;    [:span.tag-field "first tag, second tag"]])

;; ;; https://twitter.com/ekstasis/status/801004674035970048

;; (defcard tweet-card
;;   (tweet {:id "801004674035970048" :user "ekstasis"}))

;; Morning message

;; Morning structure

;; Evening message

;; Evening structure




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

