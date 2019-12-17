(ns cartagena.getters.mainEntities-test
  (:require [clojure.test :refer :all]
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

(deftest players-test
    (testing "players"
        (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
            (players fullGameState)))
        ))

(deftest turn-order-test
    (testing "turn-order"
        (is (= {:green :red, :red :yellow, :yellow :green}
                (turn-order fullGameState)))

        ))

(deftest turn-test
    (testing "players"
        (is (= :green
                (turn fullGameState)))
        ))

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
            (board fullGameState)))
    ))

(deftest deck-test
    (testing "players"
        (is (= [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]
                (deck fullGameState)))

        ))

