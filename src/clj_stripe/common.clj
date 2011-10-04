(ns clj-stripe.common
    "Common functions for Stripe API"
    (:require [clj-stripe.util :as util]))

(defonce *api-root* "https://api.stripe.com/v1")
(defonce stripe-token nil)
(defmacro with-token
  [token & body]
  `(binding [stripe-token ~token] ~@body)
  )

(defmulti execute :operation)

(defn address1 [addr] {"card[address_line_1]" addr})
(defn address2 [addr] {"card[address_line_2]" addr})
(defn zip [zipcode] {"card[address_zip]" zipcode})
(defn state [addr-state] {"card[address_state]" addr-state})
(defn country [addr-country] {"card[address_country]" addr-country})
(defn address
  [& elements]
  (util/merge-maps elements)
)
(defn expiration [month year] {"card[exp_month]" month "card[exp_year]" year})
(defn cvc [v] {"card[cvc]" v})
(defn number [n] {"card[number]" n})
(defn description [d] {"description" d})

(defn offset [o] {"offset" o})
(defn limit-count [c] {"count" c})
(defn position [c o] (into (limit-count c) (offset o)))

(defn coupon [c] {"coupon" c})
(defn plan [p] {"plan" p})
(defn trial-end [t] {"trial_end" t})