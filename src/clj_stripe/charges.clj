;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.charges
    "Functions for Stripe Charges API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn customer
   "Creates the data for a customer identifier"
  [customer-id]
  {"customer" customer-id})

(defn create-charge
  "Creates a new charge operation.
  Requires one money quantity (see common/money-quantity), and either a customer (see customer) or a card (see common/card).
  Optionally accepts a desciption (see common/description).
  Execute the operation using common/execute."
  [money-quantity & extra-info]
  (apply util/merge-maps {:operation :charge} money-quantity extra-info))

(defmethod execute :charge 
  [op-data] 
  (util/post-request stripe-token (str *api-root* "/charges") (dissoc op-data :operation)))

(defn get-charge
  "Creates a new get-charge operation.
  Requires a charge id as a string.
  Execute the operation using common/execute."
  [charge-id]
  {:operation :get-charge "id" charge-id})

(defmethod execute :get-charge 
  [op-data] 
  (util/get-request stripe-token (str *api-root* "/charges/" (get op-data "id"))))

(defn get-all-charges
  "Creates a new get-all-charges operation.
  Optionally accepts a customer (see common/customer), offset and count (see common/offset, common/limit-count and common/position).
  Execute the operation using common/execute."
  ([& extra-info] 
    (apply util/merge-maps {:operation :get-all-charges} extra-info)))

(defmethod execute :get-all-charges 
  [op-data] 
  (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/charges") op-data ["customer" "count" "offset"])))

(defn create-refund
  "Creates a charge-refund operation.
  Requires the charge id (as a string).
  Optionally accepts the amount to be refunded (see common/amount).
  Execute the operation using common/execute."
  ([charge-id] (create-refund charge-id nil))
  ([charge-id amount] (util/merge-maps {:operation :refund-charge "id" charge-id } amount)))

(defmethod execute :refund-charge 
  [op-data]
  (util/post-request stripe-token (str *api-root* "/charges/" (get op-data "id") "/refund") (dissoc op-data :operation)))

