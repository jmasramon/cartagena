(ns cartagena.setters.sdariEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.setters.sdariEntities :refer :all]))

(deftest update-player-in-players-test
  (testing "update-player-in-players"
    (is (=  [   {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
           (update-player-in-players    [   {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                                {:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
                            :green
                            {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}})))
    ))

(deftest inc-square-players-test
  (testing "inc-square-players"
    (is (= {:pieces {:green 0, :red 1, :yellow 0}, :type :start}
          (inc-square-players {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
            :red)))))

(deftest dec-square-players-test
  (testing "dec-square-players"
    (is (= {:pieces {:green 1, :red 1, :yellow 0}, :type :start}
          (dec-square-players {:pieces {:green 2, :red 1, :yellow 0}, :type :start}
            :green)))))

(deftest add-piece-test
  (testing "add-piece"
    (is (= [
        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
        {:pieces {:green 0, :red 1, :yellow 0}, :type :bottle}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
        (add-piece [
        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
        1
        :red)))))

(deftest remove-piece-test
  (testing "remove-piece"
    (is (= [
        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
        {:pieces {:green 3, :red 2, :yellow 0}, :type :hat}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
        (remove-piece [
        {:pieces {:green 0, :red 0, :yellow 0}, :type :start}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
        {:pieces {:green 3, :red 2, :yellow 1}, :type :hat}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
        4
        :yellow)))))

