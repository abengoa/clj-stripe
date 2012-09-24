;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.invoices
    "Functions for Stripe Invoices API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn get-invoice 
  "Creates a get invoice operation.
  Requires the invoice ID as a string." 
  [id] 
  {:operation :get-invoice :id id})

(defmethod execute :get-invoice 
  [op-data]
  (util/get-request *stripe-token* (str *api-root* "/invoices/" (get op-data :id))))

(defn get-all-invoices 
  "Creates a get all invoices operation.
  Optionally accepts a count, offset (see common/limit-count and common/offset) or an interval (see common/position). 
  Also optionally a customer ID (see common/customer)."
  [& extra-info] 
  (apply util/merge-maps {:operation :get-all-invoices} extra-info))

(defmethod execute :get-all-invoices 
  [op-data]
  (util/get-request *stripe-token* (util/url-with-optional-params (str *api-root* "/invoices") op-data ["count" "offset" "customer"])))

(defn get-upcoming-invoice 
  "Creates a get upcoming invoice operation.
  Requires the customer ID (see common/customer)."
  [customer] 
  (util/merge-maps {:operation :get-upcoming-invoice} customer))

(defmethod execute :get-upcoming-invoice 
  [op-data]
  (util/get-request *stripe-token* (str *api-root* "/invoices/upcoming?customer=" (get op-data "customer"))))
