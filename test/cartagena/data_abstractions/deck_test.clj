(ns cartagena.data-abstractions.deck-test
  (:require  [clojure.test :refer :all]
             [clojure.data.generators :refer [*rnd*]]
             [cartagena.core :refer [card-types]]
             [cartagena.data-abstractions.deck :refer :all]))

(deftest random-deck-test
  (testing "random-deck"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c d & more] (random-deck 50 card-types)]
        (is (=  :sword
                a))
        (is (=  :pistol
                b))
        (is (=  :keys
                c))
        (is (=  :flag
                d))
        (is (=  46
                (count more)))))))

(deftest draw-one-from-test
  (testing "draw-one-from"
    (is (=  [:sword '(:pistol :keys :flag)]
            (draw-one-from '(:sword :pistol :keys :flag))))))

(deftest draw-test
  (testing "draw"
    (is (=  ['(:sword :pistol :keys) '(:flag)]
            (draw '(:sword :pistol :keys :flag) 3)))))
