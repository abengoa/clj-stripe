;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.invoiceitems
    "Functions for Stripe Invoice Items API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn create-invoice-item
  "Creates a create invoice operation.
  Requires the customer ID (see common/customer) and the amount to be billed (see common/amount, common/currency and common/money-quantity).
  Optionally accepts a description for the invoice item (see common/description)."
  [customer money-amount & extra-info]
  (apply util/merge-maps {:operation :create-invoice-item} customer money-amount extra-info))

(defmethod execute :create-invoice-item 
  [op-data]
  (util/post-request *stripe-token* (str *api-root* "/invoiceitems") (dissoc op-data :operation)))

(defn get-invoice-item
  "Creates a get invoice item operation.
  Requires the invoice item id as a string."
  [id]
  {:operation :get-invoice-item "id" id})

(defmethod execute :get-invoice-item
  [op-data]
  (util/get-request *stripe-token* (str *api-root* "/invoiceitems/" (get op-data "id"))))

(defn update-invoice-item
  "Creates an update invoice item operation.
  Requires the invoice item ID as a string and the new amount to be billed (see common/amount, common/currency and common/money-quantity).
  Optionally accepts a new description for the invoice item as a string."
  [id money-amount & extra-info]
  (apply util/merge-maps {:operation :update-invoice-item :id id} money-amount extra-info))

(defmethod execute :update-invoice-item 
  [op-data]
  (util/post-request *stripe-token* (str *api-root* "/invoiceitems/" (get op-data :id)) (dissoc op-data :operation :id)))

(defn delete-invoice-item
  "Creates a delete invoice operation.
  Requires the invoice item ID as a string."
  [id]
  {:operation :delete-invoice-item :id id})

(defmethod execute :delete-invoice-item 
  [op-data]
  (util/delete-request *stripe-token* (str *api-root* "/invoiceitems/" (get op-data :id))))

(defn get-all-invoice-items
  "Creates a get all invoices operation.
  Optionally accepts a count and an offset (see common/limit-count and common/offset) or alternatively an interval (see common/position),
  and a customer ID (see common/customer)."
  ([& extra-info] 
    (apply util/merge-maps {:operation :get-all-invoice-items} extra-info)))

(defmethod execute :get-all-invoice-items 
  [op-data] 
  (util/get-request *stripe-token* (util/url-with-optional-params (str *api-root* "/invoiceitems") op-data ["count" "offset" "customer"])))
