(ns cartagena.data-abstractions.game-test
  (:require  [clojure.test :refer :all]
             [clojure.data.generators :refer [*rnd*]]
             [cartagena.core :refer [pirate-colors card-types]]
             [cartagena.data-abstractions.player :refer [random-players]]
             [cartagena.data-abstractions.game :refer :all]))

(def fullGameState {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
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

(deftest players-turns-test
  (testing "players-turns-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [res1 (players-turns  (flatten (map keys (random-players 3 pirate-colors 6 card-types))))
            res2 (players-turns  (flatten (map keys (random-players 3 pirate-colors 6 card-types))))]
        (is (=  {:green :red, :red :yellow, :yellow :green}
                res1))
        (is (=  {:green :red, :red :yellow, :yellow :green}
                res2))))))

;; TODO: make this test work by making the result of make-game deterministic
;; (deftest make-game-test
;;   (testing "players-turns-turn"
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
            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
           (players fullGameState)))))

(deftest turn-order-test
  (testing "turn-order"
    (is (= {:green :red, :red :yellow, :yellow :green}
           (turn-order fullGameState)))))

(deftest turn-test
  (testing "players"
    (is (= :green
           (turn fullGameState)))))

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

(deftest active-player-test
  (testing "active-player"
    (is (=  {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}
            (active-player {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                      {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                                      {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                            :turn-order {:green :red, :red :yellow, :yellow :green}
                            :turn :red})))))

(deftest active-player-color-test
  (testing "active-player-color"
    (is (=  :red
            (active-player-color {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                                  :turn-order {:green :red, :red :yellow, :yellow :green}
                                  :turn :red})))))

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

(deftest start-turn-test
  (testing "start-turn"
    (is (=  {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                       {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                       {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]}
            (start-turn {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                   {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                   {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]}
                        :green)))))