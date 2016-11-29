(ns hyperlexia.item
  (:require [cljs.spec :as s]
            [cljs.test :refer [testing is]])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

;; Identifiable
(defprotocol Identifiable (id [x] "Return a context-appropriate unique identifier."))

(s/def ::item (s/or :has-key (s/keys :req-un [::id]) :has-fn #(satisfies? Identifiable %)))

(s/def ::href (s/and string? #(re-matches #"https?://.*" %)))
(s/def ::pin (s/keys :req [::href]))

(def tweet-regex #"https?://twitter.com/(\w+)/status/(\d+)")
(s/def ::tweet-url (s/and string? #(re-matches tweet-regex %)))

(s/def ::tweet (s/and ::pin #(s/valid? ::tweet-url (:href %))))

(deftest twitter-href-specification
  (testing "twitter URLs"
    (is (s/valid? ::tweet-url "https://twitter.com/zeynep/status/803256287622549504"))
    (is (not (s/valid? ::tweet-url "hi there")))
    (is (not (s/valid? ::tweet-url "https://thenewyorktimes.com")))))

(deftest tweet-destructuring
  (testing "url -> tweet with data"
    (let [href "https://twitter.com/mathpunk/status/123412341234"
          tweet (make-tweet href)]
      (is (= "zeynep" (:user tweet)))
      (is (= "803256287622549504" (:id tweet))))))
