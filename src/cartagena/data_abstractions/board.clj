(ns cartagena.data-abstractions.board
  (:require
   [clojure.data.generators :as gen]
   [cartagena.core :as c]
   [cartagena.data-abstractions.player-bis :as p]
   [cartagena.data-abstractions.square-bis :as sq]))

;; ABSTRACTION LAYER: Layer 2 (Composite Structures)
;;
;; This namespace represents the game board as a collection of squares.
;; It depends ONLY on:
;;   - Layer 0: cartagena.core (for card-types constant)
;;   - Layer 1: cartagena.data-abstractions.square-bis (square operations)
;;              cartagena.data-abstractions.player-bis (for player colors)
;;
;; Higher layers (game, moves, UI) interact with the board through this
;; namespace's public API, never directly accessing square internals.
;;
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


;; builders
(defn- make-board-section
  "Creates a board section (6 squares)"
  ([used-colors]
   (make-board-section c/CARD-TYPES used-colors))
  ([types used-colors]
   (let [shuffled-types (gen/shuffle (vec types))]
     (vec (for [type shuffled-types]
            (sq/make-square type used-colors))))))

(defn make-board
  "Creates an initial board"
  ([players]
   (make-board c/CARD-TYPES players))
  ([types players]
   (let [used-colors (map p/color players)]
     (conj
      (into []
            (concat
             [(sq/make-square :start used-colors)]
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)
             (make-board-section types used-colors)))
      (sq/make-square :boat used-colors)))))

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

(defn pieces-numbers-list-in
  "Get list of piece counts for all players in a square"
  [board index]
  (sq/pieces-numbers-list-in (square board index)))

(defn square-type
  "Return the type of the square"
  [board index]
  (sq/type-of (square board index)))

(defn squares-of-type
  "Returns vector of all squares of a type"
  [board type]
  (filter (partial sq/square-of-type? type) board))

(defn indexes-squares-of-type
  "Returns ordered list of all squares of type"
  [board type]
  (keep-indexed #(when (sq/square-of-type? type %2) %1) board))


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
  (pos? (num-pieces-in board index color)))

(defn empty-slot?
  "Is the square of certain type and empty of pieces (of certain color)?"
  ([square type color]
   (and
    (sq/square-of-type? type square)
    (zero? (sq/num-pieces-in square color))))
  ([square type]
   (and
    (sq/square-of-type? type square)
    (zero? (sq/num-pieces-in square)))))

(defn nonempty-slot?
  "Has the square any piece?"
  [square]
  (pos? (sq/num-pieces-in square)))

;; finders
(defn next-empty-slot-index
  "Index of first slot after origin, of same type as card and with nobody (of same color) there"
  ([board card origin color]
   (let [sub-board (subvec board (inc origin))
         same-card-indexes (keep-indexed #(when (empty-slot? %2 card color) %1) sub-board)]
     (+ (inc origin)
        (first same-card-indexes))))
  ([board card origin]
   (let [sub-board (subvec board (inc origin))
         same-card-indexes (keep-indexed #(when (empty-slot? %2 card) %1) sub-board)]
     (if (seq same-card-indexes)
       (+ (inc origin)
          (first same-card-indexes))
       (dec (count board))))))

(defn index-closest-nonempty-slot
  "Index of closest slot before origin with somebody already there"
  [board origin]
  (let [sub-board (subvec board 0 origin)
        non-empty-slots-indexes (keep-indexed #(when (nonempty-slot? %2) %1) sub-board)]
    (when (seq non-empty-slots-indexes)
      (let [candidate (last non-empty-slots-indexes)]
        (when (pos? candidate)
          candidate)))))


