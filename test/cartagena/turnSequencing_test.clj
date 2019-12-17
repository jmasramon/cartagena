(ns cartagena.turnSequencing-test
  (:require [clojure.test :refer :all]
            [cartagena.turnSequencing :refer :all]))

(deftest start-turn-test
  (testing "start-turn"
    (is (=  {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
    					{:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
    					{:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]}
            (start-turn {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
			            			{:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
			    					{:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]} 
            			:green)))))


