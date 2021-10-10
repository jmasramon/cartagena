(ns cartagena.data-abstractions.board-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.data-abstractions.board :refer :all]))

(deftest make-board-section-test
  (testing "make-board-section"
    (let [make-board-section #'cartagena.data-abstractions.board/make-board-section]
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
                                   [:yellow :green :red])))))))

(deftest make-board-test
  (testing "make-board"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= [{:pieces {:green 6, :yellow 6}, :type :start}
              {:pieces {:green 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :yellow 0}, :type :sword}
              {:pieces {:green 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :yellow 0}, :type :sword}
              {:pieces {:green 0, :yellow 0}, :type :sword}
              {:pieces {:green 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :yellow 0}, :type :sword}
              {:pieces {:green 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :yellow 0}, :type :sword}
              {:pieces {:green 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :yellow 0}, :type :sword}
              {:pieces {:green 0, :yellow 0}, :type :pistol}
              {:pieces {:green 0, :yellow 0}, :type :keys}
              {:pieces {:green 0, :yellow 0}, :type :hat}
              {:pieces {:green 0, :yellow 0}, :type :flag}
              {:pieces {:green 0, :yellow 0}, :type :bottle}
              {:pieces {:green 0, :yellow 0}, :type :boat}]
             (make-board   cartagena.core/card-types
                           [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]))))))

(deftest getters-tests
  (testing "boat"
    (is (=  {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
            (boat [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                   {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]))))
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
  (testing "square-pieces"
    (is (=  18
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           0))
        "should return start pieces")
    (is (=  6
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 3, :red 2, :yellow 1}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           2))
        "should return boat pieces")
    (is (=  3
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 3, :red 2, :yellow 1}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           2
                           :green))
        "should only green pieces")
    (is (=  2
            (square-pieces [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 3, :red 2, :yellow 1}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                           2
                           :red))
        "should only red pieces"))
  (testing "square-pieces-as-vector"
    (is (=  [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
            (square-pieces-as-vector [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                                     0))
        "should return start pieces")
    (is (=  [:green :red :red :yellow]
            (square-pieces-as-vector [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                      {:pieces {:green 1, :red 2, :yellow 1}, :type :boat}]
                                     7))
        "should return boat pieces"))
  (testing "square-type"
    (is (=  :start
            (square-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                         0))
        "should :start")
    (is (=  :boat
            (square-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                         7))
        "should return :boat"))
  (testing "squares-of-type"
    (is (=  [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}]
            (squares-of-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                             :start))
        "should :start")
    (is (=  [{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}]
            (squares-of-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                             :sword))
        "should return :boat"))
  (testing "indexes-squares-of-type"
    (is (= '(0)
           (indexes-squares-of-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                                    :start))
        "should :start")
    (is (= '(3)
           (indexes-squares-of-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                                    :sword))
        "should return :boat")
    (is (= '(1 3 6)
           (indexes-squares-of-type [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                                    :sword))
        "should return :boat")))

(deftest space-available?-test
  (testing "space-available?"
    (is (= false
           (space-available? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                              {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                             0)))
    (is (= true
           (space-available? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                              {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                              {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                             1)))))

(deftest square-full?-test
  (testing "square-full?"
    (is (= true
           (square-full? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                         0)))
    (is (= false
           (square-full? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                         1)))))

(deftest square-has-color?-test
  (testing "square-has-color?"
    (is (= false
           (square-has-color? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                               {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                              3
                              :green)))
    (is (= true
           (square-has-color? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                               {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                              0
                              :red)))))

(deftest add-piece-to-test
  (testing "add-piece-to"
    (is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
            {:pieces {:green 0, :red 1, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
           (add-piece-to [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                         1
                         :red)))))

(deftest remove-piece-from-test
  (testing "remove-piece-from"
    (is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 3, :red 2, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
           (remove-piece-from [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                               {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                               {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                              4
                              :yellow)))))

(def board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
            {:pieces {:green 6, :red 6, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}])

(deftest empty-slot?-test
  (testing "empty-slot?"
    (is (= true
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            :bottle
            :green)))
    (is (= true
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            :keys
            :red)))
    (is (= false
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol
            :yellow)))
    (is (= true
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol
            :green))))
  (testing "empty-slot? one param"
    (is (= true
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            :bottle)))
    (is (= true
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            :keys)))
    (is (= false
           (empty-slot?
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol)))
    (is (= false
           (empty-slot?
            {:pieces {:green 1, :red 0, :yellow 0}, :type :pistol}
            :pistol)))
    (is (= false
           (empty-slot?
            {:pieces {:green 1, :red 2, :yellow 0}, :type :pistol}
            :pistol)))))

(deftest next-empty-slot-index-test
  (testing "next-empty-slot-index"
    (is (= 5
           (next-empty-slot-index board :keys 0 :green)))
    (is (= 1
           (next-empty-slot-index board :bottle 0 :red)))

    (is (= 13
           (next-empty-slot-index board :keys 6 :yellow)))
    (is (= 13
           (next-empty-slot-index board :keys 5 :green)))
    (is (= 5
           (next-empty-slot-index board :keys 4 :red)))

    (is (= 8
           (next-empty-slot-index board :flag 2 :yellow))))
  (testing "next-empty-slot-index two params"
    (is (= 5
           (next-empty-slot-index board :keys 0)))
    (is (= 1
           (next-empty-slot-index board :bottle 0)))
    (is (= 13
           (next-empty-slot-index board :keys 6)))
    (is (= 13
           (next-empty-slot-index board :keys 5)))
    (is (= 5
           (next-empty-slot-index board :keys 4)))
    (is (= 10
           (next-empty-slot-index board :flag 2)))
    (is (= 15
           (next-empty-slot-index board :flag 10)))))

(deftest nonempty-slot?-test
  (testing "nonempty-slot?"
    (is (= false
           (nonempty-slot?
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle})))

    (is (= true
           (nonempty-slot?
            {:pieces {:green 2, :red 1, :yellow 3}, :type :keys})))

    (is (= true
           (nonempty-slot?
            {:pieces {:green 0, :red 1, :yellow 1}, :type :pistol})))

    (is (= true
           (nonempty-slot?
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol})))))

(deftest index-closest-nonempty-slot-test
  (testing "index-closest-nonempty-slot"
    (is (= 0
           (index-closest-nonempty-slot board 3)))
    (is (= 0
           (index-closest-nonempty-slot board 4)))
    (is (= 0
           (index-closest-nonempty-slot board 5)))
    (is (= 0
           (index-closest-nonempty-slot board 6)))
    (is (= 0
           (index-closest-nonempty-slot board 7)))
    (is (= 0
           (index-closest-nonempty-slot board 8)))
    (is (= 8
           (index-closest-nonempty-slot board 9)))
    (is (= 8
           (index-closest-nonempty-slot board 10)))
    (is (= 8
           (index-closest-nonempty-slot board 11)))
    (is (= nil
           (index-closest-nonempty-slot [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
                                         {:pieces {:green 6, :red 6, :yellow 0}, :type :flag}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                         {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                                        8)))))

(deftest move-piece-test
  (testing "move-piece"
    (is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 1, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 3, :red 1, :yellow 1}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
           (move-piece [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                       4
                       2
                       :red)))))


