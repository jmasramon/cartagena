(ns cartagena.data-abstractions.square-test
  (:require [clojure.test :refer [deftest is testing]]
            [cartagena.core]
            [cartagena.data-abstractions.square :refer [type-of remove-piece-from add-piece-to make-square square-pieces-as-vector square-of-type? pieces-in num-pieces-in pieces-numbers-list-in is-square?]]))

(deftest make-pieces-test
  (testing "make-pieces"
    (let [make-pieces #'cartagena.data-abstractions.square/make-pieces]
      (is (=  {:pieces {:blue 3, :green 3, :red 3}}
              (make-pieces '(:red :green :blue) 3))))))

(deftest make-starting-square-test
  (testing "make-starting-square"
    (let [make-starting-square  #'cartagena.data-abstractions.square/make-starting-square
          res (make-starting-square '(:red :green :blue))]
      (is (=  {:pieces {:blue 6, :green 6, :red 6}, :type :start}
              res)))))

(deftest make-empty-pieces-test
  (testing "make-empty-pieces"
    (let [make-empty-pieces  #'cartagena.data-abstractions.square/make-empty-pieces
          res (make-empty-pieces '(:red :green :blue))]
      (is (=  {:pieces {:blue 0, :green 0, :red 0}}
              res)))))

(deftest make-square-test
  (testing "make-square for start"
    (let [res (make-square :start '(:red :green :blue))]
      (is (=  {:blue 6, :green 6, :red 6}
              (pieces-in res))
          "all pieces should be 6")
      (is (=  :start
              (type-of res))
          "type should be :start")))

  (testing "make-square for :boat"
    (let [res (make-square :boat '(:red :green :blue))]
      (is (=  {:blue 0, :green 0, :red 0}
              (pieces-in res))
          "all pieces should be 0")
      (is (=  :boat
              (type-of res))
          "type should be :boat")))

  (testing "make-square for intern square"
    (let [res (make-square :hat '(:red :green :blue))]
      (is (=  {:blue 0, :green 0, :red 0}
              (pieces-in res))
          "all pieces should be 0")
      (is (=  :hat
              (type-of res))
          "type should be :hat"))))

(deftest add-piece-to-test
  (testing "add-piece-to"
    (is (= {:pieces {:green 0, :red 1, :yellow 0}, :type :start}
           (add-piece-to {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                         :red)))))

(deftest remove-piece-from-test
  (testing "remove-piece-from"
    (is (= {:pieces {:green 1, :red 1, :yellow 0}, :type :start}
           (remove-piece-from {:pieces {:green 2, :red 1, :yellow 0}, :type :start}
                              :green)))))

(deftest type-of-test
  (testing "type-of"
    (is (= :start
           (type-of {:pieces {:green 2, :red 1, :yellow 0}, :type :start})))))

(deftest pieces-in-test
  (testing "pieces-in"
    (is (=  {:green 6, :red 6, :yellow 6}
            (pieces-in {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))

    (is (=  {:green 2, :red 1, :yellow 2}
            (pieces-in {:pieces {:green 2, :red 1, :yellow 2}, :type :bottle})))))

(deftest pieces-numbers-list-in-test
  (testing "pieces-numbers-list-in"
    (is (=   '(6 6 6)
             (pieces-numbers-list-in {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))

    (is (=  '(2 1 2)
            (pieces-numbers-list-in {:pieces {:green 2, :red 1, :yellow 2}, :type :bottle})))))


(deftest num-pieces-in-test
  (testing "num-pieces-in 1 param"
    (is (=  18
            (num-pieces-in  {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))

    (is (=  2
            (num-pieces-in {:pieces {:green 0, :red 2, :yellow 0}, :type :boat}))))
  (testing "num-pieces-in 2 params"
    (is (=  6
            (num-pieces-in  {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            :red)))

    (is (=  0
            (num-pieces-in {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
                           :yellow)))))

(deftest square-pieces-as-vector-test
  (testing "square-pieces-as-vector one param"
    (is (= [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
           (square-pieces-as-vector {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))
    (is (= [:green :red :red :yellow :yellow :yellow]
           (square-pieces-as-vector {:pieces {:green 1, :red 2, :yellow 3}, :type :start})))
    (is (= []
           (square-pieces-as-vector {:pieces {:green 0, :red 0, :yellow 0}, :type :start})))))

(deftest square-of-type?-test
  (testing "square-of-type?"
    (is (= false
           (square-of-type? :flag {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))
    (is (= true
           (square-of-type? :start {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))))

(deftest is-square?-test
  (testing "is-square?"
    (is (= false
           (is-square?  '({:green 6, :red 6, :yellow 6}, :type :start))))
    (is (= true
           (is-square? {:pieces {:green 6, :red 6, :yellow 6}, :type :start})))))




