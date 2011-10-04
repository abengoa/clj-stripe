(ns clj-stripe.plans
    "Functions for Stripe Plans API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn monthly [] {"interval" "month"})
(defn yearly [] {"interval" "year"})
(defn trial-period-days [td] {"trial_period_days" td})

(defn create-plan
  [id money-amount interval name & extra-info]
  (apply util/merge-maps {:operation :create-plan "id" id "name" name} money-amount interval extra-info)
  )
(defmethod execute :create-plan [op-data] (util/post-request stripe-token (str *api-root* "/plans") (dissoc op-data :operation)))

(defn get-plan
  [id]
  {:operation :get-plan "id" id}
  )
(defmethod execute :get-plan [op-data] (util/get-request stripe-token (str *api-root* "/plans/" (get op-data "id"))))

(defn delete-plan
  [id]
  {:operation :delete-plan :id id}
  )
(defmethod execute :delete-plan [op-data] (util/delete-request stripe-token (str *api-root* "/plans/" (get op-data :id))))

(defn get-all-plans
  ([& extra-info] 
    (apply util/merge-maps {:operation :get-all-plans} extra-info))
  )
(defmethod execute :get-all-plans [op-data] (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/plans") op-data ["count" "offset"])))

