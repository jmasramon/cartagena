(ns cartagena.getters.sdariEntities
	(:require [cartagena.getters.mainEntities :refer :all]))

(defn player [players color]
	; (= color (first (map first (map keys players)))))
	(first (filter #(= color (first (keys %)))
			players)))

(defn actions [players color]
	(:actions (color (player players color))))

(defn cards [players player-color]
	(((player players player-color) player-color) :cards))

(defn boat [board]
	(last board))

(defn square [board index]
	(board index))

(defn square-pieces 
	([board index]
		(get-in board [index :pieces]))
	([board index color]
		(get-in board [index :pieces color])))

(defn square-type [board index]
	(get-in board [index :type]))

(defn squares-of-type [board type]
	(filterv #(= (% :type) type) board ))

(defn square-contents-vector
  "Convert the pieces on the given square to a vector of pieces"
  ([square] 
    (vec (reduce concat (map #(repeat (val %) (key %)) square))))
  ([game-state index]
    (square-contents-vector (square-pieces (board game-state) index))))

