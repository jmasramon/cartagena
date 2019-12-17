(ns cartagena.getters.sdariEntities-test
  (:require [clojure.test :refer :all]
            [cartagena.getters.sdariEntities :refer :all]))

(def fullGameState {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
                            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}],
                    :turn-order {:green :red, :red :yellow, :yellow :green}
                    :turn :green
                    :board [
                        {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
                        {:pieces {:green 1, :red 2, :yellow 3}, :type :bottle}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
                        {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
                    :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]})

(deftest boat-test
  (testing "boat"
    (is (=  {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}
    		(boat [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}])))))

(deftest player-test
  (testing "player"
    (is (=  {:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
           (player [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
					{:green {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 3}}
					{:red {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}]
       			   :green)))
    ))

(deftest cards-test
	(testing "cards"
		(is (= '(:bottle :keys :pistol :bottle :keys :sword)
				(cards  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
						 {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
						:yellow)))))

(deftest square-test
  (testing "square"
    (is (=  {:pieces {:green 6, :red 6, :yellow 6}, :type :start}
    		(square [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				0)))

    (is (=  {:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
    		(square [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				4)))

    ))

(deftest square-pieces-test
  (testing "square-pieces 2 params"
    (is (=  {:green 6, :red 6, :yellow 6}
    		(square-pieces [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				0)))

    (is (=  {:green 2, :red 1, :yellow 2}
    		(square-pieces [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 2, :red 1, :yellow 2}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				4)))

    )
  (testing "square-pieces 3 params"
    (is (=  6
    		(square-pieces [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				0
				:red)))

    (is (=  0
    		(square-pieces [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				4
				:yellow)))

    )
  )

(deftest actions-test
	(testing "actions"
		(is (= 0
			(actions [
            					{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
			              		{:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
				          		{:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
					:green)))
		(is (= 3
			(actions [
            					{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
			              		{:green {:cards '(:flag :keys :pistol :sword :bottle :hat), :actions 0}}
				          		{:red {:cards '(:bottle :pistol :flag :flag :keys :bottle), :actions 3}}]
					:red)))
		))

(deftest square-type-test
	(testing "square-type"
		(is (= :flag
			(square-type [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				2)))))

(deftest squares-of-type-test
	(testing "squares-of-type"
		(is (= [{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
           {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
           {:pieces {:green 0, :red 0, :yellow 0}, :type :flag}]
			(squares-of-type [
				{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :flag}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :sword}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :hat}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :keys}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :pistol}
				{:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
				:flag)))))

(deftest square-contents-vector-test
    (testing "square-contents-vector one param"
        (is (= [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
            (square-contents-vector {:green 6, :red 6, :yellow 6})))
        (is (= [:green :red :red :yellow :yellow :yellow]
            (square-contents-vector {:green 1, :red 2, :yellow 3})))
        (is (= []
            (square-contents-vector {:green 0, :red 0, :yellow 0})))
        )
    (testing "square-contents-vector two param"
        (is (= [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
            (square-contents-vector fullGameState 0)))
        (is (= [:green :red :red :yellow :yellow :yellow]
            (square-contents-vector fullGameState 1)))
        (is (= []
            (square-contents-vector fullGameState 2)))
        ))



