(ns cartagena.generators-test
  (:require [clojure.test :refer :all]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.core :refer :all]
            [cartagena.generators :refer :all]))

(deftest a-test
  (testing "compare lists"
    (is (=  '(:yellow :yellow :green)
            '(:yellow :yellow :green)))))

(deftest random-players-test
  (testing "Random players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [ [a b c ] (random-players 3 pirate-colors 6 card-types)]
        (is (=  {:yellow {:cards '(:sword :pistol :keys :flag :flag :sword), :actions 3}}
                a))
        (is (=  {:green {:cards '(:sword :hat :bottle :keys :pistol :bottle), :actions 3}}
                b))
        (is (=  {:red {:cards '(:keys :sword :flag :keys :pistol :sword), :actions 3}}
                c))
        ))))

(deftest get-random-players-colors
  (testing "get-random-players-colors"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [ players (random-players 3 pirate-colors 6 card-types)]
        (is (= '(:yellow :green :red)
                (players-colors players)))
      ))))

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
                  (random-initial-turn  pirate-colors)))
      )))

(deftest players-turns-test
  (testing "players-turns-turn"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [res1 (players-turns  (flatten (map keys (random-players 3 pirate-colors 6 card-types))))
            res2 (players-turns  (flatten (map keys (random-players 3 pirate-colors 6 card-types))))]
        (is (=  {:green :red, :red :yellow, :yellow :green}
                res1))
        (is (=  {:green :red, :red :yellow, :yellow :green}
                res2))
        ))))

(deftest random-deck-test
  (testing "random-deck"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [ [a b c d & more] (random-deck 50 card-types)]
        (is (=  :sword
                a))
        (is (=  :pistol
                b))
        (is (=  :keys
                c))
        (is (=  :flag
                d))
        (is (=  46
                (count more)))
        ))))

(deftest starting-pieces-test
  (testing "starting-pieces"
    (let [res (starting-pieces '(:red :green :blue))]
      (is (=  {
        :pieces {:blue 6, :green 6, :red 6}, :type :start} 
              res)))))

(deftest new-board-section-test
  (testing "new-board-section"
    (binding [*rnd* (java.util.Random. 12345)]
        (is (=  [{
          :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :sword}]
              (new-board-section card-types '(:red :green :yellow))))
        (is (=  [{
          :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :sword}]
              (new-board-section card-types '(:red :green :yellow))))
        )))

(deftest initial-board-test
  (testing "initial-board"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [res (initial-board card-types (random-players 3 pirate-colors 6 card-types))]
        (is (=  [{
          :pieces {:green 6, :red 6, :yellow 6}, :type :start} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :sword} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :sword} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :sword} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :sword} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :sword} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :sword} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :pistol} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :keys} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :hat} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :flag} {
            :pieces {:green 0, :red 0, :yellow 0}, :type :bottle} {
          :pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
              res))))))

; (deftest initial-state-test
;   (testing "Initial state generator"
;     (binding [*rnd* (java.util.Random. 12345)]
;       (is (= {}
;               (initial-state))
;       ))))
