(ns cartagena.queries-test
  (:require [clojure.test :refer :all]
            [cartagena.queries :refer :all]))

(deftest winner-test
	(testing "winner?"
		(is (= false
			(winner? {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}],
            	:board [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]})))
		(is (= true
			(winner? {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}],
            	:board [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]})))
		))

(deftest space-available?-test
	(testing "space-available?"
		(is (= false
			(space-available? [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
					0)))
		(is (= true
			(space-available? [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
					1)))
		))

(deftest space-occupied?-test
	(testing "space-occupied?"
		(is (= true
			(space-occupied? [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
					0)))
		(is (= false
			(space-occupied? [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
					1)))
		))

(deftest player-has-card?-test
	(testing "player-has-card?"
		(is (= true
			(player-has-card? [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
					{:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
					{:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
					:green
					:bottle)))

		(is (= false
			(player-has-card? [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
					{:green {:cards '(:bottle :keys :pistol :bottle :keys :pistol), :actions 3}}
					{:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
					:green
					:sword)))

		))

(deftest square-has-color?-test
	(testing "square-has-color?"
		(is (= false
			(square-has-color? [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
					3
					:green)))
		(is (= true
			(square-has-color? [
					{:pieces {:green 6, :red 6, :yellow 6}, :type :start} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :flag} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :sword} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :hat} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :keys} 
					{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol} 
					{:pieces {:green 6, :red 0, :yellow 0}, :type :boat}]
					0
					:red)))
		))

(deftest players-turn?-test
	(testing "players-turn?"
		(is (= false
			(players-turn? [
            					{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
			              		{:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}} 
				          		{:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
					:green)))
		(is (= true
			(players-turn? [
            					{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
			              		{:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}} 
				          		{:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
					:red)))
		))

(deftest square-of-type?-test
    (testing "square-of-type?"
        (is (= false
            (square-of-type? {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            :flag)))
        (is (= true
            (square-of-type? {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                            :start)))
        ))

