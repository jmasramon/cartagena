(ns cartagena.data-abstractions.game-test
  (:require  [clojure.test :refer [deftest is testing]]
             [clojure.data.generators :refer [*rnd*]]
             [cartagena.core :refer [pirate-colors card-types]]
             [cartagena.data-abstractions.deck :as d]
             [cartagena.data-abstractions.player-bis :as p]
             [cartagena.data-abstractions.board :as b]
             [cartagena.data-abstractions.game :refer :all]))

(binding [*rnd* (java.util.Random. 12345)]
  (def yellow-cards (d/random-deck 6))
  (def green-cards (d/random-deck 6))
  (def red-cards (d/random-deck 6))
  (def yellow-player (p/make-player :yellow yellow-cards))
  (def the-players [yellow-player
                    (p/make-player :green green-cards)
                    (p/make-player :red red-cards)])
  (def the-turn (random-initial-turn (colors the-players)))
  (def the-turn-order (make-turn-order (colors the-players)))
  (def the-board (b/make-board the-players))
  (def the-deck (d/random-deck)))


(def the-game {:players the-players
               :turn-order the-turn-order
               :turn the-turn
               :board the-board
               :deck the-deck})

(deftest random-initial-turn-test
  (testing "random-initial-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  :green
              (random-initial-turn  pirate-colors)))
      (is (=  :red
              (random-initial-turn  pirate-colors)))
      (is (=  :red
              (random-initial-turn  pirate-colors)))
      (is (=  :yellow
              (random-initial-turn  pirate-colors))))))

(deftest make-turn-order-test
  (testing "make-turn-order-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  {:green :red, :red :yellow, :yellow :green}
              (make-turn-order  (colors (make-random-players 3 pirate-colors 6 card-types)))))
      (is (=  {:green :red, :red :yellow, :yellow :green}
              (make-turn-order  (colors (make-random-players 3 pirate-colors 6 card-types))))))))

(deftest make-random-players-test
  (testing "Random players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c] (make-random-players 3)]
        (is (=  :yellow
                (p/color a)))
        (is (=  3
                (p/actions a)))
        (is (=  '(:sword :pistol :keys :flag :flag :sword)
                (p/cards a)))
        (is (=  :green
                (p/color b)))
        (is (=  3
                (p/actions b)))
        (is (=  '(:sword :hat :bottle :keys :pistol :bottle)
                (p/cards b)))
        (is (=  :red
                (p/color c)))
        (is (=  3
                (p/actions c)))
        (is (=  '(:keys :sword :flag :keys :pistol :sword)
                (p/cards c)))
        (let [[a b c] (make-random-players 3 pirate-colors 6 card-types)]
          (is (=  :yellow
                  (p/color a)))
          (is (=  3
                  (p/actions a)))
          (is (=  '(:bottle :hat :bottle :pistol :flag :flag)
                  (p/cards a)))
          (is (=  :green
                  (p/color b)))
          (is (=  3
                  (p/actions b)))
          (is (=  '(:keys :bottle :flag :pistol :sword :flag)
                  (p/cards b)))
          (is (=  :red
                  (p/color c)))
          (is (=  3
                  (p/actions c)))
          (is (=  '(:bottle :bottle :bottle :hat :sword :sword)
                  (p/cards c))))))))

(deftest colors-test
  (testing "colors-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (make-random-players 3 pirate-colors 6 card-types)]
        (is (= '(:yellow :green :red)
               (colors players)))))))

(deftest make-game-test
  (testing "make-turn-order-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [game (make-game)]
        (is (= 3
               (count (players game))))
        (is (= '(:yellow :green :red)
               (colors (players game))))
        (is (= 3
               (count (turn-order game))))
        (is (= :red
               (turn game)))
        (is (= :yellow
               (next-turn game)))
        (is (= 50
               (count (deck game))))
        (is (= 38
               (count (board game))))
        (is (= false
               (winner? game)))))))

;; getters
(deftest players-test
  (testing "players"
    (is (= the-players
           (players the-game)))))

(deftest turn-order-test
  (testing "turn-order"
    (is (= the-turn-order
           (turn-order the-game)))))

(deftest turn-test
  (testing "getting the turn"
    (is (= the-turn
           (turn the-game)))))

(deftest active-player-test
  (testing "active-player"
    (is (=  (player the-players the-turn)
            (active-player the-game)))))

(deftest active-player-color-test
  (testing "getting the active-player-color"
    (is (= the-turn
           (active-player-color the-game)))))

(deftest next-turn-test
  (testing "players"
    (is (= :yellow
           (next-turn the-game)))))

(deftest board-test
  (testing "board"
    (is (=  the-board
            (board the-game)))))

(deftest deck-test
  (testing "deck"
    (is (= the-deck
           (deck the-game)))))

(deftest winner-test
  (testing "winner?"
    (is (= false
           (winner? the-game)))
    (let [final-game (move-piece
                      (move-piece
                       (move-piece
                        (move-piece
                         (move-piece
                          (move-piece the-game 0 37) 0 37) 0 37) 0 37) 0 37) 0 37)]
      (is (= true
             (winner? final-game))))))

;;setters
(deftest set-players-test
  (testing "set-players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player-1 (p/make-player :yellow '(:bottle :keys :pistol :bottle :keys) 3)
            player-2 (p/make-player :red '(:keys :bottle :pistol :bottle :keys :sword) 0)
            [player-1' player-2'] (players (set-players the-game [player-1 player-2]))]
        (is (= [player-1 player-2]
               [player-1' player-2']))))))

