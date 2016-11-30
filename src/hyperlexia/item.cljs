(ns hyperlexia.item
  (:require [cljs.spec :as s]
            [cljs.test :refer [testing is]])
  (:require-macros [devcards.core :as dc :refer [defcard deftest]]))

;; Identifiable
(defprotocol Identifiable (id [x] "Return a context-appropriate unique identifier."))

(s/def ::item (s/or :has-key (s/keys :req-un [::id]) :has-fn #(satisfies? Identifiable %)))

(s/def ::href (s/and string? #(re-matches #"https?://.*" %)))
(s/def ::pin (s/keys :req-un [::href]))

(deftest href-specification
  (testing "string URLs"
    (is (s/valid? ::href "https://twitter.com"))
    (is (s/valid? ::href "http://google.com"))
    (is (not (s/valid? ::href "hi there")))))

(deftest pin-specification
  (testing "simple pin object"
    (is (s/valid? ::pin {:href "https://twittre.com"}))
    (is (not (s/valid? ::pin {})))
    (is (not (s/valid? ::pin {:href "clik me"})))))

(def tweet-regex #"https?://twitter.com/(\w+)/status/(\d+)")
(s/def ::tweet-url (s/and string? #(re-matches tweet-regex %)))

(s/def ::tweet (s/and ::pin #(s/valid? ::tweet-url (:href %))))

(deftest twitter-href-specification
  (testing "twitter URLs"
    (is (s/valid? ::tweet-url "https://twitter.com/mathpunk/status/1234123412341234"))
    (is (not (s/valid? ::tweet-url "hi there")))
    (is (not (s/valid? ::tweet-url "https://thenewyorktimes.com")))))

(deftest twitter-pin-specification
  (testing "simple objects with and without twitter URLs"
    (is (s/valid? ::tweet {:href "https://twitter.com/mathpunk/status/1234123412341234"}))
    (is (not (s/valid? ::tweet {:freetext "some words"})))
    (is (not (s/valid? ::tweet {:href "https://gogole.com"})))
    ))

(defn href->tweet [href]
  (let [regex #"https://twitter.com/(\w+)/status/(\d+).*"]
    (if-let [matches (first (re-seq regex href))]
      (let [id (nth matches 2)
            user (nth matches 1)]
        {:href href
         :user user
         :id id})
      {:href href})))

  (deftest tweet-destructuring
    (testing "url -> tweet with data"
      (let [href "https://twitter.com/mathpunk/status/1234123412341234"
            tweet (href->tweet href)]
        (is (= "mathpunk" (:user tweet)))
        (is (= "1234123412341234" (:id tweet))))))
