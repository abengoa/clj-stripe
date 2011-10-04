(ns clj-stripe.customers
    "Functions for Stripe Customers API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn email [e] {"email" e})

(defn create-customer
  [& extra-info]
  (apply util/merge-maps {:operation :create-customer} extra-info)
  )
(defmethod execute :create-customer [op-data] (util/post-request stripe-token (str *api-root* "/customers") (dissoc op-data :operation)))

(defn get-customer
  [customer-id]
  {:operation :get-customer :customer-id customer-id}
  )
(defmethod execute :get-customer [op-data] (util/get-request stripe-token (str *api-root* "/customers/" (get op-data :customer-id))))

(defn get-customers
  ([] {:operation :get-customers})
  ([position] (util/merge-maps {:operation :get-customers} position))
  )
(defmethod execute :get-customers [op-data] (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/customers") op-data ["offset" "count"])))

(defn update-customer
  [customer-id & extra-info]
  (apply util/merge-maps {:operation :update-customer :customer-id customer-id} extra-info)
  )
(defmethod execute :update-customer [op-data] (util/post-request stripe-token (str *api-root* "/customers/" (:customer-id op-data)) (dissoc op-data :operation :customer-id)))

(defn delete-customer
  [customer-id]
  {:operation :delete-customer :customer-id customer-id}
  )
(defmethod execute :delete-customer [op-data] (util/delete-request stripe-token (str *api-root* "/customers/" (get op-data :customer-id))))
