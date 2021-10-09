(ns cartagena.data-abstractions.player-test
  (:require [clojure.test :refer :all]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.core :refer [pirate-colors card-types]]
            [cartagena.data-abstractions.player :refer :all]
            [cartagena.data-abstractions.player :refer :all]
            [cartagena.data-abstractions.game :refer [make-random-players]]))

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

(deftest set-cards-test
  (testing "set-cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (make-random-players 1 pirate-colors 6 card-types))]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards (set-cards player '(:sword :pistol :keys :flag :flag :sword)))))))))

(deftest add-random-card-to-player-test
  (testing "add-random-card-to-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  {:yellow {:cards '(:bottle :bottle :keys :keys :pistol :sword :sword), :actions 0}}
              (add-random-card-to-player {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}))))))

(deftest reset-actions-test
  (testing "reset-actions"
    (is (= {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
           (reset-actions {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))))

(deftest decrease-actions-test
  (testing "decrease-actions"
    (is (=
         {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 2}}
         (decrease-actions {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}})))))

(deftest color-test
  (testing "color-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (make-random-players 1 pirate-colors 6 card-types))]
        (is (= :yellow
               (color player)))))))

(deftest player-has-card?-test
  (testing "player-has-card?"
    (is (= true
           (player-has-card? {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                             :bottle)))
    (is (= false
           (player-has-card? {:yellow {:cards '(:bottle :keys :pistol :bottle :keys), :actions 0}}
                             :sword)))))

(deftest actions-test
  (testing "actions-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= 2
             (actions {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 2}}))))))

(deftest cards-test
  (testing "cards-test"
    (is (= '(:bottle :keys :pistol :bottle :keys :sword)
           (cards {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))))

(deftest set-actions-test
  (testing "set-actions-test"
    (is (= {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
           (set-actions {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}} 3)))))