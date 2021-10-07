(ns cartagena.data-abstractions.square-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.core]
            [cartagena.data-abstractions.square :refer [dec-square-players inc-square-players make-square make-starting-square make-empty-pieces make-board-section square-contents-vector square-of-type? square-pieces]]))

(def board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
            {:pieces {:green 1, :red 2, :yellow 3}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}])


(deftest make-starting-square-test
  (testing "make-starting-square"
    (let [res (make-starting-square '(:red :green :blue))]
      (is (=  {:pieces {:blue 6, :green 6, :red 6}, :type :start}
              res)))))

(deftest make-empty-pieces-test
  (testing "make-empty-pieces"
    (let [res (make-empty-pieces '(:red :green :blue))]
      (is (=  {:pieces {:blue 0, :green 0, :red 0}}
              res)))))

(deftest make-square-test
  (testing "make-square for start"
    (let [res (make-square :start '(:red :green :blue))]
      (is (=  {:blue 6, :green 6, :red 6}
              (:pieces res))
          "all pieces should be 6")
      (is (=  :start
              (:type res))
          "type should be :start")))

  (testing "make-square for :boat"
    (let [res (make-square :boat '(:red :green :blue))]
      (is (=  {:blue 0, :green 0, :red 0}
              (:pieces res))
          "all pieces should be 0")
      (is (=  :boat
              (:type res))
          "type should be :boat")))

  (testing "make-square for intern square"
    (let [res (make-square :hat '(:red :green :blue))]
      (is (=  {:blue 0, :green 0, :red 0}
              (:pieces res))
          "all pieces should be 0")
      (is (=  :hat
              (:type res))
          "type should be :hat"))))

(deftest make-board-section-test
  (testing "make-board-section"
    (is (= true
           (vector? (make-board-section cartagena.core/card-types
                                        [:yellow :green :red])))
        "should return a vector")
    (is (= 6
           (count (make-board-section cartagena.core/card-types
                                      [:yellow :green :red])))
        "should return 6 squares")
    (is (= 6
           (count (into (make-board-section cartagena.core/card-types
                                            [:yellow :green :red])
                        #{})))
        "should not have repeated types of squares")
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}]
             (make-board-section cartagena.core/card-types
                                 [:yellow :green :red]))))))

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

