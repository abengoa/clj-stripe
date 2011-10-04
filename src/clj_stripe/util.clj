(ns clj-stripe.util
	(:require [clj-http.client :as client]
		[clojure.contrib.json :as json])
)

(defn keys-2-strings [km] (reduce (fn [m [k v]] (assoc m (name k) v)) {} km))

(defn remove-nulls [m] (into {} (remove (comp nil? second) m)))

(defn append-param
  [s name value]
  (if value
    (let [print-param (fn [n v] (str n "=" v))]
    (if (and s (> (count s) 0))
      (str s "&" (print-param name value))
      (print-param name value)))
    s))

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
