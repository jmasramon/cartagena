(ns cartagena.data-abstractions.game-test
  (:require  [clojure.test :refer [deftest is testing]]
             [clojure.data.generators :refer [*rnd*]]
             [cartagena.data-abstractions.deck :as d]
             [cartagena.data-abstractions.player-bis :as p]
             [cartagena.data-abstractions.board :as b]
             [cartagena.data-abstractions.game :as g]))

;; trick to get private functions
(def g-random-initial-turn #'cartagena.data-abstractions.game/random-initial-turn)
(def g-make-turn-order #'cartagena.data-abstractions.game/make-turn-order)
(def g-make-random-players #'cartagena.data-abstractions.game/make-random-players)
(def g-colors #'cartagena.data-abstractions.game/colors)
(def g-player #'cartagena.data-abstractions.game/player)

(binding [*rnd* (java.util.Random. 12345)]
  (def yellow-cards (d/random-deck))
  (def green-cards (d/random-deck))
  (def red-cards (d/random-deck))
  (def yellow-player (p/make-player :yellow yellow-cards))
  (def the-players [yellow-player
                    (p/make-player :green green-cards)
                    (p/make-player :red red-cards)])
  (def the-turn (g-random-initial-turn (g-colors the-players)))
  (def the-turn-order (g-make-turn-order (g-colors the-players)))
  (def the-board (b/make-board the-players))
  (def the-deck (d/random-deck)))


(def the-game {:players the-players
               :turn-order the-turn-order
               :turn the-turn
               :board the-board
               :deck the-deck})

;; Add these to game_test.clj

(deftest random-initial-turn-is-random-test
  (testing "random-initial-turn produces variety"
    (let [turns (repeatedly 100 #(g-random-initial-turn g/PIRATE-COLORS))
          unique-turns (set turns)]
      (is (> (count unique-turns) 2)
          "Should produce multiple different colors in 100 draws"))))

(deftest random-initial-turn-distribution-test
  (testing "random-initial-turn has roughly uniform distribution"
    (let [sample-size 5000
          samples (repeatedly sample-size #(g-random-initial-turn g/PIRATE-COLORS))
          frequencies (frequencies samples)
          num-colors (count g/PIRATE-COLORS)
          expected (/ sample-size num-colors)]
      ;; Each color should appear roughly 1000 times (5000 / 5)
      ;; Allow 20% variance (800-1200)
      (doseq [[color freq] frequencies]
        (is (< 800 freq 1200)
            (str color " appeared " freq " times, expected ~" expected))))))

(deftest make-random-players-is-random-test
  (testing "make-random-players produces different hands"
    (let [players1 (g-make-random-players 3)
          players2 (g-make-random-players 3)
          cards1 (map p/cards players1)
          cards2 (map p/cards players2)]
      (is (not= cards1 cards2)
          "Two calls should produce different player hands"))))

(deftest make-random-players-variety-test
  (testing "make-random-players creates varied hands"
    (let [players (repeatedly 10 #(g-make-random-players 3))
          all-hands (mapcat #(map p/cards %) players)
          unique-hands (set all-hands)]
      (is (> (count unique-hands) 15)
          "Should produce many different hands across multiple games"))))

(deftest make-random-players-card-distribution-test
  (testing "make-random-players distributes cards across all types"
    (let [sample-size 100
          all-players (repeatedly sample-size #(g-make-random-players 3))
          all-cards (mapcat (fn [players]
                              (mapcat p/cards players))
                            all-players)
          frequencies (frequencies all-cards)
          total-cards (count all-cards)]
      ;; With 100 games * 3 players * 6 cards = 1800 cards total
      ;; Each of 6 card types should appear roughly 300 times
      ;; Allow 25% variance (225-375)
      (is (= 6 (count frequencies))
          "All card types should appear")
      (doseq [[card-type freq] frequencies]
        (is (< 225 freq 375)
            (str card-type " appeared " freq " times in " total-cards " total cards"))))))

(deftest random-initial-turn-test
  (testing "random-initial-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  :green
              (g-random-initial-turn  g/PIRATE-COLORS)))
      (is (=  :brown
              (g-random-initial-turn  g/PIRATE-COLORS)))
      (is (=  :brown
              (g-random-initial-turn  g/PIRATE-COLORS)))
      (is (=  :green
              (g-random-initial-turn  g/PIRATE-COLORS))))))

(deftest make-turn-order-test
  (testing "make-turn-order-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  {:green :red, :red :yellow, :yellow :green}
              (g-make-turn-order  (g-colors (g-make-random-players 3)))))
      (is (=  {:green :red, :red :yellow, :yellow :green}
              (g-make-turn-order  (g-colors (g-make-random-players 3))))))))

(deftest make-random-players-test
  (testing "Random players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c] (g-make-random-players 3)]
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
        (let [[a b c] (g-make-random-players 3)]
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
      (let [players (g-make-random-players 3)]
        (is (= '(:yellow :green :red)
               (g-colors players)))))))

(deftest make-game-test
  (testing "make-turn-order-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [game (g/make-game)]
        (is (= 3
               (count (g/players game))))
        (is (= '(:yellow :green :red)
               (g-colors (g/players game))))
        (is (= 3
               (count (g/turn-order game))))
        (is (= :red
               (g/turn game)))
        (is (= :yellow
               (g/next-turn game)))
        (is (= 50
               (count (g/deck game))))
        (is (= 38
               (count (g/board game))))
        (is (= false
               (g/winner? game)))))))

;; getters
(deftest players-test
  (testing "players"
    (is (= the-players
           (g/players the-game)))))

(deftest turn-order-test
  (testing "turn-order"
    (is (= the-turn-order
           (g/turn-order the-game)))))

(deftest turn-test
  (testing "getting the turn"
    (is (= the-turn
           (g/turn the-game)))))

(deftest active-player-test
  (testing "active-player"
    (is (=  (g-player the-players the-turn)
            (g/active-player the-game)))))

(deftest active-player-color-test
  (testing "getting the active-player-color"
    (is (= the-turn
           (g/active-player-color the-game)))))

(deftest next-turn-test
  (testing "players"
    (is (= :yellow
           (g/next-turn the-game)))))

(deftest board-test
  (testing "board"
    (is (=  the-board
            (g/board the-game)))))

(deftest deck-test
  (testing "deck"
    (is (= the-deck
           (g/deck the-game)))))

(deftest winner-test
  (testing "winner?"
    (is (= false
           (g/winner? the-game)))
    (let [final-game (g/move-piece
                      (g/move-piece
                       (g/move-piece
                        (g/move-piece
                         (g/move-piece
                          (g/move-piece the-game 0 37) 0 37) 0 37) 0 37) 0 37) 0 37)]
      (is (= true
             (g/winner? final-game))))))

;;setters
(deftest set-players-test
  (testing "set-players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player-1 (p/make-player :yellow '(:bottle :keys :pistol :bottle :keys) 3)
            player-2 (p/make-player :red '(:keys :bottle :pistol :bottle :keys :sword) 0)
            [player-1' player-2'] (g/players (g/set-players the-game [player-1 player-2]))]
        (is (= [player-1 player-2]
               [player-1' player-2']))))))

