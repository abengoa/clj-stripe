;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

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
(defn elem-name [n] {"name" n})

(defn card
	([card-token] {"card" card-token})
	([number expiration & extra-info] (apply util/merge-maps number expiration extra-info))
	)

(defn amount [a] {"amount" a})
(defn currency [c] {"currency" c})
(defn money-quantity [a c] (into (currency c) (amount a)))

(defn offset [o] {"offset" o})
(defn limit-count [c] {"count" c})
(defn position [c o] (into (limit-count c) (offset o)))

(defn coupon [c] {"coupon" c})
(defn plan [p] {"plan" p})
(defn trial-end [t] {"trial_end" t})