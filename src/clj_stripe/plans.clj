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