;; TODO: blackbox-it
(deftest set-turn-order-test
  (testing "set-turn-order"
    (is (= {:red :yellow, :green :red, :yellow :green}
           (turn-order (set-turn-order the-game {:red :yellow, :green :red, :yellow :green}))))))

(deftest set-turn-test
  (testing "set-turn"
    (is (= :yellow
           (turn (set-turn the-game :yellow))))))

(deftest set-board-test
  (let [new-board (b/move-piece the-board 0 2 :green)]
    (testing "set-board"
      (is (=  new-board
              (board (set-board the-game new-board)))))))

(deftest set-deck-test
  (testing "set-deck"
    (is (= [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]
           (deck (set-deck the-game [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]))))))

;;state-changers
(deftest update-player-test
  (testing "update-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [new-player (p/make-player the-turn (d/random-deck 6))
            current-player (player the-players the-turn)]
        (is (=  new-player
                (update-player   the-turn
                                 new-player
                                 current-player)))
        (is (=  current-player
                (update-player   :brown
                                 {:whatever 0}
                                 current-player)))))))

(deftest update-player-in-players-test
  (testing "update-player-in-players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [original-player (player the-players :green)
            new-player (p/make-player :green (d/random-deck 6))
            new-players (update-player-in-players the-players
                                                  :green
                                                  new-player)
            updated-player (player new-players :green)]
        (is (not=  original-player
                   updated-player))
        (is (=  new-player
                updated-player))))))

(deftest add-random-card-to-active-player-test
  (testing "add-random-card-to-active-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [new-game (add-random-card-to-active-player the-game)
            new-players (players new-game)
            new-cards (cards new-players the-turn)
            new-card-amounts (d/cards-amounts new-cards)
            old-card-amounts (d/cards-amounts (p/cards (active-player the-game)))]
        (is (not= old-card-amounts
                  new-card-amounts))
        (is (not= (d/from-freqs-to-seq old-card-amounts)
                  (d/from-freqs-to-seq new-card-amounts)))
        (is (= (+ 1 (count (d/from-freqs-to-seq old-card-amounts)))
               (count (d/from-freqs-to-seq new-card-amounts))))))))

(deftest turn-played-test
  (testing "turn-played"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [one-turn-game  (turn-played the-game)
            two-turn-game  (turn-played one-turn-game)
            one-turn-act-pl-actions (p/actions (active-player one-turn-game))
            two-turn-act-pl-actions (p/actions (active-player two-turn-game))
            original-actions (p/actions (active-player the-game))]
        (is (= one-turn-act-pl-actions
               (- original-actions 1))
            "Only the actions of the active (green) player should change from 2 to 1")
        (is (= two-turn-act-pl-actions
               (- original-actions 2))
            "The actions of the active (green) player should change from 2 to 0 and the ones from the next (red) player should go from 0 to 3. Also the active player should go to the next one")))))

(deftest remove-played-card-test
  (testing "remove-played-card"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [removed-key :keys
            new-game (remove-played-card the-game removed-key)
            new-player (active-player new-game)
            new-cards (p/cards new-player)
            original-cards (p/cards (active-player the-game))]
        (is (= (count new-cards)
               (- (count original-cards) 1))
            "The actions of the active player should go down by 1")
        (is (= (count (filter #(= removed-key %) new-cards))
               (- (count (filter #(= removed-key %) original-cards)) 1))
            "Only the actions of the active player should go down by 1")))))

(deftest add-random-card-to-player-in-players-test
  (testing "add-random-card-to-active-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [new-players (add-random-card-to-player-in-players the-players
                                                              :green)
            new-cards (p/cards (player new-players :green))
            original-cards (p/cards (player the-players :green))]
        (is (=  (count new-cards)
                (+ 1 (count original-cards))))))))

(deftest add-random-card-to-active-player-test
  (testing "add-random-card-to-active-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [new-game (add-random-card-to-active-player the-game)
            new-players (players new-game)
            new-cards (p/cards (player new-players :green))
            original-cards (p/cards (player the-players :green))]
        (is (=  (count new-cards)
                (+ 1 (count original-cards))))))))

(deftest reset-actions-test
  (testing "reset-actions"
    (let [new-players  (reset-actions (decrease-actions the-players :red) :red)
          new-actions (p/actions (player new-players :red))
          original-actions (p/actions (player the-players :red))]
      (is (= new-actions
             original-actions))
      "Should put the decreased actions back to 3")))

(deftest decrease-actions-test
  (testing "decrease-actions"
    (let [new-players  (decrease-actions the-players :red)
          new-actions (p/actions (player new-players :red))
          original-actions (p/actions (player the-players :red))]
      (is (= new-actions
             (- original-actions 1)))
      "Should decrease actions by one")))

(deftest player-has-card?-test
  (testing "player-has-card?"
    (is (= true
           (player-has-card? the-players
                             :green
                             :keys))
        "The green player should have the :keys card")
    (is (= false
           (player-has-card? the-players
                             :green
                             :bottle))
        "The green player should have the :bottle card")
    (is (= true
           (player-has-card? the-players
                             :green
                             :flag))
        "The green player should not have the :flag card")))

(deftest actions-test
  (testing "actions-test"
    (is (= 3
           (actions the-players :green)
           (actions the-players :yellow)
           (actions the-players :red)))))

(deftest cards-test
  (testing "cards-test"
    (is (= green-cards
           (cards the-players :green)))))

(deftest player-test
  (testing "player-test"
    (is (= yellow-player
           (player the-players :yellow)))))
