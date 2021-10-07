(ns cartagena.data-abstractions.square-test
  (:require [clojure.test :refer :all]
            [cartagena.data-abstractions.square :refer :all]
            [cartagena.data-abstractions.board :refer [square]]))

(def board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
            {:pieces {:green 1, :red 2, :yellow 3}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}])


(deftest starting-pieces-test
  (testing "starting-pieces"
    (let [res (starting-pieces '(:red :green :blue))]
      (is (=  {:pieces {:blue 6, :green 6, :red 6}, :type :start}
              res)))))

(deftest empty-pieces-test
  (testing "empty-pieces"
    (let [res (empty-pieces '(:red :green :blue))]
      (is (=  {:pieces {:blue 0, :green 0, :red 0}}
              res)))))

(deftest make-square-test
  (testing "make-square for start"
    (let [res (make-square :start '(:red :green :blue))]
      (is (=  {:pieces {:blue 6, :green 6, :red 6}, :type :start}
              res))))
  (testing "make-square for :boat"
    (let [res (make-square :boat '(:red :green :blue))]
      (is (=  {:pieces {:blue 0, :green 0, :red 0}, :type :boat}
              res))))
  (testing "make-square for intern square"
    (let [res (make-square :hat '(:red :green :blue))]
      (is (=  {:pieces {:blue 0, :green 0, :red 0}, :type :hat}
              res)))))

(deftest inc-square-players-test
  (testing "inc-square-players"
    (is (= {:pieces {:green 0, :red 1, :yellow 0}, :type :start}
           (inc-square-players {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                               :red)))))

(deftest dec-square-players-test
  (testing "dec-square-players"
    (is (= {:pieces {:green 1, :red 1, :yellow 0}, :type :start}
           (dec-square-players {:pieces {:green 2, :red 1, :yellow 0}, :type :start}
                               :green)))))

(testing "square"
  (is (=  {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
          (square [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                  0)))

  (is (=  {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
          (square [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                  4))))

(deftest square-pieces-test
  (testing "square-pieces 2 params"
    (is (=  {:green 6, :red 6, :yellow 6}
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           0)))

    (is (=  {:green 2, :red 1, :yellow 2}
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 2, :red 1, :yellow 2}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           4)))

    (is (=  {:green 1, :red 2, :yellow 3}
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 1, :red 2, :yellow 3}, :type :bottle}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 2, :red 1, :yellow 2}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           1)))))

(testing "square-pieces 3 params"
  (is (=  6
          (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                         0
                         :red)))

  (is (=  0
          (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                         4
                         :yellow))))

(deftest square-contents-vector-test
  (testing "square-contents-vector one param"
    (is (= [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
           (square-contents-vector {:green 6, :red 6, :yellow 6})))
    (is (= [:green :red :red :yellow :yellow :yellow]
           (square-contents-vector {:green 1, :red 2, :yellow 3})))
    (is (= []
           (square-contents-vector {:green 0, :red 0, :yellow 0}))))
  (testing "square-contents-vector two param"
    (is (= [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
           (square-contents-vector board 0)))
    (is (= [:green :red :red :yellow :yellow :yellow]
           (square-contents-vector board 1)))
    (is (= []
           (square-contents-vector board 2)))))

(deftest square-of-type?-test
  (testing "square-of-type?"
    (is (= false
           (square-of-type? {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            :flag)))
    (is (= true
           (square-of-type? {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            :start)))))

