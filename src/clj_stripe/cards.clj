;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

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
  (util/post-request *stripe-token* (str *api-root* "/tokens") (dissoc op-data :operation)))

(defn get-card-token
  "Creates a get-card-token operation.
  Requires a token id as a string.
  Execute with common/execute."
  [id]
  {:operation :get-card-token "id" id})

(defmethod execute :get-card-token
  [op-data]
  (util/get-request *stripe-token* (str *api-root* "/tokens/" (get op-data "id"))))
