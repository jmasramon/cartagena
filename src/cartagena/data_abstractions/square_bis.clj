(ns cartagena.data-abstractions.square-bis
  (:require [cartagena.core :refer [num-starting-pieces]]))

;; ABSTRACTION LAYER: Layer 1 (Basic Data Structures)
;;
;; This namespace represents a square as an individual board position.
;; It depends ONLY on:
;;   - Layer 0: cartagena.core (for num-starting-pieces constant)
;;
;; Higher layers (board, game, UI) interact with squares through this
;; namespace's public API, never directly accessing square internals.
;;
;; Alternative square representation used to validate the data abstraction
;; barrier. This namespace implements squares as a vector
;; [type-set-instance pieces-map] while keeping the same public API as
;; cartagena.data-abstractions.square. The rest of the code and tests
;; should work unchanged when switching between these implementations.
;;
;; Functions that need to know how a square is implemented.
;; A square is implemented as a vector [type-set-instance pieces-map]
;; where pieces-map is a map from player color keywords to piece counts, e.g.
;; {:green green-num, :red red-num, :yellow yellow-num} for all players in game.
;; type-set-instance is one of #{:hat :flag :pistol :sword :bottle :keys} plus :start and :boat.
;; Example: [:bottle {:green 1, :red 2, :yellow 1}]
;;
;; API responsibilities:
;;   - construct squares (needs type and colors of players)
;;   - get/add/remove pieces
;;   - get type (never changes)

;; builders
(defn- make-pieces
  "Create a pieces map for a square for the given colors, each with num-pieces."
  [used-colors num-pieces]
  (into (sorted-map) (zipmap used-colors (repeat num-pieces))))

(defn- make-empty-pieces
  "Create an empty pieces map (0 pieces for each color)."
  [used-colors]
  (make-pieces used-colors 0))

(defn- make-starting-square
  "creates the start square"
  [used-colors]
  (conj [:start] (make-pieces used-colors num-starting-pieces)))

(defn make-square
  "Creates a square of specific type"
  [type used-colors]
  (let [empty-pieces (make-empty-pieces used-colors)]
    (case type
      :start (make-starting-square used-colors)
      (conj [type] empty-pieces))))

;; setters
(defn- change-piece-quantity
  [square color f]
  (update-in square [1 color] f))

(defn add-piece-to
  "Add a piece of a certain color;
   actually returns new square with one more piece"
  [square color]
  (change-piece-quantity square color inc))

(defn remove-piece-from
  "Remove a piece of a certain color; 
   actually returns new square with one less piece"
  [square color]
  (change-piece-quantity square color dec))

;; getters
(defn type-of
  "Get type of square"
  [square]
  (first square))

(defn pieces-in
  "Get hashmap of pieces"
  [square]
  (second square))

(defn pieces-numbers-list-in
  "A list with the number of pieces"
  [square]
  (vals (pieces-in square)))

(defn num-pieces-in
  "Number of pieces in a square.
   Arity 1: total pieces in the square.
   Arity 2: pieces of a given color."
  ([square]
   (reduce + (vals (pieces-in square))))
  ([square color]
   (get-in square [1 color])))

;; testers
(defn square-of-type?
  "Is the square of a certain type?"
  [type square]
  (= (type-of square) type))

(defn is-square?
  "Is the data structure a square?"
  [data-structure]
  (and (vector? data-structure)
       (= 2 (count data-structure))
       (instance? clojure.lang.Keyword (first data-structure))
       (map? (second data-structure))))

;; helpers
(defn pieces-to-vector
  "Convert the given pieces map {:color n, ...} to a vector of color keywords."
  [pieces]
  (vec (reduce concat (map #(repeat (val %) (key %)) pieces))))

(defn square-pieces-as-vector
  "Convert the pieces hashmap from the given square into a vector"
  [square]
  (pieces-to-vector (pieces-in square)))



