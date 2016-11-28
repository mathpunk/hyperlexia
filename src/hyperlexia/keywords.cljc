(ns hyperlexia.keywords
  (:require #?(:clj [clojure.spec :as s]
               :cljs [cljs.spec :as s])))

(s/def ::keyword string?)
(s/def ::keywords (s/and set? (s/coll-of ::keyword)))
(s/def ::topic ::keywords)
(s/fdef down-set
        :args (s/or ::keyword ::keywords)
        :ret  set?
        :fn #( true ;; goal: arg is subset of ret
              ))
(s/fdef down-set
        :args (s/or ::keyword ::keywords)
        :ret  set?
        :fn #( true ;; goal: arg is superset of every item in ret
              ))

;; (s/def ::concept ,,,)

