(ns cartagena.data-abstractions.square-bis-test
  (:require [clojure.test :refer [deftest is testing]]
            [cartagena.core]
            [cartagena.data-abstractions.square-bis 
             :refer [type-of remove-piece-from add-piece-to make-square square-pieces-as-vector square-of-type? pieces-in num-pieces-in]]))

(deftest make-pieces-test
  (testing "make-pieces"
    (let [make-pieces #'cartagena.data-abstractions.square-bis/make-pieces]
      (is (=  {:blue 3, :green 3, :red 3}
              (make-pieces '(:red :green :blue) 3))))))

(deftest make-empty-pieces-test
  (testing "make-empty-pieces"
    (let [make-empty-pieces  #'cartagena.data-abstractions.square-bis/make-empty-pieces]
      (is (=  {:blue 0, :green 0, :red 0}
              (make-empty-pieces '(:red :green :blue)))))))

(deftest make-starting-square-test
  (testing "make-starting-square"
    (let [make-starting-square  #'cartagena.data-abstractions.square-bis/make-starting-square
          res (make-starting-square '(:red :green :blue))]
      (is (=   [:start {:blue 6, :green 6, :red 6}]
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
    (is (=  [:start {:green 0, :red 1, :yellow 0}] 
            (add-piece-to [:start {:green 0, :red 0, :yellow 0}]
                          :red)))))

(deftest remove-piece-from-test
  (testing "remove-piece-from"
    (is (= [:start {:green 1, :red 1, :yellow 0}]
           (remove-piece-from [:start {:green 2, :red 1, :yellow 0}]
                              :green)))))

(deftest type-of-test
  (testing "type-of"
    (is (= :start
           (type-of [:start {:green 2, :red 1, :yellow 0}])))))

(deftest pieces-in-test
  (testing "pieces-in"
    (is (=  {:green 6, :red 6, :yellow 6}
            (pieces-in [:start {:green 6, :red 6, :yellow 6}])))

    (is (=  {:green 2, :red 1, :yellow 2}
            (pieces-in [:bottle {:green 2, :red 1, :yellow 2}])))))

(deftest num-pieces-in-test
  (testing "num-pieces-in 1 param"
    (is (=  18
            (num-pieces-in [:start {:green 6, :red 6, :yellow 6}])))

    (is (=  5
            (num-pieces-in [:bottle {:green 2, :red 1, :yellow 2}]))))
  (testing "num-pieces-in 2 params"
    (is (=  1
            (num-pieces-in  [:bottle {:green 2, :red 1, :yellow 2}]
                            :red)))

    (is (=  2
            (num-pieces-in [:bottle {:green 2, :red 1, :yellow 2}]
                           :yellow)))))

(deftest square-pieces-as-vector-test
  (testing "square-pieces-as-vector one param"
    (is (= [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
           (square-pieces-as-vector [:start {:green 6, :red 6, :yellow 6}])))
    (is (= [:green :red :red :yellow :yellow :yellow]
           (square-pieces-as-vector [:start {:green 1, :red 2, :yellow 3}])))
    (is (= []
           (square-pieces-as-vector [:start {:green 0, :red 0, :yellow 0}]))))
  )

(deftest square-of-type?-test
  (testing "square-of-type?"
    (is (= false
           (square-of-type? :flag [:start {:green 6, :red 6, :yellow 6}])))
    (is (= true
           (square-of-type? :start [:start {:green 6, :red 6, :yellow 6}])))))

