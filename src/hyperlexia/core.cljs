(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [cljs.pprint :refer [ pprint ]]
            [cljs.spec :as s]
            [hyperlexia.data :refer [likes]]
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
  "Alas... the text shows, but there is none of the material design styling. I included it in $project_root/resources, in the index file. Why doesn't it show here?")

(defc deletable-tag-chip [name]
  (mdl/chip {:mdl [:deletable]} (mdl/chip-text name)
            (mdl/chip-action :button {:type "button"} (mdl/icon "cancel"))))

(defcard deletable-tag-chip-card
  (deletable-tag-chip "ill-tagged"))

(defcard "Note that the button is being made, but there's no style to it. Moving on...")


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

(defc tweet [pin]
  "A simple view of a tweet that you can click and read using usual browser, and that you can add tags to."
  (let [tweet (destructure pin)])
  [:div.tweet
   [:span.user (:user tweet)] " | "
   [:span.tweet-link [:a {:href (str "https://twitter.com/" (:user tweet) "/status/" (:id tweet))} "follow link"]] " | " "{ "
   [:span.tag-field "first tag, second tag"] " }"])

(defcard tweet
  (tweet (destructure {
                       :timestamp "2016-11-11"
                       :user "MadeUpHuman"
                       :id 1234123412341234
                       :type :tweet
                       :href "https://twitter.com/MadeUpMan/status/1234123412341234"
                       })))

(defcard "Now write new CSS styling rules to separate the information instead of typography. J/k! I'd rather use Material or React-Bootstrap components and I don't know how to interoperate with those. Let's stick with data.

## Viewing Collections
Let's assume we don't want to view everything; we'll take like 10 tweets, skipping non-tweets, and examine them.")

(defc tweets [v]
  [:ul (map #([:li (tweet %)]) (take 10 v))])

;; What's a dumb way to stick some data into the page? Maybe shove it into localStorage?

;; First, how even does localStorage work.

;; (.setItem js/localStorage
;;           "likes" "data/likes_late_november.txt")

;; (println (.getItem js/localStorage "likes"))

;; That does what I'd think. Only, I don't want the string "data/likes_late_november.txt", I want the resource there. Hm.

;; Well, the quickest and dumbest way I can think of is to include it in a file and either write functions or abuse the clojure reader. Hence, I wrote a data.cljs with the data in it, and we require it after transforming it into stuff like we'd expect to see kind of.

(defcard "What's our data look like?")

(defcard some-like
  (str (first likes) ))

(defcard destr-like
  (str (type-pin (first likes))))

(defcard destr-likes
  (str (map type-pin (take 10 likes))))

;; Hm, fakey types get me again. I'm not sure if I'm transforming my data at the right point.

(def some-likes (map type-pin (take 10 likes)))

(defcard a-tweet
  (do
    (println (first some-likes))
    (tweet (first some-likes))))

;; So there's a bug: how am I showing an href with empty stuff like this? Since the println shows the data looking pretty sweet, =tweet= is the only place the bug can be.

;; I'm like, destructuring wrong. And my functions are wrong. Refactor to correct I do believe. 


;; (defcard map-tweets
;;   (tweets (map type-pin likes)))



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

