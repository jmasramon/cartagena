(ns cartagena.gameplay-test
  (:require [clojure.test :refer :all]
            [cartagena.gameplay :refer :all]))

(deftest draw-one-test
  (testing "draw-one"
    (is (=  [:sword '(:pistol :keys :flag)]
            (draw-one '(:sword :pistol :keys :flag))))))

(deftest draw-test
  (testing "draw"
    (is (=  ['(:sword :pistol :keys) '(:flag)]
            (draw '(:sword :pistol :keys :flag) 3)))))

(deftest pass-test
  (testing "pass"
    (is (=  {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
							{:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}
                            {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 0}}],
            	   :board [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
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
                            {:red {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 1}}],
            	   :board [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
					{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				   :turn-order {:green :red, :red :yellow, :yellow :green}
                   :turn :red})))
    ))

