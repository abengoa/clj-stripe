;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.plans
    "Functions for Stripe Plans API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn monthly 
  "Specifies that the plan is billed monthly."
  [] 
  {"interval" "month"})

(defn yearly 
  "Specifies that the plan is billed yearly."
  [] 
  {"interval" "year"})

(defn trial-period-days 
  "Specifies the amount of days that the plan is free to try."
  [td]
  {"trial_period_days" td})

(defn create-plan
  "Defines a create plan operation.
  Requires:
    The plan id as a string.
    The amount of money to be billed (see common/money-quantity).
    The billing period (see monthly and yearly).
    The name of the plan as a string.
  Optionally accepts the number of days of free trial (see trial-period-days)."
  [id money-amount interval name & extra-info]
  (apply util/merge-maps {:operation :create-plan "id" id "name" name} money-amount interval extra-info))

(defmethod execute :create-plan 
  [op-data] 
  (util/post-request *stripe-token* (str *api-root* "/plans") (dissoc op-data :operation)))

(defn get-plan
  "Defines a get plan operation.
  Requires the ID of the plan as a string."
  [id]
  {:operation :get-plan "id" id})

(defmethod execute :get-plan 
  [op-data] 
  (util/get-request *stripe-token* (str *api-root* "/plans/" (get op-data "id"))))

(defn delete-plan
  "Creates a delete plan operation.
  Requires the ID of the plan as a string."
  [id]
  {:operation :delete-plan :id id})

(defmethod execute :delete-plan 
  [op-data] 
  (util/delete-request *stripe-token* (str *api-root* "/plans/" (get op-data :id))))

(defn get-all-plans
  "Creates a get all plans operation.
  Optionally accepts a count and an offset (see common/limit-count and common/offset) or alternatively an interval (see common/position)."
  ([& extra-info] 
    (apply util/merge-maps {:operation :get-all-plans} extra-info)))

(defmethod execute :get-all-plans 
  [op-data]
  (util/get-request *stripe-token* (util/url-with-optional-params (str *api-root* "/plans") op-data ["count" "offset"])))

