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

;; Unnecessary? (s/def ::tweet-url #(re-matches #"https?://twitter.com/\w+/status/\d+" %))

(defn conform [pin]
  (let [href (:href pin)
        regex #"https://twitter.com/(\w+)/status/(\d+).*"]
    (if-let [matches (first (re-seq regex href))]
      (assoc pin :type :tweet :id (nth matches 2) :user (nth matches 1))
      (assoc pin :type :link))))

(defc tweet [{:keys [user id]}]
  "A simple view of a tweet that you can click and read using usual browser, and that you can add tags to."
  [:div.tweet
   [:span.user user] " | "
   [:span.tweet-link [:a {:href (str "https://twitter.com/" user "/status/" id)} "tweet"]] " | " "{ "
   [:span.tag-field "first tag, second tag"] " }"])

(defcard tweet
  (tweet { :timestamp "2016-11-11"
           :user "MadeUpHuman"
           :id 1234123412341234
           :type :tweet
           :href "https://twitter.com/MadeUpMan/status/1234123412341234" }))

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
  (str (conform (first likes))))

(defcard destr-likes
  (str (map conform (take 10 likes))))

(def some-likes (map conform (take 10 likes)))

(defcard a-tweet
  (do
    (tweet (first some-likes))))



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

