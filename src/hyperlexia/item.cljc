(ns hyperlexia.item
  (:require [clojure.spec :as s]))

;; Things that may be identified.
(s/def ::item (s/keys :req [::id]))

;; Things resembling bookmarks.
(s/def ::pin (s/and ::item (s/keys :req [::href])))
(s/def ::tags (s/or :string string? :set set?))
(s/def ::tagged (s/and (s/keys :req [::tags])
                      #(apply (complement empty?) [(:tags %)])))

;; Things true about tweets.
(def tweet-regex #"https?://twitter.com/(\w+)/status/(\d+)")
(s/def ::tweet-url (s/and string? #(re-matches tweet-regex %)))
(s/def ::tweet (s/and ::pin #(s/valid? ::tweet-url (:href %))))

;; We may do files later.
;; (s/def ::path #?(:clj #(clojure.java.io/as-relative-path %)
;;                  :cljs string?))
;; (s/def ::file (s/keys (s/or ::path
;;                             (s/and vector? (s/coll-of ::path)))))
