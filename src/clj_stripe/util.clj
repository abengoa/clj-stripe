;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.util
	(:require [clj-http.client :as client]
		[clojure.contrib.json :as json])
)

(defn keys-2-strings [km] (reduce (fn [m [k v]] (assoc m (name k) v)) {} km))

(defn remove-nulls [m] (into {} (remove (comp nil? second) m)))

(defn merge-maps
  [& maps]
  (remove-nulls (reduce into {} maps))
  )

(defn append-param
  [s name value]
  (if value
    (let [print-param (fn [n v] (str n "=" v))]
    (if (and s (> (count s) 0))
      (str s "&" (print-param name value))
      (print-param name value)))
    s))

(defn url-with-optional-params
  [url m [& param-names]]
  (let [params-str (reduce #(append-param %1 %2 (get m %2 nil)) nil param-names)]
    (str url (if params-str (str "?" params-str) ""))
    )
  )

(defn post-request
	[token url params]
		(try
		(let [result (client/post url {:basic-auth [token] :query-params params :throw-exceptions false})]
			(json/read-json (:body result))
		)
		(catch java.lang.Exception e e))
	)

(defn get-request
	[token url]
		(try
		(let [result (client/get url {:basic-auth [token] :throw-exceptions false})]
			(json/read-json (:body result))
		)
		(catch java.lang.Exception e e))
	)

(defn delete-request
	[token url]
		(try
		(let [result (client/delete url {:basic-auth [token] :throw-exceptions false})]
			(json/read-json (:body result))
		)
		(catch java.lang.Exception e e))
	)
