;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.test.customers
  (:use [clj-stripe common customers])
  (:require [clojure.test :as test]))

(with-token ":"

  (def test-card (card (number "4242424242424242") (expiration 12 2020) (cvc 123) (owner-name "Mr. Owner")))
  (def create-customer-op (create-customer
			      test-card
			      (email "mrtest@teststripe.com")
			      (description "A test customer")
			      ;(trial-end (+ 10000 (System/currentTimeMillis)))
				  ))

  (def customer-result (execute create-customer-op))
  (def get-customer-op (get-customer (:id customer-result)))
  (def get-customer-result (execute get-customer-op))
  (def get-all-customers-op (get-customers))
  (def get-all-customers-result (execute get-all-customers-op))

  (test/deftest customers-test
    (test/is (= get-customer-result customer-result))
    (test/is (some #{(:id get-customer-result)} (map :id (:data get-all-customers-result))))
    )

  (def new-email (email "a new email"))
  (def update-customer-op (update-customer (:id customer-result) new-email))
  (def update-customer-result (execute update-customer-op))
  (def get-customer-op-2 (get-customer (:id customer-result)))
  (def get-customer-result-2 (execute get-customer-op-2))

  (test/deftest modify-customer-test
    (test/is (= (assoc customer-result :email (get new-email "email")) get-customer-result-2)))

  (def delete-customer-op (delete-customer (:id customer-result)))
  (def delete-customer-result (execute delete-customer-op))
  (def get-all-customers-result-2 (execute get-all-customers-op))

  ;; This test worked at the start of Stripe business, but now there are too many users in the test account
  ;; and pagination should be used to search for the newly created user.
  ;(test/deftest delete-customer-test
  ;  (test/is (not (nil? (some #{(:id get-customer-result)} (map :id (:data get-all-customers-result-2)))))))

	)
