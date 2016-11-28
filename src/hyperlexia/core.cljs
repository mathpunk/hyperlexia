(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [cljs.pprint :refer [pprint]]
            [cljs.spec :as s]
            [hyperlexia.data :refer [likes]]
            [rum.core :as rum :refer [defc]])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

(enable-console-print!)

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

(defn conform [pin]
  (let [href (:href pin)
        regex #"https://twitter.com/(\w+)/status/(\d+).*"]
    (if-let [matches (first (re-seq regex href))]
      (assoc pin :type :tweet :id (nth matches 2) :user (nth matches 1))
      (assoc pin :type :link))))

;; I'm thinking that really I want to be using single and aggregate types of material design components, rather than raw ul's and stuff. But why isn't my Material getting required correctly?

;; Let's redefine tweets to be more like text to recognize that your html is no good right now. 

(defc tweet [pin]
  [:li (:user pin ) " | " [:a {:href (:href pin)} "tweet"] " | " " { first tag, second }" ]
  )

(defcard tweeeeeeeeeeeeeeeet
  [:ul (tweet { :timestamp "2016-11-11"
          :user "MadeUpHuman"
          :id 1234123412341234
          :type :tweet
          :href "https://twitter.com/MadeUpMan/status/1234123412341234" }) ])


(defcard tweeeeeeeeeeeeeet
  (defc hi [wat] [:ul  [:li "hey"]] ))



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

