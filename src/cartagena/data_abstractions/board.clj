(ns cartagena.data-abstractions.board
  (:require
   [cartagena.data-abstractions.square
    :refer [square-of-type? inc-square-players dec-square-players make-starting-square make-board-section make-empty-pieces]]))

(defn initial-board [types players]
  (let [used-colors (flatten (map keys players))]
    (vec (flatten [(make-starting-square used-colors)
                   (make-board-section types used-colors)
                   (make-board-section types used-colors)
                   (make-board-section types used-colors)
                   (make-board-section types used-colors)
                   (make-board-section types used-colors)
                   (make-board-section types used-colors)
                   (merge (make-empty-pieces used-colors) ;; TODO: use make-square instead
                          {:type :boat})]))))

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

(defn space-available? [board index]
  (< (reduce + (vals ((board index) :pieces))) 2))

(def space-occupied? (complement space-available?))

(defn square-has-color? [board index color]
  (> (square-pieces board index color) 0))

;; setters
(defn add-piece [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (inc-square-players square color))))

(defn remove-piece [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (dec-square-players square color))))

(defn empty-slot?
  ([slot card color]
   (and
    (= (slot :type) card)
    (= 0 ((slot :pieces) color))))
  ([slot card]
   (and
    (= (slot :type) card)
    (= 0 (count (filter #(not= 0 (val %)) (slot :pieces)))))))

(defn find-empty-slot
  ([board card origin color]
   "first slot after origin, of same type as card and with nobody of same color there"
	; (println "find-empty-slot with card:" card "origin:" origin)
   (let [subBoard (subvec board (inc origin))]
		; (first (filter f coll)
     (+ (inc origin) (first (keep-indexed #(if (empty-slot? %2 card color) %1) subBoard)))))
  ([board card origin]
   "first slot after origin, of same type as card and with nobody there"
	; (println "find-empty-slot with card:" card "origin:" origin)
   (let [subBoard (subvec board (inc origin))
         sameCardIndexes (keep-indexed #(if (empty-slot? %2 card) %1) subBoard)]
		; (println "sameCardIndexes" sameCardIndexes "of len" (count sameCardIndexes))
     (if (> (count sameCardIndexes) 0)
       (+ (inc origin) (first sameCardIndexes))
       (dec (count board))))))

(defn nonempty-slot? [slot]
  (not= 0 (count (filter #(not= 0 (val %)) (slot :pieces)))))

(defn find-nonempty-slot [board origin]
  (let [subBoard (subvec board 0 origin)
        nonemptySlotsIndexes (keep-indexed #(if (nonempty-slot? %2) %1) subBoard)]
    (if (> (count nonemptySlotsIndexes) 0)
      (last nonemptySlotsIndexes)
      0)))

(defn move-piece [board from to color]
	; (println "move-piece with from:" from "to:" to "color:" color)
  (let [board' (remove-piece board from color)]
    (add-piece board' to color)))