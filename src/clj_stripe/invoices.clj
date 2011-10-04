(ns clj-stripe.invoices
    "Functions for Stripe Invoices API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn get-invoice [id] {:operation :get-invoice :id id})
(defmethod execute :get-invoice [op-data] (util/get-request stripe-token (str *api-root* "/invoices/" (get op-data "id"))))

(defn get-all-invoices [& extra-info] 
  (apply util/merge-maps {:operation :get-all-invoices} extra-info))
(defmethod execute :get-all-invoices [op-data] (util/get-request stripe-token (util/url-with-optional-params (str *api-root* "/invoices") op-data ["count" "offset" "customer"])))

(defn get-upcoming-invoice [customer] (util/merge-maps {:operation :get-upcoming-invoice} customer))
(defmethod execute :get-upcoming-invoice [op-data] (util/get-request stripe-token (str *api-root* "/invoices/upcoming?customer=" (get op-data "customer"))))