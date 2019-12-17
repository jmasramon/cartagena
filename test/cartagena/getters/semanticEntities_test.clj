(ns cartagena.getters.semanticEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.getters.semanticEntities :refer :all]))

(deftest next-active-player-color-test
  (testing "next-active-player-color"
    (is (=  :red
            (next-active-player-color {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                        :turn-order {:green :red, :red :yellow, :yellow :green}
                        :turn :red})))
    ))

(deftest active-player-test
  (testing "active-player"
    (is (=  {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}
            (active-player {:players [
                                {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                {:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
                                {:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
                            :turn-order {:green :red, :red :yellow, :yellow :green}
                            :turn :red})))
    ))

