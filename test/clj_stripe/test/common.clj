;   Copyright (c) 2011 Alberto Bengoa. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns clj-stripe.test.common
  (:use clj-stripe.common)
  (:require [clojure.test :as test]))


(def sample-address (address (address1 "address line 1") (address2 "address line 2") (zip 90123) (state "ca") (country "us")))

(test/deftest address-fn-test
  (test/is (= 
    sample-address
    {"card[address_line_1]" "address line 1", "card[address_line_2]" "address line 2", "card[address_zip]" 90123, "card[address_state]" "ca", "card[address_country]" "us"}))
  (test/is (= 
    (address (address2 "address line 2") (zip 90123))
    {"card[address_line_2]" "address line 2", "card[address_zip]" 90123}))
  )

(test/deftest expiration-date-test
  (test/is (= (expiration 5 2011) {"card[exp_month]" 5, "card[exp_year]" 2011}))
  )

(test/deftest card-test
  (test/is (= (card (number "4242424242424242") (expiration 12 2012)) {
	      "card[number]" "4242424242424242", "card[exp_month]" 12, "card[exp_year]" 2012}))
  (test/is (= (card (number "4242424242424242") (expiration 12 2012) (cvc 123) (description "a normal cc") (elem-name "my first cc"))
	      {"card[number]" "4242424242424242", "card[exp_month]" 12, "card[exp_year]" 2012, "card[cvc]" 123, "description" "a normal cc", "name" "my first cc"}))
  )

(test/deftest position-test
  (test/is (= (offset 5) {"offset" 5}))
  (test/is (= (limit-count 6) {"count" 6}))
  (test/is (= (position 6 5) {"offset" 5 "count" 6}))
  )

(test/deftest plan-test
  (test/is (= (coupon "mycoupon") {"coupon" "mycoupon"}))
  (test/is (= (plan "myplan") {"plan" "myplan"}))
  (test/is (= (trial-end 12345) {"trial_end" 12345}))
  )

(test/deftest money
  (test/is (= (amount 3000) {"amount" 3000}))
  (test/is (= (money-quantity 3000 "usd") {"amount" 3000, "currency" "usd"}))
  )