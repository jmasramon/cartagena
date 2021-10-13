(ns cartagena.data-abstractions.moves-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.data-abstractions.player-bis :as p :refer [actions cards]]
            [cartagena.data-abstractions.game :refer [make-game active-player remove-played-card turn-played players player move-piece]]
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
      (let [the-cards (p/cards (active-player game))
            set-fallback-game (turn-played (remove-played-card (move-piece game 0 8) (first the-cards)))
            intermediate-game (turn-played (remove-played-card (move-piece set-fallback-game 0 12) (second the-cards)))
            final-game (fall-back intermediate-game 12)]
        (is (=  3
                (actions (player (players game) :red))))
        (is (=  2
                (actions (player (players set-fallback-game) :red))))
        (is (=  1
                (actions (player (players intermediate-game) :red))))
        (is (=  0
                (actions (player (players final-game) :red)))
            "Remove an action")

        (is (=  6
                (count (cards (player (players game) :red)))))
        (is (=  5
                (count (cards (player (players set-fallback-game) :red)))))
        (is (=  4
                (count (cards (player (players intermediate-game) :red)))))
        (is (=  5
                (count (cards (player (players final-game) :red))))
            "Should place the card and also add a new car to the player")))))

