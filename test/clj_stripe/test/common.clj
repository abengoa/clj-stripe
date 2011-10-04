(ns clj-stripe.test.common
  (:use clj-stripe.common)
  (:require [clojure.test :as test]))



(def test-address (address (address1 "address line 1") (address2 "address line 2") (zip 90123) (state "ca") (country "us")))


(test/deftest address-fn-test
  (test/is (= test-address {"card[address_line_1]" "address line 1", "card[address_line_2]" "address line 2", "card[address_zip]" 90123, "card[address_state]" "ca", "card[address_country]" "us"}))
)