(ns cartagena.setters.otherEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.setters.otherEntities :refer :all]))

(deftest update-player-test
  (testing "update-player"
    (is (=  {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
           (update-player   :green
                            {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                            {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}})))
    ))


(deftest set-cards-test
  (testing "set-cards"
    (is (=  {:green {:cards '(:bottle :keys :pistol :keys :sword), :actions 3}}
           (set-cards   {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                        '(:bottle :keys :pistol :keys :sword))))
    ))


