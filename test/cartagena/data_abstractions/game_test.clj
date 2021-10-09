(ns cartagena.data-abstractions.game-test
  (:require  [clojure.test :refer :all]
             [clojure.data.generators :refer [*rnd*]]
             [cartagena.core :refer [pirate-colors card-types]]
             [cartagena.data-abstractions.game :refer :all]))

(def fullGameState {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}
                              {:red {:cards '(:keys :sword :pistol :bottle :keys :sword), :actions 0}}]
                    :turn-order {:green :red, :red :yellow, :yellow :green}
                    :turn :green
                    :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                            {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                    :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]})

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
              (make-turn-order  (flatten (map keys (make-random-players 3 pirate-colors 6 card-types))))))
      (is (=  {:green :red, :red :yellow, :yellow :green}
              (make-turn-order  (flatten (map keys (make-random-players 3 pirate-colors 6 card-types)))))))))

(deftest make-random-players-test
  (testing "Random players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c] (make-random-players 3)]
        (is (=  {:yellow {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                a))
        (is (=  {:green {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                b))
        (is (=  {:red {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                c)))
      (let [[a b c] (make-random-players 3 pirate-colors 6 card-types)]
        (is (=  {:yellow {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                a))
        (is (=  {:green {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                b))
        (is (=  {:red {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                c))))))


(deftest colors-test
  (testing "colors-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (make-random-players 3 pirate-colors 6 card-types)]
        (is (= '(:yellow :green :red)
               (colors players)))))))



;; TODO: make this test work by making the result of make-game deterministic
;; (deftest make-game-test
;;   (testing "make-turn-order-turn"
;;     (binding [*rnd* (java.util.Random. 12345)]
;;       (let [res (make-game)]
;;         (is (=  {:board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
;;                          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
;;                  :deck '(:pistol
;;                          :flag
;;                          :flag
;;                          :keys
;;                          :bottle
;;                          :flag
;;                          :pistol
;;                          :sword
;;                          :flag
;;                          :bottle
;;                          :bottle
;;                          :bottle
;;                          :hat
;;                          :sword
;;                          :sword
;;                          :hat
;;                          :pistol
;;                          :bottle
;;                          :flag
;;                          :hat
;;                          :pistol
;;                          :sword
;;                          :hat
;;                          :keys
;;                          :sword
;;                          :bottle
;;                          :hat
;;                          :hat
;;                          :keys
;;                          :hat
;;                          :bottle
;;                          :bottle
;;                          :keys
;;                          :sword
;;                          :flag
;;                          :sword
;;                          :flag
;;                          :keys
;;                          :keys
;;                          :flag
;;                          :hat
;;                          :sword
;;                          :bottle
;;                          :hat
;;                          :keys
;;                          :hat
;;                          :hat
;;                          :hat
;;                          :pistol
;;                          :keys)
;;                  :players [{:yellow {:actions 3
;;                                      :cards '(:flag :flag :sword :sword :hat :bottle)}}
;;                            {:green {:actions 3
;;                                     :cards '(:keys :pistol :bottle :keys :sword :flag)}}
;;                            {:red {:actions 3
;;                                   :cards '(:keys :pistol :sword :bottle :hat :bottle)}}]
;;                  :turn :red
;;                  :turn-order {:green :red, :red :yellow, :yellow :green}}
;;                 res))))))

;; getters
(deftest players-test
  (testing "players"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}
            {:red {:actions 0, :cards '(:keys :sword :pistol :bottle :keys :sword)}}]
           (players fullGameState)))))

(deftest turn-order-test
  (testing "turn-order"
    (is (= {:green :red, :red :yellow, :yellow :green}
           (turn-order fullGameState)))))

(deftest turn-test
  (testing "getting the turn"
    (is (= :green
           (turn fullGameState)))))

(deftest active-player-test
  (testing "active-player"
    (is (=  {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}
            (active-player {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                      {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                                      {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                            :turn-order {:green :red, :red :yellow, :yellow :green}
                            :turn :red})))))

(deftest active-player-color-test
  (testing "getting the active-player-color"
    (is (= :green
           (active-player-color fullGameState)))
    (is (=  :red
            (active-player-color {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                                  :turn-order {:green :red, :red :yellow, :yellow :green}
                                  :turn :red})))))

(deftest next-turn-test
  (testing "players"
    (is (= :red
           (next-turn fullGameState)))))

(deftest board-test
  (testing "board"
    (is (=  [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
            (board fullGameState)))))

(deftest deck-test
  (testing "players"
    (is (= [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]
           (deck fullGameState)))))

(deftest winner-test
  (testing "winner?"
    (is (= false
           (winner? {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                     :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]})))
    (is (= true
           (winner? {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                     :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                             {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                             {:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]})))))

;;setters
(deftest set-players-test
  (testing "set-players"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys), :actions 3}}
            {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]
           (players (set-players fullGameState  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys), :actions 3}}
                                                 {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]))))))

(deftest set-turn-order-test
  (testing "set-turn-order"
    (is (= {:red :yellow, :green :red, :yellow :green}
           (turn-order (set-turn-order fullGameState {:red :yellow, :green :red, :yellow :green}))))))

(deftest set-turn-test
  (testing "set-turn"
    (is (= :yellow
           (turn (set-turn fullGameState :yellow))))))

(deftest set-board-test
  (testing "set-board"
    (is (=  [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
             {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
            (board (set-board fullGameState [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                                             {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]))))))

(deftest set-deck-test
  (testing "set-deck"
    (is (= [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]
           (deck (set-deck fullGameState [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]))))))

(deftest add-random-card-to-active-player-test
  (testing "add-random-card-to-active-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                        {:green {:cards '(:keys :keys :bottle :bottle :pistol :sword :sword), :actions 2}}
                        {:red {:actions 0
                               :cards '(:keys :sword :pistol :bottle :keys :sword)}}]
              :turn-order {:green :red, :red :yellow, :yellow :green}
              :turn :green
              :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
              :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]}
             (add-random-card-to-active-player fullGameState))))))

(deftest turn-played-test
  (testing "turn-played"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= {:players [{:yellow {:actions 0
                                  :cards '(:bottle :keys :pistol :bottle :keys :sword)}}
                        {:green {:actions 1
                                 :cards '(:keys :bottle :pistol :bottle :keys :sword)}}
                        {:red {:actions 0
                               :cards '(:keys :sword :pistol :bottle :keys :sword)}}]
              :turn-order {:green :red, :red :yellow, :yellow :green}
              :turn :green
              :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
              :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]}
             (turn-played fullGameState))
          "Only the actions of the active (green) player should change from 2 to 1")
      (is (= {:players [{:yellow {:actions 0
                                  :cards '(:bottle :keys :pistol :bottle :keys :sword)}}
                        {:green {:actions 0
                                 :cards '(:keys :bottle :pistol :bottle :keys :sword)}}
                        {:red {:actions 3
                               :cards '(:keys :sword :pistol :bottle :keys :sword)}}]
              :turn-order {:green :red, :red :yellow, :yellow :green}
              :turn :red
              :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
              :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]}
             (turn-played (turn-played fullGameState)))
          "The actions of the active (green) player should change from 2 to 0 and the ones from the next (red) player should go from 0 to 3. Also the active player should go to the next one"))))

(deftest remove-played-card-test
  (testing "remove-played-card"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= {:players [{:yellow {:actions 0
                                  :cards '(:bottle :keys :pistol :bottle :keys :sword)}}
                        {:green {:actions 2
                                 :cards '(:keys :bottle :bottle :pistol :sword)}}
                        {:red {:actions 0
                               :cards '(:keys :sword :pistol :bottle :keys :sword)}}]
              :turn-order {:green :red, :red :yellow, :yellow :green}
              :turn :green
              :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
              :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]}
             (remove-played-card fullGameState :keys))
          "Only the actions of the active (green) player should change from 2 to 1"))))

(deftest add-random-card-to-player-test
  (testing "add-random-card-to-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
               {:green {:cards '(:bottle :bottle :keys :keys :pistol :sword :sword), :actions 0}}
               {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
              (add-random-card-to-player [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                          {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                          {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                                         :green))))))

(deftest reset-actions-test
  (testing "reset-actions"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
           (reset-actions [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                           {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                           {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                          :red)))))

(deftest decrease-actions-test
  (testing "decrease-actions"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 2}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
           (decrease-actions [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 3}}
                              {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                             :green)))))

(deftest player-has-card?-test
  (testing "player-has-card?"
    (is (= true
           (player-has-card? [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                              {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                             :green
                             :bottle)))

    (is (= false
           (player-has-card? [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:bottle :keys :pistol :bottle :keys :pistol), :actions 3}}
                              {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                             :green
                             :sword)))))

(deftest actions-test
  (testing "actions-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (make-random-players 3 pirate-colors 6 card-types)]
        (is (= 3
               (actions players :yellow)))))))

(deftest cards-test
  (testing "cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (make-random-players 3 pirate-colors 6 card-types)]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards players :green)))))))

(deftest player-test
  (testing "player-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (make-random-players 3 pirate-colors 6 card-types)]
        (is (= {:yellow {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
               (player players :yellow)))))))
