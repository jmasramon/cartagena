(ns cartagena.moves-test
  (:require [clojure.test :refer :all]
            [cartagena.moves :refer :all]))

(def fullGameState {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}],
                    :turn-order {:green :red, :red :yellow, :yellow :green}
                    :turn :green
                    :board [
                        {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
                        {:pieces {:green 6, :red 6, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                    :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]})


(deftest move-piece-test
  (testing "move-piece"
    (is (= [
        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
        {:pieces {:green 0, :red 1, :yellow 0}, :type :flag}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
        {:pieces {:green 3, :red 1, :yellow 1}, :type :hat}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
        (move-piece [
        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
        {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
        4
        2
        :red)))))

(deftest empty-slot-test
  (testing "empty-slot"
    (is (= true
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            :bottle
            :green)))
    (is (= true
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            :keys
            :red)))
    (is (= false
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol
            :yellow)))
    (is (= true
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol
            :green)))

    )
  (testing "empty-slot one param"
    (is (= true
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            :bottle)))
    (is (= true
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
            :keys)))
    (is (= false
        (empty-slot
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol)))
    (is (= false
        (empty-slot
            {:pieces {:green 1, :red 0, :yellow 0}, :type :pistol}
            :pistol)))
    (is (= false
        (empty-slot
            {:pieces {:green 1, :red 2, :yellow 0}, :type :pistol}
            :pistol)))

    )  )


(deftest find-empty-slot-test
  (testing "find-empty-slot"
    (is (= 5
        (find-empty-slot fullGameState :keys 0 :green)))
    (is (= 1
        (find-empty-slot fullGameState :bottle 0 :red)))

    (is (= 13
        (find-empty-slot fullGameState :keys 6 :yellow)))
    (is (= 13
        (find-empty-slot fullGameState :keys 5 :green)))
    (is (= 5
        (find-empty-slot fullGameState :keys 4 :red)))

    (is (= 8
        (find-empty-slot fullGameState :flag 2 :yellow)))
    )
    (testing "find-empty-slot two params"
    (is (= 5
        (find-empty-slot fullGameState :keys 0)))
    (is (= 1
        (find-empty-slot fullGameState :bottle 0)))
    (is (= 13
        (find-empty-slot fullGameState :keys 6)))
    (is (= 13
        (find-empty-slot fullGameState :keys 5)))
    (is (= 5
        (find-empty-slot fullGameState :keys 4)))
    (is (= 10
        (find-empty-slot fullGameState :flag 2)))
    (is (= 15
        (find-empty-slot fullGameState :flag 10)))

    ))

(deftest nonempty-slot-test
  (testing "nonempty-slot"
    (is (= false
        (nonempty-slot
            {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
            )))

    (is (= true
        (nonempty-slot
            {:pieces {:green 2, :red 1, :yellow 3}, :type :keys}
            )))

    (is (= true
        (nonempty-slot
            {:pieces {:green 0, :red 1, :yellow 1}, :type :pistol}
            )))

    (is (= true
        (nonempty-slot
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            )))

    ))

(deftest find-nonempty-slot-test
  (testing "find-nonempty-slot"
    (is (= 0
        (find-nonempty-slot fullGameState 3)))
    (is (= 0
        (find-nonempty-slot fullGameState 4)))
    (is (= 0
        (find-nonempty-slot fullGameState 5)))
    (is (= 0
        (find-nonempty-slot fullGameState 6)))
    (is (= 0
        (find-nonempty-slot fullGameState 7)))
    (is (= 0
        (find-nonempty-slot fullGameState 8)))
    (is (= 8
        (find-nonempty-slot fullGameState 9)))
    (is (= 8
        (find-nonempty-slot fullGameState 10)))
    (is (= 8
        (find-nonempty-slot fullGameState 11)))
    (is (= 0
        (find-nonempty-slot {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}],
                    :turn-order {:green :red, :red :yellow, :yellow :green}
                    :turn :green
                    :board [
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
                        {:pieces {:green 6, :red 6, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                    :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]} 8)))
    ))


