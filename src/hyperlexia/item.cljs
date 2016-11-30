(ns hyperlexia.item
  (:require [cljs.spec :as s]
            [cljs.test :refer [testing is]])
  (:require-macros [devcards.core :as dc :refer [deftest]]))

;; Identifiable
(defprotocol Identifiable (id [x] "Return a context-appropriate unique identifier."))

(s/def ::item (s/or :has-key (s/keys :req-un [::id]) :has-fn #(satisfies? Identifiable %)))

(s/def ::href (s/and string? #(re-matches #"https?://.*" %)))
(s/def ::pin (s/keys :req [::href]))
