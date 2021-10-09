(ns cartagena.data-abstractions.player-test
  (:require [clojure.test :refer :all]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.core :refer [pirate-colors card-types]]
            [cartagena.data-abstractions.player :refer :all]))

(deftest make-player-test
  (testing "make-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [make-player #'cartagena.data-abstractions.player/make-player] ;; private function
        (is (=  {:yellow {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                (make-player :yellow '(:sword :pistol :keys :flag :flag :sword))))
        (is (=  {:green {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                (make-player :green '(:sword :hat :bottle :keys :pistol :bottle))))
        (is (=  {:red {:actions 3, :cards '(:keys :sword :flag :keys :pistol :sword)}}
                (make-player :red '(:keys :sword :flag :keys :pistol :sword))))))))

(deftest random-players-test
  (testing "Random players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c] (random-players 3)]
        (is (=  {:yellow {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                a))
        (is (=  {:green {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                b))
        (is (=  {:red {:actions 3, :cards '(:sword :pistol :keys :flag :flag :sword)}}
                c)))
      (let [[a b c] (random-players 3 pirate-colors 6 card-types)]
        (is (=  {:yellow {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                a))
        (is (=  {:green {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                b))
        (is (=  {:red {:actions 3, :cards '(:sword :hat :bottle :keys :pistol :bottle)}}
                c))))))

(deftest set-cards-test
  (testing "set-cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (random-players 1 pirate-colors 6 card-types))]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards (set-cards player '(:sword :pistol :keys :flag :flag :sword)))))))))

(deftest update-player-test
  (testing "update-player"
    (let [update-player #'cartagena.data-abstractions.player/update-player]
      (is (=  {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
              (update-player   :green
                               {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                               {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))
      (is (=  {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
              (update-player   :yellow
                               {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                               {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}))))))

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

(deftest add-random-card-to-player-test
  (testing "add-random-card-to-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  {:yellow {:cards '(:bottle :bottle :keys :keys :pistol :sword :sword), :actions 0}}
              (add-random-card-to-player {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))
      (is (=  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
               {:green {:cards '(:bottle :bottle :keys :keys :pistol :pistol :sword), :actions 0}}
               {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
              (add-random-card-to-player [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                          {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                          {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                                         :green))))))

(deftest reset-actions-test
  (testing "reset-actions"
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 3}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
           (reset-actions [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                           {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                           {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                          :green)))
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
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 2}}]
           (decrease-actions [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                              {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                             :red)))
    (is (= [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
            {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 2}}
            {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
           (decrease-actions [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                              {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 3}}
                              {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 0}}]
                             :green)))))

(deftest color-test
  (testing "color-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (random-players 1 pirate-colors 6 card-types))]
        (is (= :yellow
               (color player)))))))

(deftest colors-test
  (testing "colors-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [players (random-players 3 pirate-colors 6 card-types)]
        (is (= '(:yellow :green :red)
               (colors players)))))))

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
               (cards players :green)))
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards (first players))))))))

(deftest set-actions-test
  (testing "set-actions-test"
    (is (= {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
           (set-actions {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}} 3)))))