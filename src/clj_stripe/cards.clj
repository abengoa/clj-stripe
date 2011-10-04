(ns clj-stripe.cards
    "Functions for Stripe Cards API"
    (:use clj-stripe.common)
    (:require [clj-stripe.util :as util]))

(defn create-card-token
  [card & extra-info]
  (apply util/merge-maps {:operation :create-card-token} card extra-info)
  )
(defmethod execute :create-card-token [op-data] (util/post-request stripe-token (str *api-root* "/tokens") (dissoc op-data :operation)))

(defn get-card-token
  [id]
  {:operation :get-card-token "id" id}
  )
(defmethod execute :get-card-token [op-data] (util/get-request stripe-token (str *api-root* "/tokens/" (get op-data "id"))))
