;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.subscriptions
    "Functions for Stripe Subscriptions API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn prorate 
  "Specifies that the difference between plans should be prorated."
  [] 
  {"prorate" true})

(defn do-not-prorate 
  "Specifies that the difference between plans should not be prorated."
  [] 
  {"prorate" false})

(defn subscribe-customer
  "Creates a subscription operation for a customer.
  Requires the plan ID (see common/plan) and the customer ID (see common/customer).
  Optionally accepts:
    A coupon code (see common/coupon).
    Prorate or not the plan switching differente (see prorate and do-not-prorate).
    End of trial period (see common/trial-end).
    New credit card to attach to the customer (see common/card)."
  [plan customer & extra-info]
  (apply util/merge-maps {:operation :subscribe-customer} plan customer extra-info))

(defmethod execute :subscribe-customer
  [op-data]
  (util/post-request *stripe-token* (str api-root "/customers/" (get op-data "customer") "/subscription") (dissoc op-data :operation "customer")))

(defn at-period-end
  "Specifies that a cancelled subscription must be continued until the end of the current subscription period."
  [] 
  {"at_period_end" true})

(defn immediately 
  "Specifies that a cancelled subscription must be terminated immediately."
  [] 
  {"at_period_end" false})

(defn unsubscribe-customer
  "Creates a subscription cancellation operation for a customer.
  Requires the customer ID  (see common/customer).
  Optionally accepts a parameter to specify when to end the subscription (see at-period-end and immediately)."
  [customer & extra-info]
  (apply util/merge-maps {:operation :unsubscribe-customer} customer extra-info))

(defmethod execute :unsubscribe-customer 
  [op-data]
  (util/delete-request *stripe-token* (util/url-with-optional-params (str api-root "/customers/" (get op-data "customer") "/subscription") op-data ["at_period_end"])))
