(ns clj-stripe.test.cards
  (:use [clj-stripe common cards])
  (:require [clojure.test :as test]))

(with-token "vtUQeOtUnYr7PGCLQ96Ul4zqpDUO4sOE:"
  (def test-card (card (number "4242424242424242") (expiration 12 2012) (cvc 123) (description "a normal cc") (elem-name "my first cc")))

  (def create-token-op (create-card-token test-card))
  (test/deftest
    card-token
    (test/is (= create-token-op
		{:operation :create-card-token, "card[number]" "4242424242424242", "card[exp_month]" 12, "card[exp_year]" 2012, "card[cvc]" 123, "description" "a normal cc", "name" "my first cc"})))

  (def token-response (execute create-token-op))

  (test/deftest
    card-token-exec
    (test/is (and (= (dissoc token-response :id :created) {:amount 0, :currency "usd", :livemode false, :object "token", :used false, :card {:country "US", :cvc_check "pass", :exp_month 12, :exp_year 2012, :last4 "4242", :object "card", :type "Visa"}}) (:id token-response) (:created token-response))))

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

