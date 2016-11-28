(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [cljs.pprint :refer [pprint]]
            [cljs.spec :as s]
            [hyperlexia.data :refer [likes]]
            [rum.core :as rum :refer [defc]])
  (:require-macros
   [devcards.core :as dc :refer [defcard deftest]]))

;; Things that may be identified.
;; Option 1: Require an id key.
;; (s/def ::item (s/keys :req [::id]))
;; Option 2: Require that =id= maybe be called on it.
;; (s/def ::item #(id %))

;; Things resembling bookmarks.
(s/def ::href (s/and string? #(re-matches #"https?://.*" %)))
(s/def ::pin (s/keys :req [::href]))

(defc spec-href [href]
  [:p (str (s/valid? ::href href) )])

(defcard specify-href-false
  (spec-href "hi"))

(defcard specify-href-true
  (spec-href "https://twitter.com/"))

(s/def ::tags (s/or :string string? :set set?))
(s/def ::tagged (s/and (s/keys :req [::tags])
                       #(apply (complement empty?) [(:tags %)])))

;; Things true about tweets.
(def tweet-regex #"https?://twitter.com/(\w+)/status/(\d+)")
(s/def ::tweet-url (s/and string? #(re-matches tweet-regex %)))
(s/def ::tweet (s/and ::pin #(s/valid? ::tweet-url (:href %))))

(enable-console-print!)

;; Items
;; -----
;; Items can be identified.
;; id tweet -> [status (re-matches...)]
;; id pin -> :hash %
;; id file -> sha1 content

;; Tweets
;; ------
;; Tweets are items whose :href conforms to ::tweet-url.

;; This =conform= idea was a sort of fakey typing but I think you should re-write this to use your specs.

(def example-tweet-pin
  {:id 803256287622549504
   :href "https://twitter.com/zeynep/status/803256287622549504"})

(defcard specification
  (defc specification-of-pin []
    [:p (str (s/valid? ::pin example-tweet-pin) )]))

(defn conform [pin]
  (let [href (:href pin)
        regex #"https://twitter.com/(\w+)/status/(\d+).*"]
    (if-let [matches (first (re-seq regex href))]
      (assoc pin :type :tweet :id (nth matches 2) :user (nth matches 1))
      (assoc pin :type :link))))

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

