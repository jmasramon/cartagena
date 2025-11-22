(ns cartagena.data-abstractions.board
  (:require
   [clojure.data.generators :as gen]
   [cartagena.core :refer [card-types]]
   [cartagena.data-abstractions.player-bis :refer [color]]
   [cartagena.data-abstractions.square-bis :as sq
    :refer [make-square type-of square-of-type?]]))

;; Functions that need to know how board is implemented

;; A board is a vector of squares. Order is important
;; it is made of six board-section's: sets of six squares each of one random type of card
;; ex: 
;; [start-square
;;  square
;; ..... (6*6 = 36 squares)
;;  square
;;  boat-square]

;; So functions of its api should be:
;; constructor board-section (needs types of cards and colors of players) board
;; get/add/remove pieces
;; get type (never changes)
;; find 
;;   empty square to move a piece to
;;   occupied square to draw back a piece to
;;   empty square to move a piece to

;; builders
(defn- make-board-section
  "Creates a board section (6 squares)"
  ([used-colors]
   (make-board-section card-types used-colors))
  ([types used-colors]
   (let [shuffled-types (gen/shuffle (vec types))]
     (vec (for [type shuffled-types]
            (make-square type used-colors))))))

(defn make-board
  "Creates an initial board"
  ([players]
   (make-board card-types players))
  ([types players]
   (let [used-colors (map color players)]
     (conj
      (into []
            (concat
             [(make-square :start used-colors)]
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)))
      (make-square :boat used-colors)))))

;; getters
(defn boat 
  [board]
  (last board))

(defn square 
  [board index]
  (board index))

(defn pieces-in 
  [board index]
  (sq/pieces-in (square board index)))

(defn num-pieces-in
  "Number of pieces in a square. Global or one color only"
  ([board index]
   (sq/num-pieces-in (square board index)))
  ([board index color]
   (sq/num-pieces-in (square board index) color)))

(defn square-pieces-as-vector
  "Convert the pieces on the square at board's index to a vector of pieces"
  [board index]
  (sq/square-pieces-as-vector (square board index)))

(defn square-type
  "Return the type of the square"
  [board index]
  (type-of (square board index)))

(defn squares-of-type
  "Returns vector of all squares of a type"
  [board type]
  (filter (partial square-of-type? type) board))

(defn indexes-squares-of-type
  "Returns ordered list of all squares of type"
  [board type]
  (keep-indexed #(when (square-of-type? type %2) %1) board))


;; setters
(defn add-piece-to
  "Add a piece of certain color to a certain square"
  [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (sq/add-piece-to square color))))

(defn remove-piece-from
  "Remove a piece of certain color from a certain square"
  [board index color]
  (let [square (square board index)]
    (assoc-in board [index]
              (sq/remove-piece-from square color))))

(defn move-piece
  "Move a piece of color from to"
  [board from to color]
  (let [board' (remove-piece-from board from color)]
    (add-piece-to board' to color)))

(defn space-available?
  "Does the square have space available for another piece? (max 3)"
  [board index]
  (<= (sq/num-pieces-in (square board index)) 2))

(def square-full?
  "Is the square full?"
  (complement space-available?))

(defn square-has-color?
  "Has the square a piece of certain color?"
  [board index color]
  (> (num-pieces-in board index color) 0))

(defn empty-slot?
  "Is the square of certain type and empty of pieces (of certain color)?"
  ([square type color]
   (and
    (square-of-type? type square)
    (= 0 (sq/num-pieces-in square color))))
  ([square type]
   (and
    (square-of-type? type square)
    (= 0 (sq/num-pieces-in square)))))

(defn nonempty-slot?
  "Has the square any piece?"
  [square]
  (not= 0 (sq/num-pieces-in square)))

;; finders
(defn next-empty-slot-index
  "Index of first slot after origin, of same type as card and with nobody (of same color) there"
  ([board card origin color]
   (let [sub-board (subvec board (inc origin))
         same-card-indexes (keep-indexed #(if (empty-slot? %2 card color) %1  nil) sub-board)]
     (+ (inc origin)
        (first same-card-indexes))))
  ([board card origin]
   (let [sub-board (subvec board (inc origin))
         same-card-indexes (keep-indexed #(if (empty-slot? %2 card) %1  nil) sub-board)]
     (if (> (count same-card-indexes) 0)
       (+ (inc origin)
          (first same-card-indexes))
       (dec (count board))))))

(defn index-closest-nonempty-slot
  "Index of closest slot before origin with somebody already there"
  [board origin]
  (let [sub-board (subvec board 0 origin)
        non-empty-slots-indexes (keep-indexed #(if (nonempty-slot? %2) %1  nil) sub-board)]
    (if (> (count non-empty-slots-indexes) 
           0)
      (let [candidate (last non-empty-slots-indexes)]
        (if (> candidate 0)
          candidate
          nil))
      nil)))


