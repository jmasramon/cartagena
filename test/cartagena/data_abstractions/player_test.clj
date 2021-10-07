(ns cartagena.data-abstractions.player-test
  (:require [clojure.test :refer :all]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.core :refer [pirate-colors card-types]]
            [cartagena.data-abstractions.player :refer :all]))

(deftest random-players-test
  (testing "Random players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c] (random-players 3 pirate-colors 6 card-types)]
        (is (=  {:yellow {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                a))
        (is (=  {:green {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                b))
        (is (=  {:red {:actions 3, :cards '(:keys :sword :flag :keys :pistol :sword)}}
                c))))))

(deftest set-cards-test
  (testing "set-cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (random-players 1 pirate-colors 6 card-types))]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (player-cards (set-cards player '(:sword :pistol :keys :flag :flag :sword)))))))))

(deftest update-player-test
  (testing "update-player"
    (is (=  {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
            (update-player   :green
                             {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                             {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))
    (is (=  {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            (update-player   :yellow
                             {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                             {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))))

(deftest update-player-in-players-test
  (testing "update-player-in-players"
    (is (=  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
             {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
             {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
            (update-player-in-players    [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                          {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                          {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                                         :green
                                         {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}})))))

(deftest add-turns-test
  (testing "add-turns"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 3}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
           (add-turns [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                       {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                       {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                      :green)))
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
           (add-turns [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                       {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                       {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                      :red)))))

(deftest decrease-turns-test
  (testing "decrease-turns"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 2}}]
           (decrease-turns [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                           :red)))
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 2}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
           (decrease-turns [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 3}}
                            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                           :green)))))

(deftest players-color-test
  (testing "player-color-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (random-players 1 pirate-colors 6 card-types))]
        (is (= :yellow
               (player-color player)))))))

(deftest players-colors-test
  (testing "players-colors-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (random-players 3 pirate-colors 6 card-types)]
        (is (= '(:yellow :green :red)
               (players-colors players)))))))

(deftest player-cards-test
  (testing "player-cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (random-players 1 pirate-colors 6 card-types))]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (player-cards player)))))))

(deftest player-test
  (testing "player-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (random-players 3 pirate-colors 6 card-types)]
        (is (= {:yellow {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
               (player players :yellow)))))))

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

(deftest players-turn?-test
  (testing "players-turn?"
    (is (= false
           (players-turn? [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                           {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                           {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                          :green)))
    (is (= true
           (players-turn? [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                           {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                           {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                          :red)))))

(deftest actions-test
  (testing "actions-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (random-players 3 pirate-colors 6 card-types)]
        (is (= 3
               (actions players :yellow)))))))

(deftest cards-test
  (testing "cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (random-players 3 pirate-colors 6 card-types)]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards players :green)))))))

