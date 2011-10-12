;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.coupons
    "Functions for Stripe Coupons API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn coupon-id
  "Creates the id of a coupon. 
  Requires the id as a string."
  [id] 
  {"id" id})

(defn percent-off 
  "Percentage to discount when applying the coupon."
  [p_off] 
  {"percent_off" p_off})

(defn forever
  "Specifies that a coupon is valid forever"
  []
  {"duration" "forever"})

(defn once
  "Specifies that a coupon is valid only once"
  []
  {"duration" "once"})

(defn repeating
  "Specifies that a coupon is valid for a given number of times.
  Requires the number of months that the coupon is in vigor, as a positive integer."
  [months]
  {"duration" "repeating" "duration_in_months" months})

(defn max-redemptions
  "Specifies the maximum number of times a coupon will be redeemed before becoming invalid.
  Requires the number of times as a positive integer."
  [m]
  {"max_redemptions" m})

(defn redeem-by
  "Specifies the limit date for redeeming this coupon.
  Requires the UTC timestamp as a string."
  [t]
  {"redeem_by" t})

(defn create-coupon
  "Defines a create coupon operation.
  Requires:
    The percentage of discount (see percent-off).
    The duration of the coupon (see forever, once and repeating).
  Optionally accepts:
    The if of the coupon (see coupon-id)
    The maximum number of times the coupon can be redeemed (see max-redemptions)
    The limit date for redemption (see redeem-by)."
  [p-off duration & extra-info]
  (apply util/merge-maps {:operation :create-coupon} p-off duration extra-info))

(defmethod execute :create-coupon 
  [op-data] 
  (util/post-request stripe-token (str *api-root* "/coupons") (dissoc op-data :operation)))

(defn get-coupon
  "Defines a get coupon operation.
  Requires the ID of the coupon as a string."
  [id]
  {:operation :get-coupon "id" id})

(defmethod execute :get-coupon 
  [op-data] 
  (util/get-request stripe-token (str *api-root* "/coupons/" (get op-data "id"))))

(defn delete-coupon
  "Creates a delete coupon operation.
  Requires the ID of the coupon as a string."
  [id]
  {:operation :delete-coupon :id id})

(defmethod execute :delete-coupon 
  [op-data] 
  (util/delete-request stripe-token (str *api-root* "/coupons/" (get op-data :id))))

(defn get-all-coupons
  "Creates a get all coupons operation.
  Optionally accepts a count and an offset (see common/limit-count and common/offset) or alternatively an interval (see common/position)."
  ([& extra-info] 
    (apply util/merge-maps {:operation :get-all-coupons} extra-info)))

(defmethod execute :get-all-coupons 
  [op-data]
  (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/coupons") op-data ["count" "offset"])))

