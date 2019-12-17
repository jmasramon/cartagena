(ns cartagena.setters.mainEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.setters.mainEntities :refer :all]
            [cartagena.getters.mainEntities :refer :all]))

(def fullGameState {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}],
                    :turn-order {:green :red, :red :yellow, :yellow :green}
                    :turn :green
                    :board [
                        {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                    :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]})

(deftest set-players-test
    (testing "set-players"
        (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys), :actions 3}}
                {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]
            (players (set-players fullGameState  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys), :actions 3}}
                                                    {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]))))
        ))

(deftest set-turn-order-test
    (testing "set-turn-order"
        (is (= {:red :yellow, :green :red, :yellow :green}
                (turn-order (set-turn-order fullGameState {:red :yellow, :green :red, :yellow :green}))))
        ))

(deftest set-turn-test
    (testing "set-turn"
        (is (= :yellow
                (turn (set-turn fullGameState :yellow))))
        ))

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
                {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]))))
    ))

(deftest set-deck-test
    (testing "set-deck"
        (is (= [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]
                (deck (set-deck fullGameState [:pistol :bottle :flag :sword :hat :flag :sword :hat  :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]))))

        ))

