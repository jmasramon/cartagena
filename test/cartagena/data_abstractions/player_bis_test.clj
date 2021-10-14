(ns cartagena.data-abstractions.player-bis-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.core :refer [pirate-colors card-types]]
            [cartagena.data-abstractions.game]
            [cartagena.data-abstractions.player-bis :refer [actions add-random-card-to-player-in-players cards color decrease-actions make-player player-has-card? reset-actions set-actions set-cards]]))

(def make-random-players #'cartagena.data-abstractions.game/make-random-players)


(deftest make-player-test
  (testing "make-player"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (make-player :yellow '(:sword :pistol :keys :flag :flag :sword))]
        (is (=  :yellow
                (color player)))
        (is (=  3
                (actions player)))
        (is (=  '(:sword :pistol :keys :flag :flag :sword)
                (cards player))))
      (let [player (make-player :green '(:sword :hat :bottle :keys :pistol :bottle))]
        (is (=  :green
                (color player)))
        (is (=  3
                (actions player)))
        (is (=  '(:sword :hat :bottle :keys :pistol :bottle)
                (cards player))))
      (let [player (make-player :red '(:keys :sword :flag :keys :pistol :sword))]
        (is (=  :red
                (color player)))
        (is (=  3
                (actions player)))
        (is (=  '(:keys :sword :flag :keys :pistol :sword)
                (cards player)))))))

(deftest set-cards-test
  (testing "set-cards-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (first (make-random-players 1 pirate-colors 6 card-types))]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards (set-cards player '(:sword :pistol :keys :flag :flag :sword)))))))))

(deftest add-random-card-to-player-in-players-test
  (testing "add-random-card-to-player-in-players"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [player (make-player :yellow '(:bottle :keys :pistol :bottle :keys :sword) 0)]
        (is (= '(:bottle :bottle :keys :keys :pistol :sword :sword)
               (cards (add-random-card-to-player-in-players player))))))))

(deftest reset-actions-test
  (testing "reset-actions"
    (let [player (make-player :yellow '(:bottle :keys :pistol :bottle :keys :sword) 0)]
      (is (= 3
             (actions (reset-actions player)))))))

(deftest decrease-actions-test
  (testing "decrease-actions"
    (let [player (make-player :red '(:bottle :pistol :flag :flag :keys :bottle) 3)]
      (is (= 2
             (actions (decrease-actions player)))))))

(deftest player-has-card?-test
  (testing "player-has-card?"
    (is (= true
           (player-has-card? (make-player :yellow '(:bottle :keys :pistol :bottle :keys :sword) 0)
                             :bottle)))
    (is (= false
           (player-has-card? (make-player :yellow '(:bottle :keys :pistol :bottle :keys) 0)
                             :sword)))))

(deftest actions-test
  (testing "actions-test"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (= 2
             (actions
              (make-player :yellow '(:bottle :keys :pistol :bottle :keys :sword) 2)))))))

(deftest cards-test
  (testing "cards-test"
    (is (= '(:bottle :keys :pistol :bottle :keys :sword)
           (cards (make-player :yellow '(:bottle :keys :pistol :bottle :keys :sword) 0))))))

(deftest set-actions-test
  (testing "set-actions-test"
    (is (= 3
           (actions (set-actions (make-player :yellow '(:bottle :keys :pistol :bottle :keys :sword) 0) 3))))))