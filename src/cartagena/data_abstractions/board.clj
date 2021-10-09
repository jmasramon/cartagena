(ns cartagena.data-abstractions.board
  (:require
   [clojure.data.generators :refer [shuffle]]
   [cartagena.core :refer [card-types]]
   [cartagena.data-abstractions.square :as sq
    :refer [make-square square-of-type?]]))

;; Functions that need to know how board is implemented

;; a board is a set of squares
;; it is made of six board-section's: sets of six squares each of one random type of card
;; ex: 
;; [{:pieces {:green 6, :yellow 6}, :type :start}
;;  {:pieces {:green 0, :yellow 0}, :type :flag}
;;  {:pieces {:green 0, :yellow 0}, :type :pistol}
;;  {:pieces {:green 0, :yellow 0}, :type :bottle}
;;  {:pieces {:green 0, :yellow 0}, :type :hat}
;;  {:pieces {:green 0, :yellow 0}, :type :keys}
;;  {:pieces {:green 0, :yellow 0}, :type :sword}
;;  {:pieces {:green 0, :yellow 0}, :type :hat}
;;  {:pieces {:green 0, :yellow 0}, :type :flag}
;;  {:pieces {:green 0, :yellow 0}, :type :bottle}
;; .....
;;  {:pieces {:green 0, :yellow 0}, :type :boat}]

;; So functions of its api should be:
;; constructor board-section (needs types of cards and colors of players) board
;; get/add/remove pieces
;; get type (never changes)
;; find 
;;   empty square to move a piece to
;;   occupied square to draw back a piece to
;;   empty square to move a piece to

;; builders
(defn make-board-section
  "creates a board section"
  ([used-colors]
   (let [shuffled-types (shuffle (vec card-types))]
     (vec (for [type shuffled-types]
            (make-square type used-colors)))))
  ([types used-colors]
   (let [shuffled-types (shuffle (vec types))]
     (vec (for [type shuffled-types]
            (make-square type used-colors))))))

(defn make-board
  "Creates an initial board"
  ([players]
   (let [used-colors (flatten (map keys players))]
     (vec (flatten [(make-square :start used-colors)
                    (make-board-section card-types used-colors)
                    (make-board-section card-types used-colors)
                    (make-board-section card-types used-colors)
                    (make-board-section card-types used-colors)
                    (make-board-section card-types used-colors)
                    (make-board-section card-types used-colors)
                    (make-square :boat used-colors)]))))
  ([types players]
   (let [used-colors (flatten (map keys players))]
     (vec (flatten [(make-square :start used-colors)
                    (make-board-section types used-colors)
                    (make-board-section types used-colors)
                    (make-board-section types used-colors)
                    (make-board-section types used-colors)
                    (make-board-section types used-colors)
                    (make-board-section types used-colors)
                    (make-square :boat used-colors)])))))

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

(defn square-contents-vector
  "Convert the pieces on the given square to a vector of pieces"
  [board index]
  (sq/square-contents-vector (square-pieces board index)))

(defn square-type [board index]
  (get-in board [index :type]))

(defn squares-of-type [board type]
  (filterv #(= (% :type) type) board))

(defn indexes-squares-of-type [board type]
  (keep-indexed #(when (square-of-type? %2 type) %1) board))

;; setters
(defn add-piece-to [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (sq/add-piece-to square color))))

(defn remove-piece-from [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (sq/remove-piece-from square color))))

(defn move-piece [board from to color]
	; (println "move-piece with from:" from "to:" to "color:" color)
  (let [board' (remove-piece-from board from color)]
    (add-piece-to board' to color)))

;; testers
(defn space-available?
  "Does the square have space available for another piece?"
  [board index]
  (< (reduce + (vals ((board index) :pieces))) 2))

(def space-occupied?
  "Is the square full?"
  (complement space-available?))

(defn square-has-color?
  "Has the square a piece of certain color?"
  [board index color]
  (> (square-pieces board index color) 0))

(defn isOfType?
  "Is the square of certain type?"
  [type square]
  (= (square :type) type))

(defn empty-slot?
  "Is the square empty of pieces (of certain color)?"
  ([square type color]
   (and
    (isOfType? type square)
    (= 0 ((square :pieces) color))))
  ([square type]
   (and
    (isOfType? type square)
    (= 0 (count (filter #(not= 0 (val %)) (square :pieces)))))))

(defn nonempty-slot?
  "Has the square any piece?"
  [square]
  (not= 0 (count (filter #(not= 0 (val %)) (square :pieces)))))

;; finders
(defn index-next-empty-slot
  "Index of first slot after origin, of same type as card and with nobody(of same color) there"
  ([board card origin color]
   (let [subBoard (subvec board (inc origin))]
     (+ (inc origin) (first (keep-indexed #(if (empty-slot? %2 card color) %1) subBoard)))))
  ([board card origin]
   (let [subBoard (subvec board (inc origin))
         sameCardIndexes (keep-indexed #(if (empty-slot? %2 card) %1) subBoard)]
     (if (> (count sameCardIndexes) 0)
       (+ (inc origin) (first sameCardIndexes))
       (dec (count board))))))

(defn index-closest-nonempty-slot
  "Index of first slot after origin with somebody already there"
  [board origin]
  (let [subBoard (subvec board 0 origin)
        nonemptySlotsIndexes (keep-indexed #(if (nonempty-slot? %2) %1) subBoard)]
    (if (> (count nonemptySlotsIndexes) 0)
      (last nonemptySlotsIndexes)
      0)))


