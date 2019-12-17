(ns cartagena.setters.semanticEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.setters.semanticEntities :refer :all]))

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
                  :red)))
    ))

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
                  :green)))
    ))

(deftest from-freqs-to-seq-test
  (testing "from-freqs-to-seq"
    (is (= '(:bottle :bottle :keys :pistol :sword)
          (from-freqs-to-seq {:bottle 2, :keys 1, :pistol 1, :sword 1})))))


(deftest remove-card-test
  (testing "remove-card"
    (is (= '(:bottle :bottle :keys :pistol :sword)
          (remove-card '(:bottle :keys :pistol :bottle :keys :sword) :keys)))))

(deftest add-card-test
  (testing "add-card"
    (is (= '(:bottle :bottle :keys :keys :keys :pistol :sword)
          (add-card '(:bottle :keys :pistol :bottle :keys :sword) :keys)))

    (is (= '(:sword :sword :sword :sword :pistol)
          (add-card '(:sword :sword :sword :sword) :pistol)))

    ))

