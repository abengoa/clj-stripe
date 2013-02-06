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

;; Root URL for the API calls
(defonce api-root "https://api.stripe.com/v1")

(defonce ^:dynamic *stripe-token* nil)

(defmacro with-token
  "Binds the specified Stripe authentication token to the stripe-token variable and executes the body."
  [token & body]
  `(binding [*stripe-token* ~token] ~@body))

(defmulti execute 
  "Executes a Stripe API operation. 
  All operations are defined using other clj-stripe functions that return data structures representing API calls, but that do not execute them. 
  For actually making the calls to the Stripe servers the execute function must be used.
  execute expects a Stripe authentication token in the context, use the macro with-token to set the token and wrap one or several execute calls."
  :operation)

(defn address1 
  [addr] 
  "Defines the first line of a credit card address."
  {"card[address_line_1]" addr})

(defn address2 
  "Defines the second line of a credit card address."
  [addr] 
  {"card[address_line_2]" addr})

(defn zip 
  "Defines the zip code of a credit card address."
  [zipcode] 
  {"card[address_zip]" zipcode})

(defn state 
  "Defines the state of a credit card address."
  [addr-state] 
  {"card[address_state]" addr-state})

(defn country 
  "Defines the country of a credit card address."
  [addr-country] 
  {"card[address_country]" addr-country})

(defn address
  "Defines a credit card address.
  Optionally accepts the following parameters:
    A first line of the address (see address1).
    A second line of the address (see address2).
    A zip code (see zip).
    A state (see state).
    A country (see country). "
  [& elements]
  (util/merge-maps elements))

(defn expiration 
  "Defines the expiration date of a credit card. 
  Requires the month and the year of expiration."
  [month year] 
  {"card[exp_month]" month "card[exp_year]" year})

(defn cvc 
  "Defines the card verification code (CVC) of a credit card. 
  Requires the code as parameter."
  [v]
  {"card[cvc]" v})

(defn number 
  "Defines the number of a credit card. 
  Requires the number as parameter."
  [n]
  {"card[number]" n})

(defn owner-name 
  "Defines the name of an element."
  [n] 
  {"card[name]" n})
  
(defn description 
  "Creates the description of an element."
  [d]
  {"description" d})

(defn elem-name 
  "Defines the name of an element."
  [n] 
  {"name" n})

(defn customer
   "Creates the data for a customer identifier"
  [customer-id]
  {"customer" customer-id})

(defn card
  "Creates a new credit card. It accepts either:
    A credit card token (see cards/create-token or stripe.js)
    or
    A credit card definition, composed of:
      Required: credit card number (see number)
      Required: expiration date (see expiration)
      Optional: credit card verification code (see cvc)
      Optional: cardholder's name (see owner-name)
      Optional: address line 1 (see address1)
      Optional: address line 2 (see address2)
      Optional: address zip code (see zip)
      Optional: address state (see state)
      Optional: address country (see country)"
	([card-token] {"card" card-token})
	([number expiration & extra-info] (apply util/merge-maps number expiration extra-info))
	)

(defn amount 
  "Defines an amount of money, as an integer."
  [a]
  {"amount" a})

(defn currency 
  "Defines a currency, for example 'usd'."
  [c] 
  {"currency" c})

(defn money-quantity 
  "Defines a quantity of money, as a pair of amount and currency."
  [a c]
  (into (currency c) (amount a)))

(defn offset 
  "Defines an offset, for selecting where to start retrieving the elements of a list."
  [o]
  {"offset" o})

(defn limit-count 
  "Defines a count value, for selecting how many elements to retrieve from a list."
  [c]
  {"count" c})

(defn position 
  "Defines an interval as a pair of count (see limit-count) and offset (see offset)."
  [c o]
  (into (limit-count c) (offset o)))

(defn coupon 
  "Defines the name of a coupon."
  [c] 
  {"coupon" c})

(defn plan 
  "Defines the ID of a subscription plan."
  [p]
  {"plan" p})

(defn trial-end 
  [t] 
  {"trial_end" t})