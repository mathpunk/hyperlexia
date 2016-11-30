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
