;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.test.cards
  (:use [clj-stripe common cards])
  (:require [clojure.test :as test]))

(with-token "sk_test_BQokikJOvBiI2HlWgH4olfQ2:"
  (def test-card (card (number "4242424242424242") (expiration 12 2020) (cvc 123) (owner-name "Mr. Owner")))

  (def create-token-op (create-card-token test-card))
  (test/deftest
    card-token
    (test/is (= create-token-op
		{:operation :create-card-token, "card[number]" "4242424242424242", "card[exp_month]" 12, "card[exp_year]" 2020, "card[cvc]" 123, "card[name]" "Mr. Owner"})))

  (def token-response (execute create-token-op))
  (def generalized-token-response (-> token-response
                                    (dissoc :id :created :client_ip)
                                    (update-in [:card] dissoc :id :fingerprint)))

  (test/deftest
    card-token-exec
    (test/is (and (= generalized-token-response
                    {:object "token", :card {:country "US", :metadata {}, :dynamic_last4 nil, :exp_month 12, :last4 "4242", :address_zip_check nil, :name "Mr. Owner", :address_line2 nil,  :cvc_check "unchecked", :address_line1 nil, :object "card", :address_city nil, :address_zip nil, :tokenization_method nil, :address_state nil, :address_line1_check nil, :brand "Visa", :exp_year 2020, :address_country nil,  :funding "credit"}, :livemode false, :type "card", :used false}))))


  (def get-token-op (get-card-token (:id token-response)))
  (def new-token-response (execute get-token-op))

  (test/deftest
    card-token-re-exec
    (test/is (= token-response new-token-response)))

  (test/deftest
    card-token-get
    (test/is (= get-token-op (assoc {:operation :get-card-token} "id" (:id token-response))))
    )
  )
