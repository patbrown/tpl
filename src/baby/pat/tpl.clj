(ns baby.pat.tpl
  (:require [orchestra.core :refer [defn-spec]]
            [baby.pat.vt :as vt] 
            [selmer.parser]))

(defn-spec <-tpl ::vt/str
  ([prefix ::vt/str template-id ::vt/kw]
   (let [name-portion (if (qualified-keyword? template-id)
                        (str (namespace template-id) "/" (name template-id))
                        (name template-id))]
     (slurp (str prefix name-portion)))))

(defn-spec <-tpl-vars ::vt/any
  ([prefix ::vt/str template-id ::vt/kw]
   (selmer.parser/known-variables (<-tpl prefix template-id))))

(defn-spec render ::vt/str
  ([prefix ::vt/str tpl ::vt/kw-or-str replacements ::vt/map]
   (selmer.parser/render (if (string? tpl)
                             tpl
                             (<-tpl prefix tpl)) replacements)))

(def tpl-functions {:tpl/<-tpl <-tpl
                    :tpl/<-tpl-vars <-tpl-vars
                    :tpl/render render})

(comment
  (render {:tpl/id :tpl/shell-command-generic-kaocha-invocation
           :kaocha-runner/file-name "sex" :test-config/id "love"})
  (render :tpl/shell-command-generic-kaocha-invocation {:kaocha-runner/file-name "sex" :test-config/id "love"})
;
  )
