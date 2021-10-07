(ns cartagena.data-abstractions.cards-test
  (:require [clojure.test :refer :all]
            [cartagena.data-abstractions.cards :refer :all]))

(deftest cards-amounts-test
  (testing "cards-amounts"
    (let [res (cards-amounts [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat])]
      (is (=  {:bottle 2, :flag 4, :hat 4, :keys 1, :pistol 2, :pistolhat 1, :sword 4}
              res)))))

(deftest playable-cards-test
  (testing "playable-cards"
    (let [res (playable-cards [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat])]
      (is (=  '([:flag 4] [:sword 4] [:hat 4] [:pistol 2] [:bottle 2] [:keys 1] [:pistolhat 1])
              res)))))

(deftest from-freqs-to-seq-test
  (testing "from-freqs-to-seq"
    (let [res (from-freqs-to-seq {:bottle 2, :flag 4, :hat 4, :keys 1, :pistol 2, :pistolhat 1, :sword 4})]
      (is (=  '(:bottle :bottle :flag :flag :flag :flag :hat :hat :hat :hat :keys :pistol :pistol :pistolhat :sword :sword :sword :sword)
              res)))))

(deftest remove-card-test
  (testing "remove-card"
    (let [res (remove-card [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat] :hat)]
      (is (=  [:flag :flag :flag :flag :sword :sword :sword :sword :hat :hat :hat :pistol :pistol :bottle :bottle :keys :pistolhat]
              res)))))

(deftest add-card-test
  (testing "add-card"
    (let [res (add-card [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat] :hat)]
      (is (=  [:flag :flag :flag :flag :sword :sword :sword :sword :hat :hat :hat :hat :hat :pistol :pistol :bottle :bottle :keys :pistolhat]
              res)))))


