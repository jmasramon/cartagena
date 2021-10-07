(ns cartagena.data-abstractions.board-test
  (:require [clojure.test :refer :all]
            [cartagena.data-abstractions.board :refer :all]))

(deftest add-piece-test
  (testing "add-piece"
    (is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
            {:pieces {:green 0, :red 1, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
           (add-piece [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                       {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                      1
                      :red)))))

(deftest remove-piece-test
  (testing "remove-piece"
    (is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
            {:pieces {:green 3, :red 2, :yellow 0}, :type :hat}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
           (remove-piece [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
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

(deftest find-empty-slot-test
  (testing "find-empty-slot"
    (is (= 5
           (find-empty-slot board :keys 0 :green)))
    (is (= 1
           (find-empty-slot board :bottle 0 :red)))

    (is (= 13
           (find-empty-slot board :keys 6 :yellow)))
    (is (= 13
           (find-empty-slot board :keys 5 :green)))
    (is (= 5
           (find-empty-slot board :keys 4 :red)))

    (is (= 8
           (find-empty-slot board :flag 2 :yellow))))
  (testing "find-empty-slot two params"
    (is (= 5
           (find-empty-slot board :keys 0)))
    (is (= 1
           (find-empty-slot board :bottle 0)))
    (is (= 13
           (find-empty-slot board :keys 6)))
    (is (= 13
           (find-empty-slot board :keys 5)))
    (is (= 5
           (find-empty-slot board :keys 4)))
    (is (= 10
           (find-empty-slot board :flag 2)))
    (is (= 15
           (find-empty-slot board :flag 10)))))

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

(deftest find-nonempty-slot-test
  (testing "find-nonempty-slot"
    (is (= 0
           (find-nonempty-slot board 3)))
    (is (= 0
           (find-nonempty-slot board 4)))
    (is (= 0
           (find-nonempty-slot board 5)))
    (is (= 0
           (find-nonempty-slot board 6)))
    (is (= 0
           (find-nonempty-slot board 7)))
    (is (= 0
           (find-nonempty-slot board 8)))
    (is (= 8
           (find-nonempty-slot board 9)))
    (is (= 8
           (find-nonempty-slot board 10)))
    (is (= 8
           (find-nonempty-slot board 11)))
    (is (= 0
           (find-nonempty-slot [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
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

(deftest space-occupied?-test
  (testing "space-occupied?"
    (is (= true
           (space-occupied? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                             {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
                            0)))
    (is (= false
           (space-occupied? [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
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

