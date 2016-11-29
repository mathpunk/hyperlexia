(ns hyperlexia.core
  (:require [sablono.core :as sab :include-macros true]
            [rum.mdl :as mdl]
            [cljs.pprint :refer [pprint]]
            [cljs.test :refer [testing is]]
            [cljs.spec :as s]
            [hyperlexia.items :as items]
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

(def tweet-regex #"https?://twitter.com/(\w+)/status/(\d+)")
(s/def ::tweet-href (s/and string? #(re-matches tweet-regex %)))

(defc spec-tweet-href [href]
  [:p (str (s/valid? ::tweet-href href))])

(defcard spec-tweet-href-false
  (spec-tweet-href "hi"))

(defcard spec-tweet-href-true
  (spec-tweet-href "https://twitter.com/zeynep/status/803256287622549504"))

(s/def ::tags (s/or :string string? :set set?))
(s/def ::tagged (s/and (s/keys :req [::tags])
                       #(apply (complement empty?) [(:tags %)])))

;; Things true about tweets.
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


;; DEFTESTING
;; =======
(deftest first-testers
  "## This is documentation
   It should work well"
  (testing "good stuff"
    (is (= (+ 3 4 55555) 5) "Testing the adding")
    (is (= (+ 1 0 0 0) 1) "This should work")
    (is (= 1 3))              
    (is false)
    (is (throw "heck"))
    (is (js/asdf)))
  "## And here is more documentation"
  (testing "bad stuff"
    (is (= (+ 1 0 0 0) 1))        
    (is (= (+ 3 4 55555) 4))
    (is false)
    (testing "mad stuff"
      (is (= (+ 1 0 0 0) 1))        
      (is (= (+ 3 4 55555) 4))
      (is false))))




(def example-tweet-pin
  {:id 803256287622549504
   :href "https://twitter.com/zeynep/status/803256287622549504"})


(defn conform [pin]
  (let [href (:href pin)
        regex #"https://twitter.com/(\w+)/status/(\d+).*"]
    (if-let [matches (first (re-seq regex href))]
      (assoc pin :type :tweet :id (nth matches 2) :user (nth matches 1))
      (assoc pin :type :link))))

#_(defc tweet [pin]
  [:li (:user pin ) " | " [:a {:href (:href pin)} "tweet"] " | " " { first tag, second }" ]
  )

(defcard tweet-fields
  "At least, maybe"
{ :timestamp "2016-11-11"
          :user "MadeUpHuman"
          :id 1234123412341234
          :type :tweet
          :href "https://twitter.com/MadeUpMan/status/1234123412341234" } )



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

