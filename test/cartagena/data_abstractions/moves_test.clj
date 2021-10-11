(ns cartagena.data-abstractions.moves-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.data-abstractions.player :refer [actions cards]]
            [cartagena.data-abstractions.game :refer [make-game players player]]
            [cartagena.data-abstractions.moves :refer [pass fall-back play-card]]))

(def game
  (binding [*rnd* (java.util.Random. 12345)]
    (make-game)))

(deftest pass-test
  (testing "pass"
    (let [new-game (pass game)]
      (is (=  3
              (actions (player (players game) :red))))
      (is (=  2
              (actions (player (players new-game) :red)))))))

(deftest play-card-test
  (testing "play-card"
    (let [new-game (play-card game :keys 0)]
      (is (=  3
              (actions (player (players game) :red))))
      (is (=  2
              (actions (player (players new-game) :red))))

      (is (=  6
              (count (cards (player (players game) :red)))))
      (is (=  5
              (count (cards (player (players new-game) :red))))))))

(deftest fall-back-test
  (binding [*rnd* (java.util.Random. 12345)]
    (testing "fall-back"
      (let [new-game (fall-back game 12)]
        (is (=  3
                (actions (player (players game) :red))))
        (is (=  2
                (actions (player (players new-game) :red))))

        (is (=  6
                (count (cards (player (players game) :red)))))
        (is (=  7
                (count (cards (player (players new-game) :red)))))))))

