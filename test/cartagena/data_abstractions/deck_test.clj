(ns cartagena.data-abstractions.deck-test
  (:require  [clojure.test :refer [deftest is testing]]
             [clojure.data.generators :refer [*rnd*]]
             [cartagena.core :refer [card-types]]
             [cartagena.data-abstractions.deck :refer :all]))

(deftest random-deck-test
  (testing "random-deck"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [[a b c d & more] (random-deck 50 card-types)]
        (is (=  :sword
                a))
        (is (=  :pistol
                b))
        (is (=  :keys
                c))
        (is (=  :flag
                d))
        (is (=  46
                (count more)))))))

(deftest draw-one-from-test
  (testing "draw-one-from"
    (is (=  [:sword '(:pistol :keys :flag)]
            (draw-one-from '(:sword :pistol :keys :flag))))))

(deftest draw-test
  (testing "draw"
    (is (=  ['(:sword :pistol :keys) '(:flag)]
            (draw '(:sword :pistol :keys :flag) 3)))))

(deftest cards-amounts-test
  (testing "cards-amounts"
      (is (=  {:bottle 2, :flag 4, :hat 4, :keys 1, :pistol 2, :pistolhat 1, :sword 4}
              (cards-amounts [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat])))))

(deftest from-freqs-to-seq-test
  (testing "from-freqs-to-seq"
    (let [res (from-freqs-to-seq {:bottle 2, :flag 4, :hat 4, :keys 1, :pistol 2, :pistolhat 1, :sword 4})]
      (is (=  '(:bottle :bottle :flag :flag :flag :flag :hat :hat :hat :hat :keys :pistol :pistol :pistolhat :sword :sword :sword :sword)
              res)))))

(deftest remove-card-from-test
  (testing "remove-card-from"
    (let [res (remove-card-from [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat] :hat)]
      (is (=  [:flag :flag :flag :flag :sword :sword :sword :sword :hat :hat :hat :pistol :pistol :bottle :bottle :keys :pistolhat]
              res)))))

(deftest add-card-test
  (testing "add-card"
    (let [res (add-card [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat] :hat)]
      (is (=  [:flag :flag :flag :flag :sword :sword :sword :sword :hat :hat :hat :hat :hat :pistol :pistol :bottle :bottle :keys :pistolhat]
              res)))))

(deftest random-card-test
  (testing "random-card"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [card (random-card card-types)]
        (is (contains? card-types card)
            "Should return a valid card type")
        (is (= :sword card)
            "Should return predictable result with fixed seed")))))
