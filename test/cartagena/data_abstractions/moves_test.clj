(ns cartagena.data-abstractions.moves-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.data-abstractions.moves :refer [pass fall-back play-card]]))

(deftest pass-test
  (testing "pass"
    (is (=  {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                       {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                       {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]
             :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
             :turn-order {:green :red, :red :yellow, :yellow :green}
             :turn :yellow}
            (pass {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                             {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                             {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 1}}]
                   :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                           {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                   :turn-order {:green :red, :red :yellow, :yellow :green}
                   :turn :red})))))

;;TODO: pending tests for fall-back and play-card
(deftest fall-back-test
  (binding [*rnd* (java.util.Random. 12345)]
    (testing "fall-back"
      (is (=
           {:players [{:yellow {:cards '(:bottle :bottle :keys :keys :pistol :sword :sword), :actions 1}}
                      {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                      {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]
            :board [{:pieces {:green 4, :red 3, :yellow 6}, :type :start}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
                    {:pieces {:green 0, :red 1, :yellow 0}, :type :bottle}
                    {:pieces {:green 1, :red 0, :yellow 0}, :type :flag}
                    {:pieces {:green 0, :red 1, :yellow 0}, :type :sword}
                    {:pieces {:green 1, :red 0, :yellow 1}, :type :hat}
                    {:pieces {:green 0, :red 1, :yellow 0}, :type :keys}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                    {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
            :turn-order {:green :red, :red :yellow, :yellow :green}
            :turn :yellow}

           (fall-back
            {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 2}}
                       {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                       {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}]
             :board [{:pieces {:green 4, :red 3, :yellow 6}, :type :start}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
                     {:pieces {:green 0, :red 1, :yellow 0}, :type :bottle}
                     {:pieces {:green 1, :red 0, :yellow 0}, :type :flag}
                     {:pieces {:green 0, :red 1, :yellow 0}, :type :sword}
                     {:pieces {:green 1, :red 0, :yellow 0}, :type :hat}
                     {:pieces {:green 0, :red 1, :yellow 1}, :type :keys}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
             :turn-order {:green :red, :red :yellow, :yellow :green}
             :turn :yellow}
            12))))))

(deftest play-card-test
  (testing "play-card"
    (is (=  {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
                       {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                       {:red {:cards '(:keys :keys :pistol :sword), :actions 0}}]
             :board [{:pieces {:green 6, :red 5, :yellow 6}, :type :start}
                     {:pieces {:green 0, :red 1, :yellow 0}, :type :bottle}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                     {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
             :turn-order {:green :red, :red :yellow, :yellow :green}
             :turn :yellow}
            (play-card
             {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                        {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                        {:red {:cards '(:keys :pistol :bottle :keys :sword), :actions 1}}]
              :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                      {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
              :turn-order {:green :red, :red :yellow, :yellow :green}
              :turn :red}
             :bottle
             0)))))

