(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [cljs.pprint :refer [ pprint ]]
            [cljs.spec :as s]
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
  "Alas... the text shows, but there is none of the material design styling. I included it in $project_root/resources, in the index file. I'm not sure what's going on.")

(defc deletable-tag-chip [name]
  (mdl/chip {:mdl [:deletable]} (mdl/chip-text name)
            (mdl/chip-action :button {:type "button"} (mdl/icon "cancel"))))

(defcard deletable-tag-chip-card
  (deletable-tag-chip "ill-tagged"))

(defcard "Let's sketch in the structure, anyway.")


;; Tweets
;; ------
(defcard "# The Data \n## Tweets & Pins

I've been chucking data into a pinboard.in account for ages. Nearly all of what is in there is tweets, and I haven't really taken notes in it. So the important fields of an object of type pinboard are the timestamp, the tags, the href.")

;; given an object from pinboard,
;; dissoc all but tags, timestamp
;; assoc :type :pin

(defcard "More than 9 out of 10 links in there is just some damn tweet.")

(s/def ::tweet #(re-matches #"https?://twitter.com/\w+/status/\d+" %))

(defn tweet? [pin]
  (let [href (:href pin)]
    (s/valid? ::tweet href)))

(defn type-pin [pin]
  (if (tweet? pin)
    (assoc pin :type :tweet)
    (assoc pin :type :link)))

(defmulti destructure (fn [p] (:type p)))

(defmethod destructure :tweet [pin]
  (let [regex #"https://twitter.com/(\w+)/status/(\d+).*"
        matches (first (re-seq regex (:href pin)))]
    (assoc pin :id (nth matches 2) :user (nth matches 1))))

(defc tweet [{:keys [user id]}]
  "A simple view of a tweet that you can click and read using usual browser, and that you can add tags to."
  [:div.tweet
   [:span.user user]
   [:span.tweet-link [:a {:href (str "https://twitter.com/" user "/status/" id)} "follow link"]]
   [:span.tag-field "first tag, second tag"]])

(defcard tweet
  (tweet (destructure {
                       :timestamp "2016-11-11"
                       :user "MadeUpMan"
                       :id 1234123412341234
                       :type :tweet
                       :href "https://twitter.com/MadeUpMan/status/1234123412341234"
                       })))

(defcard "That's appalling for a number of reasons.")

;; Collections of tweets
;; ---------------------

;; You can map this tweet function of a seq of tweet strings. But, how do you get data into the app in the first place? Right now it's just in resources/data, in a text file.




;; Contexts
;; --------

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