;; TODO: blackbox-it
(deftest set-turn-order-test
  (testing "set-turn-order"
    (is (= {:red :yellow, :green :red, :yellow :green}
           (g/turn-order (g/set-turn-order the-game {:red :yellow, :green :red, :yellow :green}))))))

(deftest set-turn-test
  (testing "set-turn"
    (is (= :yellow
           (g/turn (g/set-turn the-game :yellow))))))

(deftest set-board-test
  (let [new-board (b/move-piece the-board 0 2 :green)]
    (testing "set-board"
      (is (=  new-board
              (g/board (g/set-board the-game new-board)))))))

(deftest set-deck-test
  (testing "set-deck"
    (is (= [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]
           (g/deck (g/set-deck the-game [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]))))))

;;state-changers
(deftest update-player-in-players-test
  (testing "update-player-in-players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [original-player (g-player the-players :green)
            new-player (p/make-player :green (d/random-deck))
            new-players (g/update-player-in-players the-players
                                                    :green
                                                    new-player)
            updated-player (g-player new-players :green)]
        (is (not=  original-player
                   updated-player))
        (is (=  new-player
                updated-player))))))

(deftest add-random-card-to-active-player-test
  (testing "add-random-card-to-active-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [new-game (g/add-random-card-to-active-player the-game)
            new-cards (g/active-player-cards new-game)
            new-card-amounts (d/cards-amounts new-cards)
            old-card-amounts (d/cards-amounts (p/cards (g/active-player the-game)))]
        (is (not= old-card-amounts
                  new-card-amounts))
        (is (not= (d/from-freqs-to-seq old-card-amounts)
                  (d/from-freqs-to-seq new-card-amounts)))
        (is (= (+ 1 (count (d/from-freqs-to-seq old-card-amounts)))
               (count (d/from-freqs-to-seq new-card-amounts))))))))

(deftest turn-played-test
  (testing "turn-played"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [one-turn-game  (g/turn-played the-game)
            two-turn-game  (g/turn-played one-turn-game)
            one-turn-act-pl-actions (p/actions (g/active-player one-turn-game))
            two-turn-act-pl-actions (p/actions (g/active-player two-turn-game))
            original-actions (p/actions (g/active-player the-game))]
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
            new-game (g/remove-played-card the-game removed-key)
            new-player (g/active-player new-game)
            new-cards (p/cards new-player)
            original-cards (p/cards (g/active-player the-game))]
        (is (= (count new-cards)
               (- (count original-cards) 1))
            "The actions of the active player should go down by 1")
        (is (= (count (filter #(= removed-key %) new-cards))
               (- (count (filter #(= removed-key %) original-cards)) 1))
            "Only the actions of the active player should go down by 1")))))

(deftest add-random-card-to-player-in-players-test
  (testing "add-random-card-to-active-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [new-players (g/add-random-card-to-player-in-players the-players
                                                                :green)
            new-cards (p/cards (g-player new-players :green))
            original-cards (p/cards (g-player the-players :green))]
        (is (=  (count new-cards)
                (+ 1 (count original-cards))))))))

