(ns clj-stripe.cards
    "Functions for Stripe Cards API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn create-card-token
  "Creates a create-card-token operation.
  Requires a card (see common/card).
  Optionally accepts a money-quantity (see common/money-quantity), an amount (see common/amount) or a currency (see common/currency).
  Execute with common/execute."
  [card & extra-info]
  (apply util/merge-maps {:operation :create-card-token} card extra-info))

(defmethod execute :create-card-token
  [op-data]
  (util/post-request stripe-token (str *api-root* "/tokens") (dissoc op-data :operation)))

(defn get-card-token
  [id]
  {:operation :get-card-token "id" id})

(defmethod execute :get-card-token
  [op-data]
  (util/get-request stripe-token (str *api-root* "/tokens/" (get op-data "id"))))
