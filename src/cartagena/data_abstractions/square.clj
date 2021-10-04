(ns cartagena.data-abstractions.square)

;; Functions that need to know how square is implemented

(defn make-square [type pieces])


;; setters
(defn inc-square-players [square color]
  (update-in square [:pieces color] inc))

(defn dec-square-players [square color]
  (update-in square [:pieces color] dec))

;; getters
;; TODO: Should not be using get-in using board. Should not know anything about board at this data abstraction level
(defn square-type [board index]
  (get-in board [index :type]))

(defn square-pieces
  ([board index]
   (get-in board [index :pieces]))
  ([board index color]
   (get-in board [index :pieces color])))

(defn num-pieces [square color]
  (get-in square [:pieces color]))

(defn square-contents-vector
  "Convert the pieces on the given square to a vector of pieces"
  ([square]
   (vec (reduce concat (map #(repeat (val %) (key %)) square))))
  ([board index]
   (square-contents-vector (square-pieces board index))))

(defn square-of-type? [square type]
  (= (square :type) type))