(deftest reset-actions-test
  (testing "reset-actions"
    (let [new-players  (g/reset-actions (g/decrease-actions the-players :red) :red)
          new-actions (p/actions (g-player new-players :red))
          original-actions (p/actions (g-player the-players :red))]
      (is (= new-actions
             original-actions))
      "Should put the decreased actions back to 3")))

(deftest decrease-actions-test
  (testing "decrease-actions"
    (let [new-players  (g/decrease-actions the-players :red)
          new-actions (p/actions (g-player new-players :red))
          original-actions (p/actions (g-player the-players :red))]
      (is (= new-actions
             (- original-actions 1)))
      "Should decrease actions by one")))

(deftest player-has-card?-test
  ;; (println "Testing player-has-card?")
  ;; (println "The players:  " the-players)
  ;; (println "Green player cards: " (p/cards (player the-players :green)))
  ;; (println "Green player has keys: " (player-has-card? the-players :green :keys))
  ;; (println "Green player has bottle: " (player-has-card? the-players :green :bottle))
  ;; (println "Green player has flag: " (player-has-card? the-players :green :flag))
  (testing "player-has-card?"
    (is (= true
           (g/player-has-card? the-players
                               :green
                               :keys))
        "The green player should have the :keys card")
    (is (= true
           (g/player-has-card? the-players
                               :green
                               :bottle))
        "The green player should have the :bottle card")
    (is (= true
           (g/player-has-card? the-players
                               :green
                               :flag))
        "The green player should not have the :flag card")))

(deftest active-player-actions-test
  (testing "active-player-actions-test"
    (is (= 3
           (g/active-player-actions the-game)))))

(deftest active-player-cards-test
  (testing "active-player-cards-test"
    (is (= green-cards
           (g/active-player-cards the-game)))))

(deftest playable-cards-test
  ;; (println "Testing playable-cards")
  ;; (println "Active player cards: " (g/active-player-cards the-game))

  (testing "playable-cards"
    (is (=  '([:bottle 10] [:flag 8] [:hat 8] [:keys 7] [:pistol 7] [:sword 10])
            (g/playable-cards the-game)))))


(deftest player-test
  (testing "player-test"
    (is (= yellow-player
           (g-player the-players :yellow)))))

(deftest next-player-color-test
  (testing "next-player-color"
    (is (= :yellow
           (g/next-player-color the-game))
        "Should return the next player color based on turn order")))

(deftest pass-turn-test
  (testing "pass-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [passed-game (g/pass-turn the-game)
            next-player (g/active-player passed-game)]
        (is (= :yellow
               (g/active-player-color passed-game))
            "Should change to next player")
        (is (= 3
               (p/actions next-player))
            "Next player should have 3 actions reset")))))

(deftest decrease-actions-from-active-player-test
  (testing "decrease-actions-from-active-player"
    (let [decreased-game (g/decrease-actions-from-active-player the-game)
          active-player-after (g/active-player decreased-game)]
      (is (= 2
             (p/actions active-player-after))
          "Active player should have one less action"))))

(deftest available-piece?-test
  (testing "available-piece?"
    (is (= true
           (g/available-piece? the-game 0))
        "Should return true for start square with pieces")
    (is (= false
           (g/available-piece? the-game 1))
        "Should return false for empty square")))

(deftest available-fall-back?-test
  (testing "available-fall-back?"
    ;; Create a scenario where we have pieces at start (0) and move some to create a fallback scenario
    (let [game-with-pieces-moved (-> the-game
                                     (g/move-piece 0 3)  ; Move one piece to position 3
                                     (g/move-piece 0 8))] ; Move another piece to position 8
      (is (= true
             (g/available-fall-back? game-with-pieces-moved 8))
          "Should return true when there's a non-empty square to fall back to")
      (is (= false
             (g/available-fall-back? the-game 1))
          "Should return false when there's no non-empty square to fall back to"))))

(deftest fallback-square-index-test
  (testing "fallback-square-index"
    ;; Create a scenario with pieces at multiple positions
    (let [game-with-pieces-moved (-> the-game
                                     (g/move-piece 0 3)  ; Move one piece to position 3
                                     (g/move-piece 0 8))] ; Move another piece to position 8
      (is (= 3
             (g/fallback-square-index game-with-pieces-moved 8))
          "Should return index of closest non-empty square"))))

(deftest next-empty-slot-index-test
  (testing "next-empty-slot-index"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [game (g/make-game)]
        (is (number? (g/next-empty-slot-index game :keys 0))
            "Should return a valid index for next empty slot of given card type")))))
