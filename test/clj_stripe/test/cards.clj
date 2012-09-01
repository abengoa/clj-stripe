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

(with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
  (def test-card (card (number "4242424242424242") (expiration 12 2012) (cvc 123) (owner-name "Mr. Owner")))

  (def create-token-op (create-card-token test-card))
  (test/deftest
    card-token
    (test/is (= create-token-op
		{:operation :create-card-token, "card[number]" "4242424242424242", "card[exp_month]" 12, "card[exp_year]" 2012, "card[cvc]" 123, "card[name]" "Mr. Owner"})))

  (def token-response (execute create-token-op))

  
  (test/deftest
    card-token-exec
    (test/is (and (= (dissoc token-response :id :created) 
	{:livemode false, :object "token", :used false, :card {:country "US", :exp_month 12, :last4 "4242", :name "Mr. Owner", :address_line2 nil, :fingerprint "qhjxpr7DiCdFYTlH", :address_line1 nil, :object "card", :address_city nil, :address_zip nil, :address_state nil, :type "Visa", :exp_year 2012, :address_country nil}}))))
	
  (def get-token-op (get-card-token (:id token-response)))
  (def new-token-response (execute get-token-op))

  (test/deftest
    card-token-exec
    (test/is (= token-response new-token-response)))

  (test/deftest
    card-token-get
    (test/is (= get-token-op (assoc {:operation :get-card-token} "id" (:id token-response))))
    )
  )