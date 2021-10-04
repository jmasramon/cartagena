(ns cartagena.data-abstractions.board
  (:require
   [cartagena.data-abstractions.square :refer [square-of-type? inc-square-players dec-square-players]]))


;; getters
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
  (filterv #(= (% :type) type) board))

(defn indexes-squares-of-type [board type]
  (keep-indexed #(when (square-of-type? %2 type) %1) board))

(defn next-open [board index symbol])

(defn next-fallback [board index])

;; setters
(defn add-piece [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (inc-square-players square color))))

(defn remove-piece [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (dec-square-players square color))))