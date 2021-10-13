(ns cartagena.data-abstractions.player-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.data-abstractions.player :refer :all]))

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
      (let [player (make-player :green '())]
        (is (= '(:sword :pistol :keys :flag :flag :sword)
               (cards (set-cards player '(:sword :pistol :keys :flag :flag :sword)))))))))

(deftest add-random-card-to-player-in-players-test
  (testing "add-random-card-to-player-in-players"
    (binding [*rnd* (java.util.Random. 12345)]
      (is (=  {:yellow {:cards '(:bottle :bottle :keys :keys :pistol :sword :sword), :actions 0}}
              (add-random-card-to-player-in-players {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}))))))

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
      (let [player (make-player :yellow '())]
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