(ns clj-stripe.subscriptions
    "Functions for Stripe Subscriptions API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn prorate [] {"prorate" true})
(defn do-not-prorate [] {"prorate" false})
(defn subscribe-customer
  [plan customer & extra-info]
  (apply util/merge-maps {:operation :subscribe-customer} plan customer extra-info))
(defmethod execute :subscribe-customer [op-data] (util/post-request stripe-token (str *api-root* "/customers/" (get op-data "customer") "/subscription") (dissoc op-data :operation "customer")))

(defn at-period-end [] {"at_period_end" true})
(defn immediately [] {"at_period_end" false})

(defn unsubscribe-customer
  [customer & extra-info]
  (apply util/merge-maps {:operation :unsubscribe-customer} customer extra-info)
  )
(defmethod execute :unsubscribe-customer [op-data] (util/delete-request stripe-token (util/url-with-optional-params (str *api-root* "/customers/" (get op-data "customer") "/subscription") op-data ["at_period_end"])))
