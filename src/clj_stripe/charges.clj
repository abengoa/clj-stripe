(ns clj-stripe.charges
    "Functions for Stripe Charges API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn customer
	[customer-id]
	{"customer" customer-id})

(defn create-charge
  [money-quantity & extra-info]
  (apply util/merge-maps {:operation :charge} money-quantity extra-info )
  )
(defmethod execute :charge [op-data] (util/post-request stripe-token (str *api-root* "/charges") (dissoc op-data :operation)))

(defn get-charge
  [charge-id]
  {:operation :get-charge "id" charge-id}
  )
(defmethod execute :get-charge [op-data] (util/get-request stripe-token (str *api-root* "/charges/" (get op-data "id"))))

(defn get-all-charges
  ([& extra-info] 
    (apply util/merge-maps {:operation :get-all-charges} extra-info))
  )
(defmethod execute :get-all-charges [op-data] (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/charges") op-data ["customer" "count" "offset"])))

(defn create-refund
  ([charge-id] (create-refund charge-id nil))
  ([charge-id amount] (util/merge-maps {:operation :refund-charge "id" charge-id } amount))
  )
(defmethod execute :refund-charge [op-data] (util/post-request stripe-token (str *api-root* "/charges/" (get op-data "id") "/refund") (dissoc op-data :operation)))

