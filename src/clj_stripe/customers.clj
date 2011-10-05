;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.customers
    "Functions for Stripe Customers API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn email 
  "Creates the data representation of a customer's email address."
  [e]
  {"email" e})

(defn create-customer
  "Creates a create-customer operation.
  Optionally accepts the following parameters:
    A credit card (see common/card)
    A coupon (see common/coupon)
    An email (see email)
    A description (see common/description)
    A plan identifier (see common/plan)
    A trial end date (see common/trial-end)
  Execute with common/execute."
  [& extra-info]
  (apply util/merge-maps {:operation :create-customer} extra-info))

(defmethod execute :create-customer [op-data] (util/post-request stripe-token (str *api-root* "/customers") (dissoc op-data :operation)))

(defn get-customer
  "Creates a get-customer operation.
  Requires the customer id as a string.
  Execute with common/execute."
  [customer-id]
  {:operation :get-customer :customer-id customer-id})

(defmethod execute :get-customer
  [op-data] 
  (util/get-request stripe-token (str *api-root* "/customers/" (get op-data :customer-id))))

(defn get-customers
  "Creates a get-customers operation.
  Optionally accepts a position. It can be an offset (see common/offset), a count (see common/limit-count) or both (see common/position).
  Execute with common/execute."
  ([] {:operation :get-customers})
  ([position] (util/merge-maps {:operation :get-customers} position)))

(defmethod execute :get-customers
  [op-data]
  (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/customers") op-data ["offset" "count"])))

(defn update-customer
  "Creates an update-customer operation.
  Requires the customer id as a string.
  Optionally accepts the following parameters:
    A credit card (see common/card)
    A coupon (see common/coupon)
    An email address (see email)
    A description (see common/description)
  Execute with common/execute."
  [customer-id & extra-info]
  (apply util/merge-maps {:operation :update-customer :customer-id customer-id} extra-info))

(defmethod execute :update-customer
  [op-data]
  (util/post-request stripe-token (str *api-root* "/customers/" (:customer-id op-data)) (dissoc op-data :operation :customer-id)))

(defn delete-customer
  "Creates an delete-customer operation.
  Requires the customer id as a string.
  Execute with common/execute."
  [customer-id]
  {:operation :delete-customer :customer-id customer-id})

(defmethod execute :delete-customer
  [op-data]
  (util/delete-request stripe-token (str *api-root* "/customers/" (get op-data :customer-id))))
