(ns cartagena.swingUI.shaping-test
  (:require [clojure.test :refer [deftest is testing]]
            [cartagena.swingUI.shaping :refer [create-square hand-shapes]]))

(deftest create-square-test
    (testing "create-square"
    	(let [square (create-square [0 0] 50)]
	        (is (= 25.0
	            (.getCenterX square)))

	        (is (= 25.0
	            (.getCenterY square)))

	        (is (= 50.0
	            (.getHeight square)))

	        )
    	(let [square (create-square [7 1] 50)]
	        (is (= 375.0
	            (.getCenterX square)))

	        (is (= 75.0
	            (.getCenterY square)))

	        (is (= 50.0
	            (.getHeight square)))

	        )))

(deftest hand-shapes-test
    (testing "hand-shapes"
    	(is (= 6
    		(count hand-shapes)))

    	(is (= "java.awt.Rectangle[x=450,y=0,width=50,height=50]"
    		(.toString (.getBounds (first (first hand-shapes))))))

    	(is (= :bottle
    		(second (first hand-shapes))))

    	(is (= "java.awt.Rectangle[x=450,y=50,width=50,height=50]"
    		(.toString (.getBounds (first (second hand-shapes))))))

    	(is (= :flag
    		(second (second hand-shapes))))

    	))
