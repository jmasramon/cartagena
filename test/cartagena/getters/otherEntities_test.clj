(ns cartagena.getters.otherEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.getters.otherEntities :refer :all]))

(deftest num-pieces-test
    (testing "num-pieces"
        (is (= 3
            (num-pieces {:pieces {:green 6, :red 3, :yellow 2}, :type :start}
                        :red)))))

(deftest indexes-squares-of-type-test
  (testing "indexes-squares-of-type"
    (is (= '(1 7 13)
        (indexes-squares-of-type [{:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                                :bottle)))))

(deftest player-color-test
    (testing "player-color"
        (is (= :yellow
            (player-color {:yellow {:cards '(:keys :hat :sword :sword :sword :sword), :actions 3}})))))

(deftest player-cards-test
    (testing "player-cards"
        (is (= '(:keys :hat :sword :sword :sword :sword)
            (player-cards {:yellow {:cards '(:keys :hat :sword :sword :sword :sword), :actions 3}})))))

(deftest cards-amounts-test
    (testing "cards-amounts"
        (is (= {:keys 1 :hat 1 :sword 4}
            (cards-amounts '(:keys :hat :sword :sword :sword :sword))))))


