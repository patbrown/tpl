(ns baby.pat.tpl
  (:require [orchestra.core :refer [defn-spec]]
            [baby.pat.jes.vt :as vt]
            [baby.pat.jes.vt.util :as u] 
            [selmer.parser]))

(defn-spec <-tpl ::vt/str
  "Atoms get deref before processing.    
   Keywords become paths, before tpls.   
   Strings are either paths or already a known template."
  ([template-or-id ::vt/atom-kw-or-str] (<-tpl "" template-or-id))
  ([prefix ::vt/str template-or-id ::vt/atom-kw-or-str]
   (let [template-or-id (u/if-atom-deref template-or-id)
         name-portion (when (keyword? template-or-id)
                        (if (qualified-keyword? template-or-id)
                          (u/qkw->relative-path template-or-id)
                          (name template-or-id)))]
     (if (keyword? template-or-id)
       (slurp (str prefix name-portion))
       (if (empty? (selmer.parser/known-variables template-or-id))
         (slurp (str prefix template-or-id))
         template-or-id)))))

(defn-spec <-tpl-keys ::vt/any
  "Returns the known keys from template."
  ([template-or-id ::vt/atom-kw-or-str] (<-tpl-keys "" template-or-id))
  ([prefix ::vt/str template-or-id ::vt/atom-kw-or-str]
   (selmer.parser/known-variables (<-tpl prefix (u/if-atom-deref template-or-id)))))

(defn-spec render ::vt/str
  "Renders templates with replacement values. Does it's best."
  ([tpl ::vt/atom-kw-or-str replacements ::vt/atom-kw-map-or-str] (render "" tpl replacements))
  ([prefix ::vt/str tpl ::vt/atom-kw-or-str replacements ::vt/atom-kw-map-or-str]
   (let [access-fn (fn [path] (-> path slurp clojure.edn/read-string))
         tpl (u/if-atom-deref tpl)
         replacements (u/if-atom-deref replacements)
         replacements (if (map? replacements) replacements
                          (if (string? replacements)
                            (access-fn replacements)
                            (access-fn (u/qkw->relative-path replacements))))]
     (selmer.parser/render (if (string? tpl)
                           ;; string execution
                             (if (empty? (selmer.parser/known-variables tpl))
                               (<-tpl prefix tpl)
                               tpl)
                             ;; kw execution
                             (<-tpl prefix tpl)) replacements))))

(comment

;;(render "Hello {{name}}!" {:name "Angelica"})
;; (spit "delete-me"  "Hello {{name}}!")

;; (render :delete-me {:name "Rita"})
;; (spit "bin/resources/delete-me"  "Hello {{name}}!")
;; (spit "bin/use-me" {:name "frank"})
;; (render :bin.resources/delete-me {:name "Lila"})
;; (render :bin.resources/delete-me "bin/use-me")
;; (render :bin.resources/delete-me :bin/use-me)
;; (render "Hello {{name}}!" :bin/use-me)
;; (render "bin/resources/delete-me" "bin/use-me")
  
  ;
  )
